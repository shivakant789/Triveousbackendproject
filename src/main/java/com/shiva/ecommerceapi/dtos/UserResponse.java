package com.shiva.ecommerceapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private Long userId;

	private String Name;
	private String mobileNumber;
	private String email;
	private String password;
	private String roles;
	private CartDTO cart;

	
}
