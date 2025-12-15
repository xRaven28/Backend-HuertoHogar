package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // -------------------------------
    // registrar()
    // -------------------------------

    @Test
    void registrar_usuarioNuevo_ok() {
        Usuario u = new Usuario();
        u.setEmail("test@mail.com");
        u.setPassword("1234");

        when(usuarioRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(usuarioRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Usuario guardado = usuarioService.registrar(u);

        assertNotNull(guardado);
        assertEquals("CLIENTE", guardado.getRol());
        assertTrue(encoder.matches("1234", guardado.getPassword()));
    }

    @Test
    void registrar_emailDuplicado_lanzaException() {
        when(usuarioRepository.findByEmail("dup@mail.com"))
                .thenReturn(Optional.of(new Usuario()));

        Usuario u = new Usuario();
        u.setEmail("dup@mail.com");
        u.setPassword("1234");

        assertThrows(RuntimeException.class,
                () -> usuarioService.registrar(u));
    }

    // -------------------------------
    // cambiarPasswordSinActual()
    // -------------------------------

    @Test
    void cambiarPasswordSinActual_ok() {
        Usuario u = new Usuario();
        u.setEmail("user@mail.com");
        u.setPassword(encoder.encode("old"));

        when(usuarioRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(u));

        ResponseEntity<?> response =
                usuarioService.cambiarPasswordSinActual("user@mail.com", "new");

        assertEquals(200, response.getStatusCode().value());
        verify(usuarioRepository).save(any());
    }

    @Test
    void cambiarPasswordSinActual_emailNoExiste() {
        when(usuarioRepository.findByEmail("no@mail.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response =
                usuarioService.cambiarPasswordSinActual("no@mail.com", "new");

        assertEquals(404, response.getStatusCode().value());
    }

    // -------------------------------
    // cambiarPasswordPorEmail()
    // -------------------------------

    @Test
    void cambiarPasswordPorEmail_ok() {
        Usuario u = new Usuario();
        u.setEmail("user@mail.com");
        u.setPassword(encoder.encode("123"));

        when(usuarioRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(u));

        ResponseEntity<?> response =
                usuarioService.cambiarPasswordPorEmail("user@mail.com", "123", "456");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void cambiarPasswordPorEmail_passwordIncorrecta() {
        Usuario u = new Usuario();
        u.setEmail("user@mail.com");
        u.setPassword(encoder.encode("123"));

        when(usuarioRepository.findByEmail("user@mail.com"))
                .thenReturn(Optional.of(u));

        ResponseEntity<?> response =
                usuarioService.cambiarPasswordPorEmail("user@mail.com", "wrong", "456");

        assertEquals(400, response.getStatusCode().value());
    }

    // -------------------------------
    // cambiarPassword(id)
    // -------------------------------

    @Test
    void cambiarPasswordPorId_ok() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setPassword(encoder.encode("old"));

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(u));

        ResponseEntity<?> response =
                usuarioService.cambiarPassword(1L, "old", "new");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void cambiarPasswordPorId_noExiste() {
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response =
                usuarioService.cambiarPassword(99L, "a", "b");

        assertEquals(404, response.getStatusCode().value());
    }

    // -------------------------------
    // existeEmail()
    // -------------------------------

    @Test
    void existeEmail_true() {
        when(usuarioRepository.findByEmail("existe@mail.com"))
                .thenReturn(Optional.of(new Usuario()));

        assertTrue(usuarioService.existeEmail("existe@mail.com"));
    }

    @Test
    void existeEmail_false() {
        when(usuarioRepository.findByEmail("no@mail.com"))
                .thenReturn(Optional.empty());

        assertFalse(usuarioService.existeEmail("no@mail.com"));
    }
}
