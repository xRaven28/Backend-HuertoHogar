package com.huertohogar.huertohogar.Model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CompraTest {

    @Test
    void crearCompra_yUsarGettersSetters() {
        Compra compra = new Compra();

        compra.setCodigo("ABC123");
        compra.setTotal(5000);
        compra.setEstado("ENTREGADO");
        compra.setMetodoPago("TARJETA");

        assertEquals("ABC123", compra.getCodigo());
        assertEquals(5000, compra.getTotal());
        assertEquals("ENTREGADO", compra.getEstado());
        assertEquals("TARJETA", compra.getMetodoPago());
        assertNotNull(compra.getFecha());
        assertNotNull(compra.getItems());
    }

    @Test
    void itemsInicializados() {
        Compra compra = new Compra();
        List<ItemCompra> items = compra.getItems();

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void setFechaManual() {
        Compra compra = new Compra();
        LocalDateTime fecha = LocalDateTime.now().minusDays(1);

        compra.setFecha(fecha);

        assertEquals(fecha, compra.getFecha());
    }
}
