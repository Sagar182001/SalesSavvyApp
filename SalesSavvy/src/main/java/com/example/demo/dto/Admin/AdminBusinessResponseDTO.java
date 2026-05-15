package com.example.demo.dto.Admin;


import java.math.BigDecimal;
import java.util.Map;

public class AdminBusinessResponseDTO {
    private BigDecimal totalRevenue;
    private Map<String, Integer> categorySales;

    public AdminBusinessResponseDTO() {}

    public AdminBusinessResponseDTO(BigDecimal totalRevenue, Map<String, Integer> categorySales) {
        this.totalRevenue = totalRevenue;
        this.categorySales = categorySales;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, Integer> getCategorySales() {
        return categorySales;
    }

    public void setCategorySales(Map<String, Integer> categorySales) {
        this.categorySales = categorySales;
    }

}
