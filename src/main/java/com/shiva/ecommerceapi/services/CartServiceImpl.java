package com.shiva.ecommerceapi.services;

import com.shiva.ecommerceapi.dtos.CartDTO;
import com.shiva.ecommerceapi.dtos.CartItemDTO;
import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.exceptions.APIException;
import com.shiva.ecommerceapi.exceptions.ResourceNotFoundException;
import com.shiva.ecommerceapi.models.Cart;
import com.shiva.ecommerceapi.models.CartItem;
import com.shiva.ecommerceapi.models.Product;
import com.shiva.ecommerceapi.models.User;
import com.shiva.ecommerceapi.repository.CartItemRepo;
import com.shiva.ecommerceapi.repository.CartRepo;
import com.shiva.ecommerceapi.repository.ProductRepo;
import com.shiva.ecommerceapi.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

//import static java.util.stream.Nodes.collect;

@Transactional
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Override
	public CartItemDTO addProductToCart(Long cartId, Long productId, Integer quantity, Long userId) {

		// Fetch the cart if it exists, or create a new one
		Cart cart = cartRepo.findById(cartId).orElse(null);
		if (cart == null) {
			cart = new Cart();
			cart.setTotalPrice(0.0);
			cart.setUser(userRepo.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId)));
		}

		// Fetch the product
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		// Check if the product is available in the desired quantity
		if (product.getQuantity() == 0) {
			throw new APIException(product.getTitle() + " is not available");
		}
		if (product.getQuantity() < quantity) {
			throw new APIException("Please, make an order of " + product.getTitle()
					+ " less than or equal to the available quantity " + product.getQuantity() + ".");
		}

		// Check if the product is already in the cart
		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
		if (cartItem != null) {
			// If the product already exists in the cart, update the quantity
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItem.setProductPrice(cartItem.getProductPrice() + (product.getPrice() * quantity));
		} else {
			// If it's a new product in the cart, create a new cart item
			cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(quantity);
			cartItem.setProductPrice(product.getPrice() * quantity);
		//	cart.getCartItems().add(cartItem);
		}
		// Deduct the ordered quantity from the product's available quantity
		product.setQuantity(product.getQuantity() - quantity);
		// Update the cart's total price
		cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
		// Save the cart and cart item
		cartRepo.save(cart);
		cartItemRepo.save(cartItem);

		// Map the cart to CartDTO and return it
//		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//		List<ProductDTO> productDTOs = cart.getCartItems().stream()
//				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//		cartDTO.setProducts(productDTOs);

		int totalProductsInCart = cart.getCartItems().stream()
				.mapToInt(CartItem::getQuantity).sum();

		// Map the cart to CartDTO and return it
		CartItemDTO cartItemDTO = modelMapper.map(cart, CartItemDTO.class);

		// Set the total quantity in the CartDTO
		cartItemDTO.setTotalQuantity(totalProductsInCart);

		// Map the product details into the CartDTO's product list
		List<ProductDTO> productDTOs = cart.getCartItems().stream()
				.map(item -> {
					ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
					productDTO.setQuantity(item.getQuantity()); // Set the quantity in the ProductDTO
					return productDTO;
				}).collect(Collectors.toList());
		cartItemDTO.setProducts(productDTOs);




		return cartItemDTO;
	}


	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts = cartRepo.findAll();

		if (carts.size() == 0) {
			throw new APIException("No cart exists");
		}

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			cartDTO.setProducts(products);

			return cartDTO;

		}).collect(Collectors.toList());

		return cartDTOs;
	}

	@Override
	public CartDTO getCart(String emailId) {

		Optional<User> user = userRepo.findByEmail(emailId);
		//Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
		Cart cart=cartRepo.findCartByUserId(user.get().getId());

		if (cart == null) {
			throw new ResourceNotFoundException("Cart does not exist for the user", "emailId", emailId);
		}

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		
		List<ProductDTO> products = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(products);

		return cartDTO;
	}

	@Override
	public void updateProductInCarts(Long cartId, Long productId) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new APIException("Product " + product.getTitle() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

		cartItem.setProductPrice(product.getPrice());

		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

		cartItem = cartItemRepo.save(cartItem);
	}

	@Override
	public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (product.getQuantity() == 0) {
			throw new APIException(product.getTitle() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new APIException("Please, make an order of the " + product.getTitle()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new APIException("Product " + product.getTitle() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

		product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);

		cartItem.setProductPrice(product.getPrice());
		cartItem.setQuantity(quantity);


		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));

		cartItem = cartItemRepo.save(cartItem);

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<ProductDTO> productDTOs = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(productDTOs);

		return cartDTO;

	}

	@Override
	public String deleteProductFromCart(Long cartId, Long productId) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

		Product product = cartItem.getProduct();
		product.setQuantity(product.getQuantity() + cartItem.getQuantity());

		cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

		return "Product " + cartItem.getProduct().getTitle() + " removed from the cart !!!";
	}

}
