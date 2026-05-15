
package com.example.demo.Filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.AuthService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final AuthService authService;
    private final UserRepository userRepository;

    // allow frontend dev origins you use (add more if needed)
    private static final List<String> ALLOWED_ORIGINS = List.of(
        "http://localhost:5173",
        "http://localhost:5174"
    );

    
    
    private static final List<String> OPEN_PATHS = List.of(
    		
    	    "/api/users/register",
    	    "/api/users/admin/register",
    	    "/api/auth/login",

    	    // ✅ public product/category APIs
    	    "/categories",
    	    "/categories/",
    	    "/uploads/",

    	    "/api/products",
    	    "/api/products/",

    	    // ✅ VERY IMPORTANT (fixes 400 loop)
    	    "/error"
    );


    public AuthenticationFilter(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        logger.info("🔥 AuthenticationFilter loaded");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();

	     // ✅ ABSOLUTE BYPASS FOR STATIC FILES
	     if (requestURI.startsWith("/uploads/")) {
	         chain.doFilter(request, response);
	         return;
	     }


        try {
            // Always add CORS headers first so browser won't block preflight/failed responses
            setCORSHeaders(httpRequest, httpResponse);

            //String requestURI = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();
            logger.info("➡️ Request: {} {}", method, requestURI);

            // Allow OPTIONS preflight through (CORS)
            if ("OPTIONS".equalsIgnoreCase(method)) {
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            
            /*for (String path : OPEN_PATHS) {
                if (requestURI.equals(path) || requestURI.startsWith(path)) {
                    chain.doFilter(request, response);
                    return;
                }
            } */

            // Skip authentication for open paths
             for (String path : OPEN_PATHS) {
                if (requestURI.startsWith(path)) {
                    logger.debug("Open path matched: {}", requestURI);
                    chain.doFilter(request, response);
                    return;
                }
            } 
            
            
            
         // 🔑 Extract JWT (HEADER FIRST, cookie fallback)
            String token = null;

            // 2️⃣ Cookie fallback
            if (token == null && httpRequest.getCookies() != null) {
                for (Cookie cookie : httpRequest.getCookies()) {
                    if ("authToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

         // 1️⃣ Authorization header
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
            
            logger.debug("Token resolved from {}",
                    token != null ? (authHeader != null ? "Authorization header" : "Cookie") : "NONE");

            if (token == null || !authService.validateToken(token)) {
                logger.warn("Unauthorized request - missing or invalid token for {}", requestURI);
                sendErrorResponse(httpRequest, httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized: missing or invalid token");
                return;
            }

            // Token valid - get username & user
            String username = authService.extractUsername(token);
            if (username == null) {
                sendErrorResponse(httpRequest, httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized: invalid token");
                return;
            }

            Optional<User> optionalUser = userRepository.findByUsername(username.toLowerCase());
            if (optionalUser.isEmpty()) {
                sendErrorResponse(httpRequest, httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized: user not found");
                return;
            }

            User authenticatedUser = optionalUser.get();
            Role role = authenticatedUser.getRole();

            // Set Spring Security context
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    authenticatedUser, null, List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Admin-only endpoints check (if you have /admin/**)
            if (requestURI.startsWith("/admin/") && role != Role.ADMIN) {
                sendErrorResponse(httpRequest, httpResponse, HttpServletResponse.SC_FORBIDDEN,
                        "Forbidden: admin access required");
                return;
            }

            // attach user for controllers if needed
            httpRequest.setAttribute("authenticatedUser", authenticatedUser);

            // Continue
            chain.doFilter(request, response);
            
            logger.error("🔥 AUTH USER: {} | ROLE: {}", authenticatedUser.getUsername(), role);


        } catch (Exception ex) {
            logger.error("Unexpected error in AuthenticationFilter", ex);
            sendErrorResponse(httpRequest, httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error");
        } finally {
            // no-op
        }
    }
    
    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("authToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // -------------------------
    // CORS helpers
    // -------------------------
    private void setCORSHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    // -------------------------
    // Error responder (always sets CORS headers)
    // -------------------------
    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                   int status, String message) throws IOException {
        setCORSHeaders(request, response);
        response.setStatus(status);
        response.setContentType("text/plain");
        response.getWriter().write(message);
    }

    // -------------------------
    // Cookie token extraction
    // -------------------------
    private String getAuthTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "authToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
    }
} 

