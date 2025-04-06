package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_user") //para que eu possa customizar a tabela no banco
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Pq esse ID vai ser autoincrement no banco H2
    private Long id;
    private String name;

    //O email do usuario eu nao quero que repita, por isso, eu posso confiigurar outro campo que nao seja
    //a chave primaria, como um campo unico que nao se repete. Eu configuro isso com a anotation abaixo
    @Column(unique = true)
    private String email;

    private String phone;
    private LocalDate birthDate;
    private String password;

    @OneToMany(mappedBy = "client") // Um usuario para muitos pedidos. O mappedBy eh o nome do atributo la na outra classe, no caso client
    private List<Order> orders = new ArrayList<>(); //Colecao de order pois o usuario pode ter varios pedidos. E ja instanciei ela vazia

    public User() {

    }

    public User(Long id, String password, LocalDate birthDate, String phone, String email, String name) {
        this.id = id;
        this.password = password;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
