import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function AdminHeader() {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  // 👉 Get admin name (adjust key if needed)
  const adminName = localStorage.getItem("username") || "Admin";

  const handleLogout = () => {
    // SAME AS USER LOGOUT
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="admin-header">
      <h2 className="logo">Zaro</h2>

      <div className="admin-profile">
        <span
          onClick={() => setOpen(!open)}
          className="profile-name"
        >
          {adminName} ⌄
        </span>

        {open && (
          <div className="dropdown">
            {/* ADMIN NAME */}
            <div className="dropdown-user">
              <strong>{adminName}</strong>
              <span>Administrator</span>
            </div>

            <hr />

            {/* LOGOUT */}
            <button onClick={handleLogout}>Logout</button>
          </div>
        )}
      </div>
    </div>
  );
}
