package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tb_payment")
public class Payment {

    @Id
    private Long Id;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") // para ser salvo com o horario do u tc
    private Instant moment;

    @OneToOne //Um pagamento esta associado a um Order. Isso serve para mapear essa tabela como um para um
    @MapsId // Assim a chave primaria do Payment, vai tambem ser uma chave estrangeira com o mesmo numero do Id do pedido correspondente
    private Order order;

    public Payment() {

    }

    public Payment(Long id, Instant moment, Order order) {
        Id = id;
        this.moment = moment;
        this.order = order;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

/*
@OneToOne: Diz que há um relacionamento 1:1 com Order.

Um Order pode ter um Payment.
Um Payment não existe sem um Order.
O Payment usa o mesmo ID da Order.

O campo id de Payment é o mesmo da Order. Justamente por causa do @OneToOne com
@MapsId, o campo id da classe Payment é o mesmo valor que o id da Order associada.

    O que isso significa na prática?
        A Payment não tem um id próprio.
        Ela herda o ID da Order.
        O campo id em Payment funciona como:
            Chave primária (PK) da tabela tb_payment
            Chave estrangeira (FK) apontando para tb_order

O @MapsId diz que esse ID, que eh a chave priamaria de Payment, vem da Order,
entao esse Id é chave primária e estrangeira ao mesmo tempo

Não precisa (e nem pode!) usar @GeneratedValue, porque o valor do ID vem do pedido.

O MapsId eh usando quando o ID vem de outra entidade (como Order)


E eu não colocasse o @Id private Long id; em Payment, o código funcionaria?
Resposta: Não!
    O JPA exige que toda entidade tenha um campo anotado com @Id.
    Mesmo que o id venha da outra entidade (Order), o Payment ainda precisa declarar o campo id como sendo sua chave primária. Sem isso, o Hibernate/JPA não consegue mapear a entidade corretamente.

Por que ainda precisamos declarar @Id em Payment?
    Porque @MapsId não cria o campo automaticamente. Ele só liga o campo id ao relacionamento com Order.

    Então, quando você faz:

        @OneToOne
        @MapsId
        private Order order;
        Você tá dizendo:

        "Esse Payment usa o id de Order como seu próprio id."

    Mas você ainda precisa dizer ao JPA qual é esse campo:

        @Id
        private Long id; // <- Esse campo é obrigatório pra definir a PK

    Resumo:
        Declaração	             Obrigatório?	Por quê
        @OneToOne + @MapsId	          sim	    Define o relacionamento e diz que o ID vem do Order
        @Id private Long id;	      sim	    Toda entidade precisa de uma chave primária declarada explicitamente

    O que aconteceria se você removesse o @Id?
        Você teria um erro do tipo:
        org.hibernate.AnnotationException: No identifier specified for entity: Payment
        Ou seja: o JPA reclama porque ele não sabe qual é o campo que representa a chave primária.

    Dica final:
        Mesmo que o id não seja usado diretamente (porque o acesso geralmente vai ser via payment.getOrder().getId()), ele precisa estar lá, mapeado como @Id.


*/