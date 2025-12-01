package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
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
        return repo.save(u);
    }

    public Usuario login(String email, String password) {
        Usuario user = repo.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
