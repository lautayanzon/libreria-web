package com.libreria.ejercicio1.servicios;

import com.libreria.ejercicio1.entidades.Autor;
import com.libreria.ejercicio1.entidades.Editorial;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.repositorios.AutorRepositorio;
import com.libreria.ejercicio1.repositorios.EditorialRepositorio;
import com.libreria.ejercicio1.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lautaro Yanzon
 */
@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libror;

    @Autowired
    private AutorRepositorio autorepo;

    @Autowired
    private EditorialRepositorio editorialrepo;

    @Transactional
    public void guardarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresP, String autor, String editorial) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, ejemplaresP);

        Autor autor1 = validarAutor(autor);

        Editorial editorial1 = validarEditorial(editorial);

        Libro l = new Libro();
        l.setIsbn(isbn);
        l.setTitulo(titulo);
        l.setAnio(anio);
        l.setEjemplares(ejemplares);
        l.setEjemplaresPrestados(ejemplaresP);
        l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());

        l.setAutor(autor1);
        l.setEditorial(editorial1);

        l.setAlta(Boolean.TRUE);

        libror.save(l);
    }

    @Transactional
    public void modificarLibro(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresP) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, ejemplaresP);

        Libro l = libror.buscarPorId(id);

        if (l != null) {
            l.setIsbn(isbn);
            l.setTitulo(titulo);
            l.setAnio(anio);
            l.setEjemplares(ejemplares);
            l.setEjemplaresPrestados(ejemplaresP);
            l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());

            libror.save(l);

        } else {
            throw new ErrorServicio("El libro no se encuentra en la base de datos.");
        }
    }

    @Transactional
    public void darDeBaja(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libror.findById(id);

        if (respuesta.isPresent()) {
            Libro l = respuesta.get();
            l.setAlta(Boolean.FALSE);
            libror.save(l);
        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    @Transactional
    public void darDeAlta(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libror.findById(id);

        if (respuesta.isPresent()) {
            Libro l = respuesta.get();
            l.setAlta(Boolean.TRUE);
            libror.save(l);
        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    @Transactional
    public void eliminarLibro(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libror.findById(id);

        if (respuesta.isPresent()) {
            Libro l = respuesta.get();

            libror.delete(l);

        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    public Libro consultarPorId(String id) throws ErrorServicio {

        if (id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El id no puede ser nulo.");
        }

        Libro libro = libror.buscarPorId(id);

        return libro;
    }

    @Transactional(readOnly = true)
    public Libro consultarPorTitulo(String titulo) throws ErrorServicio {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorServicio("El titulo no puede ser nulo.");
        }

        Libro libro = libror.buscarPorTitulo(titulo);

        return libro;
    }

    @Transactional(readOnly = true)
    public Libro consultarPorAutor(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        Libro libro = libror.buscarPorAutor(nombre);

        return libro;
    }

    @Transactional(readOnly = true)
    public Libro consultarPorEditorial(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        Libro libro = libror.buscarPorEditorial(nombre);

        return libro;
    }

    @Transactional(readOnly = true)
    public Libro consultarPorISBN(Long isbn) throws ErrorServicio {

        if (isbn == null) {
            throw new ErrorServicio("El ISBN no puede ser nulo.");
        }

        Libro libro = libror.buscarPorISBN(isbn);

        return libro;
    }

    @Transactional(readOnly = true)
    public List<Libro> consultarActivos() {

        List<Libro> listaLibros = libror.buscarActivos();

        return listaLibros;
    }

    @Transactional(readOnly = true)
    public List<Libro> consultarTodos() {

        List<Libro> listaLibros = libror.buscarTodos();

        return listaLibros;
    }

    private void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresP) throws ErrorServicio {
        if (isbn == null) {
            throw new ErrorServicio("El ISBN no puede ser nulo.");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorServicio("El titulo no puede ser nulo.");
        }
        if (anio == null) {
            throw new ErrorServicio("El año no puede ser nulo.");
        }
        if (ejemplares == null || ejemplares < 0) {
            throw new ErrorServicio("Los ejemplares no pueden ser nulos.");
        }
        if (ejemplaresP == null || ejemplaresP < 0) {
            throw new ErrorServicio("Los ejemplares prestados no pueden ser nulos.");
        }
        
        if (ejemplares < ejemplaresP) {
            throw new ErrorServicio("No hay suficientes ejemplares.");
        }
    }

    private Autor validarAutor(String autor) throws ErrorServicio {

        Autor autor1 = autorepo.buscarPorNombre(autor);

        if (autor1 != null) {
            return autor1;
        } else {

            if (autor == null || autor.trim().isEmpty()) {
                return null;
            }

            Autor a = new Autor();
            a.setNombre(autor);
            a.setAlta(Boolean.TRUE);

            autorepo.save(a);

            return a;
        }
    }

    private Editorial validarEditorial(String editorial) {

        Editorial editorial1 = editorialrepo.buscarPorNombre(editorial);

        if (editorial1 != null) {
            return editorial1;
        } else {

            if (editorial == null || editorial.trim().isEmpty()) {
                return null;
            }

            Editorial e = new Editorial();
            e.setNombre(editorial);
            e.setAlta(Boolean.TRUE);

            editorialrepo.save(e);

            return e;
        }
    }

}
