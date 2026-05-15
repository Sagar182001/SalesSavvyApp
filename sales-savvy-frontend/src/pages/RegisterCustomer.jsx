
import React, { useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axiosConfig.js";
import "./Register.css";

function RegisterCustomer() {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [passwordError, setPasswordError] = useState("");

  const validatePassword = (password) => {
    const strongRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return strongRegex.test(password);
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });

    if (e.target.name === "password") {
      setPasswordError(
        validatePassword(e.target.value)
          ? ""
          : "Password must contain 8+ chars, uppercase, lowercase, number & special character"
      );
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (passwordError !== "") {
      alert("Fix password before submitting");
      return;
    }

    try {
      const response = await api.post("/api/users/register", form);
      alert("Customer Registered Successfully");
      window.location.href = "/login";
    } catch (error) {
      alert(error.response?.data?.error || "Registration failed");
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2 className="register-title">Customer Registration</h2>

        <form onSubmit={handleSubmit}>
          <input
            name="username"
            placeholder="Username"
            className="register-input"
            onChange={handleChange}
            required
          />

          <input
            name="email"
            placeholder="Email"
            className="register-input"
            onChange={handleChange}
            required
          />

          <div className="password-wrapper tooltip">
            <input
              name="password"
              placeholder="Password"
              type={showPassword ? "text" : "password"}
              className="register-input password-input"
              onChange={handleChange}
              required
            />

            <span
              className="eye-icon"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? "👁️" : "👁️‍🗨️"}
            </span>

            <span className="tooltip-text">
              Password Requirements:
              <br />• Min 8 characters
              <br />• One uppercase (A–Z)
              <br />• One lowercase (a–z)
              <br />• One number (0–9)
              <br />• One special character (@$!%*?&)
            </span>
          </div>

          {passwordError && <p className="error-text">{passwordError}</p>}

          <button className="register-button">Register</button>
        </form>

        <p className="register-footer">
          Already have an account?<Link to="/login"> Login</Link>
        </p>
      </div>
    </div>
  );
}

export default RegisterCustomer;