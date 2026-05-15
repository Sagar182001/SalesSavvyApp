package com.example.demo.Controller.Admin;


import com.example.demo.Service.Admin.*;
import com.example.demo.dto.Admin.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/admin/business")
public class AdminBusinessController {

    private final AdminBusinessService adminBusinessService;

    public AdminBusinessController(AdminBusinessService adminBusinessService) {
        this.adminBusinessService = adminBusinessService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyBusiness(@RequestParam int month, @RequestParam int year) {
        try {
            if (month < 1 || month > 12) throw new IllegalArgumentException("Invalid month");
            AdminBusinessResponseDTO report = adminBusinessService.calculateMonthlyBusiness(month, year);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyBusiness(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date); // expects ISO format yyyy-MM-dd
            AdminBusinessResponseDTO report = adminBusinessService.calculateDailyBusiness(localDate);
            return ResponseEntity.ok(report);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid date format. Use yyyy-MM-dd"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/yearly")
    public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {
        try {
            AdminBusinessResponseDTO report = adminBusinessService.calculateYearlyBusiness(year);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/overall")
    public ResponseEntity<?> getOverallBusiness() {
        try {
            AdminBusinessResponseDTO report = adminBusinessService.calculateOverallBusiness();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

}
