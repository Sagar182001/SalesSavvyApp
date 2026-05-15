import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import "./AdminProductForm.css";

const API_BASE = "http://localhost:9090";

export default function AdminProductForm({ mode = "add" }) {
  const navigate = useNavigate();
  const { productId } = useParams();

  const [categories, setCategories] = useState([]);
  const [images, setImages] = useState([]);
  const [form, setForm] = useState({
    name: "",
    description: "",
    price: "",
    stock: "",
    categoryId: "",
  });

  /* ================= LOAD CATEGORIES ================= */
  useEffect(() => {
    axios
      .get(`${API_BASE}/categories`, { withCredentials: true })
      .then((res) => setCategories(res.data || []))
      .catch(() => alert("Failed to load categories"));
  }, []);

  /* ================= LOAD PRODUCT (EDIT MODE) ================= */
  useEffect(() => {
    if (mode === "edit" && productId) {
      axios
        .get(`${API_BASE}/admin/products/all`, { withCredentials: true })
        .then((res) => {
          const product = res.data.find(
            (p) => p.productId === Number(productId)
          );
          if (product) {
            setForm({
              name: product.name,
              description: product.description,
              price: product.price,
              stock: product.stock,
              categoryId: product.categoryId,
            });
          }
        });
    }
  }, [mode, productId]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleImageChange = (e) => {
    setImages([...e.target.files]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();

    const productPayload = {
      name: form.name,
      description: form.description,
      price: Number(form.price),
      stock: Number(form.stock),
      categoryId: parseInt(form.categoryId, 10),
    };

    console.log("Submitting product payload:", productPayload);

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
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
      } else {
        await axios.put(
          `${API_BASE}/admin/products/update/${productId}`,
          data,
          {
            withCredentials: true,
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
      }

      navigate("/admin/products");
    } catch (err) {
      console.error("ADMIN ADD PRODUCT ERROR:", err.response || err);
      alert(
        err.response?.data?.message ||
        "Failed to save product"
      );
    }
  };


  return (
    <div className="admin-product-form">
      <h2>{mode === "add" ? "Add Product" : "Update Product"}</h2>

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

        <input
          name="stock"
          type="number"
          placeholder="Stock"
          value={form.stock}
          onChange={handleChange}
          required
        />

        <select
          name="categoryId"
          value={form.categoryId}
          onChange={handleChange}
          required
        >
          <option value="">Select Category</option>
          {categories.map((c) => (
            <option key={c.categoryId} value={c.categoryId}>
              {c.categoryName}
            </option>
          ))}
        </select>

        <input type="file" multiple onChange={handleImageChange} />

        <button type="submit">
          {mode === "add" ? "Add Product" : "Update Product"}
        </button>
      </form>
    </div>
  );
}
