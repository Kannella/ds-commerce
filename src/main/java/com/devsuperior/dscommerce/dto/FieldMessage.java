package com.devsuperior.dscommerce.dto;

//Criando um tipo especifico para representar a mensagem que retorna do bean validation caso o campo nao atenda as constraints
public class FieldMessage {
    private String fieldName;
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
