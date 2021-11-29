package com.libreria.ejercicio1.controladores;

import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.servicios.UsuarioServicio;
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
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioservicio;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio() {

        return "inicio.html";
    }

    @GetMapping("/login")
    public String login() {

        return "login.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {

        modelo.put("pagtitulo", "Registrar un usuario");
        modelo.put("formhead", "un usuario");
        modelo.put("urlaction", "/registrar");
        modelo.put("urlvolver", "/");

        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave, @RequestParam(required = false) String clave2) {

        try {
            usuarioservicio.registrarUsuario(nombre, apellido, mail, clave, clave2);
        } catch (ErrorServicio ex) {
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("error", ex.getMessage());
            modelo.put("pagtitulo", "Registrar un usuario");
            modelo.put("formhead", "un usuario");
            modelo.put("urlaction", "/registrar");
            modelo.put("urlvolver", "/");

            return "registro.html";
        }
        return "redirect:/";
    }

}
