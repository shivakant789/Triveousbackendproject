package com.shiva.ecommerceapi.services;


import com.shiva.ecommerceapi.dtos.CategoryDTO;
import com.shiva.ecommerceapi.dtos.CategoryResponse;
import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.exceptions.APIException;
import com.shiva.ecommerceapi.exceptions.ResourceNotFoundException;
import com.shiva.ecommerceapi.models.Category;
import com.shiva.ecommerceapi.models.Product;
import com.shiva.ecommerceapi.repository.CategoryRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ProductService productService;


	@Autowired
	private ModelMapper modelMapper;



	@Override
	public CategoryDTO createCategory(Category category) {
		Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());



		savedCategory = categoryRepo.save(category);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public List<CategoryResponse> getCategories() {


		//Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		List<Category> categories = categoryRepo.findAll();


		if (categories.size() == 0) {
			throw new APIException("No category is created till now");
		}

		List<CategoryResponse> categoryResponse = categories.stream()
				.map(category -> modelMapper.map(category, CategoryResponse.class)).collect(Collectors.toList());

//		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
//				.collect(Collectors.toList());
		
		return categoryResponse;
	}



	@Override
	public String deleteCategory(Long categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		List<Product> products = category.getProducts();

		products.forEach(product -> {
			productService.deleteProduct(product.getId());
		});
		
		categoryRepo.delete(category);

		return "Category with categoryId: " + categoryId + " deleted successfully !!!";
	}

}
