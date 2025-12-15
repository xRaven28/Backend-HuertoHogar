package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Service.UsuarioService;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // GET /api/usuarios
    // =========================
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // =========================
    // GET /api/usuarios/{id}
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // POST /api/usuarios   👈 FIX CLAVE PARA LOS TESTS
    // =========================
    @PostMapping
    public ResponseEntity<?> registrarDirecto(@RequestBody Usuario usuario) {

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email obligatorio");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Password obligatorio");
        }

        if (service.existeEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        Usuario nuevo = service.registrar(usuario);
        return ResponseEntity.ok(nuevo);
    }

    // =========================
    // POST /api/usuarios/registrar (se mantiene)
    // =========================
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        return registrarDirecto(usuario);
    }

    // =========================
    // PUT /api/usuarios/{id}
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario datos) {

        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setNombre(datos.getNombre());
                    u.setRut(datos.getRut());
                    u.setTelefono(datos.getTelefono());
                    u.setDireccion(datos.getDireccion());
                    u.setRol(datos.getRol());
                    return ResponseEntity.ok(usuarioRepository.save(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // POST /api/usuarios/cambiar-password-email
    // =========================
    @PostMapping("/cambiar-password-email")
    public ResponseEntity<?> cambiarPasswordPorEmail(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String actual = body.get("passwordActual");
        String nueva = body.getOrDefault("passwordNueva", body.get("nuevaPassword"));

        if (email == null || nueva == null) {
            return ResponseEntity.badRequest().body("Datos incompletos");
        }

        if (actual == null || actual.isBlank()) {
            return service.cambiarPasswordSinActual(email, nueva);
        }

        return service.cambiarPasswordPorEmail(email, actual, nueva);
    }

    // =========================
    // PUT /api/usuarios/{id}/password
    // =========================
    @PutMapping("/{id}/password")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return service.cambiarPassword(
                id,
                body.get("passwordActual"),
                body.get("passwordNueva")
        );
    }

    // =========================
    // DELETE /api/usuarios/{id}
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado");
    }

    // =========================
    // PUT /api/usuarios/{id}/bloqueo
    // =========================
    @PutMapping("/{id}/bloqueo")
    public ResponseEntity<?> cambiarBloqueo(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {

        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setBloqueado(body.get("bloqueado"));
                    usuarioRepository.save(u);
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
