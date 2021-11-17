package com.libreria.ejercicio1.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Lautaro Yanzon
 */
@Controller
@RequestMapping("/")
public class PortalControlador {

    @GetMapping("/")
    public String index() {
        
        return "index.html";
    }
    
}
