package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Cliente;
import com.libreria.ejercicio1.entidades.Libro;
import com.libreria.ejercicio1.entidades.Prestamo;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.ClienteServicio;
import com.libreria.ejercicio1.servicios.LibroServicio;
import com.libreria.ejercicio1.servicios.PrestamoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
@RequestMapping("/prestamo")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoservicio;

    @Autowired
    private LibroServicio libroservicio;

    @Autowired
    private ClienteServicio clienteservicio;

    @GetMapping("/tabla")
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

    @GetMapping("/formprestamo")
    public String formulario(ModelMap modelo) {

        List<Libro> listaLibros = libroservicio.consultarActivos();

        List<Cliente> listaClientes = clienteservicio.consultarActivos();

        modelo.put("listalibros", listaLibros);
        modelo.put("listaclientes", listaClientes);
        modelo.put("pagtitulo", "Formulario prestamos");
        modelo.put("formhead", "un prestamo");
        modelo.put("urlvolver", "/prestamo/tabla");
        modelo.put("form", "prestamo");
        modelo.put("urlaction", "/prestamo/prestar");

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
            modelo.put("urlvolver", "/prestamo/tabla");
            modelo.put("form", "prestamo");
            modelo.put("urlaction", "/prestamo/prestar");
            return "formulario.html";
        }
        return "redirect:/prestamo/tabla";
    }

}
