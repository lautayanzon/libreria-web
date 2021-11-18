package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Cliente;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.entidades.Prestamo;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.ClienteServicio;
import com.libreria.ejercicio1.servicios.LibroServicio;
import com.libreria.ejercicio1.servicios.PrestamoServicio;
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
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoservicio;

    @Autowired
    private LibroServicio libroservicio;

    @Autowired
    private ClienteServicio clienteservicio;

    @GetMapping("/prestamo")
    public String tabla(ModelMap modelo) {

        List<Prestamo> listaPrestamos = prestamoservicio.consultarActivos();

        modelo.put("lista", listaPrestamos);
        modelo.put("tablahead", "prestamos");
        modelo.put("pagtitulo", "Prestamos");
        modelo.put("tr", "prestamos");
        modelo.put("urlguardar", "/prestamo/formprestamo");
        modelo.put("btguardar", "un prestamo");

        return "tabla.html";
    }

    @GetMapping("/prestamo/formprestamo")
    public String formulario(ModelMap modelo) {

        List<Libro> listaLibros = libroservicio.consultarActivos();

        List<Cliente> listaClientes = clienteservicio.consultarActivos();

        modelo.put("listalibros", listaLibros);
        modelo.put("listaclientes", listaClientes);
        modelo.put("pagtitulo", "Formulario prestamos");
        modelo.put("formhead", "un prestamo");
        modelo.put("urlvolver", "/prestamo");
        modelo.put("form", "prestamo");
//        modelo.put("urlaction", "/prestar/__${cliente.id}__&__${libro.id}__");
        modelo.put("urlaction", "/prestar");

        return "formulario.html";
    }

    @PostMapping("/prestar")
    public String prestar(ModelMap modelo, @RequestParam(required = false) String idCliente, @RequestParam(required = false) String idLibro) {

        try {
            prestamoservicio.prestar(idCliente, idLibro);
        } catch (ErrorServicio ex) {
            modelo.put("librotitulo", "libro error");
            modelo.put("clientenombre", "cliente error");
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Formulario prestamos");
            modelo.put("formhead", "un prestamos");
            modelo.put("urlvolver", "/prestamo");
            modelo.put("form", "prestamo");
            modelo.put("urlaction", "/prestar");
            return "formulario.html";
        }
        return "redirect:/prestamo";
    }

}
