package com.libreria.ejercicio1.servicios;

import com.libreria.ejercicio1.entidades.Cliente;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.entidades.Prestamo;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.repositorios.ClienteRepositorio;
import com.libreria.ejercicio1.repositorios.LibroRepositorio;
import com.libreria.ejercicio1.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lautaro Yanzon
 */
@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamorepo;

    @Autowired
    private ClienteRepositorio clienterepo;

    @Autowired
    private LibroRepositorio librorepo;

    @Transactional
    public void prestar(String idCliente, String idLibro) throws ErrorServicio {

        Prestamo p = new Prestamo();

        p.setFechaPrestamo(new Date());
        p.setAlta(Boolean.TRUE);

        Cliente c = clienterepo.buscarPorId(idCliente);

        if (c != null) {
            p.setCliente(c);
        } else {
            throw new ErrorServicio("No se encontro al cliente.");
        }

        Libro l = librorepo.buscarPorId(idLibro);

        if (l != null) {
            if (l.getEjemplaresRestantes() != 0) {
                l.setEjemplaresRestantes(l.getEjemplaresRestantes() - 1);
                librorepo.save(l);
                p.setLibro(l);
            } else {
                throw new ErrorServicio("No quedan ejemplares para prestar.");
            }
        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }

        prestamorepo.save(p);
    }

    @Transactional
    public void darDeBaja(String idPrestamo) throws ErrorServicio {

        Prestamo p = prestamorepo.buscarPorId(idPrestamo);

        if (p != null) {

            p.setFechaDevolucion(new Date());
            p.setAlta(Boolean.FALSE);

            prestamorepo.save(p);
        } else {
            throw new ErrorServicio("No se encontro el prestamo.");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Prestamo> consultarActivos(){
        
        return prestamorepo.buscarActivos();
    }

}
