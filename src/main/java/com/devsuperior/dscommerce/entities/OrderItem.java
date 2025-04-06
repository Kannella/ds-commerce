package com.devsuperior.dscommerce.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {

    @EmbeddedId // Essa abaixo eh uma chave estrangeira composta que aponta tanto para product quanto para order
    private OrderItemPK id = new OrderItemPK(); // Quando a gente tem essa classe auxiliar como atributo precisamos instanciar ela na hora

    private Integer quantity; //alem da chave ser composta e apontar para product e order eu tenho a propriedade quantity

    private Double price; //alem da chave ser composta e apontar para product e order eu tenho a propriedade price

    public OrderItem() {
    }

    //Eu nao vou expor um parametro do tipo OrderItemPK, eu coloco somente o order mesmo e o product pq sao as unicas classes que eh preciso conhecer na hora de instanciar essa classe OrderItem. Nao eh necessario outras calsses saberem da implementascao da chave composta
    //Por isso eu coloco no construtor somente o resto.
    //

    public OrderItem(Order order, Product product, Integer quantity, Double price) {
        id.setOrder(order); //passa como parametro para a classe OrderItemPK que aponta para Order e Product
        id.setProduct(product); //passa como parametro para a classe OrderItemPK que aponta para Order e Product
        this.quantity = quantity;
        this.price = price;
    }


    // A partir do OrderItem eu consigo pegar meu Order
    public Order getOrder() {
        return id.getOrder();
    }

    // A partir do setOrder eu consigo settar meu Order
    public void setOrder(Order order) {
        id.setOrder(order);
    }

    // A partir do OrderItem eu consigo pegar meu Product
    public Product getProduct() {
        return id.getProduct();
    }

    // A partir do OrderItem eu consigo settar meu Product
    public void setProduct(Product product) {
        id.setProduct(product);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderItem orderItem)) return false;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
