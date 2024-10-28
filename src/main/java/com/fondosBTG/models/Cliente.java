package com.fondosBTG.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un cliente dentro del sistema de fondos.
 * Se persiste en la colección "clientes" de MongoDB.
 *
 * @author Guillermo Ramirez
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(value = "clientes")
public class Cliente {

    /** Identificador único del cliente */
    @Id
    private String id;

    /** Nombre del cliente */
    private String nombre;

    /** Saldo inicial del cliente */
    private Double saldoInicial;

    /** Dirección de correo electrónico del cliente */
    private String email;

    /** Número de teléfono del cliente */
    private String telefono;
}
