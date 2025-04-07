package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class ProductDTO {

    // Perceba que eu nao tenho nada de JPA aqui no meu DTO pois o DTO eh um objeto simples para transferencia de dados, sem o JPA.

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    public ProductDTO() {

    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
    }

    //DTO eu nao preciso nem colocar SET pois eu nao vou querer alterar esses dados

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }


}
