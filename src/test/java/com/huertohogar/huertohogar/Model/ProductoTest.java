package com.huertohogar.huertohogar.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    @Test
    void crearProducto_yUsarGettersSetters() {
        Producto p = new Producto();

        p.setId(1L);
        p.setName("Tomate");
        p.setPrecio(1000);
        p.setCategoria("Verduras");
        p.setDescripcion("Tomate fresco");
        p.setCompania("HuertoHogar");
        p.setImg("img.jpg");
        p.setHabilitado(true);
        p.setOferta(true);
        p.setDescuento(20);
        p.setStock(10);

        assertEquals(1L, p.getId());
        assertEquals("Tomate", p.getName());
        assertEquals(1000, p.getPrecio());
        assertEquals("Verduras", p.getCategoria());
        assertEquals("Tomate fresco", p.getDescripcion());
        assertEquals("HuertoHogar", p.getCompania());
        assertEquals("img.jpg", p.getImg());
        assertTrue(p.isHabilitado());
        assertTrue(p.isOferta());
        assertEquals(20, p.getDescuento());
        assertEquals(10, p.getStock());
    }

    @Test
    void valoresPorDefecto() {
        Producto p = new Producto();

        assertTrue(p.isHabilitado());
        assertFalse(p.isOferta());
        assertEquals(0, p.getDescuento());
    }
}
