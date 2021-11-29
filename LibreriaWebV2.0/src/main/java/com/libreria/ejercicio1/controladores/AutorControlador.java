package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Autor;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.AutorServicio;
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
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorservicio;

    @Autowired
    private LibroServicio libroservicio;

    //Mostrar tabla
    @GetMapping("/tabla")
    public String autor(ModelMap modelo) {

        List<Libro> listaAutores = libroservicio.consultarPorAutor();

        modelo.put("lista", listaAutores);
        modelo.put("tablahead", "autores");
        modelo.put("pagtitulo", "Autores");
        modelo.put("tr", "autores");
        modelo.put("urlguardar", "/autor/formautor");
        modelo.put("btguardar", "un autor");

        return "tabla.html";
    }

    //Guardar autores
    @GetMapping("/formautor")
    public String guardarAutor(ModelMap modelo) {

        List<Libro> listaLibros = libroservicio.consultarPorAutorActivo();

        modelo.put("lista", listaLibros);

        modelo.put("pagtitulo", "Formulario autores");
        modelo.put("formhead", "un autor");
        modelo.put("urlvolver", "/autor/tabla");
        modelo.put("form", "autoreditorial");
        modelo.put("label", "del autor");
        modelo.put("urlaction", "/autor/registrarautor");

        return "formulario.html";
    }

    @PostMapping("/registrarautor")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String nombre, @RequestParam(required = false) String titulo) {

        try {
            autorservicio.guardarAutor(nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Formulario autores");
            modelo.put("formhead", "un autor");
            modelo.put("titulo", titulo);
            modelo.put("urlvolver", "/autor/tabla");
            modelo.put("form", "autoreditorial");
            modelo.put("label", "del autor");
            modelo.put("urlaction", "/autor/registrarautor");
            return "formulario.html";
        }

        return "redirect:/autor/tabla";
    }

    //Modificar autores
    @GetMapping("/modificar/{id}")
    public String modificarAutor(ModelMap modelo, @PathVariable String id) {

        try {
            Autor autor = autorservicio.consultarPorID(id);

            modelo.put("pagtitulo", "Modificar autor");
            modelo.put("formhead", "un autor");
            modelo.put("urlvolver", "/autor/tabla");
            modelo.put("form", "autor");

            modelo.put("autor", autor);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modificar.html";
        }

        return "modificar.html";
    }

    @PostMapping("/modificarautor/{id}")
    public String modificarAutor(ModelMap modelo, @RequestParam String id, @RequestParam String nombre, @RequestParam(required = false) String titulo) {

        try {
            autorservicio.modificarAutor(id, nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("titulo", titulo);
            modelo.put("nombre", nombre);
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Modificar autor");
            modelo.put("formhead", "un autor");
            modelo.put("urlvolver", "/autor/tabla");
            modelo.put("form", "autor");
            return "modificar.html";
        }

        return "redirect:/autor/tabla";
    }

    //Dar de baja-alta
    @GetMapping("/baja/{id}")
    public String darDeBaja(@PathVariable String id) {

        try {
            autorservicio.darDeBaja(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/autor/tabla";
    }

    @GetMapping("/alta/{id}")
    public String darDeAlta(@PathVariable String id) {

        try {
            autorservicio.darDeAlta(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/autor/tabla";
    }

}
