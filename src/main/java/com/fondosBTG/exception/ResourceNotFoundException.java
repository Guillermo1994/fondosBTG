package com.fondosBTG.exception;

/**
 * Excepción personalizada lanzada cuando un recurso solicitado no se encuentra en el sistema.
 * Esta excepción extiende RuntimeException y permite especificar un mensaje para detallar el recurso no encontrado.
 *
 * @author Guillermo Ramirez
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
