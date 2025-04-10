package com.devsuperior.dscommerce.controllers.handlers;

import com.devsuperior.dscommerce.dto.CustomError;
import com.devsuperior.dscommerce.dto.ValidationError;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourcesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;

//Essas anotation e eu criei essa classe personalizada para sempre que dar uma excessao do tipo ResourcesNotFoundException, essas anotations do jpa vao interceptar e rodar o metodo resourceNotFound, pegando os dados do meu erro instanciando o objeto CustomError e tratando esse erro logo depois

// Esse metodo ResourcesNotFound eh um metodo que vai tratar ResourcesNotFoundException

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<CustomError> resourcesNotFound(ResourcesNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; //vou converter abaixo o tipo enumerado status em interio com o .value E VOU retornar um BAD REQUEST
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI()); //Instanciadno meu objeto de erro e epassando os argumentos para o meu parametro do meu erro personalizado
        return ResponseEntity.status(status).body(err); //retornando um ResponseEntity (que customiza minha resposta para responder o status certo de erro e tbm onde o corpo do erro la no postman vai ser o objeto armazenado na variavel err
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; //vou converter abaixo o tipo enumerado status em interio com o .value  E VOU retornar um BAD REQUEST
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI()); //Instanciadno meu objeto de erro e epassando os argumentos para o meu parametro do meu erro personalizado
        return ResponseEntity.status(status).body(err); //retornando um ResponseEntity (que customiza minha resposta para responder o status certo de erro e tbm onde o corpo do erro la no postman vai ser o objeto armazenado na variavel err
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; //vou converter abaixo o tipo enumerado status em interio com o .value  E VOU retornar um UNPROCESSABLE_ENTITY
        ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados invalidos", request.getRequestURI()); //Instanciadno meu objeto de erro e epassando os argumentos para o meu parametro do meu erro personalizado

        // e.getBindingResult().getFieldError(); //essa lista que vai conter as excessoes que vao sendo geradas conforme as anotations do bean validation que tem la no ProductDTO
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        //err.addError("name", "mensagem de teste");
        //err.addError("price", "preco invalido");

        return ResponseEntity.status(status).body(err); //retornando um ResponseEntity (que customiza minha resposta para responder o status certo de erro e tbm onde o corpo do erro la no postman vai ser o objeto armazenado na variavel err
    }
}