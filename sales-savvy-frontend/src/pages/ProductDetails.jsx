/*import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./ProductDetails.css";

const API_BASE = "http://localhost:9090";

export default function ProductDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [selectedImage, setSelectedImage] = useState(0);
  const [search, setSearch] = useState("");
  const [showAccount, setShowAccount] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const [loadingCart, setLoadingCart] = useState(false);

  // 🔥 NEW
  const [selectedSize, setSelectedSize] = useState(null);

  useEffect(() => {
    axios
      .get(`${API_BASE}/api/products/${id}`, { withCredentials: true })
      .then((res) => setProduct(res.data))
      .catch(() => alert("Failed to load product"));
  }, [id]);

  const handleSearch = (e) => {
    if (e.key === "Enter" && search.trim()) {
      navigate(`/products?keyword=${search}`);
    }
  };

  const addToCart = async (redirect = false) => {
    // 🔥 Size validation
    if (product.sizes?.length > 0 && !selectedSize) {
      alert("Please select a size");
      return;
    }

    try {
      setLoadingCart(true);

      await axios.post(
        `${API_BASE}/api/cart/add`,
        {
          productId: product.productId,
          quantity,
          sizeId: selectedSize?.sizeId || null,
        },
        { withCredentials: true }
      );

      if (redirect) {
        navigate("/checkout");
      } else {
        alert("Added to cart");
      }
    } catch {
      alert("Please login to add items to cart");
      navigate("/login");
    } finally {
      setLoadingCart(false);
    }
  };

  if (!product) return <p className="loading">Loading...</p>;

  const images =
    product.images?.map((img) =>
      img.startsWith("http") ? img : `${API_BASE}${img}`
    ) || [];

  return (
    <div className="page">
      {/* HEADER }
      <header className="header">
        <div className="logo" onClick={() => navigate("/home")}>
          Zaro
        </div>

        <div className="search-container">
          <input
            className="search"
            placeholder="Search for products"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={handleSearch}
          />

          {search && (
            <span className="clear-icon" onClick={() => setSearch("")}>
              ✖
            </span>
          )}

          <span
            className="search-icon"
            onClick={() =>
              search.trim() && navigate(`/products?keyword=${search}`)
            }
          >
            🔍
          </span>
        </div>

        <div className="header-icons">
          <span
            className="icon"
            onClick={() => navigate("/cart")}
            style={{ cursor: "pointer" }}
          >
            🛒
          </span>

          <div
            className="account"
            onClick={() => setShowAccount(!showAccount)}
          >
            👤 Account
            {showAccount && (
              <div className="login-dropdown">
                <div
                  className="login-item"
                  onClick={() => navigate("/orders")}
                >
                  My Orders
                </div>
                <div
                  className="login-item"
                  onClick={() => {
                    localStorage.removeItem("token");
                    navigate("/login");
                  }}
                >
                  Logout
                </div>
              </div>
            )}
          </div>
        </div>
      </header>

      {/* PRODUCT DETAILS }
      <div className="product-details-page">
        {/* LEFT IMAGES }
        <div className="image-section">
          <div className="thumbnail-list">
            {images.map((img, index) => (
              <img
                key={index}
                src={img}
                alt="thumb"
                className={index === selectedImage ? "thumb active" : "thumb"}
                onClick={() => setSelectedImage(index)}
              />
            ))}
          </div>

          <div className="main-image">
            <img src={images[selectedImage]} alt={product.name} />
          </div>
        </div>

        {/* RIGHT INFO }
        <div className="info-section">
          <h2>{product.name}</h2>
          <p className="category">{product.categoryName}</p>
          <p className="price">₹{product.price}</p>
          <p className="description">{product.description}</p>

          {/* 🔥 SIZE SELECTION }
          {product.sizes && product.sizes.length > 0 && (
            <div className="size-section">
              <p className="size-title">Select Size</p>

              <div className="size-options">
                {product.sizes.map((s) => (
                  <button
                    key={s.sizeId}
                    className={
                      selectedSize?.sizeId === s.sizeId
                        ? "size-btn active"
                        : "size-btn"
                    }
                    disabled={s.stock === 0}
                    onClick={() => setSelectedSize(s)}
                  >
                    {s.size}
                  </button>
                ))}
              </div>

              {selectedSize && (
                <p className="size-stock">Stock: {selectedSize.stock}</p>
              )}
            </div>
          )}

          {/* QUANTITY }
          <div className="quantity-box">
            <span>Quantity:</span>
            <button
              disabled={loadingCart}
              onClick={() => setQuantity((q) => Math.max(1, q - 1))}
            >
              −
            </button>
            <span className="qty">{quantity}</span>
            <button
              disabled={loadingCart}
              onClick={() => setQuantity((q) => q + 1)}
            >
              +
            </button>
          </div>

          {/* ACTIONS }
          <div className="actions">
            <button
              className="add-cart"
              disabled={loadingCart}
              onClick={() => addToCart(false)}
            >
              {loadingCart ? "Adding..." : "Add to Cart"}
            </button>

            <button
              className="buy-now"
              disabled={loadingCart}
              onClick={() => addToCart(true)}
            >
              Buy Now
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}*/

