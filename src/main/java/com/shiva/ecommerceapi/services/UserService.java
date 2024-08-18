package com.shiva.ecommerceapi.services;

import com.shiva.ecommerceapi.dtos.UserResponse;
import com.shiva.ecommerceapi.dtos.UserDTO;

import java.util.List;

public interface UserService {
	UserDTO registerUser(UserDTO userDTO);

	List<UserDTO> getAllUsers();
	
	UserDTO getUserById(Long userId);
	

	
	String deleteUser(Long userId);
}
