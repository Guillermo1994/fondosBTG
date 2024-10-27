package com.fondosBTG.services.IServices;

import com.fondosBTG.models.Transaccion;

import java.util.List;

public interface ITransaccionService {
    String realizarApertura(String clienteId, String fondoId, double monto, String canalNotificacion);

    String realizarCancelacion(String clienteId, String fondoId, String transaccionId);

    List<Transaccion> verHistorial(String clienteId);
}
