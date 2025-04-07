package com.devsuperior.dscommerce.repositories;

//Cada componente responsavel por acessar o banco de dados em relacao a uma entidade vai chamar: nomedaentidade + repository
//Entao esse repository vai ser responsavvel por implementar operacoes com o banco de dados em relacao a produto

import com.devsuperior.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Como eu faco pra criar um repository usando Spring
// Coloca extends JpaRepository<Tipo da Entidade, Tipo do id da Entidade>
// So fazendo isso acima eu ja criei um compenete no meu sistema que vai ter varias operacoes pra gente

public interface ProductRepository extends JpaRepository<Product, Long> {

}
