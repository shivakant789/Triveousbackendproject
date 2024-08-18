package com.shiva.ecommerceapi.services;



import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.dtos.ProductResponse;
import com.shiva.ecommerceapi.exceptions.APIException;
import com.shiva.ecommerceapi.exceptions.ResourceNotFoundException;
import com.shiva.ecommerceapi.models.Cart;
import com.shiva.ecommerceapi.models.Category;
import com.shiva.ecommerceapi.models.Product;
import com.shiva.ecommerceapi.repository.CartRepo;
import com.shiva.ecommerceapi.repository.CategoryRepo;
import com.shiva.ecommerceapi.repository.ProductRepo;
import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartService cartService;



	@Autowired
	private ModelMapper modelMapper;

//	@Value("${project.image}")
//	private String path;

	@Override
	public ProductDTO addProduct(Long categoryId, ProductDTO productdto) {

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		boolean isProductNotPresent = true;

		List<Product> products = category.getProducts();

		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getTitle().equals(productdto.getTitle())
					&& products.get(i).getDescription().equals(productdto.getDescription())) {

				isProductNotPresent = false;
				break;
			}
		}

		if (isProductNotPresent) {
			Product product= new Product();

			product.setImgURL(productdto.getImgURL());

			product.setCategory(category);
			product.setTitle(productdto.getTitle());
			product.setDescription(productdto.getDescription());
			product.setQuantity(productdto.getQuantity());

			//double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
			product.setPrice(productdto.getPrice());



			Product savedProduct = productRepo.save(product);

			return modelMapper.map(savedProduct, ProductDTO.class);
		} else {
			throw new APIException("Product already exists !!!");
		}
	}

	@Override
	public ProductResponse getAllProducts() {

		List<Product> products = productRepo.findAll();
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(productDTOs);

		return productResponse;
	}

	/*@Override
	public List<ProductResponse> searchByCategory(Long categoryId) {

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));





		List<Product> products = ProductRepo.findProductByCategory_Id(categoryId);

		if (products.size() == 0) {
			throw new APIException(category.getName() + " category doesn't contain any products !!!");
		}

		List<ProductResponse> productresponse = products.stream().map(p -> modelMapper.map(p, ProductResponse.class))
				.collect(Collectors.toList());




		return productresponse;
	}*/


	@Override
	public Page<Product> getSearchProducts(String query, int numberOfResults, int offset) {
		Page<Product> products = productRepo.findAllByTitleContaining(
				query,
				PageRequest.of((offset/numberOfResults),numberOfResults)
		);
		return products;
	}

	/*@Override
	public ProductDTO updateProduct(Long productId, Product product) {
		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}

		product.setImgURL(productFromDB.getImgURL());
		product.setId(productId);
		product.setCategory(productFromDB.getCategory());

		//double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
		product.setPrice(product.getPrice());

		Product savedProduct = productRepo.save(product);

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			cartDTO.setProducts(products);

			return cartDTO;

		}).collect(Collectors.toList());

		cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

		return modelMapper.map(savedProduct, ProductDTO.class);
	}*/


	@Override
	public String deleteProduct(Long productId) {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		carts.forEach(cart -> cartService.deleteProductFromCart(cart.getId(), productId));

		productRepo.delete(product);

		return "Product with productId: " + productId + " deleted successfully !!!";
	}

	@Override
	public ProductDTO getProductById(Long productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		return modelMapper.map(product, ProductDTO.class);
	}

}
