package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourcesNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
        Product product = repository.findById(id).orElseThrow(() -> new ResourcesNotFoundException("Recurso não encontrado!")); // o repository foi la no banco e buscou o produto que tem esse id e o resultado eh um objeto do tipo result. Caso nao consiga acessar o objeto eu vou lancar uma excessao minha personalizada com uma expressao lambda
        return new ProductDTO(product); //Converto em dto
    };

    @Transactional(readOnly = true) // somente uma leitura no banco
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable); // o repository foi la no banco e buscou todos os produtos passando o pageable (para fazer a busca paginada) como argumento e retornou um Page de Product e armazenou na varaivel result
        // Na linha de cima o repository.findAll(pageable) retorna um Page de Product e abaixo eu preciso converter esse Page (que ja eh uma Stream) de products em um Page de ProductsDTO para depois passar esse DTO para o controller la na classe ProductController
        return result.map(x -> new ProductDTO(x)); //Converto cada registro da minha Page de product em dto usando o map assim filtrar os dados (converter todos eles em ProductDTO). Agora temos um page de ProductDTO
    };

    //Configurando uma requisicao POST na rota /products esprando o corpo JSON enviado do Front-end
    @Transactional // nao eh somente leitura agora
    public ProductDTO insert(ProductDTO dto) {
        //Recebe um productDTO e converte para entity
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        //Salvar uma entidade no banco de dados usando o Repository (dependencia dessa classe)
        //Salvo essa entidade (que eu criei acima com o new) no banco e armazeno essa referencia para essa entidade nova e salvo nessa mesma variavel
        entity = repository.save(entity);

        //Reconverto a entidade que eu acabei de salvar no banco para ProductDTO para passar para o Controller
        return new ProductDTO(entity);
    };

    //Configurando uma requisicao POST na rota /products esprando o corpo JSON enviado do Front-end
    @Transactional // nao eh somente leitura agora
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            //Instanciando um produto pelo id passado na url
            Product entity = repository.getReferenceById(id); //Essa operacao getReferenceById nao vai no banco de dados, ele so prepara um objeto monitorado pela JPA.

            //Copiei os dados desse produto para o meu dto
            copyDtoToEntity(dto, entity);

            //Salvar uma entidade no banco de dados usando o Repository (dependencia dessa classe)
            //Salvo essa entidade (que eu criei acima com o new) no banco e armazeno essa referencia para essa entidade nova e salvo nessa mesma variavel
            entity = repository.save(entity);

            //Reconverto a entidade que eu acabei de salvar no banco para ProductDTO para passar para o Controller
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourcesNotFoundException("Recurso não encontrado");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }

    @Transactional(propagation = Propagation.SUPPORTS) // Esse parametro so vai executar essa transacao (que nao eh so uma leitura) se esse metodo estiver no contexto de outra transao, se nao estiver nao precisa envolver com o Transactional e captura corretamente.
    public void delete(Long id) {

        /*
        Antes de tentar deletar o produto, o serviço verifica se o ID realmente existe.
        Se não existir, lança a ResourcesNotFoundException.
        Essa exceção, por sua vez, é interceptada pelo nosso ControllerExceptionHandler:
        */

        //Esse trecho abaixo impede que você chegue na parte onde pode ocorrer a DataIntegrityViolationException, porque o recurso precisa existir no banco.
        /*
        O que precisa acontecer para cair na exceção DatabaseException?
        Você precisa deletar um registro que existe, mas está sendo referenciado por outro, ou seja, há uma chave estrangeira (foreign key) apontando para esse produto.

        Exemplo típico:
        Você tem um Product que está vinculado a um OrderItem. Aí você tenta deletar esse Product, mas como ele está em uso, o banco impede — isso gera uma DataIntegrityViolationException.
        */

        if(!repository.existsById(id)) { //se nao existir o id que foi informado no banco
            throw new ResourcesNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) { //DataIntegrityViolationException pois se eu tentar apagr um produto para o qual tem um pedido vai dar erro de integridade
            throw new DatabaseException("Falha de integridade referencial"); //chamo minha excessao personalizada de servico de banco de dados
        }
    };
}
