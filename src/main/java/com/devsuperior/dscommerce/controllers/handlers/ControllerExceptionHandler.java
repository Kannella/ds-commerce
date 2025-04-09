package com.devsuperior.dscommerce.controllers.handlers;

import com.devsuperior.dscommerce.dto.CustomError;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourcesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}