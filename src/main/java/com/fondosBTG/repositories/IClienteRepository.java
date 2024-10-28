package com.fondosBTG.repositories;

import com.fondosBTG.models.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la entidad Cliente, que proporciona métodos CRUD estándar
 * para interactuar con la base de datos MongoDB.
 *
 * @author Guillermo Ramirez
 */
public interface IClienteRepository extends MongoRepository<Cliente, String> {
}
