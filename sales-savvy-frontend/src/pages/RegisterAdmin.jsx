import React from 'react'
import { useState } from 'react';
import "./Register.css";
import api from "../api/axiosConfig";
import { Link } from "react-router-dom";


function RegisterAdmin() {

  const [form, setForm] = useState({
    username: "",
    email: "",
    password: ""
  });

  const [secret, setSecret] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {

    e.preventDefault();
    console.log("Form Submitted");

    try {
      const response = await api.post(
        "/api/users/admin/register", 
        {
          username: form.username,
          email: form.email,
          password: form.password,
          secret: secret
        }
      );

      alert("Admin Registered Successfully");

    } catch (error) {
      alert(error.response?.data?.error || "Admin Creation Failed");
    }
};


  return (

    <div className="register-container">
      <div className="register-card">

        <h2 className="register-title">Admin Registration</h2>

        <form onSubmit={handleSubmit}>
          <input name="username" placeholder="Username" className="register-input" onChange={handleChange} required />
          <input name="email" placeholder="Email" className="register-input" onChange={handleChange} required />
          <input name="password" placeholder="Password" type="password" className="register-input" onChange={handleChange} required />

          <input placeholder="Admin Secret Key" className="register-input" onChange={(e) => setSecret(e.target.value)} required />

          <button className="register-button">Create Admin</button>
        </form>
        <p className="register-footer">
          <Link to="/login"> Login </Link>
        </p>
      </div>
    </div>

    
  );

}

export default RegisterAdmin;