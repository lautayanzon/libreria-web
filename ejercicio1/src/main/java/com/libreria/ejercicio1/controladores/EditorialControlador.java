package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Editorial;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/editorial")
    public String editorial(ModelMap model) {

        List<Editorial> listaEditorial = ediservicio.consultarTodos();

        model.put("editoriales", listaEditorial);

        return "editorial.html";
    }

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

}
