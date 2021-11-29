package com.libreria.ejercicio1.servicios;

import com.libreria.ejercicio1.entidades.Usuario;
import com.libreria.ejercicio1.enums.Rol;
import com.libreria.ejercicio1.excepciones.ErrorServicio;
import com.libreria.ejercicio1.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Lautaro Yanzon
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuariorepositorio;

    @Transactional
    public void registrarUsuario(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        validar(nombre, apellido, mail, clave, clave2);

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setMail(mail);

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        u.setClave(encriptada);

        u.setAlta(new Date());

        u.setRol(Rol.USUARIO);

        usuariorepositorio.save(u);
    }

    @Transactional
    public void modificarUsuario(String id, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        validar(nombre, apellido, mail, clave, clave2);

        Usuario u = usuariorepositorio.buscarPorId(id);

        if (u != null) {
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setMail(mail);

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            u.setClave(encriptada);

            usuariorepositorio.save(u);
        } else {
            throw new ErrorServicio("No se encontro al usuario.");
        }

    }

    @Transactional(readOnly = true)
    public Usuario consultarPorId(String id) {
        return usuariorepositorio.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Usuario consultarPorMail(String mail) {
        return usuariorepositorio.buscarPorMail(mail);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> consultarActivos() {
        return usuariorepositorio.buscarActivos();
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {

        Usuario u = usuariorepositorio.buscarPorId(id);
        if (u != null) {

            u.setBaja(new Date());
            usuariorepositorio.save(u);
        } else {
            throw new ErrorServicio("No se encontró el usuario.");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {

        Usuario u = usuariorepositorio.buscarPorId(id);
        if (u != null) {

            u.setBaja(null);
            usuariorepositorio.save(u);
        } else {
            throw new ErrorServicio("No se encontró el usuario.");
        }
    }

    @Transactional
    public void cambiarRol(String id) throws ErrorServicio {

        Usuario u = usuariorepositorio.buscarPorId(id);

        if (u != null) {

            if (u.getRol().equals(Rol.USUARIO)) {

                u.setRol(Rol.ADMIN);

            } else if (u.getRol().equals(Rol.ADMIN)) {
                u.setRol(Rol.USUARIO);
            }
        }
    }

    public void validar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo.");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo.");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServicio("La clave no puede ser nula.");
        }
        if (clave.length() <= 6) {
            throw new ErrorServicio("La clave debe ser mayor a 6 caracteres.");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no son iguales.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = usuariorepositorio.buscarPorMail(mail);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            //Creo una lista de permisos! 
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            
            permisos.add(p1);

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario); // llave + valor

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);

            return user;
        } else {
            return null;
        }
    }

}
