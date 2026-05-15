/*import { BrowserRouter, Routes, Route} from "react-router-dom";
import RegisterCustomer from "./pages/RegisterCustomer";
import RegisterAdmin from "./pages/RegisterAdmin";
import Login from "./pages/Login";
import Home from "./pages/Home";
import ProductDetails from "./pages/ProductDetails";
import ProductList from "./pages/ProductList";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import Payment from "./pages/Payment";
import OrderSuccess from "./pages/OrderSuccess";
import MyOrders from "./pages/MyOrders";
import AdminDashboard from "./admin/pages/AdminDashboard";

function App() {
  
  return (
    
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RegisterCustomer />} />
        <Route path="/admin-register" element={<RegisterAdmin />} />
        <Route path="/login" element={<Login />} />

        {/* After login redirects 
        <Route path="/home" element={<Home />} />
        
        <Route path="/products/:category" element={<ProductList />} />
        <Route path="/products" element={<ProductList />} />
        <Route path="/product/:id" element={<ProductDetails />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/checkout" element={<Checkout />} />
        <Route path="/payment" element={<Payment />} />
        <Route path="/order-success/:orderId" element={<OrderSuccess />} />
        <Route path="/orders" element={<MyOrders />} />
        
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/admin/products" element={<h1>Admin Products Page</h1>} />

      </Routes>
    </BrowserRouter>

  );
}

export default App; */

import { BrowserRouter, Routes, Route } from "react-router-dom";
import RegisterCustomer from "./pages/RegisterCustomer";
import RegisterAdmin from "./pages/RegisterAdmin";
import Login from "./pages/Login";
import Home from "./pages/Home";
import ProductDetails from "./pages/ProductDetails";
import ProductList from "./pages/ProductList";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import Payment from "./pages/Payment";
import OrderSuccess from "./pages/OrderSuccess";
import MyOrders from "./pages/MyOrders";

import AdminLayout from "./admin/layout/AdminLayout";
import AdminDashboard from "./admin/pages/AdminDashboard";
import AdminOrders from "./admin/pages/AdminOrders";
import AdminProducts from "./admin/pages/AdminProducts";
import AdminProductForm from "./admin/pages/AdminProductForm";
import AdminSizeProductForm from "./admin/pages/AdminSizeProductForm";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* PUBLIC */}
        <Route path="/" element={<RegisterCustomer />} />
        <Route path="/admin-register" element={<RegisterAdmin />} />
        <Route path="/login" element={<Login />} />

        {/* USER */}
        <Route path="/home" element={<Home />} />
        <Route path="/products/:category" element={<ProductList />} />
        <Route path="/products" element={<ProductList />} />
        <Route path="/product/:id" element={<ProductDetails />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/checkout" element={<Checkout />} />
        <Route path="/payment" element={<Payment />} />
        <Route path="/order-success/:orderId" element={<OrderSuccess />} />
        <Route path="/orders" element={<MyOrders />} />

        {/* ADMIN (WITH LAYOUT) */}
        <Route path="/admin" element={<AdminLayout />}>
          <Route path="dashboard" element={<AdminDashboard />} />
          <Route path="products" element={<AdminProducts/>} />
          <Route path="orders" element={<AdminOrders />} />
          <Route path="/admin/products/add" element={<AdminProductForm mode="add" />} />
          <Route path="/admin/products/edit/:productId" element={<AdminProductForm mode="edit" />} />
          <Route path="/admin/size-products/add" element={<AdminSizeProductForm mode="add" />} />
          <Route path="/admin/size-products/edit/:productId" element={<AdminSizeProductForm mode="edit" />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

