package com.fondosBTG.repositories;

import com.fondosBTG.models.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repositorio para la entidad Transaccion, que proporciona métodos CRUD estándar
 * para interactuar con la base de datos MongoDB.
 *
 * @author Guillermo Ramirez
 */
public interface ITransaccionRepository extends MongoRepository<Transaccion, String> {

    /**
     * Encuentra todas las transacciones asociadas a un cliente específico
     * usando su identificador único.
     *
     * @param clienteId El identificador del cliente cuyas transacciones se desean obtener.
     * @return Una lista de transacciones asociadas al cliente.
     */
    List<Transaccion> findByClienteId(String clienteId);
}
