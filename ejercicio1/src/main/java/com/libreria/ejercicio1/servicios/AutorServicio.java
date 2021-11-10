package com.libreria.ejercicio1.servicios;

import com.libreria.ejercicio1.entidades.Autor;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.repositorios.AutorRepositorio;
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
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepo;

    @Autowired
    private LibroRepositorio librorepo;

    @Transactional
    public void guardarAutor(String nombre, String titulo) throws ErrorServicio {

        validar(nombre);

        Autor a = new Autor();
        a.setNombre(nombre);
        a.setAlta(Boolean.TRUE);

        Libro libro = librorepo.buscarPorTitulo(titulo);

        if (libro != null) {
            libro.setAutor(a);
            librorepo.save(libro);
        }

        autorRepo.save(a);
    }

    @Transactional
    public void modificarAutor(String id, String nombre, String titulo) throws ErrorServicio {

        validar(nombre);

        Optional<Autor> respuesta = autorRepo.findById(id);

        if (respuesta.isPresent()) {
            Autor a = respuesta.get();
            a.setNombre(nombre);

            Libro libro = librorepo.buscarPorTitulo(titulo);

            if (libro != null) {
                libro.setAutor(a);
                librorepo.save(libro);
            }

            autorRepo.save(a);
        } else {
            throw new ErrorServicio("No se encontro al autor.");
        }
    }

    @Transactional(readOnly = true)
    public Autor consultarPorID(String id) throws ErrorServicio {

        if (id == null) {
            throw new ErrorServicio("La ID no puede ser nula");
        }

        return autorRepo.buscarPorID(id);
    }

    @Transactional(readOnly = true)
    public Autor consultarPorNombre(String nombre) throws ErrorServicio {

        validar(nombre);

        Autor autor = autorRepo.buscarPorNombre(nombre);

        if (autor == null) {
            throw new ErrorServicio("El autor no existe.");
        }

        return autor;
    }

    @Transactional(readOnly = true)
    public List<Autor> consultarTodos() {

        return autorRepo.buscarTodos();
    }

    @Transactional
    public void eliminarAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepo.findById(id);

        if (respuesta.isPresent()) {
            Autor a = respuesta.get();

            autorRepo.delete(a);
        } else {
            throw new ErrorServicio("No se encontro el autor.");
        }
    }

    @Transactional
    public void darDeBaja(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepo.findById(id);

        if (respuesta.isPresent()) {
            Autor a = respuesta.get();
            a.setAlta(Boolean.FALSE);
        } else {
            throw new ErrorServicio("No se encontro el autor.");
        }
    }

    @Transactional
    public void darDeAlta(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepo.findById(id);

        if (respuesta.isPresent()) {
            Autor a = respuesta.get();
            a.setAlta(Boolean.TRUE);
        } else {
            throw new ErrorServicio("No se encontro el autor.");
        }
    }

    private void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede ser nulo.");
        }
    }

}
