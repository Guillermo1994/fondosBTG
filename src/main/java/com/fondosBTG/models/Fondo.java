package com.fondosBTG.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un fondo dentro del sistema de fondos.
 * Se persiste en la colección "fondos" de MongoDB.
 *
 * @author Guillermo Ramirez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(value = "fondos")
public class Fondo {

    /** Identificador único del fondo */
    @Id
    private String id;

    /** Nombre del fondo */
    private String nombre;

    /** Monto mínimo requerido para invertir en el fondo */
    private Double montoMinimo;

    /** Categoría del fondo */
    private String categoria;
}
