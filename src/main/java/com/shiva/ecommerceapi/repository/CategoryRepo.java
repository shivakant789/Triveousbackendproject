package com.shiva.ecommerceapi.repository;

import com.shiva.ecommerceapi.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

	Category findByCategoryName(String categoryName);

	List<Category>  findAll();



}
