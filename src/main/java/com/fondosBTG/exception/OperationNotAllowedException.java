package com.fondosBTG.exception;

/**
 * Excepción personalizada lanzada cuando se intenta realizar una operación no permitida.
 * Esta excepción extiende RuntimeException y permite indicar el motivo de la restricción.
 *
 * @author Guillermo Ramirez
 */
public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
