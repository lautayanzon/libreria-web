package com.libreria.ejercicio1.repositorios;

import com.libreria.ejercicio1.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lautaro Yanzon
 */
@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {

    @Query("SELECT p FROM Prestamo p WHERE id = :id")
    public Prestamo buscarPorId(@Param("id") String id);

    @Query("SELECT p FROM Prestamo p WHERE p.alta = 1 ORDER BY p.fechaPrestamo")
    public List<Prestamo> buscarActivos();
    
}
