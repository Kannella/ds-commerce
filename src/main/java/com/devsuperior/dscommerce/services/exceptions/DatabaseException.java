package com.devsuperior.dscommerce.services.exceptions;

//Eu tenho uma classe de excessao personbalikzada que exige que eu informe uma mensagem na hora de instaciar o objeto dela

//Criando uma excessao customizada quando voce nao encontrar algum recurso que voce nao estiver usando
public class DatabaseException extends RuntimeException { //A minha excessao customizada para que ela seja uma excessao do java e que responda o try cath eu tenho que extender ela da excessao do java, no caso eu coloquei a RunTime pq ela nao exige que vc coloque um try catch

    public DatabaseException(String msg) {
        super(msg); //chamando o construtor da RunTimeExeption repassando essa mensagem para a RunTimeExeption
    }

}
