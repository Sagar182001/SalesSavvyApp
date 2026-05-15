import { useEffect, useState } from "react";
import axios from "axios";
import "./AdminDashboard.css";

const API_BASE = "http://localhost:9090";

export default function AdminDashboard() {
  const [revenue, setRevenue] = useState(0);
  const [categorySales, setCategorySales] = useState({});
  const [totalProducts, setTotalProducts] = useState(0);
  const [totalOrders, setTotalOrders] = useState(0);
  const [recentOrders, setRecentOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const [
        businessRes,
        productsRes,
        ordersRes
      ] = await Promise.all([
        axios.get(`${API_BASE}/admin/business/overall`, { withCredentials: true }),
        axios.get(`${API_BASE}/admin/products/all`, { withCredentials: true }),
        axios.get(`${API_BASE}/admin/orders`, { withCredentials: true })
      ]);

      setRevenue(businessRes.data.totalRevenue || 0);
      setCategorySales(businessRes.data.categorySales || {});
      setTotalProducts(productsRes.data.length || 0);
      setTotalOrders(ordersRes.data.length || 0);
      setRecentOrders(ordersRes.data.slice(0, 5));
    } catch (err) {
      console.error("Failed to load admin dashboard", err);
    } finally {
      setLoading(false);
    }
  };

  const getTopCategory = () => {
    if (!categorySales || Object.keys(categorySales).length === 0) return "N/A";
    return Object.entries(categorySales)
      .sort((a, b) => b[1] - a[1])[0][0];
  };

  if (loading) return <p className="admin-loading">Loading dashboard...</p>;

  return (
    <div className="admin-dashboard">
      <h2>Admin Dashboard</h2>

      {/* STAT CARDS */}
      <div className="stats-grid">
        <div className="stat-card">
          <h4>Total Revenue</h4>
          <p>₹ {revenue}</p>
        </div>

        <div className="stat-card">
          <h4>Total Orders</h4>
          <p>{totalOrders}</p>
        </div>

        <div className="stat-card">
          <h4>Total Products</h4>
          <p>{totalProducts}</p>
        </div>

        <div className="stat-card">
          <h4>Top Category</h4>
          <p>{getTopCategory()}</p>
        </div>
      </div>

      {/* RECENT ORDERS */}
      <div className="recent-orders">
        <h3>Recent Orders</h3>

        <table>
          <thead>
            <tr>
              <th>Order ID</th>
              <th>User</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            {recentOrders.map(order => (
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.userName || "User"}</td>
                <td>₹ {order.totalAmount}</td>
                <td>{order.fulfillmentStatus}</td>
                <td>{order.createdAt?.substring(0, 10)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
