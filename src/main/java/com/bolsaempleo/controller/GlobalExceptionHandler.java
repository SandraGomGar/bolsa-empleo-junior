package com.bolsaempleo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleJsonParseError(HttpMessageNotReadableException ex) {
        String mensaje = "Error en el formato de los datos. Verifica los campos enviados.";

        if (ex.getMessage().contains("TipoUsuario")) {
            mensaje = "Tipo de usuario no válido. Usa 'CANDIDATO' o 'EMPRESA'.";
        }

        return Map.of(
                "success", false,
                "message", mensaje
        );
    }
}