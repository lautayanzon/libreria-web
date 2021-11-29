package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.entidades.Cliente;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.ClienteServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteservicio;

    @GetMapping("/tabla")
    public String tabla(ModelMap modelo) {

        List<Cliente> listaClientes = clienteservicio.consultarActivos();

        modelo.put("lista", listaClientes);
        modelo.put("tablahead", "clientes");
        modelo.put("pagtitulo", "Clientes");
        modelo.put("tr", "clientes");
        modelo.put("urlguardar", "/cliente/formulario");
        modelo.put("btguardar", "un cliente");

        return "tabla.html";
    }

    @GetMapping("/formulario")
    public String formulario(ModelMap modelo) {

        modelo.put("pagtitulo", "Formulario clientes");
        modelo.put("formhead", "un cliente");
        modelo.put("urlvolver", "/cliente");
        modelo.put("form", "cliente");
        modelo.put("urlaction", "/cliente/registrarcliente");

        return "formulario.html";
    }

    @PostMapping("/registrarcliente")
    public String guardarCliente(ModelMap modelo, @RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido, @RequestParam(required = false) Long documento, @RequestParam(required = false) String telefono) {

        try {
            clienteservicio.guardarCliente(nombre, apellido, documento, telefono);
        } catch (ErrorServicio ex) {
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("documento", documento);
            modelo.put("telefono", telefono);
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Formulario clientes");
            modelo.put("formhead", "un cliente");
            modelo.put("urlvolver", "/cliente");
            modelo.put("form", "cliente");
            modelo.put("urlaction", "/registrarcliente");

            return "formulario.html";
        }

        return "redirect:/cliente/tabla";
    }

}
