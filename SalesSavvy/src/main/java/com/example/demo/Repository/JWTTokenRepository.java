package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.JWTToken;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer> {
	
	//Custom query to find token by user_id
	@Query("Select t from JWTToken  t WHERE t.user.user_id= :user_id")
	JWTToken findByUserId(@Param("user_id") int user_id);

	Optional<JWTToken> findByToken(String token);
	
	 @Modifying
    @Transactional
    @Query("DELETE FROM JWTToken t WHERE t.user.user_id = :userId")
    void deleteByUserId(@Param("userId") int userId);
	
}
