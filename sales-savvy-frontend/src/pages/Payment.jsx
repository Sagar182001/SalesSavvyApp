import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../Components/Header";
import "./Payment.css";

const API_BASE = "http://localhost:9090";

export default function Payment() {
  const navigate = useNavigate();
  const { state } = useLocation();

  const { addressId, cartItems, totalAmount } = state || {};
  const userId = localStorage.getItem("userId");

  const [paymentMethod, setPaymentMethod] = useState("UPI");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!addressId || !cartItems || cartItems.length === 0) {
      navigate("/checkout");
    }
  }, [addressId, cartItems, navigate]);

  const handlePayment = async () => {
    try {
      setLoading(true);

      /* 1️⃣ CREATE ORDER (Backend + Razorpay dummy) */
      const createRes = await axios.post(
        `${API_BASE}/api/payment/create`,
        cartItems,
        {
          params: {
            userId,
            addressId,
            totalAmount,
          },
          withCredentials: true,
        }
      );

      const { razorpayOrderId } = createRes.data;

      /* 2️⃣ VERIFY PAYMENT (MOCK SUCCESS) */
      const verifyRes = await axios.post(
        `${API_BASE}/api/payment/verify`,
        cartItems,
        {
          params: {
            razorpayOrderId,
            razorpayPaymentId: "MOCK_PAYMENT_ID",
            razorpaySignature: "MOCK_SIGNATURE",
          },
          withCredentials: true,
        }
      );

      alert(verifyRes.data.message);

      /* 3️⃣ REDIRECT TO ORDER SUCCESS PAGE ✅ */
      navigate(`/order-success/${razorpayOrderId}`);

    } catch (err) {
      console.error(err);
      alert("Payment failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Header />

      <div className="payment-page">
        <div className="payment-card">
          <h2>Select Payment Method</h2>

          <label>
            <input
              type="radio"
              checked={paymentMethod === "UPI"}
              onChange={() => setPaymentMethod("UPI")}
            />
            UPI
          </label>

          <label>
            <input
              type="radio"
              checked={paymentMethod === "COD"}
              onChange={() => setPaymentMethod("COD")}
            />
            Cash on Delivery
          </label>

          <div className="payment-summary">
            <p>Total Items: {cartItems?.length || 0}</p>
            <p>
              <strong>Total Amount: ₹{totalAmount}</strong>
            </p>
          </div>

          <button onClick={handlePayment} disabled={loading}>
            {loading ? "Processing..." : "Pay Now"}
          </button>
        </div>
      </div>
    </>
  );
}
