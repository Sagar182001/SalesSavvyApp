/*import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../Components/Header";
import "./MyOrders.css";

const API_BASE = "http://localhost:9090";

const TRACKING_STEPS = ["CREATED", "CONFIRMED", "SHIPPED", "DELIVERED"];

export default function MyOrders() {
  const [orders, setOrders] = useState([]);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {

      const res = await axios.get(`${API_BASE}/api/orders`, 
        {
          withCredentials: true,
        });

      setOrders(Array.isArray(res.data) ? res.data : []);


    } catch (err) {

      console.error("Failed to load orders", err);
      setOrders([]);

    }
  };

  return (
    <>
      <Header />

      <div className="myorders-page">
        <h2>My Orders</h2>

        {orders.length === 0 && <p>No orders found</p>}

        {orders.map((order) => {
          const currentStep = TRACKING_STEPS.indexOf(
            order.fulfillmentStatus
          );

          return (
            <div key={order.orderId} className="order-card">
              {/* ORDER HEADER 
              <div className="order-header">
                <div>
                  <strong>Order ID:</strong> {order.orderId}
                </div>
                <div>
                  <strong>Placed On:</strong>{" "}
                  {new Date(order.createdAt).toLocaleString()}
                </div>
                <div>
                  <strong>Total:</strong> ₹{order.totalAmount}
                </div>
              </div>

              {/* TRACKING TIMELINE 
              <div className="tracking">
                {TRACKING_STEPS.map((step, index) => (
                  <div
                    key={step}
                    className={`tracking-step ${
                      index <= currentStep ? "active" : ""
                    }`}
                  >
                    <div className="dot"></div>
                    <span>{step}</span>
                    {index < TRACKING_STEPS.length - 1 && (
                      <div className="line"></div>
                    )}
                  </div>
                ))}
              </div>

              {/* ADDRESS 
              <div className="order-address">
                <h4>Delivery Address</h4>
                <p>
                  <strong>{order.address.fullName}</strong>{" "}
                  ({order.address.addressType})
                </p>
                <p>
                  {order.address.street}, {order.address.city},{" "}
                  {order.address.state} - {order.address.pincode}
                </p>
                <p>Phone: {order.address.phone}</p>
              </div>

              {/* ORDER ITEMS 
              <div className="order-items">
                <h4>Items</h4>

                {order.items.map((item, idx) => (
                  <div key={idx} className="order-item">
                    <img
                      src={
                        item.image?.startsWith("http")
                          ? item.image
                          : `${API_BASE}${item.image}`
                      }
                      alt={item.productName}
                    />

                    <div className="item-info">
                      <p className="name">{item.productName}</p>
                      <p>Qty: {item.quantity}</p>
                      <p>₹{item.price}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          );
        })}
      </div>
    </>
  );
} 

import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../Components/Header";
import "./MyOrders.css";

const API_BASE = "http://localhost:9090";

const TRACKING_STEPS = ["CREATED", "CONFIRMED", "SHIPPED", "DELIVERED"];

// Map backend status → UI status
const STATUS_MAP = {
  CREATED: "CREATED",
  CONFIRMED: "CONFIRMED",
  SHIPPED: "SHIPPED",
  DELIVERED: "DELIVERED",
  SUCCESS: "DELIVERED",
};

export default function MyOrders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await axios.get(`${API_BASE}/api/orders`, {
        withCredentials: true,
      });

      setOrders(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Failed to load orders", err);
      setOrders([]);
    }
  };

  return (
    <>
      <Header />

      <div className="myorders-page">
        <h2>My Orders</h2>

        {orders.length === 0 && <p>No orders found</p>}

        {orders.map((order) => {
          const uiStatus =
            STATUS_MAP[order.fulfillmentStatus] || "CREATED";

          const currentStep = TRACKING_STEPS.indexOf(uiStatus);

          return (
            <div key={order.orderId} className="order-card">
              {/* ORDER HEADER 
              <div className="order-header">
                <div>
                  <strong>Order ID:</strong> {order.orderId}
                </div>
                <div>
                  <strong>Placed On:</strong>{" "}
                  {new Date(order.createdAt).toLocaleString()}
                </div>
                <div>
                  <strong>Total:</strong> ₹{order.totalAmount}
                </div>
              </div>

              {/* TRACKING TIMELINE 
              <div className="tracking">
                {TRACKING_STEPS.map((step, index) => {
                  const isCompleted = index < currentStep;
                  const isActive = index === currentStep;

                  return (
                    <div
                      key={step}
                      className={`tracking-step ${
                        isCompleted ? "completed" : ""
                      } ${isActive ? "active" : ""}`}
                    >
                      <div className="dot">
                        {isCompleted && "✓"}
                      </div>

                      <span className="label">{step}</span>

                      {index < TRACKING_STEPS.length - 1 && (
                        <div
                          className={`line ${
                            isCompleted ? "line-active" : ""
                          }`}
                        />
                      )}
                    </div>
                  );
                })}
              </div>

              {/* ADDRESS 
              <div className="order-address">
                <h4>Delivery Address</h4>
                <p>
                  <strong>{order.address?.fullName}</strong>{" "}
                  ({order.address?.addressType})
                </p>
                <p>
                  {order.address?.street}, {order.address?.city},{" "}
                  {order.address?.state} - {order.address?.pincode}
                </p>
                <p>Phone: {order.address?.phone}</p>
              </div>

              {/* ORDER ITEMS 
              <div className="order-items">
                <h4>Items</h4>

                {order.items?.map((item, idx) => (
                  <div key={idx} className="order-item">
                    <img
                      src={
                        item.image?.startsWith("http")
                          ? item.image
                          : `${API_BASE}${item.image}`
                      }
                      alt={item.productName}
                    />

                    <div className="item-info">
                      <p className="name">{item.productName}</p>
                      <p>Qty: {item.quantity}</p>
                      <p>₹{item.price}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          );
        })}
      </div>
    </>
  );
} */

