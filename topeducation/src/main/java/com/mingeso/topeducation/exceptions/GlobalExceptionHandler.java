package com.mingeso.topeducation.exceptions;

import com.mingeso.topeducation.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(RegistroNoExisteException.class)
    public ResponseEntity<Response> handleRegistroNoExisteException(RegistroNoExisteException ex){
        Response response = new Response(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FechaNoPermitidaException.class)
    public ResponseEntity<Response> handleFechaNoPermitidadException(FechaNoPermitidaException ex){
        Response response = new Response(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValorFueraDeRangoException.class)
    public ResponseEntity<Response> handleValorFueraDeRangoException(ValorFueraDeRangoException ex){
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<Response> handleRegistroDuplicadoException(RegistroDuplicadoException ex){
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrosFaltantesException.class)
    public ResponseEntity<Response> handleRegistrosFaltantesException(RegistrosFaltantesException ex){
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
