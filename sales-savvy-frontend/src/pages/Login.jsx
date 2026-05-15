import React from 'react'
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "./Login.css";


function Login() {
    
    const navigate = useNavigate();

    const [form, setForm] = useState({
    username: "",
    password: ""
    });

    const [showPassword, setShowPassword] = useState(false);

    const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await api.post("/api/auth/login", form, {
            withCredentials: true, // IMPORTANT for cookies
            });

            const { role, userId, username } = response.data;

            // 🔥 STORE USER INFO (VERY IMPORTANT)
            localStorage.setItem("userId", userId);
            localStorage.setItem("username", username);
            localStorage.setItem("role", role);

            alert("Login successful!");

            // Redirect based on role
            if (role === "ADMIN") {
            navigate("/admin/dashboard");
            } else {
            navigate("/home");
            }

        } catch (error) {
            alert(error.response?.data?.error || "Login failed");
        }

    };

    return (
    <div className="login-container">

        <div className="login-card">

        <h2 className="login-title">Login</h2>

        <form onSubmit={handleSubmit}>

            <input
                name="username"
                placeholder="Username"
                className="login-input"
                onChange={handleChange}
                required
            />

            <div className="password-wrapper">

                <input
                    name="password"
                    placeholder="Password"
                    type={showPassword ? "text" : "password"}
                    className="login-input"
                    value={form.password}
                    onChange={handleChange}
                    required
                />

                {/* Eye icon */}
                <span
                    className="password-eye"
                    onClick={() => setShowPassword(!showPassword)}
                >
                 {showPassword ? "🙈" : "👁️"}
                 
                </span>

            </div>

            <button className="login-button">Login</button>

        </form>

        <p className="login-footer">
            Don't have an account? <Link to="/">Register</Link>
        </p>

        </div>

    </div>

    );

}

export default Login;