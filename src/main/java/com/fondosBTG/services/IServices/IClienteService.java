package com.fondosBTG.services.IServices;

import com.fondosBTG.models.Cliente;

public interface IClienteService {
    Cliente obtenerClientPorId(String id);

    Cliente guardarCliente(Cliente cliente);
}
