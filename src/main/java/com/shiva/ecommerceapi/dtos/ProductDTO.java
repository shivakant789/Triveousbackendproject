package com.shiva.ecommerceapi.dtos;

import com.shiva.ecommerceapi.models.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {
    private Long Id;
    private String title;
    private double price;
    private String description;
    private String imgURL;
    private String category;
    private Integer quantity;
}
