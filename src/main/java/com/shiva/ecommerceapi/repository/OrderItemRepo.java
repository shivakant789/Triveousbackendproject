package com.shiva.ecommerceapi.repository;


import com.shiva.ecommerceapi.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}
