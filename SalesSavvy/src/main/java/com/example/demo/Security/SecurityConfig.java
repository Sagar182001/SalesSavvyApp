package com.example.demo.Security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.Filters.AuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	
    private final AuthenticationFilter authenticationFilter;
     
    
	public SecurityConfig(AuthenticationFilter authenticationFilter) {
		super();
		this.authenticationFilter = authenticationFilter;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return web -> web.ignoring()
	            .requestMatchers("/uploads/**");
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/users/admin/register").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/uploads**").permitAll()  // ⭐ PUBLIC
                .requestMatchers("/categories").permitAll()
                .requestMatchers("/api/cart/**").authenticated()   // ⭐ TOKEN REQUIRED
                .requestMatchers("/admin/**").hasRole("ADMIN")     // ⭐ ADMIN ONLY
                .anyRequest().authenticated()
            );

        
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(form -> form.disable())         // <<< ADD
        .httpBasic(basic -> basic.disable());
        
        return http.build();
        
        
    }
	
	
	 // CORS configuration that returns the correct Access-Control-Allow-Origin for your dev ports
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // set the front-end dev origins you use
        config.setAllowedOrigins(List.of(
            "http://localhost:5173"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // allow cookies if you use them

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

