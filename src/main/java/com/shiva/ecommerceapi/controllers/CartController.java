package com.shiva.ecommerceapi.controllers;

import com.shiva.ecommerceapi.dtos.CartItemDTO;
import com.shiva.ecommerceapi.services.CartService;
import com.shiva.ecommerceapi.dtos.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class CartController {
	
	@Autowired
	private CartService cartService;

	@PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}/user/{userId}")
	public ResponseEntity<CartItemDTO> addProductToCart(@PathVariable Long cartId,@PathVariable Long productId,
													@PathVariable Integer quantity,@PathVariable Long userId) {
		CartItemDTO cartDTO = cartService.addProductToCart(cartId,productId, quantity,userId);
		
		return new ResponseEntity<CartItemDTO>(cartDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("/admin/carts")
	public ResponseEntity<List<CartDTO>> getCarts() {
		
		List<CartDTO> cartDTOs = cartService.getAllCarts();
		
		return new ResponseEntity<>(cartDTOs, HttpStatus.FOUND);

	}
	
	@GetMapping("/public/users/{emailId}")
	public ResponseEntity<CartDTO> getCartById(@PathVariable String emailId) {
		CartDTO cartDTO = cartService.getCart(emailId);
		
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
		
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("/public/carts/{cartId}/product/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
