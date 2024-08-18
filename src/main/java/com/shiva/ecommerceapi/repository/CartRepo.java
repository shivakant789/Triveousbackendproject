package com.shiva.ecommerceapi.repository;


import com.shiva.ecommerceapi.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
	
	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
	Cart findCartByEmailAndCartId(String email, Long cartId);

	@Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
	Cart findCartByUserId(Long userId);

	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
	List<Cart> findCartsByProductId(Long productId);

	Cart findCartById(Long cartId);
}
