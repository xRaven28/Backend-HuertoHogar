package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Producto;
import com.huertohogar.huertohogar.Repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    // =========================
    // Guardar Producto OK
    // =========================
    @Test
    void guardar_ok() {
        Producto p = new Producto();
        p.setName("Tomate");
        p.setPrecio(1200);
        p.setStock(10);

        when(productoRepository.save(any()))
                .thenAnswer(invocation -> {
                    Producto prod = invocation.getArgument(0);
                    prod.setId(1L);
                    return prod;
                });

        Producto guardado = productoService.guardar(p);

        assertNotNull(guardado.getId());
        assertEquals("Tomate", guardado.getName());
        verify(productoRepository).save(any());
    }

    // =========================
    // Guardar Producto con nombre null
    // =========================
    @Test
    void guardar_nombreNull() {
        Producto p = new Producto();
        p.setPrecio(1000);
        p.setStock(5);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.guardar(p));

        assertEquals("Nombre obligatorio", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    // =========================
    // Guardar Producto con precio inválido
    // =========================
    @Test
    void guardar_precioInvalido() {
        Producto p = new Producto();
        p.setName("Lechuga");
        p.setPrecio(0);
        p.setStock(5);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.guardar(p));

        assertEquals("Precio inválido", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    // =========================
    // Guardar Producto con stock negativo
    // =========================
    @Test
    void guardar_stockNegativo() {
        Producto p = new Producto();
        p.setName("Papa");
        p.setPrecio(800);
        p.setStock(-1);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.guardar(p));

        assertEquals("Stock inválido", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    // =========================
    // Listar Productos
    // =========================
    @Test
    void listar_ok() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> productos = productoService.listar();

        assertNotNull(productos);
        verify(productoRepository).findAll();
    }

    // =========================
    // Actualizar Producto OK
    // =========================
    @Test
    void actualizar_ok() {
        Producto existente = new Producto();
        existente.setId(1L);
        existente.setName("Lechuga");
        existente.setPrecio(1000);
        existente.setStock(10);

        Producto datos = new Producto();
        datos.setName("Lechuga Orgánica");
        datos.setPrecio(1200);
        datos.setStock(15);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any())).thenReturn(existente);

        Producto actualizado = productoService.actualizar(1L, datos);

        assertEquals("Lechuga Orgánica", actualizado.getName());
        assertEquals(1200, actualizado.getPrecio());
        verify(productoRepository).save(any());
    }

    // =========================
    // Actualizar Producto NO encontrado
    // =========================
    @Test
    void actualizar_productoNoEncontrado() {
        Producto datos = new Producto();
        datos.setName("Pepino");
        datos.setPrecio(700);
        datos.setStock(20);

        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.actualizar(99L, datos));

        assertEquals("Producto no encontrado", ex.getMessage());
    }

    // =========================
    // Eliminar Producto OK
    // =========================
    @Test
    void eliminarProducto_ok() {
        Producto existente = new Producto();
        existente.setId(1L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        doNothing().when(productoRepository).delete(existente);

        productoService.eliminar(1L);

        verify(productoRepository).delete(existente);
    }

    // =========================
    // Eliminar Producto NO encontrado
    // =========================
    @Test
    void eliminarProducto_noEncontrado() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoService.eliminar(99L));

        assertEquals("Producto no encontrado", ex.getMessage());
    }
}
