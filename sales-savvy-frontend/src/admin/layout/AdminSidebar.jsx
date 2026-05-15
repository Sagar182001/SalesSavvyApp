import { NavLink } from "react-router-dom";

export default function AdminSidebar() {
  return (
    <div className="admin-sidebar">
      <h3>Zaro Admin</h3>

      <NavLink to="/admin/dashboard">Dashboard</NavLink>
      <NavLink to="/admin/products">Products</NavLink>
      <NavLink to="/admin/orders">Orders</NavLink>
    </div>
  );
}
