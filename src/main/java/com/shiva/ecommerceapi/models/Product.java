package com.shiva.ecommerceapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseModel {

    private String title;
    private double price;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    private  Category category;
    private String imgURL;
    private Integer quantity;
}
