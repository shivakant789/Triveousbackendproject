package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.OrderDTO;
import com.shiva.ecommerceapi.dtos.OrderResponse;

import java.util.List;

public interface OrderService {

	OrderDTO placeOrder(String emailId);

	OrderDTO getOrder(String emailId, Long orderId);

	List<OrderDTO> getOrdersByUser(String emailId);

	OrderResponse getAllOrders();


}