import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./ProductDetails.css";

const API_BASE = "http://localhost:9090";

export default function ProductDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [selectedImage, setSelectedImage] = useState(0);
  const [search, setSearch] = useState("");
  const [showAccount, setShowAccount] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const [loadingCart, setLoadingCart] = useState(false);

  // 🔥 SIZE STATE
  const [selectedSize, setSelectedSize] = useState(null);

  useEffect(() => {
    axios
      .get(`${API_BASE}/api/products/${id}`, { withCredentials: true })
      .then((res) => setProduct(res.data))
      .catch(() => alert("Failed to load product"));
  }, [id]);

  const handleSearch = (e) => {
    if (e.key === "Enter" && search.trim()) {
      navigate(`/products?keyword=${search}`);
    }
  };

  const addToCart = async (redirect = false) => {
    // 🔥 SIZE VALIDATION
    if (product.sizes?.length > 0 && !selectedSize) {
      alert("Please select a size");
      return;
    }

    try {
      setLoadingCart(true);

      await axios.post(
        `${API_BASE}/api/cart/add`,
        {
          productId: product.productId,
          quantity,
          size: selectedSize?.size || null, // ✅ FIXED (STRING SIZE)
        },
        { withCredentials: true }
      );

      if (redirect) {
        navigate("/checkout");
      } else {
        alert("Added to cart");
      }
    } catch {
      alert("Please login to add items to cart");
      navigate("/login");
    } finally {
      setLoadingCart(false);
    }
  };

  if (!product) return <p className="loading">Loading...</p>;

  const images =
    product.images?.map((img) =>
      img.startsWith("http") ? img : `${API_BASE}${img}`
    ) || [];

  return (
    <div className="page">
      {/* HEADER */}
      <header className="header">
        <div className="logo" onClick={() => navigate("/home")}>
          Zaro
        </div>

        <div className="search-container">
          <input
            className="search"
            placeholder="Search for products"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={handleSearch}
          />

          {search && (
            <span className="clear-icon" onClick={() => setSearch("")}>
              ✖
            </span>
          )}

          <span
            className="search-icon"
            onClick={() =>
              search.trim() && navigate(`/products?keyword=${search}`)
            }
          >
            🔍
          </span>
        </div>

        <div className="header-icons">
          <span
            className="icon"
            onClick={() => navigate("/cart")}
            style={{ cursor: "pointer" }}
          >
            🛒
          </span>

          <div
            className="account"
            onClick={() => setShowAccount(!showAccount)}
          >
            👤 Account
            {showAccount && (
              <div className="login-dropdown">
                <div
                  className="login-item"
                  onClick={() => navigate("/orders")}
                >
                  My Orders
                </div>
                <div
                  className="login-item"
                  onClick={() => {
                    localStorage.removeItem("token");
                    navigate("/login");
                  }}
                >
                  Logout
                </div>
              </div>
            )}
          </div>
        </div>
      </header>

      {/* PRODUCT DETAILS */}
      <div className="product-details-page">
        {/* LEFT IMAGES */}
        <div className="image-section">
          <div className="thumbnail-list">
            {images.map((img, index) => (
              <img
                key={index}
                src={img}
                alt="thumb"
                className={index === selectedImage ? "thumb active" : "thumb"}
                onClick={() => setSelectedImage(index)}
              />
            ))}
          </div>

          <div className="main-image">
            <img src={images[selectedImage]} alt={product.name} />
          </div>
        </div>

        {/* RIGHT INFO */}
        <div className="info-section">
          <h2>{product.name}</h2>
          <p className="category">{product.categoryName}</p>
          <p className="price">₹{product.price}</p>
          <p className="description">{product.description}</p>

          {/* 🔥 SIZE SELECTION */}
          {product.sizes && product.sizes.length > 0 && (
            <div className="size-section">
              <p className="size-title">Select Size</p>

              <div className="size-options">
                {product.sizes.map((s) => (
                  <button
                    key={s.sizeId}
                    className={
                      selectedSize?.sizeId === s.sizeId
                        ? "size-btn active"
                        : "size-btn"
                    }
                    disabled={s.stock === 0}
                    onClick={() => setSelectedSize(s)}
                  >
                    {s.size}
                  </button>
                ))}
              </div>

              {selectedSize && (
                <p className="size-stock">Stock: {selectedSize.stock}</p>
              )}
            </div>
          )}

          {/* QUANTITY */}
          <div className="quantity-box">
            <span>Quantity:</span>
            <button
              disabled={loadingCart}
              onClick={() => setQuantity((q) => Math.max(1, q - 1))}
            >
              −
            </button>
            <span className="qty">{quantity}</span>
            <button
              disabled={loadingCart}
              onClick={() => setQuantity((q) => q + 1)}
            >
              +
            </button>
          </div>

          {/* ACTIONS */}
          <div className="actions">
            <button
              className="add-cart"
              disabled={loadingCart}
              onClick={() => addToCart(false)}
            >
              {loadingCart ? "Adding..." : "Add to Cart"}
            </button>

            <button
              className="buy-now"
              disabled={loadingCart}
              onClick={() => addToCart(true)}
            >
              Buy Now
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

