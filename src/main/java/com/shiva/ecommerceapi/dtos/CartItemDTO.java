package com.shiva.ecommerceapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

	private Long cartId;
	private Double totalPrice;
	private Integer totalQuantity; // Add this field for total quantity
	private List<ProductDTO> products; // This will hold the product details
}
