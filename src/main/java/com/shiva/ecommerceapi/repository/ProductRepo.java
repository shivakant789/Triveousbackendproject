package com.shiva.ecommerceapi.repository;

import com.shiva.ecommerceapi.dtos.ProductDTO;
import com.shiva.ecommerceapi.dtos.ProductResponse;
import com.shiva.ecommerceapi.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	List<Product> findAll();
	Page<Product> findAllByTitleContaining(String title, Pageable pageable);

//	Page<Product> findByProductNameLike(Pageable pageable);

	Product findProductsById(Long id);

	List<Product> findProductByCategory_Id(Long categoryId);

}
