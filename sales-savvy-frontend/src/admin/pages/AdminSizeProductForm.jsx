import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import "./AdminProductForm.css";

const API_BASE = "http://localhost:9090";

/* ================= SIZE PRESETS BY CATEGORY ================= */
const SIZE_PRESETS = {
  Shirts: ["S", "M", "L", "XL", "XXL"],
  Jackets: ["S", "M", "L", "XL", "XXL"],
  Pants: ["28", "30", "32", "34", "36"],
  Shoes: ["5", "6", "7", "8", "9", "10", "11"],
};

/* Categories allowed in this form */
const SIZE_CATEGORIES = ["Shirts", "Pants", "Jackets", "Shoes"];

export default function AdminSizeProductForm({ mode = "add" }) {
  const navigate = useNavigate();
  const { productId } = useParams();

  const [categories, setCategories] = useState([]);
  const [selectedCategoryName, setSelectedCategoryName] = useState("");

  const [images, setImages] = useState([]);

  const [form, setForm] = useState({
    name: "",
    description: "",
    price: "",
    categoryId: "",
  });

  const [sizes, setSizes] = useState([
    { size: "S", stock: "" },
  ]);

  const [loading, setLoading] = useState(false);

  /* ================= LOAD CATEGORIES ================= */
  useEffect(() => {
    axios
      .get(`${API_BASE}/categories`, { withCredentials: true })
      .then((res) => {
        const allowed = (res.data || []).filter((c) =>
          SIZE_CATEGORIES.includes(c.categoryName)
        );
        setCategories(allowed);
      })
      .catch(() => alert("Failed to load categories"));
  }, []);

  /* ================= LOAD PRODUCT (EDIT MODE) ================= */
  useEffect(() => {
    if (mode === "edit" && productId) {
      setLoading(true);

      axios
        .get(`${API_BASE}/admin/products/all`, { withCredentials: true })
        .then((res) => {
          const product = res.data.find(
            (p) => p.productId === Number(productId)
          );
          if (!product) return;

          setForm({
            name: product.name,
            description: product.description,
            price: product.price,
            categoryId: product.categoryId,
          });

          setSelectedCategoryName(product.categoryName || "");

          if (product.sizes && product.sizes.length > 0) {
            setSizes(
              product.sizes.map((s) => ({
                size: s.size,
                stock: s.stock,
              }))
            );
          }
        })
        .finally(() => setLoading(false));
    }
  }, [mode, productId]);

  /* ================= HANDLERS ================= */

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleCategoryChange = (e) => {
    const categoryId = e.target.value;
    const category = categories.find(
      (c) => c.categoryId === Number(categoryId)
    );

    setForm({ ...form, categoryId });
    setSelectedCategoryName(category?.categoryName || "");

    // reset sizes when category changes
    setSizes([]);
  };

  const handleImageChange = (e) => {
    setImages([...e.target.files]);
  };

  const addSizeRow = () => {
    setSizes([...sizes, { size: "", stock: "" }]);
  };

  const removeSizeRow = (index) => {
    setSizes(sizes.filter((_, i) => i !== index));
  };

  const updateSize = (index, field, value) => {
    const updated = [...sizes];
    updated[index][field] = value;
    setSizes(updated);
  };

  const addPresetSize = (sizeValue) => {
    const exists = sizes.some((s) => s.size === sizeValue);
    if (exists) return;

    setSizes([...sizes, { size: sizeValue, stock: "" }]);
  };

  /* ================= SUBMIT ================= */

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.categoryId) {
      alert("Select category");
      return;
    }

    if (sizes.length === 0) {
      alert("At least one size is required");
      return;
    }

    const productPayload = {
      name: form.name,
      description: form.description,
      price: Number(form.price),
      categoryId: parseInt(form.categoryId, 10),
      sizes: sizes
        .filter((s) => s.size && s.stock !== "")
        .map((s) => ({
          size: s.size,
          stock: Number(s.stock),
        })),
    };

    const data = new FormData();

    data.append(
      "product",
      new Blob([JSON.stringify(productPayload)], {
        type: "application/json",
      })
    );

    images.forEach((img) => {
      data.append("images", img);
    });

    try {
      if (mode === "add") {
        await axios.post(
          `${API_BASE}/admin/products/add`,
          data,
          {
            withCredentials: true,
            headers: { "Content-Type": "multipart/form-data" },
          }
        );
      } else {
        await axios.put(
          `${API_BASE}/admin/products/update/${productId}`,
          data,
          {
            withCredentials: true,
            headers: { "Content-Type": "multipart/form-data" },
          }
        );
      }

      navigate("/admin/products");
    } catch (err) {
      console.error("SIZE PRODUCT ERROR:", err.response || err);
      alert(
        err.response?.data?.message ||
          "Failed to save size-based product"
      );
    }
  };

  if (loading) return <p>Loading product...</p>;

  /* ================= UI ================= */

  return (
    <div className="admin-product-form">
      <h2>
        {mode === "add"
          ? "Add Size-Based Product"
          : "Update Size-Based Product"}
      </h2>

      <form onSubmit={handleSubmit}>
        <input
          name="name"
          placeholder="Product name"
          value={form.name}
          onChange={handleChange}
          required
        />

        <textarea
          name="description"
          placeholder="Description"
          value={form.description}
          onChange={handleChange}
        />

        <input
          name="price"
          type="number"
          placeholder="Price"
          value={form.price}
          onChange={handleChange}
          required
        />

        <select
          name="categoryId"
          value={form.categoryId}
          onChange={handleCategoryChange}
          required
        >
          <option value="">Select Category</option>
          {categories.map((c) => (
            <option key={c.categoryId} value={c.categoryId}>
              {c.categoryName}
            </option>
          ))}
        </select>

        {/* SIZE PRESETS */}
        {SIZE_PRESETS[selectedCategoryName] && (
          <div className="size-presets">
            <p>Quick Add Sizes:</p>
            {SIZE_PRESETS[selectedCategoryName].map((size) => (
              <button
                key={size}
                type="button"
                onClick={() => addPresetSize(size)}
              >
                {size}
              </button>
            ))}
          </div>
        )}

        {/* SIZE ROWS */}
        <div className="size-stock-box">
          <h4>Size Based Stock</h4>

          {sizes.map((row, index) => (
            <div key={index} className="size-row">
              <input
                placeholder="Size"
                value={row.size}
                onChange={(e) =>
                  updateSize(index, "size", e.target.value)
                }
                required
              />

              <input
                type="number"
                placeholder="Stock"
                value={row.stock}
                onChange={(e) =>
                  updateSize(index, "stock", e.target.value)
                }
                required
              />

              <button
                type="button"
                onClick={() => removeSizeRow(index)}
              >
                ❌
              </button>
            </div>
          ))}

          <button type="button" onClick={addSizeRow}>
            + Add Size
          </button>
        </div>

        <input type="file" multiple onChange={handleImageChange} />

        <button type="submit">
          {mode === "add" ? "Add Product" : "Update Product"}
        </button>
      </form>
    </div>
  );
}
