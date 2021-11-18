

package com.libreria.ejercicio1.repositorios;

import com.libreria.ejercicio1.entidades.Cliente;
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
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{

    @Query("SELECT c FROM Cliente c WHERE id = :id")
    public Cliente buscarPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Cliente c WHERE c.alta = 1 ORDER BY c.nombre")
    public List<Cliente> buscarActivos();
}
