package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//O Service Depende do Repository
//O Controller depende do Service
//Aqui que eu vou implementar a logica de busca no banco de dados
@Service
public class ProductService {


    //Declarando uma dependencia, no caso a dependencia eh ProductRepository
    @Autowired
    private ProductRepository repository;

    //Metodo que vai retornar um ProductDTO a partir do ID passado como parametro
    //Esse metodo vai ter que ir la no banco de dados buscar um produto, converter para DTO e retornar pro Controller
    @Transactional(readOnly = true) // somente uma leitura no banco
    public ProductDTO findById(Long id) {
        Optional<Product> result = repository.findById(id); // o repository foi la no banco e buscou o produto que tem esse id e o resultado eh um objeto do tipo result
        Product product = result.get(); // Pegando agora o produto que esta dentro do objeto option armazenado na variavel result
        ProductDTO dto = new ProductDTO(product); //Converto em dto
        return dto;
    };

    @Transactional(readOnly = true) // somente uma leitura no banco
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable); // o repository foi la no banco e buscou todos os produtos passando o pageable (para fazer a busca paginada) como argumento e retornou um Page de Product e armazenou na varaivel result
        // Na linha de cima o repository.findAll(pageable) retorna um Page de Product e abaixo eu preciso converter esse Page (que ja eh uma Stream) de products em um Page de ProductsDTO para depois passar esse DTO para o controller la na classe ProductController
        return result.map(x -> new ProductDTO(x)); //Converto cada registro da minha Page de product em dto usando o map assim filtrar os dados (converter todos eles em ProductDTO). Agora temos um page de ProductDTO
    };
}
