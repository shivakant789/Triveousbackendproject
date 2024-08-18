package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.*;
import com.shiva.ecommerceapi.exceptions.APIException;
import com.shiva.ecommerceapi.exceptions.ResourceNotFoundException;
import com.shiva.ecommerceapi.models.*;
import com.shiva.ecommerceapi.repository.*;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	public UserRepo userRepo;

	@Autowired
	public CartRepo cartRepo;

	@Autowired
	public OrderRepo orderRepo;


	@Autowired
	public OrderItemRepo orderItemRepo;

	@Autowired
	public CartItemRepo cartItemRepo;

	@Autowired
	public UserService userService;

	@Autowired
	public CartService cartService;

	@Autowired
	public ModelMapper modelMapper;

	@Override
	public OrderDTO placeOrder(String emailId) {

		Optional<User> user = userRepo.findByEmail(emailId);
		//Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
		Cart cart=cartRepo.findCartByUserId(user.get().getId());

		if (cart == null) {
			throw new ResourceNotFoundException("no cart for the user", "emailId", emailId);
		}

		Order order = new Order();

		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());

		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted !");







		Order savedOrder = orderRepo.save(order);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemRepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cart.getId(), item.getProduct().getId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}

	@Override
	public List<OrderDTO> getOrdersByUser(String emailId) {
		List<Order> orders = orderRepo.findAllByEmail(emailId);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + emailId);
		}

		return orderDTOs;
	}

	@Override
	public OrderDTO getOrder(String emailId, Long orderId) {

		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return modelMapper.map(order, OrderDTO.class);
	}

	@Override
	public OrderResponse getAllOrders() {



		List<Order> orders = orderRepo.findAll();



		List<OrderDTO> orderdtos = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderdtos.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}
//
//		List<Product> products = productRepo.findAll();
//		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
//				.collect(Collectors.toList());

		OrderResponse orderResponse = new OrderResponse();

		orderResponse.setContent(orderdtos);





		return orderResponse;
	}



}
