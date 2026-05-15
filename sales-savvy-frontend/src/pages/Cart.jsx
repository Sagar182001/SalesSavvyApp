/*import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../Components/Header";
import "./Cart.css";

const API_BASE = "http://localhost:9090";

export default function Cart() {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");

  useEffect(() => {
    fetchCart();
  }, []);

  /* 👇 ADD THIS RIGHT BELOW 
    useEffect(() => {
    console.log("CART ITEMS:", cartItems);
    }, [cartItems]);

  /* ================= FETCH CART ================= 
  const fetchCart = async () => {
    try {
      const res = await axios.get(`${API_BASE}/api/cart/items`, {
       withCredentials: true,
      });

      setCartItems(res.data);
    } catch (err) {
      console.error("Failed to load cart", err);
    } finally {
      setLoading(false);
    }
  };

  /* ================= UPDATE QUANTITY ================= 
  const updateQuantity = async (productId, qty) => {
    if (qty < 1) return;

    try {
      await axios.put(
        `${API_BASE}/api/cart/update`,
        {
          productId,
          quantity: qty,
        },
        {
          withCredentials: true,
        }
      );

      setCartItems((prev) =>
        prev.map((item) =>
          item.productId === productId
            ? { ...item, quantity: qty }
            : item
        )
      );
    } catch (err) {
      console.error("Failed to update quantity", err);
    }
  };

  /* ================= REMOVE ITEM ================= 
  const removeItem = async (productId) => {
    try {
      await axios.delete(
        `${API_BASE}/api/cart/delete/${productId}`,
        {
          withCredentials: true,
        }
      );

      setCartItems((prev) =>
        prev.filter((item) => item.productId !== productId)
      );
    } catch (err) {
      console.error("Failed to remove item", err);
    }
  };

  const totalPrice = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  if (loading) return <p className="loading">Loading cart...</p>;

  return (
    <>
      <Header />

      <div className="cart-page">
        <h2>My Cart</h2>

        {cartItems.length === 0 ? (
          <p>Your cart is empty</p>
        ) : (
          <div className="cart-content">
            {/* LEFT - ITEMS *}
            <div className="cart-items">
              {cartItems.map((item) => (
                <div className="cart-item" key={item.productId}>

                  <img
                  
                    src={
                        item.images?.[0]?.startsWith("http")
                        ? item.images[0]                     // S3 / external image
                        : `${API_BASE}${item.images?.[0]}`   // local uploads
                    }
                    alt={item.productName}
                    onError={(e) => {
                        e.target.src = "/no-image.png"; // optional fallback
                    }}

                  />


                  <div className="cart-info">
                    <h4>{item.productName}</h4>
                    <p>₹{item.price}</p>

                    <div className="qty-control">
                      <button
                        onClick={() =>
                          updateQuantity(item.productId, item.quantity - 1)
                        }
                      >
                        −
                      </button>

                      <span>{item.quantity}</span>

                      <button
                        onClick={() =>
                          updateQuantity(item.productId, item.quantity + 1)
                        }
                      >
                        +
                      </button>
                    </div>

                    <button
                      className="remove-btn"
                      onClick={() => removeItem(item.productId)}
                    >
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* RIGHT - SUMMARY *}
            <div className="cart-summary">
              <h3>Price Details</h3>

              <p>
                Total Items: <span>{cartItems.length}</span>
              </p>

              <p className="total">
                Total Price: <span>₹{totalPrice}</span>
              </p>

              <button
                className="checkout-btn"
                onClick={() => navigate("/checkout")}
              >
                Proceed to Checkout
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
} */

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../Components/Header";
import "./Cart.css";

const API_BASE = "http://localhost:9090";

export default function Cart() {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCart();
  }, []);

  /* ================= FETCH CART ================= */
  const fetchCart = async () => {
    try {
      const res = await axios.get(`${API_BASE}/api/cart/items`, {
        withCredentials: true,
      });
      setCartItems(res.data);
    } catch (err) {
      console.error("Failed to load cart", err);
    } finally {
      setLoading(false);
    }
  };

  /* ================= UPDATE QUANTITY (SIZE-AWARE) ================= */
  const updateQuantity = async (productId, size, qty) => {
    if (qty < 1) return;

    try {
      await axios.put(
        `${API_BASE}/api/cart/update`,
        {
          productId,
          size,        // 🔥 REQUIRED
          quantity: qty,
        },
        { withCredentials: true }
      );

      setCartItems((prev) =>
        prev.map((item) =>
          item.productId === productId && item.size === size
            ? { ...item, quantity: qty }
            : item
        )
      );
    } catch (err) {
      console.error("Failed to update quantity", err);
    }
  };

  /* ================= REMOVE ITEM (SIZE-AWARE) ================= */
  const removeItem = async (productId, size) => {
    try {
      await axios.delete(
        `${API_BASE}/api/cart/delete/${productId}`,
        {
          params: { size }, // 🔥 REQUIRED
          withCredentials: true,
        }
      );

      setCartItems((prev) =>
        prev.filter(
          (item) =>
            !(item.productId === productId && item.size === size)
        )
      );
    } catch (err) {
      console.error("Failed to remove item", err);
    }
  };

  const totalPrice = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  if (loading) return <p className="loading">Loading cart...</p>;

  return (
    <>
      <Header />

      <div className="cart-page">
        <h2>My Cart</h2>

        {cartItems.length === 0 ? (
          <p>Your cart is empty</p>
        ) : (
          <div className="cart-content">
            {/* LEFT - ITEMS */}
            <div className="cart-items">
              {cartItems.map((item) => (
                <div
                  className="cart-item"
                  key={`${item.productId}-${item.size || "nosize"}`} // 🔥 SAFE KEY
                >
                  <img
                    src={
                      item.images?.[0]?.startsWith("http")
                        ? item.images[0]
                        : `${API_BASE}${item.images?.[0]}`
                    }
                    alt={item.productName}
                    onError={(e) => {
                      e.target.src = "/no-image.png";
                    }}
                  />

                  <div className="cart-info">
                    <h4>{item.productName}</h4>

                    {/* 🔥 SHOW SIZE ONLY IF PRESENT */}
                    {item.size && <p>Size: <b>{item.size}</b></p>}

                    <p>₹{item.price}</p>

                    <div className="qty-control">
                      <button
                        onClick={() =>
                          updateQuantity(
                            item.productId,
                            item.size,
                            item.quantity - 1
                          )
                        }
                      >
                        −
                      </button>

                      <span>{item.quantity}</span>

                      <button
                        onClick={() =>
                          updateQuantity(
                            item.productId,
                            item.size,
                            item.quantity + 1
                          )
                        }
                      >
                        +
                      </button>
                    </div>

                    <button
                      className="remove-btn"
                      onClick={() =>
                        removeItem(item.productId, item.size)
                      }
                    >
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* RIGHT - SUMMARY */}
            <div className="cart-summary">
              <h3>Price Details</h3>

              <p>
                Total Items: <span>{cartItems.length}</span>
              </p>

              <p className="total">
                Total Price: <span>₹{totalPrice}</span>
              </p>

              <button
                className="checkout-btn"
                onClick={() => navigate("/checkout")}
              >
                Proceed to Checkout
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
 
