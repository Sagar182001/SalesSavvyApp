package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	
	@Query("SELECT a FROM Address a WHERE a.user.user_id = :userId")
	List<Address> findByUserId(Integer userId);

}
