package com.fondosBTG.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Representa una transacción realizada por un cliente en el sistema de fondos.
 * Se persiste en la colección "transacciones" de MongoDB.
 *
 * @author Guillermo Ramirez
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(value = "transacciones")
public class Transaccion {

    /** Identificador único de la transacción */
    @Id
    private String id;

    /** Tipo de transacción (por ejemplo, "Apertura", "Cancelación") */
    private String tipo;

    /** Identificador del fondo relacionado con la transacción */
    private String fondoId;

    /** Nombre del fondo relacionado con la transacción */
    private String fondoNombre;

    /** Monto involucrado en la transacción */
    private double monto;

    /** Fecha y hora en que se realizó la transacción */
    private LocalDateTime fecha;

    /** Identificador del cliente que realizó la transacción */
    private String clienteId;
}
