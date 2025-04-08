package com.devsuperior.dscommerce.controllers;

//Para que eu possa configurar essa classe no framework
// SpringBoot para que responda pela web pela rota /product eu tenho
// que fazer algumas configuracoes na classe usando as anotations

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
        return service.findById(id);
    };

    //Pageable serve para fazer a paginacao dos dados, em vez de mostrar uma cacetada deles
    //Por padrao o Pageable retorna 20 dados mas eu posso mexer nisso no frontend ou colocando na URL um queryparam tipo: http://localhost:8080/products?size=12 ai vai exibir 12 produtos e vai mudar o numero de pagina pois eu dividi em cada pagina 12 produtos.
    //Posso colocar outro queryparam tipo: http://localhost:8080/products?size=12&page=1 e ai vai aparecer no caso do produto 13 ate o 24 (12 produtos so que na segunda pagina). Se fosse http://localhost:8080/products?size=12&page=1 vai aparecer so o produto 25 pois temos 25 produtos e na ultima pagina vai ficar o produto que restou pra aparecer
    //Posso colocar outro queryparam tipo: http://localhost:8080/products?size=12&page=0&sort=name -> isso significa: a primeira pagina (page = 0) de tamanho 12 (com 12 produtos cada pagina) e organizar quando mostrar por nome (sort=name)
    //Posso colocar outro queryparam tipo: http://localhost:8080/products?size=12&page=0&sort=name,desc -> isso significa: a primeira pagina (page = 0) de tamanho 12 (com 12 produtos cada pagina) e organizar quando mostrar por nome de maneira descrescente (sort=name, desc)
    //Entao voce tem uma infinita gama de possibilidades de paginacao e configuracoes somente colocando esse pageable nos metodos
    //Concluimos a primeira parte do caso de uso Consultar catalogo, que eh: O sistema informa uma listagem paginada de nome, imagem e preço dos
    //produtos, ordenada por nome. Agora falta mais algumas funcionalidades desse mesmo caso de uso, como consulta por nome que iremos fazer mais pra frente
    @GetMapping //Resposta de um Get nessa rota products
    public Page<ProductDTO> findAll(Pageable pageable) { // Nosso metodo retorna um Page. Esse metodo recebe um pageable de argumento e eu vou passar esse argumento para a chamada de metodo abaixo
        return service.findAll(pageable); //Retorna o retorno do metodo findAll do ProductService passando esse pageable como parametro para esse metodo
    };
}
