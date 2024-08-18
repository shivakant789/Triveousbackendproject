package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.CartDTO;
import com.shiva.ecommerceapi.dtos.CartItemDTO;

import java.util.List;

public interface CartService {

	CartItemDTO addProductToCart(Long cartId, Long productId, Integer quantity, Long userId);
	
	List<CartDTO> getAllCarts();
	
	CartDTO getCart(String emailId);
	
	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);
	
	void updateProductInCarts(Long cartId, Long productId);
	
	String deleteProductFromCart(Long cartId, Long productId);
	
}
