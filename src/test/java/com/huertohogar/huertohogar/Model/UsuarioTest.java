package com.huertohogar.huertohogar.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void crearUsuario_yUsarGettersSetters() {
        Usuario usuario = new Usuario();

        usuario.setNombre("Juan Perez");
        usuario.setEmail("juan@mail.com");
        usuario.setPassword("1234");
        usuario.setRol("CLIENTE");
        usuario.setRut("11.111.111-1");
        usuario.setTelefono("987654321");
        usuario.setDireccion("Av Siempre Viva 123");
        usuario.setBloqueado(false);

        assertEquals("Juan Perez", usuario.getNombre());
        assertEquals("juan@mail.com", usuario.getEmail());
        assertEquals("1234", usuario.getPassword());
        assertEquals("CLIENTE", usuario.getRol());
        assertEquals("11.111.111-1", usuario.getRut());
        assertEquals("987654321", usuario.getTelefono());
        assertEquals("Av Siempre Viva 123", usuario.getDireccion());
        assertFalse(usuario.getBloqueado());
    }

    @Test
    void usuario_inicialmente_noBloqueado() {
        Usuario usuario = new Usuario();

        // ✔ por defecto debe ser false, no null
        assertFalse(usuario.getBloqueado());
    }

    @Test
    void usuario_puedeSerBloqueado() {
        Usuario usuario = new Usuario();

        usuario.setBloqueado(true);

        assertTrue(usuario.getBloqueado());
    }
}
