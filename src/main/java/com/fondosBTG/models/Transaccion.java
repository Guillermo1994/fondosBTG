package com.fondosBTG.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(value = "transacciones")
public class Transaccion {

    @Id
    private String id;
    private String tipo;
    private String fondoId;
    private String fondoNombre;
    private double monto;
    private LocalDateTime fecha;
    private String clienteId;


}
