 /*import React from 'react'
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";

function Home() {

    const navigate = useNavigate();
    const [search, setSearch] = useState("");
    const [showLoginMenu, setShowLoginMenu] = useState(false);
    
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


    const handleSearch = (e) => {

        if (e.key === "Enter" && search.trim() !== "") {
            navigate(`/products?keyword=${query}`);
        }
};
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
                    type="text"
                    placeholder="Search for products"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    onKeyDown={handleSearch}
                />
                <span
                    className="search-icon"
                    onClick={() => {
                    if (search.trim() !== "") {
                        navigate(`/products/${search}`);
                    }
                    }}
                >
                🔍
                </span>

                {search && (
                    <span
                    className="clear-icon"
                    onClick={() => setSearch("")}
                    >
                    ❌
                    </span>
                )}
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
                    className="login-wrapper"
                    onClick={() => setShowLoginMenu(!showLoginMenu)}
                >
                    <span className="icon">👤 Account</span>

                    {showLoginMenu && (
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


        {/* NAVBAR (NO DROPDOWN) }
        <nav className="navbar">
            <ul className="nav-list">

                {categories.map((cat) => (
                <li
                key={cat}
                className="nav-item"
                onClick={() => navigate(`/products/${cat}`)}
                >
                {cat}
                </li>
                ))}

            </ul>
        </nav>


        {/* MAIN CONTENT }
        <main className="main">
            
            <div className="banner">60–80% OFF</div>

        </main>


        {/* FOOTER }
        <footer className="footer">Contact us on @Zaro.com</footer>
    </div>
  );
}

export default Home; */

import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Home.css";

const API_BASE = "http://localhost:9090";

function Home() {
  const navigate = useNavigate();

  const [search, setSearch] = useState("");
  const [showLoginMenu, setShowLoginMenu] = useState(false);
  const [categories, setCategories] = useState([]);

  /* ================= LOAD CATEGORIES ================= */
  useEffect(() => {
    axios
      .get(`${API_BASE}/categories`)
      .then((res) => {
        setCategories(res.data || []);
      })
      .catch((err) => {
        console.error("Failed to load categories", err);
      });
  }, []);

  /* ================= SEARCH ================= */
  const handleSearch = (e) => {
    if (e.key === "Enter" && search.trim() !== "") {
      navigate(`/products?keyword=${search}`);
    }
  };

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
            type="text"
            placeholder="Search for products"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={handleSearch}
          />

          <span
            className="search-icon"
            onClick={() => {
              if (search.trim() !== "") {
                navigate(`/products?keyword=${search}`);
              }
            }}
          >
            🔍
          </span>

          {search && (
            <span
              className="clear-icon"
              onClick={() => setSearch("")}
            >
              ❌
            </span>
          )}
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
            className="login-wrapper"
            onClick={() => setShowLoginMenu(!showLoginMenu)}
          >
            <span className="icon">👤 Account</span>

            {showLoginMenu && (
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

      {/* NAVBAR */}
      <nav className="navbar">
        <ul className="nav-list">
          {categories.length === 0 && (
            <li className="nav-item">Loading...</li>
          )}

          {categories.map((cat) => (
            <li
              key={cat.categoryId}
              className="nav-item"
              onClick={() =>
                navigate(`/products/${cat.categoryName}`)
              }
            >
              {cat.categoryName}
            </li>
          ))}
        </ul>
      </nav>

      {/* MAIN CONTENT */}
      <main className="main">
        <div className="banner">60–80% OFF</div>
      </main>

      {/* FOOTER */}
      <footer className="footer">
        Contact us on @Zaro.com
      </footer>
    </div>
  );
}

export default Home;
