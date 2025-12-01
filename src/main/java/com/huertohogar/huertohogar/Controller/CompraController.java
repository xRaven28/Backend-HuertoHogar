package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.Model.Compra;
import com.huertohogar.huertohogar.Service.CompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:5173")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // ============================
    // REGISTRAR COMPRA (CHECKOUT)
    // ============================
    @PostMapping
    public ResponseEntity<?> registrarCompra(@RequestBody Compra compra) {
        try {
            System.out.println("=== COMPRA RECIBIDA ===");
            System.out.println(compra);

            Compra guardada = compraService.registrarCompra(compra);

            return ResponseEntity.ok(guardada);
        } catch (Exception e) {
            e.printStackTrace(); // <-- ESTO MUESTRA EL ERROR REAL
            return ResponseEntity.badRequest().body(
                    Map.of("error", "No se pudo registrar la compra: " + e.getMessage()));
        }
    }

    // ============================
    // LISTAR TODAS LAS COMPRAS
    // ============================
    @GetMapping
    public ResponseEntity<List<Compra>> listarTodas() {
        List<Compra> lista = compraService.listarTodas();
        lista.forEach(c -> c.getItems().forEach(i -> i.setCompra(null)));
        return ResponseEntity.ok(lista);
    }

    // ============================
    // LISTAR COMPRAS POR USUARIO
    // ============================
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Compra>> comprasPorUsuario(@PathVariable Long id) {
        List<Compra> compras = compraService.comprasDeUsuario(id);
        compras.forEach(c -> c.getItems().forEach(i -> i.setCompra(null)));
        return ResponseEntity.ok(compras);
    }

    // ============================
    // ACTUALIZAR ESTADO
    // ============================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String estado = body.get("estado");

        if (estado == null || estado.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "El campo 'estado' es obligatorio"));
        }

        try {
            Compra actualizada = compraService.actualizarEstado(id, estado);
            actualizada.getItems().forEach(i -> i.setCompra(null));

            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "No se pudo cambiar el estado: " + e.getMessage()));
        }
    }
}
