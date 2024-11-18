package com.migueltarifa.transportesTestAI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejador de excepción para IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return errorRespuesta(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return errorRespuesta(HttpStatus.BAD_REQUEST.value(), "Falta el parámetro obligatorio: " + name);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleMissingFilePart(MissingServletRequestPartException ex) {
        String name = ex.getRequestPartName();
        return errorRespuesta(HttpStatus.BAD_REQUEST.value(), "Falta el parámetro obligatorio: " + name);
    }

    private ResponseEntity<Map<String, Object>> errorRespuesta(int status, String error){
        Map<String, Object> errorResponse = new HashMap<>();

        // Configura el mensaje de error personalizado
        errorResponse.put("status", status);
        errorResponse.put("error", error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(errorResponse);
    }
}
