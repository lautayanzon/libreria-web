package com.libreria.ejercicio1.servicios;

import com.libreria.ejercicio1.entidades.Editorial;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.repositorios.EditorialRepositorio;
import com.libreria.ejercicio1.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lautaro Yanzon
 */
@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio ediRepo;

    @Autowired
    private LibroRepositorio librorepo;

    @Transactional
    public void guardarEditorial(String nombre, String titulo) throws ErrorServicio {

        validar(nombre);
        
        Editorial editorial1 = ediRepo.buscarPorNombre(nombre);
        
        if (editorial1 != null) {
            throw new ErrorServicio("La editorial ya existe.");
        }

        Editorial e = new Editorial();
        e.setNombre(nombre);
        e.setAlta(Boolean.TRUE);

        Libro libro = librorepo.buscarPorTitulo(titulo);
        if (libro != null) {
            libro.setEditorial(e);
        }

        ediRepo.save(e);
    }

    @Transactional
    public void modificarEditorial(String id, String nombre, String titulo) throws ErrorServicio {

        validar(nombre);

        Optional<Editorial> respuesta = ediRepo.findById(id);

        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            e.setNombre(nombre);

            Libro libro = librorepo.buscarPorTitulo(titulo);

            if (libro != null) {
                libro.setEditorial(e);
                librorepo.save(libro);
            }

            ediRepo.save(e);
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
    }

    @Transactional(readOnly = true)
    public Editorial consultarPorID(String id) throws ErrorServicio {

        if (id == null) {
            throw new ErrorServicio("El ID no puede ser nulo.");
        }

        return ediRepo.buscarPorID(id);
    }

    @Transactional(readOnly = true)
    public Editorial consultarPorNombre(String nombre) throws ErrorServicio {

        validar(nombre);

        Editorial editorial = ediRepo.buscarPorNombre(nombre);

        if (editorial == null) {
            throw new ErrorServicio("La editorial no existe.");
        }

        return editorial;
    }

    @Transactional(readOnly = true)
    public List<Editorial> consultarTodos() {

        return ediRepo.buscarTodos();
    }

    @Transactional
    public void eliminarEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = ediRepo.findById(id);

        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();

            ediRepo.delete(e);
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
    }

    @Transactional
    public void darDeBaja(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = ediRepo.findById(id);

        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            e.setAlta(Boolean.FALSE);
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
    }

    @Transactional
    public void darDeAlta(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = ediRepo.findById(id);

        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            e.setAlta(Boolean.TRUE);
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
    }

    private void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre de la editorial no puede ser nulo.");
        }
    }

}
