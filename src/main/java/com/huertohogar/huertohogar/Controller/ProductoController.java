package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.Model.Producto;
import com.huertohogar.huertohogar.Service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // =========================
    // GET /api/productos
    // =========================
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // =========================
    // GET /api/productos/{id}
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // POST /api/productos
    // =========================
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody(required = false) Producto producto) {

        // 🔴 VALIDACIÓN CLAVE (sube cobertura y arregla tests)
        if (producto == null ||
            producto.getName() == null || producto.getName().isBlank() ||
            producto.getPrecio() == null || producto.getPrecio() <= 0) {
            return ResponseEntity.badRequest().body("Datos de producto inválidos");
        }

        try {
            Producto guardado = productoService.guardar(producto);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================
    // PUT /api/productos/{id}
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody(required = false) Producto datos) {

        if (datos == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Producto actualizado = productoService.actualizar(id, datos);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================
    // PUT /api/productos/{id}/oferta/{descuento}
    // =========================
    @PutMapping("/{id}/oferta/{descuento}")
    public ResponseEntity<?> ponerEnOferta(
            @PathVariable Long id,
            @PathVariable int descuento) {

        try {
            Producto producto = productoService.ponerEnOferta(id, descuento);
            return ResponseEntity.ok(producto);
        } catch (IllegalArgumentException e) {
            // 🔴 RAMA IMPORTANTE PARA COBERTURA
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================
    // PUT /api/productos/{id}/quitar-oferta
    // =========================
    @PutMapping("/{id}/quitar-oferta")
    public ResponseEntity<?> quitarOferta(@PathVariable Long id) {
        try {
            Producto producto = productoService.quitarOferta(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================
    // DELETE /api/productos/{id}
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
