package com.devsuperior.dscommerce.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class OrderItemPK {

    //A classe OrderItemPK tem 2 chaves primarias (chave composta): order_id e product_id

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItemPK() {

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

/*
@Embeddable explicado:
    Essa anotação diz:

    “Essa classe não é uma entidade sozinha, mas sim um componente que será
    usado como chave composta dentro de outra entidade.”

    Ou seja, a classe OrderItemPK representa a chave composta que será
    embutida dentro da entidade OrderItem.
*/