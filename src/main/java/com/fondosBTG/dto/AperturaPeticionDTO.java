package com.fondosBTG.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AperturaPeticionDTO {
    private double monto;
    private String canalNotificacion;
}
