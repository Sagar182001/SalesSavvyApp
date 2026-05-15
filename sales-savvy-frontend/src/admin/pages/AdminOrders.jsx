import { useEffect, useState } from "react";
import axios from "axios";
import "./AdminOrders.css";

const API_BASE = "http://localhost:9090";

const FULFILLMENT_STATUSES = [
  "CREATED",
  "CONFIRMED",
  "SHIPPED",
  "DELIVERED",
  "CANCELLED",
];

export default function AdminOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await axios.get(`${API_BASE}/admin/orders`, {
        withCredentials: true,
      });
      setOrders(res.data || []);
    } catch (err) {
      console.error("Failed to fetch orders", err);
    } finally {
      setLoading(false);
    }
  };

  const updateFulfillmentStatus = async (orderId, newStatus) => {
    try {
      setUpdatingId(orderId);

      const res = await axios.put(
        `${API_BASE}/admin/orders/${orderId}/fulfillment-status`,
        { fulfillmentStatus: newStatus },
        { withCredentials: true }
      );

      // Update only the changed row (no full reload)
      setOrders((prev) =>
        prev.map((o) =>
          o.orderId === orderId ? res.data : o
        )
      );
    } catch (err) {
      alert("Failed to update fulfillment status");
      console.error(err);
    } finally {
      setUpdatingId(null);
    }
  };

  if (loading) return <p>Loading orders...</p>;

  return (
    <div className="admin-orders">
      <h2>Orders Management</h2>

      <table className="orders-table">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>User ID</th>
            <th>Amount</th>
            <th>Payment</th>
            <th>Delivery Status</th>
            <th>Date</th>
          </tr>
        </thead>

        <tbody>
          {orders.map((order) => (
            <tr key={order.orderId}>
              <td>{order.orderId}</td>
              <td>{order.userId}</td>
              <td>₹ {order.totalAmount}</td>

              {/* PAYMENT STATUS */}
              <td>
                <span
                  className={`badge payment ${order.paymentStatus.toLowerCase()}`}
                >
                  {order.paymentStatus}
                </span>
              </td>

              {/* FULFILLMENT STATUS */}
              <td>
                <select
                  value={order.fulfillmentStatus}
                  disabled={
                    order.paymentStatus !== "SUCCESS" ||
                    updatingId === order.orderId
                  }
                  onChange={(e) =>
                    updateFulfillmentStatus(
                      order.orderId,
                      e.target.value
                    )
                  }
                >
                  {FULFILLMENT_STATUSES.map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>
              </td>

              <td>{order.createdAt?.substring(0, 10)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
