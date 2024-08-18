package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.CartDTO;
import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.dtos.UserDTO;
import com.shiva.ecommerceapi.exceptions.APIException;
import com.shiva.ecommerceapi.exceptions.ResourceNotFoundException;
import com.shiva.ecommerceapi.models.Cart;
import com.shiva.ecommerceapi.models.CartItem;
import com.shiva.ecommerceapi.models.User;
//import com.shiva.ecommerceapi.repository.RoleRepo;
import com.shiva.ecommerceapi.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

//	@Autowired
//	private RoleRepo roleRepo;



	@Autowired
	private CartService cartService;

	//@Autowired
	//private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDTO registerUser(UserDTO userDTO) {

		try {
			User user = modelMapper.map(userDTO, User.class);

			Cart cart = new Cart();
			user.setCart(cart);

		//	Role role = roleRepo.findById(AppConstants.USER_ID).get();
		//	user.get().add(role);

			//String encodedPass = passwordEncoder.encode(userDTO.getPassword());

			User registeredUser = userRepo.save(user);

			cart.setUser(registeredUser);

			userDTO = modelMapper.map(registeredUser, UserDTO.class);

		//	userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

			return userDTO;
		} catch (DataIntegrityViolationException e) {
			throw new APIException("User already exists with emailId: " + userDTO.getEmail());
		}

	}

	@Override
	public List<UserDTO> getAllUsers() {



		
		List<User> users = userRepo.findAll();
		


		if (users.size() == 0) {
			throw new APIException("No User exists !!!");
		}

		List<UserDTO> userDTOs = users.stream().map(user -> {
			UserDTO dto = modelMapper.map(user, UserDTO.class);

		//	if (user.getAddresses().size() != 0) {
		//		dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
		//	}

			CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

			List<ProductDTO> products = user.getCart().getCartItems().stream()
					.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			dto.setCart(cart);

			dto.getCart().setProducts(products);

			return dto;

		}).collect(Collectors.toList());


		

		
		return userDTOs;
	}

	@Override
	public UserDTO getUserById(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

	//	userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	}


	@Override
	public String deleteUser(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		List<CartItem> cartItems = user.getCart().getCartItems();
		Long cartId = user.getCart().getId();

		cartItems.forEach(item -> {

			Long productId = item.getProduct().getId();

			cartService.deleteProductFromCart(cartId, productId);
		});

		userRepo.delete(user);

		return "User with userId " + userId + " deleted successfully!!!";
	}

}
