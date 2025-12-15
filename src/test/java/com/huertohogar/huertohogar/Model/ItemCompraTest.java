package com.huertohogar.huertohogar.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemCompraTest {

    @Test
    void crearItemCompra_yUsarGettersSetters() {
        ItemCompra item = new ItemCompra();
        Compra compra = new Compra();

        item.setProductoId(10L);
        item.setNombre("Lechuga");
        item.setCantidad(2);
        item.setPrecio(1500);
        item.setCompra(compra);

        assertEquals(10L, item.getProductoId());
        assertEquals("Lechuga", item.getNombre());
        assertEquals(2, item.getCantidad());
        assertEquals(1500, item.getPrecio());
        assertEquals(compra, item.getCompra());
    }
}
