package com.example.demo.Controller;
	
import com.example.demo.Entity.Address;
import com.example.demo.Service.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // 1. Add address
    @PostMapping("/add")
    public ResponseEntity<?> addAddress(
            @RequestParam Integer userId,
            @RequestBody Address address) {

        Address saved = addressService.addAddress(userId, address);
        return ResponseEntity.ok(saved);
    }

    // 2. Get all addresses for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Address>> getAddresses(@PathVariable Integer userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    // 3. Get a single address
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddress(id));
    }

    // 4. Delete address
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted");
    }

}
