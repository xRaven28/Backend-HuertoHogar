package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    // =========================
    // LISTAR USUARIOS
    // =========================
    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    // =========================
    // OBTENER POR ID
    // =========================
    public Optional<Usuario> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    // =========================
    // REGISTRAR
    // =========================
    public Usuario registrar(Usuario u) {

        if (u.getEmail() == null || u.getPassword() == null) {
            throw new RuntimeException("Email y contraseña obligatorios");
        }

        if (repo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya existe");
        }

        u.setPassword(passwordEncoder.encode(u.getPassword()));

        if (u.getRol() == null || u.getRol().isEmpty()) {
            u.setRol("CLIENTE");
        }

        return repo.save(u);
    }

    // =========================
    // ACTUALIZAR USUARIO
    // =========================
    public Optional<Usuario> actualizar(Long id, Usuario datos) {
        return repo.findById(id).map(u -> {
            u.setNombre(datos.getNombre());
            u.setRut(datos.getRut());
            u.setTelefono(datos.getTelefono());
            u.setDireccion(datos.getDireccion());
            u.setRol(datos.getRol());
            return repo.save(u);
        });
    }

    // =========================
    // ELIMINAR USUARIO
    // =========================
    public boolean eliminar(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    // =========================
    // BLOQUEAR / DESBLOQUEAR
    // =========================
    public Optional<Usuario> cambiarBloqueo(Long id, boolean bloqueado) {
        return repo.findById(id).map(u -> {
            u.setBloqueado(bloqueado);
            return repo.save(u);
        });
    }

    // =========================
    // CAMBIOS DE PASSWORD
    // =========================
    public ResponseEntity<?> cambiarPasswordSinActual(String email, String nueva) {

        Usuario usuario = repo.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Correo no registrado");
        }

        usuario.setPassword(passwordEncoder.encode(nueva));
        repo.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    public ResponseEntity<?> cambiarPasswordPorEmail(String email, String actual, String nueva) {

        Usuario usuario = repo.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Correo no registrado");
        }

        if (!passwordEncoder.matches(actual, usuario.getPassword())) {
            return ResponseEntity.status(400).body("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nueva));
        repo.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    public ResponseEntity<?> cambiarPassword(Long id, String actual, String nueva) {

        Usuario usuario = repo.findById(id).orElse(null);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        if (!passwordEncoder.matches(actual, usuario.getPassword())) {
            return ResponseEntity.status(400).body("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nueva));
        repo.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    // =========================
    // VALIDACIONES
    // =========================
    public boolean existeEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }
}
