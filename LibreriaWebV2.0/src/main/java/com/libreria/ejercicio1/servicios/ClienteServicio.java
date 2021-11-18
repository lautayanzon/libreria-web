package com.libreria.ejercicio1.servicios;

import com.libreria.ejercicio1.entidades.Cliente;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.repositorios.ClienteRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lautaro Yanzon
 */
@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienterepositorio;

    @Transactional
    public void guardarCliente(String nombre, String apellido, Long documento, String telefono) throws ErrorServicio {

        validar(nombre, apellido, documento, telefono);

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDocumento(documento);
        cliente.setTelefono(telefono);

        cliente.setAlta(Boolean.TRUE);

        clienterepositorio.save(cliente);

    }

    @Transactional
    public void modificarCliente(String id, String nombre, String apellido, Long documento, String telefono) throws ErrorServicio {

        validar(nombre, apellido, documento, telefono);

        Cliente cliente = clienterepositorio.buscarPorId(id);

        if (cliente != null) {

            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDocumento(Long.MIN_VALUE);
            cliente.setTelefono(telefono);

            cliente.setAlta(Boolean.TRUE);

            clienterepositorio.save(cliente);

        } else {
            throw new ErrorServicio("No se encontro el cliente en la base de datos.");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> consultarActivos(){
        return clienterepositorio.buscarActivos();
    }

    public void validar(String nombre, String apellido, Long documento, String telefono) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }

        if (documento == null) {
            throw new ErrorServicio("El documento no puede ser nulo");
        }

        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ErrorServicio("El telefono no puede ser nulo");
        }
    }

}
