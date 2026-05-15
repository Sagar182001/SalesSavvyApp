import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../Components/Header";
import "./Checkout.css";
import { useNavigate } from "react-router-dom";

const API_BASE = "http://localhost:9090";

export default function Checkout() {
  const [cartItems, setCartItems] = useState([]);
  const [addresses, setAddresses] = useState([]);
  const [selectedAddressId, setSelectedAddressId] = useState(null);
 

  const [newAddress, setNewAddress] = useState({
    fullName: "",
    phone: "",
    pincode: "",
    state: "",
    city: "",
    street: "",
    landmark: "",
    addressType: "HOME",
  });

  const [totalPrice, setTotalPrice] = useState(0);

  useEffect(() => {
    fetchCart();
    fetchAddresses();
  }, []);

  /* ================= CART ================= */
  const fetchCart = async () => {
    try {
      const res = await axios.get(`${API_BASE}/api/cart/items`, {
        withCredentials: true,
      });

      setCartItems(res.data || []);

      const total = res.data.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
      );
      setTotalPrice(total);
    } catch (err) {
      console.error("Failed to load cart", err);
    }
  };

  /* ================= ADDRESSES ================= */
  const fetchAddresses = async () => {
    try {
      const userId = localStorage.getItem("userId");

      const res = await axios.get(
        `${API_BASE}/address/user/${userId}`,
        { withCredentials: true }
      );

      setAddresses(res.data || []);
    } catch (err) {
      console.error("Failed to load addresses", err);
    }
  };

  const addAddress = async () => {
    try {
      const userId = localStorage.getItem("userId");

      const res = await axios.post(
        `${API_BASE}/address/add?userId=${userId}`,
        newAddress,
        { withCredentials: true }
      );

      setAddresses([...addresses, res.data]);
      setSelectedAddressId(res.data.id);

      setNewAddress({
        fullName: "",
        phone: "",
        pincode: "",
        state: "",
        city: "",
        street: "",
        landmark: "",
        addressType: "HOME",
      });
    } catch {
      alert("Failed to add address");
    }
  };

  /* ================= PLACE ORDER ================= */
    const navigate = useNavigate();

    const placeOrder = () => {

        if (!selectedAddressId) {
            alert("Please select a delivery address");
            return;
        }

        navigate("/payment", {
            state: {
            addressId: selectedAddressId,
            cartItems,
            totalAmount: totalPrice
            }
        });

    };

  return (
    <>
      <Header />

      <div className="checkout-page">
        {/* LEFT */}
        <div className="checkout-left">
          {/* DELIVERY ADDRESS */}
          <div className="section">
            <h2>Delivery Address</h2>

            {addresses.map((addr) => (
              <label key={addr.id} className="address-card">
                <input
                  type="radio"
                  name="address"
                  checked={selectedAddressId === addr.id}
                  onChange={() => setSelectedAddressId(addr.id)}
                />

                <div>
                  <strong>{addr.fullName}</strong> ({addr.addressType}) <br />
                  {addr.street}, {addr.city}, {addr.state} - {addr.pincode}
                  {addr.landmark && <> , {addr.landmark}</>}
                  <br />
                  Phone: {addr.phone}
                </div>
              </label>
            ))}
          </div>

          {/* ADD NEW ADDRESS */}
        <div className="address-card">
            <h3>Add New Address</h3>

            <div className="address-form">
                <input
                placeholder="Full Name"
                value={newAddress.fullName}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, fullName: e.target.value })
                }
                />

                <input
                placeholder="Phone"
                value={newAddress.phone}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, phone: e.target.value })
                }
                />

                <input
                placeholder="Pincode"
                value={newAddress.pincode}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, pincode: e.target.value })
                }
                />

                <input
                placeholder="State"
                value={newAddress.state}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, state: e.target.value })
                }
                />

                <input
                placeholder="City"
                value={newAddress.city}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, city: e.target.value })
                }
                />

                <input
                placeholder="Street Address"
                value={newAddress.street}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, street: e.target.value })
                }
                />

                <input
                placeholder="Landmark (optional)"
                value={newAddress.landmark}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, landmark: e.target.value })
                }
                />

                {/* ADDRESS TYPE */}
                <select
                value={newAddress.addressType}
                onChange={(e) =>
                    setNewAddress({ ...newAddress, addressType: e.target.value })
                }
                >
                <option value="HOME">Home</option>
                <option value="OFFICE">Office</option>
                <option value="SHOP">Shop</option>
                </select>

                <button className="save-address-btn" onClick={addAddress}>
                Save Address
                </button>
            </div>
        </div>

          {/* ORDER ITEMS */}
          <div className="section">
            <h2>Order Items</h2>

            {cartItems.length === 0 && <p>No items in cart</p>}

            {cartItems.map((item) => (
              <div key={item.productId} className="checkout-item">
                <img
                  src={
                    item.images?.[0]?.startsWith("http")
                      ? item.images[0]
                      : `${API_BASE}${item.images?.[0]}`
                  }
                  alt={item.productName}
                />

                <div className="checkout-item-details">
                  <h4>{item.productName}</h4>
                  <p>Qty: {item.quantity}</p>
                  <p>₹{item.price * item.quantity}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* RIGHT */}
        <div className="checkout-right">
          <h3>Price Details</h3>
          <p>Total Items: {cartItems.length}</p>
          <p>Total Price: ₹{totalPrice}</p>

          <button className="place-order-btn" onClick={placeOrder}>
            Place Order
          </button>
        </div>
      </div>
    </>
  );
}
