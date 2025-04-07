package com.devsuperior.dscommerce.controllers;

//Para que eu possa configurar essa classe no framework
// SpringBoot para que responda pela web pela rota /product eu tenho
// que fazer algumas configuracoes na classe usando as anotations

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

//Essa anotation serve para quando minha aplicacao rodar o
// que eu implementar nessa classe vai estar respondendo pela web
@RestController
// vai responder via web por essa rota
@RequestMapping(value = "/products")
public class ProductController {

    // A anotation @Autowired eh uma forma alternativa ao inves de colocar o construtor
    // e fazer a injecao de dependencia por meio do construtor o @Autowired automaticamente
    // faz por baixo dos panos esse trabalho pra voce

    //@Autowired -> declara que esse atributo abaixo eh uma dependencia de ProductController

    //Abaixo estamos declarando as dependencias de ProductController
    @Autowired
    private ProductService service;

    //O id da URL tem que casar com o parametro do metodo, por isso eu coloco a anotation @PathVariable. Pq ai o parametro da URL vai direto pro parametro desse metodo
    @GetMapping(value = "/{id}") //Resposta de um Get nessa rota products
    public ProductDTO findById(@PathVariable Long id) {
        ProductDTO dto = service.findById(id);
        return dto;
    };
}
