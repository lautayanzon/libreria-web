package com.libreria.ejercicio1;

import com.libreria.ejercicio1.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Ejercicio1Application {

    @Autowired
    private UsuarioServicio usuarioservicio;

    public static void main(String[] args) {
        SpringApplication.run(Ejercicio1Application.class, args);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioservicio)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

}
