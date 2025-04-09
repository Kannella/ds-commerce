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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

//-----------------------------------------------------------------------------------------------------------

    //O id da URL tem que casar com o parametro do metodo, por isso eu coloco a anotation @PathVariable. Pq ai o parametro da URL vai direto pro parametro desse metodo
    //@GetMapping(value = "/{id}") //Resposta de um Get nessa rota products
    //public ProductDTO findById(@PathVariable Long id) {
    //    return service.findById(id);
    //};

//-----------------------------------------------------------------------------------------------------------

    //Agora meu metodos vao retornar um tipo especial no Spring chamado: ResponseEntity<Tipo Especial que voce quer no corpo da resposta>

    @GetMapping(value = "/{id}") //Resposta de um Get nessa rota products
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto); //Estou customizando minha resposta para responder 200 onde o corpo vai ser o objeto armazenado na variavel dto
    };

//-----------------------------------------------------------------------------------------------------------

    //Pageable serve para fazer a paginacao dos dados, em vez de mostrar uma cacetada deles
    //Por padrao o Pageable retorna 20 dados mas eu posso mexer nisso no frontend ou colocando na URL um queryparam tipo: http://localhost:8080/products?size=12 ai vai exibir 12 produtos e vai mudar o numero de pagina pois eu dividi em cada pagina 12 produtos.
    //Posso colocar outro queryparam tipo: http://localhost:8080/products?size=12&page=1 e ai vai aparecer no caso do produto 13 ate o 24 (12 produtos so que na segunda pagina). Se fosse http://localhost:8080/products?size=12&page=1 vai aparecer so o produto 25 pois temos 25 produtos e na ultima pagina vai ficar o produto que restou pra aparecer
    //Posso colocar outro queryparam tipo: http://localhost:8080/products?size=12&page=0&sort=name -> isso significa: a primeira pagina (page = 0) de tamanho 12 (com 12 produtos cada pagina) e organizar quando mostrar por nome (sort=name)
    //Posso colocar outro queryparam tipo: http://localhost:8080/products?size=12&page=0&sort=name,desc -> isso significa: a primeira pagina (page = 0) de tamanho 12 (com 12 produtos cada pagina) e organizar quando mostrar por nome de maneira descrescente (sort=name, desc)
    //Entao voce tem uma infinita gama de possibilidades de paginacao e configuracoes somente colocando esse pageable nos metodos
    //Concluimos a primeira parte do caso de uso Consultar catalogo, que eh: O sistema informa uma listagem paginada de nome, imagem e preço dos
    //produtos, ordenada por nome. Agora falta mais algumas funcionalidades desse mesmo caso de uso, como consulta por nome que iremos fazer mais pra frente
    //@GetMapping //Resposta de um Get nessa rota products
    //public Page<ProductDTO> findAll(Pageable pageable) { // Nosso metodo retorna um Page. Esse metodo recebe um pageable de argumento e eu vou passar esse argumento para a chamada de metodo abaixo
    //    return service.findAll(pageable); //Retorna o retorno do metodo findAll do ProductService passando esse pageable como parametro para esse metodo
    //};

//-----------------------------------------------------------------------------------------------------------

    @GetMapping //Resposta de um Get nessa rota products
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) { // Nosso metodo retorna um Page. Esse metodo recebe um pageable de argumento e eu vou passar esse argumento para a chamada de metodo abaixo
        Page<ProductDTO> dto = service.findAll(pageable); //Retorna o retorno do metodo findAll do ProductService passando esse pageable como parametro para esse metodo
        return ResponseEntity.ok(dto);
    };

//-----------------------------------------------------------------------------------------------------------

    //Configurando uma requisicao POST na rota /products esprando o corpo JSON enviado do Front-end que eh recebido por meio do argumento do metodo que eh configurado como @RequestBody ProductDTO dto e com essa configuracao o corpo da requisicao vai entrar neste parametro e vai instanciar um DTO correspondente com as informacoes do JSON recebidas
    //@PostMapping //Agora minha requisicaon eh um Post
    //public ProductDTO insert(@RequestBody ProductDTO dto) {
    //    dto = service.insert(dto); //Vai atribuir a variavel dto do tipo Objeto (ProductDTO) o retorno da funcao insert do ProductService (que colocou o dto que veio como parametro, como argumento para o metodo insert do ProductService)
    //    return dto; // Retorna o resultado da funcao insert do ProductService (que colocou o dto que veio como parametro, como argumento para o metodo insert do ProductService)
    //}

//-----------------------------------------------------------------------------------------------------------

    //Configurando uma requisicao POST na rota /products esprando o corpo JSON enviado do Front-end que eh recebido por meio do argumento do metodo que eh configurado como @RequestBody ProductDTO dto e com essa configuracao o corpo da requisicao vai entrar neste parametro e vai instanciar um DTO correspondente com as informacoes do JSON recebidas
    @PostMapping //Agora minha requisicaon eh um Post
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
        dto = service.insert(dto); //Vai atribuir a variavel dto do tipo Objeto (ProductDTO) o retorno da funcao insert do ProductService (que colocou o dto que veio como parametro, como argumento para o metodo insert do ProductService)
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri(); //preciso passar um objeto desse para o metodo created abaixo
        return ResponseEntity.created(uri).body(dto); // Retorna o resultado da funcao insert do ProductService (que colocou o dto que veio como parametro, como argumento para o metodo insert do ProductService) e alem disso estou customizando minha resposta para responder 201 (que signif recusro criado) onde o corpo vai ser o objeto armazenado na variavel dto ( .body(dto) ). E alem disso assim, na resposta alem de responder o codigo 201 no cabecalho vai ter o link para o recurso criado
    }

//-----------------------------------------------------------------------------------------------------------

    @PutMapping(value = "/{id}") //Resposta de um Get nessa rota products
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        dto = service.update(id, dto); //armazeno a referencia atualizada do dto depois de chamar o update no ProductService
        return ResponseEntity.ok(dto); //Estou customizando minha resposta para responder 200 onde o corpo vai ser o objeto armazenado na variavel dto
    };


}
