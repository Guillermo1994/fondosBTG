package com.fondosBTG.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(value = "clientes")
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private Double saldoInicial = 500000.0; // Saldo inicial de COP $500.000
    private String email;
    private String telefono;
}
