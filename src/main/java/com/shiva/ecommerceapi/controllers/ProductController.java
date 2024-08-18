package com.shiva.ecommerceapi.controllers;


import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.dtos.ProductResponse;
import com.shiva.ecommerceapi.models.Product;
import com.shiva.ecommerceapi.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")

public class ProductController {



	@Autowired
	private ProductService productService;

	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product, @PathVariable Long categoryId) {

		ProductDTO savedProduct = productService.addProduct(categoryId, product);

		return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
	}

	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getProducts() {

		ProductResponse productResponse = productService.getAllProducts();

		return new ResponseEntity<ProductResponse>((ProductResponse) productResponse, HttpStatus.FOUND);
	}


//	@GetMapping("/public/categories/{categoryId}/products")
//	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
//
//		ProductResponse productResponse = productService.searchByCategory(categoryId);
//
//		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
//	}
	
	@GetMapping("/public/products/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
		ProductDTO productDTO = productService.getProductById(productId);

		return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.FOUND);
	}



	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<String> deleteProductById(@PathVariable Long productId) {
		String status = productService.deleteProduct(productId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
