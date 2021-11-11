package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Editorial;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
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
public class EditorialControlador {

    @Autowired
    private EditorialServicio ediservicio;

    @Autowired
    private LibroServicio libroservicio;

    //Mostrar tabla
    @GetMapping("/editorial")
    public String editorial(ModelMap model) {

        List<Editorial> listaEditorial = ediservicio.consultarTodos();

        model.put("editoriales", listaEditorial);

        return "editorial.html";
    }

    //Guardar editoriales
    @GetMapping("/editorial/formeditorial")
    public String guardarEditorial() {
        return "formeditorial.html";
    }

    @PostMapping("/registrareditorial")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String nombre, @RequestParam(required = false) String titulo) {

        try {
            ediservicio.guardarEditorial(nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            return "formeditorial.html";
        }

        return "redirect:/editorial";
    }

    //Modificar editoriales
    @GetMapping("/editorial/modificar/{id}")
    public String modificarEditorial(ModelMap modelo, @PathVariable String id) {

        try {
            Editorial editorial = ediservicio.consultarPorID(id);

            modelo.put("editorial", editorial);

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modeditorial.html";
        }

        return "modeditorial.html";
    }

    @PostMapping("/modificareditorial/{id}")
    public String modificarEditorial(ModelMap modelo, @RequestParam String id, @RequestParam String nombre, @RequestParam(required = false) String titulo) {

        try {
            ediservicio.modificarEditorial(id, nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("titulo", titulo);
            modelo.put("nombre", nombre);
            modelo.put("error", ex.getMessage());
            return "modeditorial.html";
        }

        return "redirect:/editorial";
    }

    //Dar de baja-alta
    @GetMapping("/editorial/baja/{id}")
    public String darDeBaja(@PathVariable String id) {

        try {
            ediservicio.darDeBaja(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/editorial";
    }

    @GetMapping("/editorial/alta/{id}")
    public String darDeAlta(@PathVariable String id) {

        try {
            ediservicio.darDeAlta(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/editorial";
    }

}
