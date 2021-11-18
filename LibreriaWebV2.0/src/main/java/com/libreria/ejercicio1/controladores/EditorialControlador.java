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
    public String editorial(ModelMap modelo) {

        List<Libro> listaEditorial = libroservicio.consultarPorEditorial();

        modelo.put("lista", listaEditorial);
        modelo.put("tablahead", "editoriales");
        modelo.put("pagtitulo", "Editoriales");
        modelo.put("tr", "editoriales");
        modelo.put("urlguardar", "/editorial/formeditorial");
        modelo.put("btguardar", "una editorial");

        return "tabla.html";
    }

    //Guardar editoriales
    @GetMapping("/editorial/formeditorial")
    public String guardarEditorial(ModelMap modelo) {

        List<Libro> listaLibros = libroservicio.consultarPorEditorialActiva();

        modelo.put("lista", listaLibros);

        modelo.put("pagtitulo", "Formulario editoriales");
        modelo.put("formhead", "una editorial");
        modelo.put("urlvolver", "/editorial");
        modelo.put("form", "autoreditorial");
        modelo.put("label", "de la editorial");
        modelo.put("urlaction", "/registrareditorial");

        return "formulario.html";
    }

    @PostMapping("/registrareditorial")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String nombre, @RequestParam(required = false) String titulo) {

        try {
            ediservicio.guardarEditorial(nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("titulo", titulo);
            modelo.put("pagtitulo", "Formulario editoriales");
            modelo.put("formhead", "una editorial");
            modelo.put("urlvolver", "/editorial");
            modelo.put("form", "autoreditorial");
            modelo.put("label", "de la editorial");
            modelo.put("urlaction", "/registrareditorial");

            return "formulario.html";
        }

        return "redirect:/editorial";
    }

    //Modificar editoriales
    @GetMapping("/editorial/modificar/{id}")
    public String modificarEditorial(ModelMap modelo, @PathVariable String id) {

        try {
            Editorial editorial = ediservicio.consultarPorID(id);

            modelo.put("editorial", editorial);
            modelo.put("pagtitulo", "Modificar editorial");
            modelo.put("formhead", "una editorial");
            modelo.put("urlvolver", "/editorial");
            modelo.put("form", "autoreditorial");

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modificar.html";
        }

        return "modificar.html";
    }

    @PostMapping("/modificareditorial/{id}")
    public String modificarEditorial(ModelMap modelo, @RequestParam String id, @RequestParam String nombre, @RequestParam(required = false) String titulo) {

        try {
            ediservicio.modificarEditorial(id, nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("titulo", titulo);
            modelo.put("nombre", nombre);
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Modificar editorial");
            modelo.put("formhead", "una editorial");
            modelo.put("urlvolver", "/editorial");
            modelo.put("form", "autoreditorial");
            return "modificar.html";
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
