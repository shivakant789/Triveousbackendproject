package com.shiva.ecommerceapi.controllers;


import com.shiva.ecommerceapi.dtos.OrderDTO;
import com.shiva.ecommerceapi.dtos.OrderResponse;
import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class OrderController {
	
	@Autowired
	public OrderService orderService;
	
	@PostMapping("/public/users/{emailId}")
	public ResponseEntity<OrderDTO> orderProducts(@PathVariable String emailId) {
		OrderDTO order = orderService.placeOrder(emailId);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
	}

	@GetMapping("/admin/orders")
	public ResponseEntity<OrderResponse> getAllOrders() {
		
		OrderResponse orderResponse = orderService.getAllOrders();

		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);

		//return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
	}
	
	@GetMapping("/public/users/{emailId}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
		List<OrderDTO> orders = orderService.getOrdersByUser(emailId);
		
		return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
	}
	
	@GetMapping("public/users/{emailId}/orders/{orderId}")
	public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
		OrderDTO order = orderService.getOrder(emailId, orderId);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
	}
	
//	@PutMapping("admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
//	public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) {
//		OrderDTO order = orderService.updateOrder(emailId, orderId, orderStatus);
//
//		return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
//	}

}
