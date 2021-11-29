package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.AutorServicio;
import com.libreria.ejercicio1.servicios.EditorialServicio;
import com.libreria.ejercicio1.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroservicio;

    @Autowired
    private AutorServicio autorS;

    @Autowired
    private EditorialServicio editorialS;

    //Mostrar libros
    @GetMapping("/tabla")
    public String libro(ModelMap modelo) {

        List<Libro> listaLibros = libroservicio.consultarActivos();

        modelo.put("lista", listaLibros);
        modelo.put("tablahead", "libros");
        modelo.put("pagtitulo", "Libros");
        modelo.put("tr", "libros");
        modelo.put("urlguardar", "/libro/formlibro");
        modelo.put("btguardar", "un libro");

        return "tabla.html";
    }

    //Guardar un libro
    @GetMapping("/formlibro")
    public String guardarLibro(ModelMap modelo) {

        modelo.put("pagtitulo", "Formulario libros");
        modelo.put("formhead", "un libro");
        modelo.put("urlvolver", "/libro/tabla");
        modelo.put("form", "libro");
        modelo.put("urlaction", "/registrarlibro");

        return "formulario.html";
    }

    @PostMapping("/registrarlibro")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String titulo, @RequestParam(required = false) Long isbn, @RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) Integer ejemplaresP, @RequestParam(required = false) String autor, @RequestParam(required = false) String editorial) {

        try {

            libroservicio.guardarLibro(isbn, titulo, anio, ejemplares, ejemplaresP, autor, editorial);

        } catch (ErrorServicio ex) {
            modelo.put("titulo", titulo);
            modelo.put("isbn", isbn);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresP", ejemplaresP);
            modelo.put("autor", autor);
            modelo.put("editorial", editorial);
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Formulario libros");
            modelo.put("formhead", "un libro");
            modelo.put("urlvolver", "/libro/tabla");
            modelo.put("form", "libro");
            modelo.put("urlaction", "/registrarlibro");
            return "formulario.html";
        }

        return "redirect:/tabla";
    }

    //Modificar un libro
    @GetMapping("/modificar/{id}")
    public String modificarLibro(ModelMap modelo, @PathVariable String id) {

        try {
            Libro libro = libroservicio.consultarPorId(id);

            modelo.put("libro", libro);
            modelo.put("pagtitulo", "Modificar libro");
            modelo.put("formhead", "un libro");
            modelo.put("urlvolver", "/libro/tabla");
            modelo.put("form", "libro");

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modificar.html";
        }
        return "modificar.html";
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
             modelo.put("pagtitulo", "Modificar libro");
            modelo.put("formhead", "un libro");
            modelo.put("urlvolver", "/libro/tabla");
            modelo.put("form", "libro");
            return "modificar.html";
        }

        return "redirect:/tabla";
    }

    //Dar de baja-alta
    @GetMapping("/baja/{id}")
    public String bajaLibro(@PathVariable String id) {

        try {
            libroservicio.darDeBaja(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/tabla";
    }

    @GetMapping("/alta/{id}")
    public String altaLibro(@PathVariable String id) {

        try {
            libroservicio.darDeAlta(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/tabla";
    }

}