import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../Components/Header";
import "./MyOrders.css";

const API_BASE = "http://localhost:9090";

const TRACKING_STEPS = ["CREATED", "CONFIRMED", "SHIPPED", "DELIVERED"];

export default function MyOrders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await axios.get(`${API_BASE}/api/orders`, {
        withCredentials: true,
      });
      setOrders(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Failed to load orders", err);
      setOrders([]);
    }
  };

  return (
    <>
      <Header />

      <div className="myorders-page">
        <h2>My Orders</h2>

        {orders.length === 0 && <p>No orders found</p>}

        {orders.map((order) => {
          const currentStep = TRACKING_STEPS.indexOf(
            order.fulfillmentStatus || "CREATED"
          );

          return (
            <div key={order.orderId} className="order-card">
              {/* HEADER */}
              <div className="order-header">
                <div><strong>Order ID:</strong> {order.orderId}</div>
                <div>
                  <strong>Placed On:</strong>{" "}
                  {new Date(order.createdAt).toLocaleString()}
                </div>
                <div><strong>Total:</strong> ₹{order.totalAmount}</div>
              </div>

              {/* CANCELLED */}
              {order.fulfillmentStatus === "CANCELLED" ? (
                <p className="cancelled-text">Order Cancelled</p>
              ) : (
                /* TRACKING */
                <div className="tracking">
                  {TRACKING_STEPS.map((step, index) => {
                    const isCompleted = index < currentStep;
                    const isActive = index === currentStep;

                    return (
                      <div
                        key={step}
                        className={`tracking-step ${
                          isCompleted ? "completed" : ""
                        } ${isActive ? "active" : ""}`}
                      >
                        <div className="dot">
                          {isCompleted ? "✓" : index + 1}
                        </div>

                        <span className="label">{step}</span>

                        {index < TRACKING_STEPS.length - 1 && (
                          <div
                            className={`line ${
                              isCompleted ? "line-active" : ""
                            }`}
                          />
                        )}
                      </div>
                    );
                  })}
                </div>
              )}

              {/* ADDRESS */}
              <div className="order-address">
                <h4>Delivery Address</h4>
                <p>
                  <strong>{order.address?.fullName}</strong>{" "}
                  ({order.address?.addressType})
                </p>
                <p>
                  {order.address?.street}, {order.address?.city},{" "}
                  {order.address?.state} - {order.address?.pincode}
                </p>
                <p>Phone: {order.address?.phone}</p>
              </div>

              {/* ITEMS */}
              <div className="order-items">
                <h4>Items</h4>

                {order.items?.map((item, idx) => (
                  <div key={idx} className="order-item">
                    <img
                      src={
                        item.image?.startsWith("http")
                          ? item.image
                          : `${API_BASE}${item.image}`
                      }
                      alt={item.productName}
                    />

                    <div className="item-info">
                      <p className="name">{item.productName}</p>

                      {item.size && (
                        <p className="size">
                          Size: <strong>{item.size}</strong>
                        </p>
                      )}

                      <p>Qty: {item.quantity}</p>
                      <p>₹{item.price}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          );
        })}
      </div>
    </>
  );
}


