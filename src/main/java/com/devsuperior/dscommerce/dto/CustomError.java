package com.devsuperior.dscommerce.dto;

import java.time.Instant;

/*
Em uma classe com a annotation @ControllerAdvice,
podemos definir tratamentos globais para exceções específicas,
sem precisar ficar colocando try-catch em várias partes do código.
*/

//Tratando o erro (com essa classe) que aparece la no json quando nao encontra o id do produto que eu capturei com minha excessao personalizada
public class CustomError {
    private Instant timestamp; //Instante que deu o erro
    private Integer status;
    private String error;
    private String path;

    public CustomError(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
