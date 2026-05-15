import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./AdminProducts.css";

const API_BASE = "http://localhost:9090";

export default function AdminProducts() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await axios.get(`${API_BASE}/admin/products/all`, {
        withCredentials: true,
      });
      setProducts(res.data || []);
    } catch (err) {
      console.error("Failed to load products", err);
    } finally {
      setLoading(false);
    }
  };

  const deleteProduct = async (productId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this product?"
    );

    if (!confirmDelete) return;

    try {
      await axios.delete(
        `${API_BASE}/admin/products/delete/${productId}`,
        { withCredentials: true }
      );

      setProducts((prev) =>
        prev.filter((p) => p.productId !== productId)
      );
    } catch (err) {
      alert("Failed to delete product");
      console.error(err);
    }
  };

  if (loading) return <p>Loading products...</p>;

  return (
    <div className="admin-products">
      <div className="products-header">
        <h2>Products</h2>

        <div className="add-product-actions">
          <button
            className="add-btn"
            onClick={() => navigate("/admin/products/add")}
          >
            + Add Simple Product
          </button>

          <button
            className="add-btn size-btn"
            onClick={() => navigate("/admin/size-products/add")}
          >
            + Add Size-Based Product
          </button>
        </div>

      </div>

      <table className="products-table">
        <thead>
          <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {products.length === 0 && (
            <tr>
              <td colSpan="6">No products found</td>
            </tr>
          )}

          {products.map((product) => {
            const image = product.images?.[0];

            const imageSrc = image
              ? image.startsWith("http")
                ? image                 // ✅ AWS S3 image
                : `${API_BASE}${image}` // ✅ Local upload
              : "/no-image.png";        // ✅ Fallback

            return (
              <tr key={product.productId}>
                <td>
                  <img
                    className="product-img"
                    src={imageSrc}
                    alt={product.name}
                  />
                </td>

                <td>{product.name}</td>
                <td>{product.categoryName}</td>
                <td>₹ {product.price}</td>
                <td>{product.stock}</td>

                <td className="actions">
                  <button
                    className="edit-btn"
                    onClick={() => {
                      if (product.sizes && product.sizes.length > 0) {
                        navigate(`/admin/size-products/edit/${product.productId}`);
                      } else {
                        navigate(`/admin/products/edit/${product.productId}`);
                      }
                    }}
                  >
                    Edit
                  </button>


                  <button
                    className="delete-btn"
                    onClick={() => deleteProduct(product.productId)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

