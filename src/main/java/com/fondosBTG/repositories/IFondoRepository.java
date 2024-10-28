package com.fondosBTG.repositories;

import com.fondosBTG.models.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la entidad Fondo, que proporciona métodos CRUD estándar
 * para interactuar con la base de datos MongoDB.
 *
 * @author Guillermo Ramirez
 */
public interface IFondoRepository extends MongoRepository<Fondo, String> {
}
