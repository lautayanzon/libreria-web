package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Autor;
import com.libreria.ejercicio1.entidades.Editorial;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.AutorServicio;
import com.libreria.ejercicio1.servicios.EditorialServicio;
import com.libreria.ejercicio1.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Lautaro Yanzon
 */
@Controller
@RequestMapping("/")
public class LibroControlador {

    @Autowired
    private LibroServicio libroservicio;

    @Autowired
    private AutorServicio autorS;

    @Autowired
    private EditorialServicio editorialS;

    //Mostrar libros
    @GetMapping("/libro")
    public String libro(ModelMap modelo) {

        List<Libro> listaLibros = libroservicio.consultarActivos();

        modelo.put("libros", listaLibros);

        return "libro.html";
    }

    //Guardar un libro
    @GetMapping("/libro/formlibro")
    public String guardarLibro() {
        return "formlibro.html";
    }

    @PostMapping("/registrarlibro")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String titulo, @RequestParam(required = false) Long isbn, @RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) Integer ejemplaresP, @RequestParam(required = false) String autor, @RequestParam(required = false) String editorial) {

        try {

            libroservicio.guardarLibro(isbn, titulo, anio, ejemplares, ejemplaresP, autor, editorial);

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "formlibro.html";
        }

        return "index.html";
    }

    //Modificar un libro
    @GetMapping("/libro/modificar/{id}")
    public String modificarLibro(ModelMap modelo, @PathVariable String id) {

        try {
            Libro libro = libroservicio.consultarPorId(id);

            modelo.put("libro", libro);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modlibro.html";
        }
        return "modlibro.html";
    }

    @PostMapping("/modificarlibro/{id}")
    public String modificarLibro(ModelMap modelo, @RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresP) {

        try {
            libroservicio.modificarLibro(id, isbn, titulo, anio, ejemplares, ejemplaresP);
        } catch (ErrorServicio ex) {
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresP);
            modelo.put("error", ex.getMessage());
            return "modlibro.html";
        }

        return "redirect:/libro";
    }

    //Dar de baja-alta
    @GetMapping("/libro/baja/{id}")
    public String bajaLibro(@PathVariable String id) {

        try {
            libroservicio.darDeBaja(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/libro";
    }

    @GetMapping("/libro/alta/{id}")
    public String altaLibro(@PathVariable String id) {

        try {
            libroservicio.darDeAlta(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/libro";
    }

}
