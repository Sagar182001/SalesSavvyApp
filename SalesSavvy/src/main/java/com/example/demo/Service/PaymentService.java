package com.example.demo.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Config.RazorPayConfig;
import com.example.demo.Entity.Address;
import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.OrderStatus;
import com.example.demo.Entity.ProductSize;
import com.example.demo.Entity.User;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ProductSizeRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.CartResponseDTO;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;


@Service
public class PaymentService {

	    private final RazorpayClient razorpayClient;
	    private final OrderRepository orderRepository;
	    private final OrderItemRepository orderItemRepository;
	    private final ProductRepository productRepository;
	    private final CartRepository cartRepository;
	    private final AddressRepository addressRepository;
	    private final UserRepository userRepository;
	    
	    private final ProductSizeRepository productSizeRepository;

	
	    public PaymentService(
	            RazorPayConfig razorPayConfig,
	            OrderRepository orderRepository,
	            OrderItemRepository orderItemRepository,
	            ProductRepository productRepository,
	            CartRepository cartRepository,
	            AddressRepository addressRepository,
	            UserRepository userRepository,
	            ProductSizeRepository productSizeRepository
	    ) throws Exception {
	
	        this.razorpayClient = razorPayConfig.razorpayClient();
	        this.orderRepository = orderRepository;
	        this.orderItemRepository = orderItemRepository;
	        this.productRepository = productRepository;
	        this.cartRepository = cartRepository;
	        this.addressRepository = addressRepository;
	        this.userRepository =  userRepository;
	        this.productSizeRepository =  productSizeRepository;
	    }
	     
	    
	    public String createOrder(int userId, int addressId, double totalAmount, List<CartResponseDTO> cartItems) throws Exception {

	        // fetch address
	        Address address = addressRepository.findById((long) addressId)
	                .orElseThrow(() -> new RuntimeException("Address not found"));

	        // fetch user
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        int amountInPaisa = (int)(totalAmount * 100);

	        JSONObject options = new JSONObject();
	        options.put("amount", amountInPaisa);
	        options.put("currency", "INR");

	        Order razorpayOrder = razorpayClient.orders.create(options);
	        String razorpayOrderId = razorpayOrder.get("id");

	        com.example.demo.Entity.Order order = new com.example.demo.Entity.Order();
	        order.setOrderId(razorpayOrderId);
	        order.setTotalAmount(totalAmount);
	        order.setUser(user);          // <-- FIXED
	        order.setAddress(address);    // <-- SAVE ADDRESS
	        order.setStatus(OrderStatus.PENDING);


	        orderRepository.save(order);

	        return razorpayOrderId;
	    }
	    
	    @Transactional
	    public String verifyPayment(String razorpayOrderId,
	            String razorpayPaymentId,
	            String razorpaySignature,
	            List<CartResponseDTO> cartItems) {
	
			// mock verification
			boolean verified = !razorpaySignature.isEmpty();
			
			com.example.demo.Entity.Order order =
			orderRepository.findById(razorpayOrderId)
			     .orElseThrow(() -> new RuntimeException("Order not found"));
			
			if (!verified) {
				order.setStatus(OrderStatus.FAILED);
				orderRepository.save(order);
				return "Payment verification failed!";
			}
			
			order.setStatus(OrderStatus.SUCCESS);
			// 🔥 REDUCE STOCK AFTER PAYMENT SUCCESS
			for (CartResponseDTO item : cartItems) {

			    // CASE 1️⃣ Product WITH size
			    if (item.getSize() != null && !item.getSize().isEmpty()) {

			        ProductSize productSize =
			                productSizeRepository
			                        .findByProduct_ProductIdAndSize(
			                                item.getProductId(),
			                                item.getSize()
			                        )
			                        .orElseThrow(() ->
			                                new RuntimeException("Product size not found"));

			        if (productSize.getStock() < item.getQuantity()) {
			            throw new RuntimeException(
			                    "Insufficient stock for product size: " + item.getSize()
			            );
			        }

			        productSize.setStock(
			                productSize.getStock() - item.getQuantity()
			        );

			        productSizeRepository.save(productSize);
			    }

			    // CASE 2️⃣ Product WITHOUT size
			    else {
			        var product =
			                productRepository.findById(item.getProductId())
			                        .orElseThrow(() ->
			                                new RuntimeException("Product not found"));

			        if (product.getStock() < item.getQuantity()) {
			            throw new RuntimeException(
			                    "Insufficient stock for product: " + product.getName()
			            );
			        }

			        product.setStock(
			                product.getStock() - item.getQuantity()
			        );

			        productRepository.save(product);
			    }
			}

			orderRepository.save(order);
			
			// SAVE ORDER ITEMS
			for (CartResponseDTO item : cartItems) {
				
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(order);
				orderItem.setProduct(productRepository.findById(item.getProductId()).get());
				orderItem.setSize(item.getSize());
				orderItem.setQuantity(item.getQuantity());
				
				BigDecimal price = item.getPrice();
				BigDecimal qty = new BigDecimal(item.getQuantity()) ;
				BigDecimal total = price.multiply(qty);
				
				orderItem.setPricePerUnit(price);
				orderItem.setTotalPrice(total);
				
				orderItemRepository.save(orderItem);
		  }
			// CLEAR CART
			cartRepository.deleteByUserId(order.getUser().getUser_id());
			
			return "Payment verified successfully!";
		
		}
	    
}  
 
