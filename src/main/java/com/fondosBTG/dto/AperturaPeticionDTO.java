package com.fondosBTG.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para manejar las solicitudes de apertura de un fondo.
 * Contiene los datos necesarios para realizar la operación de apertura.
 *
 * @author Guillermo Ramirez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AperturaPeticionDTO {
    private double monto;
    private String canalNotificacion;
}
