import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Header.css";

export default function Header() {
  const navigate = useNavigate();

  const [query, setQuery] = useState("");
  const [showAccount, setShowAccount] = useState(false);

  const handleSearch = (e) => {
    e.preventDefault();
    if (!query.trim()) return;
    navigate(`/products?keyword=${query}`);
    setQuery("");
  };

  return (
    <header className="header">
      {/* LOGO */}
      <div className="logo" onClick={() => navigate("/home")}>
        Zaro
      </div>

      {/* SEARCH */}
      <form className="search-form" onSubmit={handleSearch}>
        <input
          className="search-input"
          type="text"
          placeholder="Search for products"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />

        {query && (
          <span className="clear-icon" onClick={() => setQuery("")}>
            ❌
          </span>
        )}

        <button className="search-btn" type="submit">
          🔍
        </button>
      </form>

      {/* ICONS */}
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
          onClick={() => setShowAccount(!showAccount)}
        >
          <span className="icon">👤 Account ⌄</span>

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
  );
}
