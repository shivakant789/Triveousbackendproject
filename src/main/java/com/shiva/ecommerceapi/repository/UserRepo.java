package com.shiva.ecommerceapi.repository;


import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.shiva.ecommerceapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
//	@Query("SELECT u FROM User u WHERE u.email = ?1")
//	Optional<User> findByEmail(String email);

	List<User> findAll();
	Optional<User> findByEmail(String email);

}
