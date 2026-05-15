import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../Components/Header";
import "./OrderSuccess.css";

export default function OrderSuccess() {
  const { orderId } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    // Clear cart badge / local cache if any
    localStorage.removeItem("cartCount");
  }, []);

  return (
    <>
      <Header />

      <div className="order-success-page">
        <div className="success-card">
          <div className="success-icon">✅</div>

          <h2>Order Placed Successfully!</h2>

          <p className="order-id">
            Order ID: <strong>{orderId}</strong>
          </p>

          <div className="status">
            Payment Status: <span>SUCCESS</span>
          </div>

          <div className="status">
            Order Status: <span>CONFIRMED</span>
          </div>

          <div className="actions">
            <button onClick={() => navigate("/orders")}>
              View My Orders
            </button>

            <button className="secondary" onClick={() => navigate("/home")}>
              Continue Shopping
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
