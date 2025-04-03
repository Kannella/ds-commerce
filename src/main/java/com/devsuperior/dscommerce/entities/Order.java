package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;

import java.time.Instant;

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
}
