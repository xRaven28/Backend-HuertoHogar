package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.DTo.LoginRequest;
import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Clave secreta más segura
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "HUERTOHOGAR_CLAVE_SECRETA_SUPER_LARGA_2024_1234567890".getBytes()
    );

    // Tiempo de expiración del token (24 horas)
    private static final long EXPIRATION_TIME = 86400000;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/crear-admin")
    public ResponseEntity<?> crearAdmin() {
        try {
            if (usuarioRepository.findByEmail("admin@huerto.cl").isPresent()) {
                return ResponseEntity.badRequest().body("El admin ya existe");
            }

            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@huerto.cl");
            admin.setRut("00000000-0");
            admin.setDireccion("N/A");
            admin.setTelefono("N/A");
            admin.setRol("ADMIN");
            admin.setBloqueado(false);
            admin.setPassword(encoder.encode("1234"));

            usuarioRepository.save(admin);

            return ResponseEntity.ok("Admin creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear admin: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        try {
            // Validar campos obligatorios
            if (login.getEmail() == null || login.getEmail().trim().isEmpty() ||
                login.getPassword() == null || login.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email y contraseña son obligatorios");
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(login.getEmail().trim().toLowerCase());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar si el usuario está bloqueado
            if (Boolean.TRUE.equals(usuario.getBloqueado())) {
                return ResponseEntity.status(403).body("Usuario bloqueado");
            }

            // Verificar contraseña
            if (!encoder.matches(login.getPassword(), usuario.getPassword())) {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }

            // Generar token JWT
            String token = Jwts.builder()
                    .setSubject(usuario.getEmail())
                    .claim("rol", usuario.getRol())
                    .claim("nombre", usuario.getNombre())
                    .claim("id", usuario.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();

            // Devolver respuesta con información del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", usuario.getId());
            response.put("nombre", usuario.getNombre());
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol());
            response.put("rut", usuario.getRut());
            response.put("telefono", usuario.getTelefono());
            response.put("direccion", usuario.getDireccion());
            response.put("bloqueado", usuario.getBloqueado());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor: " + e.getMessage());
        }
    }
}