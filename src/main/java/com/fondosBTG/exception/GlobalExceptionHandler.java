package com.fondosBTG.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * Proporciona métodos para capturar y manejar excepciones específicas
 * y devolver una respuesta adecuada con el estado HTTP correspondiente.
 *
 * @author Guillermo Ramirez
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo ResourceNotFoundException.
     * Devuelve un mensaje de error y un estado HTTP 404.
     *
     * @param ex La excepción ResourceNotFoundException capturada.
     * @return Un ResponseEntity que contiene los detalles del error y el estado HTTP.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de tipo OperationNotAllowedException.
     * Devuelve un mensaje de error y un estado HTTP 400.
     *
     * @param ex La excepción OperationNotAllowedException capturada.
     * @return Un ResponseEntity que contiene los detalles del error y el estado HTTP.
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<?> handleOperationNotAllowedException(OperationNotAllowedException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja todas las demás excepciones no capturadas por manejadores específicos.
     * Devuelve un mensaje de error genérico y un estado HTTP 500.
     *
     * @param ex La excepción global capturada.
     * @return Un ResponseEntity que contiene los detalles del error y el estado HTTP.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Ocurrió un error inesperado: " + ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
