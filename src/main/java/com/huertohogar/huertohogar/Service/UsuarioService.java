package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Usuario registrar(Usuario u) {

        if (repo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya existe");
        }

        u.setPassword(passwordEncoder.encode(u.getPassword()));

        if (u.getRol() == null || u.getRol().isEmpty()) {
            u.setRol("CLIENTE");
        }

        return repo.save(u);
    }

    public ResponseEntity<?> cambiarPasswordSinActual(String email, String nueva) {
        Usuario usuario = repo.findByEmail(email).orElse(null);

        if (usuario == null) return ResponseEntity.status(404).body("Correo no registrado");

        usuario.setPassword(passwordEncoder.encode(nueva));
        repo.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    public ResponseEntity<?> cambiarPasswordPorEmail(String email, String actual, String nueva) {

        Usuario usuario = repo.findByEmail(email).orElse(null);
        if (usuario == null) return ResponseEntity.status(404).body("Correo no registrado");

        if (!passwordEncoder.matches(actual, usuario.getPassword())) {
            return ResponseEntity.status(400).body("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nueva));
        repo.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    public ResponseEntity<?> cambiarPassword(Long id, String actual, String nueva) {

        Usuario usuario = repo.findById(id).orElse(null);
        if (usuario == null) return ResponseEntity.notFound().build();

        if (!passwordEncoder.matches(actual, usuario.getPassword())) {
            return ResponseEntity.status(400).body("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nueva));
        repo.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    public boolean existeEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }
}
