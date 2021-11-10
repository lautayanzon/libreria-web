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
public class AutorControlador {

    @Autowired
    private AutorServicio autorservicio;

    @Autowired
    private LibroServicio libroservicio;

    //Mostrar tabla
    @GetMapping("/autor")
    public String autor(ModelMap model) {

        List<Autor> listaAutores = autorservicio.consultarTodos();

        model.put("autores", listaAutores);

        return "autor.html";
    }

    //Guardar autores
    @GetMapping("/autor/formautor")
    public String guardarAutor() {
        return "formautor.html";
    }

    @PostMapping("/registrarautor")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String nombre, @RequestParam(required = false) String titulo) {

        try {
            autorservicio.guardarAutor(nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "formautor.html";
        }

        return "redirect:/autor";
    }

    //Modificar autores
    @GetMapping("/autor/modificar/{id}")
    public String modificarAutor(ModelMap modelo, @PathVariable String id) {

        try {
            Autor autor = autorservicio.consultarPorID(id);
            Libro libro = libroservicio.consultarPorAutor(autor.getNombre());

            modelo.put("autor", autor);
            modelo.put("libro", libro);

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modautor.html";
        }

        return "modautor.html";
    }

    @PostMapping("/modificarautor/{id}")
    public String modificarAutor(ModelMap modelo, @RequestParam String id, @RequestParam String nombre, @RequestParam(required = false) String titulo) {

        try {
            autorservicio.modificarAutor(id, nombre, titulo);
        } catch (ErrorServicio ex) {
            modelo.put("titulo", titulo);
            modelo.put("nombre", nombre);
            modelo.put("error", ex.getMessage());
            return "modautor.html";
        }

        return "redirect:/autor";
    }
    
    //Dar de baja-alta
    
    @GetMapping("/autor/baja/{id}")
    public String darDeBaja(@PathVariable String id){
        
        try {
            autorservicio.darDeBaja(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "redirect:/autor";
    }
    
    @GetMapping("/autor/alta/{id}")
    public String darDeAlta(@PathVariable String id){
        
        try {
            autorservicio.darDeAlta(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "redirect:/autor";
    }

}
