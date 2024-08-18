package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.dtos.ProductResponse;
import com.shiva.ecommerceapi.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

	ProductDTO addProduct(Long categoryId, ProductDTO product);


	ProductResponse getAllProducts();

	//List<ProductResponse> searchByCategory(Long categoryId);

	//ProductDTO updateProduct(Long productId, Product product);



	Page<Product> getSearchProducts(String query, int numberOfResults, int offset);

	String deleteProduct(Long productId);

	ProductDTO getProductById(Long productId);


}
