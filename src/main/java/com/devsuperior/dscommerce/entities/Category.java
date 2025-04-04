package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_category") //para que eu possa customizar a tabela no banco
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Pq esse ID vai ser autoincrement no banco H2
    private Long id;
    private String name;

    //relacionamento muito para muitos. O mappedBy quer dizer que,
    // como do outro lado (classe Product) ja foi mapeado o categories (private Set<Category>
    // categories = new HashSet<>();) entao na classe Category eu vou colocar o @ManyToMany(mappedBy = "categories") que mapeia o outro lado
    // pelo nome do atributo do set (categories)
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    public Category() {

    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
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

    public Set<Product> getProducts() {
        return products;
    }
}
