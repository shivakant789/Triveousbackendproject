package com.shiva.ecommerceapi.controllers;


import com.shiva.ecommerceapi.dtos.CategoryDTO;
import com.shiva.ecommerceapi.dtos.CategoryResponse;
import com.shiva.ecommerceapi.models.Category;
import com.shiva.ecommerceapi.services.CategoryService;
//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/admin/category")
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
		CategoryDTO savedCategoryDTO = categoryService.createCategory(category);

		return new ResponseEntity<CategoryDTO>(savedCategoryDTO, HttpStatus.CREATED);
	}

	@GetMapping("/public/categories")
	public ResponseEntity<List<CategoryResponse>> getCategories() {

		List<CategoryResponse> getCategories= categoryService.getCategories();

		//return new ResponseEntity<CategoryResponse>((CategoryResponse) getCategories, HttpStatus.FOUND);
	  return new ResponseEntity<>(getCategories, HttpStatus.FOUND);
	}



	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		String status = categoryService.deleteCategory(categoryId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
