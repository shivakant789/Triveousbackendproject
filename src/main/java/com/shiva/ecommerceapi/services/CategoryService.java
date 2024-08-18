package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.CategoryDTO;
import com.shiva.ecommerceapi.dtos.CategoryResponse;
import com.shiva.ecommerceapi.models.Category;

import java.util.List;

public interface CategoryService {

	CategoryDTO createCategory(Category category);

	List<CategoryResponse> getCategories();



	String deleteCategory(Long categoryId);


}
