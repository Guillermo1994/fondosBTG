package com.fondosBTG.repositories;

import com.fondosBTG.models.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByClienteId(String clienteId);
}
