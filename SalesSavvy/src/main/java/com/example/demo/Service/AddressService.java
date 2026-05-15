package com.example.demo.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Address;
import com.example.demo.Entity.User;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    /*Add address */
    public Address addAddress(Integer userId, Address address) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setUser(user);

        return addressRepository.save(address);
    }

    /* Get all addresses for user */
    public List<Address> getUserAddresses(Integer userId) {
        return addressRepository.findByUserId(userId);
    }

    /* Get single address */
    public Address getAddress(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    /* Delete */
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

		
}
