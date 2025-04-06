package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;
import org.aspectj.weaver.ast.Or;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant moment; //Recomendacao: para que a gente configure esse campo para que ele seja salvo no banco de dados por padrao, no UTC, colocamos a anotation @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")

    private OrderStatus status;

    @ManyToOne //Relacionamento um para muitos em relacao a User (muitos pedidos para 1 cliente). Essa anotation vai incluir na tabela order um campo que vai ser a chave estrangeira com o nome que eu coloquei no "name" abaixo, no caso client_id
    @JoinColumn(name = "client_id") //Nome da chave estrangeira do banco.
    private User client;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();


    public Order() {

    }

    public Order(long id, Instant moment, OrderStatus status, User client, Payment payment) {
        Id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Set<OrderItem> getItems() {
        return items;
    }
    public List<Product> getProducts() {
        return items.stream().map(x -> x.getProduct()).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Id == order.Id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Id);
    }

    /*
    1. items.stream()
    Converte a lista items (do tipo List<OrderItem>) em um Stream.

    Um Stream permite aplicar operações funcionais como map, filter, collect, etc.

    2. .map(x -> x.getProduct())
    pega cada elemetento da stream e transforma eles aplicando
    a função getProduct() em cada elemento (x) da lista items.

    Ou seja, para cada OrderItem, ele extrai o produto associado.

    Resultado: um Stream<Product>

    3. .toList()
    Converte o Stream<Product> de volta para uma List<Product>
    */
}

/*


Pensa numa lista como uma caixa com coisas.
Agora pensa numa Stream como uma esteira que pega essa caixa e vai passando item por item, permitindo que você:
    Transforme (map) -> a funcao map transforma cada elemento
    Filtre (filter)
    Some (reduce)
    Colete em outra coisa (collect)

@OneToOne: Define o relacionamento 1:1 com Payment.

Um Order pode ter um Payment.
Um Payment não existe sem um Order.
O Payment usa o mesmo ID da Order.

@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
private Payment payment;

    Significa que a classe Order NÃO é a dona da relação.
    O campo "order" em mappedBy = "order" se refere ao atributo da classe Payment:

        @OneToOne
        @MapsId
        private Order order; // <-- esse aqui

    Em banco de dados:
        A chave estrangeira criada (order_id) fica na tabela tb_payment, não na tb_order.
        Ou seja: quem possui a chave estrangeira no banco é o Payment, então ele é o dono da relação.

┌──────────────┐         ┌───────────────┐
│   tb_order   │         │  tb_payment   │
├──────────────┤         ├───────────────┤
│ id (PK)      │◄───────▶│ id (PK, FK)   │
│ moment       │         │ moment        │
│ status       │         │ order_id (*)  │
│ client_id    │         └───────────────┘
└──────────────┘

tb_payment.id é ao mesmo tempo:

    chave primária (PK)
    chave estrangeira (FK) que referencia tb_order.id

SQL gerado (exemplo aproximado)
Tabela tb_order:
CREATE TABLE tb_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    moment TIMESTAMP,
    status INT,
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES tb_user(id)
);
💸 Tabela tb_payment:
CREATE TABLE tb_payment (
    id BIGINT PRIMARY KEY,
    moment TIMESTAMP,
    FOREIGN KEY (id) REFERENCES tb_order(id) -- chave compartilhada!
);
Repara que não tem um order_id separado:
O campo id da tb_payment já faz esse papel, graças ao @MapsId. Entao o campo id "vira" order_id (que eh a primary key e a foreign key ao mesmo tempo de payment)
tudo isso para que os ids de payments sejam os mesmos de order.

O atributo id de Payment é dupla função:
    Função                  o que faz
    Chave Primária (PK)	    Identifica de forma única um Payment
    Chave Estrangeira (FK)	Faz referência ao id da entidade Order

    E por que isso acontece?

        @OneToOne
        @MapsId
        private Order order;

    @OneToOne: cria o relacionamento 1:1
    @MapsId: diz que o ID do Payment vem da entidade Order

    Resultado prático: Quando o Hibernate for gerar a tabela tb_payment, ele faz:

        CREATE TABLE tb_payment (
            id BIGINT PRIMARY KEY, -- é PK
            moment TIMESTAMP,
            FOREIGN KEY (id) REFERENCES tb_order(id) -- e também é FK
        );

Por que no banco aparece só order_id na tabela tb_payment?
Quando você usa em Payment:

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Order order;

    O que o JPA faz?
    Ele entende que:

        O campo id é "um espelho" de order.id
        Então, ele não cria dois campos no banco (id e order_id)
        Ele cria só um campo físico na tabela: order_id
        E esse campo é chave primária e chave estrangeira ao mesmo tempo

    Mas na entidade Java aparecem dois atributos:

        private Long id;
        private Order order;

    Isso é só pra facilitar o acesso no código. Mas no banco, os dois se referem ao mesmo campo.

Resultado na tabela tb_payment (no banco H2, por exemplo):

    CREATE TABLE tb_payment (
        order_id BIGINT PRIMARY KEY,
        moment TIMESTAMP,
        FOREIGN KEY (order_id) REFERENCES tb_order(id)
    );

    Repara que:

        order_id é o nome do campo real
        É PK e FK ao mesmo tempo
        O Hibernate ignora o nome id e prioriza o JoinColumn implícito no @MapsId

Resumo pra não esquecer:
No Java	No Banco de Dados	Observações
private Long id;	order_id (campo físico)	Serve só pra acessar o valor diretamente
@MapsId + Order	order_id (chave estrangeira)	Define que order_id é também a chave primária

Todo esse esquema com @OneToOne, @MapsId, e o id compartilhado existe justamente para garantir duas coisas muito importantes:

✅ 1. Relacionamento 1:1 (um pedido tem exatamente um pagamento)
Exemplo real:
    Você compra no e-commerce.
    Isso gera um pedido.
    E esse pedido vai ter um único pagamento associado a ele.
    Isso é perfeitamente modelado com @OneToOne.

2. Compartilhar o mesmo ID entre Order e Payment
    Usar o mesmo id na tabela tb_payment faz com que:

        O pagamento só exista se o pedido existir
        O id do pagamento nunca seja duplicado
        Fique muito fácil fazer joins e consultas

    Por que usar @MapsId? Porque ele:

        Garante que Payment.id == Order.id
        Evita que a gente precise gerar outro id
        Evita inconsistência (ex: um pagamento sem pedido)

Resumindo com uma frase:
    Usar @OneToOne + @MapsId faz com que o Payment dependa completamente do Order, garantindo que haja um pagamento por pedido e que eles compartilhem o mesmo identificador no banco.


Geralmente voce vai usar a anotation OneToOne quando voce quiser que por exemplo, um pedido tenha apenas um pagamento, e alem disso, caso voce queira que
o id do pagamento seja igual ao id do pedido voce faz esse MapsId em payment e @OneToOne(mappedBy = "order", cascade = CascadeType.ALL) em order.
*/
