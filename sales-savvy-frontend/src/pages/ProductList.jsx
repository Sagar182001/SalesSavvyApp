 /* import { useEffect, useState } from "react";
import { useParams, useSearchParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../Components/Header";
import "./ProductList.css";

const API_BASE = "http://localhost:9090";

const categories = [
  "Beauty",
  "Electronics",
  "Furniture",
  "Groceries",
  "Medicines",
  "Mobile Accessory",
  "Mobiles",
  "Pants",
  "Shirts",
  "Smart Phones",
];

export default function ProductList() {
  const { category } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const keyword = searchParams.get("keyword");

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  // filters (frontend level for now)
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");

  useEffect(() => {
    fetchProducts();
  }, [category, keyword]);

  const fetchProducts = async () => {
    try {
      setLoading(true);

      let url = "http://localhost:9090/api/products";

      if (category) {
        url += `?category=${category}`;
      } else if (keyword) {
        url += `?keyword=${keyword}`;
      }

      const res = await axios.get(url, {
        withCredentials: true,
      });

      setProducts(res.data);
    } catch (err) {
      console.error("Failed to fetch products", err);
    } finally {
      setLoading(false);
    }
  };

  // apply frontend price filter
  const filteredProducts = products.filter((p) => {
    if (minPrice && p.price < minPrice) return false;
    if (maxPrice && p.price > maxPrice) return false;
    return true;
  });

  if (loading) return <h2 className="loading">Loading...</h2>;

  return (
    <>
      {/* HEADER }
      <Header />

      {/* CATEGORY BAR }
      <div className="category-bar">
        {categories.map((cat) => (
          <span
            key={cat}
            className={`category-item ${
              cat === category ? "active" : ""
            }`}
            onClick={() => navigate(`/products/${cat}`)}
          >
            {cat}
          </span>
        ))}
      </div>

      {/* PAGE CONTENT }
      <div className="product-page">
        <h2 className="page-title">
          {category
            ? category
            : keyword
            ? `Search results for "${keyword}"`
            : "All Products"}
        </h2>

        <div className="content">
          {/* FILTERS }
          <aside className="filters">
            <h3>Filters</h3>

            <div className="filter-group">
              <label>Min Price</label>
              <input
                type="number"
                value={minPrice}
                onChange={(e) => setMinPrice(e.target.value)}
              />
            </div>

            <div className="filter-group">
              <label>Max Price</label>
              <input
                type="number"
                value={maxPrice}
                onChange={(e) => setMaxPrice(e.target.value)}
              />
            </div>

            <button
              className="clear-btn"
              onClick={() => {
                setMinPrice("");
                setMaxPrice("");
              }}
            >
              Clear Filters
            </button>
          </aside>

          {/* PRODUCT GRID }
          <div className="product-grid">
            {filteredProducts.length === 0 && (
              <p>No products found</p>
            )}

            {filteredProducts.map((p) => (
              <div
                key={p.productId}
                className="product-card"
                onClick={() => navigate(`/product/${p.productId}`)}
              >
                 <img
                    className="product-img"
                    src={
                      p.images?.[0]?.startsWith("http")
                        ? p.images[0]                     // S3 image
                        : `${API_BASE}${p.images?.[0]}`   // local upload
                    }
                    alt={p.productName}
                    onError={(e) => {
                      e.target.src = "/no-image.png";
                    }}
                 />


                <h3>{p.name}</h3>
                <p className="price">₹{p.price}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
} */

import { useEffect, useState } from "react";
import {
  useParams,
  useSearchParams,
  useNavigate,
} from "react-router-dom";
import axios from "axios";
import Header from "../Components/Header";
import "./ProductList.css";

const API_BASE = "http://localhost:9090";

export default function ProductList() {
  const { category } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const keyword = searchParams.get("keyword");

  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  // filters (frontend)
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");

  /* ================= LOAD CATEGORIES ================= */
  useEffect(() => {
    axios
      .get(`${API_BASE}/categories`)
      .then((res) => setCategories(res.data || []))
      .catch((err) =>
        console.error("Failed to load categories", err)
      );
  }, []);

  /* ================= LOAD PRODUCTS ================= */
  useEffect(() => {
    fetchProducts();
  }, [category, keyword]);

  const fetchProducts = async () => {
    try {
      setLoading(true);

      let url = `${API_BASE}/api/products`;

      if (category) {
        url += `?category=${category}`;
      } else if (keyword) {
        url += `?keyword=${keyword}`;
      }

      const res = await axios.get(url, {
        withCredentials: true,
      });

      setProducts(res.data || []);
    } catch (err) {
      console.error("Failed to fetch products", err);
    } finally {
      setLoading(false);
    }
  };

  /* ================= FRONTEND PRICE FILTER ================= */
  const filteredProducts = products.filter((p) => {
    if (minPrice && p.price < Number(minPrice)) return false;
    if (maxPrice && p.price > Number(maxPrice)) return false;
    return true;
  });

  if (loading) return <h2 className="loading">Loading...</h2>;

  return (
    <>
      {/* HEADER */}
      <Header />

      {/* CATEGORY BAR */}
      <div className="category-bar">
        {categories.length === 0 && (
          <span className="category-item">Loading...</span>
        )}

        {categories.map((cat) => (
          <span
            key={cat.categoryId}
            className={`category-item ${
              cat.categoryName === category ? "active" : ""
            }`}
            onClick={() =>
              navigate(`/products/${cat.categoryName}`)
            }
          >
            {cat.categoryName}
          </span>
        ))}
      </div>

      {/* PAGE CONTENT */}
      <div className="product-page">
        <h2 className="page-title">
          {category
            ? category
            : keyword
            ? `Search results for "${keyword}"`
            : "All Products"}
        </h2>

        <div className="content">
          {/* FILTERS */}
          <aside className="filters">
            <h3>Filters</h3>

            <div className="filter-group">
              <label>Min Price</label>
              <input
                type="number"
                value={minPrice}
                onChange={(e) => setMinPrice(e.target.value)}
              />
            </div>

            <div className="filter-group">
              <label>Max Price</label>
              <input
                type="number"
                value={maxPrice}
                onChange={(e) => setMaxPrice(e.target.value)}
              />
            </div>

            <button
              className="clear-btn"
              onClick={() => {
                setMinPrice("");
                setMaxPrice("");
              }}
            >
              Clear Filters
            </button>
          </aside>

          {/* PRODUCT GRID */}
          <div className="product-grid">
            {filteredProducts.length === 0 && (
              <p>No products found</p>
            )}

            {filteredProducts.map((p) => (
              <div
                key={p.productId}
                className="product-card"
                onClick={() =>
                  navigate(`/product/${p.productId}`)
                }
              >
                <img
                  className="product-img"
                  src={
                    p.images?.[0]?.startsWith("http")
                      ? p.images[0] // S3
                      : `${API_BASE}${p.images?.[0]}` // local
                  }
                  alt={p.name}
                  onError={(e) => {
                    e.target.src = "/no-image.png";
                  }}
                />

                <h3>{p.name}</h3>
                <p className="price">₹{p.price}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
}
