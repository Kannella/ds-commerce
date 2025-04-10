package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class ProductDTO {

    // Perceba que eu nao tenho nada de JPA aqui no meu DTO pois o DTO eh um objeto simples para transferencia de dados, sem o JPA.
    //Adicionando as constraints do Bean Validation pra validar os dados inseridos e que vao vir pro json e vao ser validado nesse objeto dessa classe
    private Long id;

    @Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres") //Limita o tamanho do nome
    @NotBlank (message = "Campo requerido") //verifica se o campo nao esta vazio (nao aceita vazio, nao aceita null e nao aceita espaco em branco). Caso essa validacao seja violada, o argumento que tem a message vai retornar essa mensagem.
    private String name;

    @Size(min = 10, message = "Descricao precisa ter no minimo 10 caracteres")
    @NotBlank (message = "Campo requerido") //verifica se o campo nao esta vazio (nao aceita vazio, nao aceita null e nao aceita espaco em branco). Caso essa validacao seja violada, o argumento que tem a message vai retornar essa mensagem.
    private String description;

    @Positive(message = "O preco precisa ser positivo")//Precisa ser positivo
    private Double price;
    private String imgUrl;

    public ProductDTO() {

    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
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
