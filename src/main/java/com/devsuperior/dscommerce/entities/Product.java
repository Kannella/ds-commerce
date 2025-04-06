package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_product") //para que eu possa customizar a tabela no banco
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Pq esse ID vai ser autoincrement no banco H2
    private Long id;
    private String name;

    //para falar que vai ser um texto grande a description e nao somente um varchar. Eu configuro isso com a anotation abaixo
    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;
    private String imgUrl;

    //Dentro da classe Product eu tenho uma referencia para uma colecao de categorias, so que a colecao no caso do muitos para muitos eu tenhoque colocar o set para evitar duplicidade de linhas iguais
    @ManyToMany //Indicando que o atributo abaixo faz parte de um Relacionamento muito para muitos
    //JoinTable eh a tabela de juncao (ou tabela de associacao) que vai ter no meio das duas
    @JoinTable(name = "tb_product_category", // o nome da tabela do meio vai ser tb_product_category
            joinColumns = @JoinColumn(name = "product_id"), //vai juntar o product_id na nova tabela. Eh o product_id pq eh na classe que voce esta
            inverseJoinColumns = @JoinColumn(name = "category_id")) //vai juntar o category_id na nova tabela tambem
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "id.product")
    private Set<OrderItem> items = new HashSet<>();

    public Product() {

    }

    public Product(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public List<Order> getOrders() {
        return items.stream().map(x -> x.getOrder()).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
Como nos temos um relacionamento muitos para muitos (muitos produtos para muitas categorias e vice versa) nos
ja sabemos que no banco vai ter uma tabela no meio dessas duas entidades com os dois IDs, entao eh importante
que esses IDs (id do produto e da categoria) nao pode repetir nessa terceira tabela auxiliar em duas linhas. Para que
nao repita, na hora de criar a colecao eu vou ter que usar o tipo set e nao o tipo list

Na tabela produto_categoria:

    produto_id	categoria_id
    1	        1
    2	        1
    3	        2
    3	        3
    Parece que a categoria 1 (Roupas) está repetida duas vezes, né? Mas na verdade, isso não é uma repetição inválida, porque cada linha da tabela representa uma relação única entre um produto e uma categoria.

    Explicando visualmente:
        Produto 1 (Camiseta Azul) → Categoria 1 (Roupas)

        Produto 2 (Calça Jeans) → Categoria 1 (Roupas)
        → Ambos estão na mesma categoria, mas são produtos diferentes, então está tudo certo.

    📌 O que não pode acontecer é isto aqui:
    produto_id	categoria_id
    1	        1
    1	        1

    Ou seja, a combinação de produto_id e categoria_id não pode se repetir. O produto nao pode aparecer duas vezes


Pq usar set e nao list
    A ideia por trás: List vs Set
    Quando estamos modelando relacionamentos muitos para muitos em linguagens como Java (com JPA/Hibernate), ou outros ORMs (como Entity Framework no .NET), usamos coleções para representar a relação entre entidades.

    List (lista):
        Permite elementos repetidos

        Mantém a ordem de inserção

        Você pode ter a mesma categoria duas vezes associada ao mesmo produto, por acidente.

    Set (conjunto):
        Não permite elementos duplicados

        Não garante ordem, mas garante que cada elemento seja único

        É perfeito para representar relações onde cada combinação deve ser única

    Você poderia acidentalmente adicionar a mesma categoria mais de uma vez ao mesmo produto, o que pode gerar duplicações na tabela
    associativa no banco de dados, mesmo com chave primária composta — isso causaria erro na hora de persistir.
    Para isso nao acontecer usamos o Set.

        Usar Set é uma forma de garantir em memória (antes de chegar no banco) que você não vai duplicar a associação entre entidades.

            Set → Sem duplicatas → Relacionamento limpo
            List → Pode ter duplicatas → Pode causar erro no banco ou dados sujos

*/