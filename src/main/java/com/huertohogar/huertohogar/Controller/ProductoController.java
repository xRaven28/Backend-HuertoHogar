package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.Model.Producto;
import com.huertohogar.huertohogar.Repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto crear(@RequestBody Producto p) {
        p.setId(null);
        return productoRepository.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto datos) {

        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto p = productoOpt.get();

            p.setName(datos.getName());
            p.setPrecio(datos.getPrecio());
            p.setCategoria(datos.getCategoria());
            p.setDescripcion(datos.getDescripcion());
            p.setCompania(datos.getCompania());
            p.setImg(datos.getImg());
            p.setHabilitado(datos.isHabilitado());
            p.setOferta(datos.isOferta());
            p.setDescuento(datos.getDescuento());

            Producto productoActualizado = productoRepository.save(p);
            return ResponseEntity.ok(productoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/oferta/{descuento}")
    public ResponseEntity<?> ponerEnOferta(
            @PathVariable Long id,
            @PathVariable int descuento) {

        if (descuento < 5 || descuento > 90) {
            return ResponseEntity.badRequest().body("El descuento debe ser entre 5% y 90%");
        }

        return productoRepository.findById(id).map(p -> {
            p.setOferta(true);
            p.setDescuento(descuento);
            productoRepository.save(p);
            return ResponseEntity.ok(p);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/quitar-oferta")
    public ResponseEntity<?> quitarOferta(@PathVariable Long id) {
        return productoRepository.findById(id).map(p -> {
            p.setOferta(false);
            p.setDescuento(0);
            productoRepository.save(p);
            return ResponseEntity.ok(p);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            productoRepository.delete(productoOpt.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}