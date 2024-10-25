package com.fondosBTG.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(value = "fondos")
public class Fondo {
    @Id
    private String id;
    private String nombre;
    private Double montoMinimo;
    private String categoria;
}
