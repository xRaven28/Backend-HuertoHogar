package com.huertohogar.huertohogar.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import com.huertohogar.huertohogar.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;
    

    // =========================
    // GET /api/usuarios
    // =========================
    @Test
    void listarUsuarios_ok() throws Exception {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    // =========================
    // GET /api/usuarios/{id} OK
    // =========================
    @Test
    void obtenerUsuario_ok() throws Exception {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setEmail("test@mail.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    // =========================
    // GET /api/usuarios/{id} NOT FOUND
    // =========================
    @Test
    void obtenerUsuario_noExiste() throws Exception {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // POST /api/usuarios/registrar OK
    // =========================
    @Test
    void registrarUsuario_ok() throws Exception {
        Usuario u = new Usuario();
        u.setEmail("test@mail.com");
        u.setPassword("1234");

        when(usuarioService.existeEmail("test@mail.com")).thenReturn(false);
        when(usuarioService.registrar(any(Usuario.class))).thenReturn(u);

        mockMvc.perform(post("/api/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isOk());
    }

    // =========================
    // POST /api/usuarios/registrar EMAIL DUPLICADO
    // =========================
    @Test
    void registrarUsuario_emailDuplicado() throws Exception {
        Usuario u = new Usuario();
        u.setEmail("test@mail.com");
        u.setPassword("1234");

        when(usuarioService.existeEmail("test@mail.com")).thenReturn(true);

        mockMvc.perform(post("/api/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isBadRequest());
    }

    // =========================
    // PUT /api/usuarios/{id} OK
    // =========================
    @Test
    void actualizarUsuario_ok() throws Exception {
        Usuario existente = new Usuario();
        existente.setId(1L);

        Usuario datos = new Usuario();
        datos.setNombre("Nuevo Nombre");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk());
    }

    // =========================
    // PUT /api/usuarios/{id} NOT FOUND
    // =========================
    @Test
    void actualizarUsuario_noExiste() throws Exception {
        Usuario datos = new Usuario();
        datos.setNombre("Test");

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isNotFound());
    }

    // =========================
    // DELETE /api/usuarios/{id} OK
    // =========================
    @Test
    void eliminarUsuario_ok() throws Exception {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    // =========================
    // DELETE /api/usuarios/{id} NOT FOUND
    // =========================
    @Test
    void eliminarUsuario_noExiste() throws Exception {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }
// =========================
// POST /api/usuarios/registrar BAD REQUEST - DATOS VACÍOS
// =========================
@Test
void registrarUsuario_datosInvalidos() throws Exception {
    Usuario u = new Usuario(); 

    mockMvc.perform(post("/api/usuarios/registrar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(u)))
            .andExpect(status().isBadRequest());
}
// =========================
// GET /api/usuarios CON DATOS
// =========================
@Test
void listarUsuarios_conDatos() throws Exception {
    Usuario u = new Usuario();
    u.setId(1L);
    u.setEmail("user@test.com");

    when(usuarioRepository.findAll()).thenReturn(List.of(u));

    mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("user@test.com"));
}
// =========================
// PUT /api/usuarios/{id} BODY VACÍO
// =========================
@Test
void actualizarUsuario_bodyVacio() throws Exception {
    mockMvc.perform(put("/api/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isBadRequest());
}
// =========================
// DELETE /api/usuarios/{id} VERIFICA DELETE
// =========================
@Test
void eliminarUsuario_verificaDelete() throws Exception {
    when(usuarioRepository.existsById(5L)).thenReturn(true);

    mockMvc.perform(delete("/api/usuarios/5"))
            .andExpect(status().isOk());

    org.mockito.Mockito.verify(usuarioRepository)
            .deleteById(5L);
}

 

}
