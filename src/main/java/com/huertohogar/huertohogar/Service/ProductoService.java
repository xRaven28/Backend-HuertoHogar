package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Producto;
import com.huertohogar.huertohogar.Repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // =========================
    // LISTAR
    // =========================
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    // =========================
    // OBTENER POR ID
    // =========================
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // =========================
    // GUARDAR
    // =========================
    public Producto guardar(Producto producto) {

        if (producto.getName() == null || producto.getName().isBlank()) {
            throw new RuntimeException("Nombre obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new RuntimeException("Precio inválido");
        }

        if (producto.getStock() < 0) {
            throw new RuntimeException("Stock inválido");
        }

        producto.setId(null);
        return productoRepository.save(producto);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public Producto actualizar(Long id, Producto datos) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setName(datos.getName());
        producto.setPrecio(datos.getPrecio());
        producto.setCategoria(datos.getCategoria());
        producto.setDescripcion(datos.getDescripcion());
        producto.setCompania(datos.getCompania());
        producto.setImg(datos.getImg());
        producto.setHabilitado(datos.isHabilitado());
        producto.setOferta(datos.isOferta());
        producto.setDescuento(datos.getDescuento());
        producto.setStock(datos.getStock());

        return productoRepository.save(producto);
    }

    // =========================
    // PONER EN OFERTA
    // =========================
    public Producto ponerEnOferta(Long id, int descuento) {

        if (descuento < 5 || descuento > 90) {
            throw new IllegalArgumentException("Descuento fuera de rango");
        }

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setOferta(true);
        producto.setDescuento(descuento);

        return productoRepository.save(producto);
    }

    // =========================
    // QUITAR OFERTA
    // =========================
    public Producto quitarOferta(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setOferta(false);
        producto.setDescuento(0);

        return productoRepository.save(producto);
    }

    // =========================
    // ELIMINAR
    // =========================
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoRepository.delete(producto);
    }
}
