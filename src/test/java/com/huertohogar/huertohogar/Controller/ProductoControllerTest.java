package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.Model.Producto;
import com.huertohogar.huertohogar.Service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    // =========================
    // GET /api/productos
    // =========================
    @Test
    void listarProductos_ok() throws Exception {
        when(productoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(productoService).listar();
    }

    // =========================
    // GET /api/productos/{id} OK
    // =========================
    @Test
    void obtenerProducto_ok() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setName("Lechuga");
        p.setPrecio(1000);

        when(productoService.obtenerPorId(1L))
                .thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lechuga"));
    }

    // =========================
    // GET /api/productos/{id} NOT FOUND
    // =========================
    @Test
    void obtenerProducto_noExiste() throws Exception {
        when(productoService.obtenerPorId(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // POST /api/productos OK
    // =========================
    @Test
    void crearProducto_ok() throws Exception {
        Producto producto = new Producto();
        producto.setName("Lechuga");
        producto.setPrecio(1000);
        producto.setStock(5);

        when(productoService.guardar(any()))
                .thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Lechuga",
                                  "precio":1000,
                                  "stock":5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lechuga"));

        verify(productoService).guardar(any());
    }

    // =========================
    // POST sin body
    // =========================
    @Test
    void crearProducto_sinBody() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).guardar(any());
    }

    // =========================
    // POST body vacío
    // =========================
    @Test
    void crearProducto_bodyVacio() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).guardar(any());
    }

    // =========================
    // POST datos inválidos
    // =========================
    @Test
    void crearProducto_datosInvalidos() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"",
                                  "precio":-1,
                                  "stock":-5
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).guardar(any());
    }

    // =========================
    // PUT /api/productos/{id} OK
    // =========================
    @Test
    void actualizarProducto_ok() throws Exception {
        Producto p = new Producto();
        p.setName("Actualizado");

        when(productoService.actualizar(anyLong(), any()))
                .thenReturn(p);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Actualizado",
                                  "precio":1500,
                                  "stock":10
                                }
                                """))
                .andExpect(status().isOk());
    }

    // =========================
    // PUT /api/productos/{id} NOT FOUND
    // =========================
    @Test
    void actualizarProducto_noExiste() throws Exception {
        when(productoService.actualizar(anyLong(), any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // PUT oferta OK
    // =========================
    @Test
    void ponerEnOferta_ok() throws Exception {
        Producto p = new Producto();
        p.setOferta(true);
        p.setDescuento(20);

        when(productoService.ponerEnOferta(anyLong(), anyInt()))
                .thenReturn(p);

        mockMvc.perform(put("/api/productos/1/oferta/20"))
                .andExpect(status().isOk());
    }

    // =========================
    // PUT oferta BAD REQUEST
    // =========================
    @Test
    void ponerEnOferta_descuentoInvalido() throws Exception {
        when(productoService.ponerEnOferta(anyLong(), anyInt()))
                .thenThrow(new IllegalArgumentException("Descuento inválido"));

        mockMvc.perform(put("/api/productos/1/oferta/2"))
                .andExpect(status().isBadRequest());
    }

    // =========================
    // PUT quitar oferta OK
    // =========================
    @Test
    void quitarOferta_ok() throws Exception {
        when(productoService.quitarOferta(anyLong()))
                .thenReturn(new Producto());

        mockMvc.perform(put("/api/productos/1/quitar-oferta"))
                .andExpect(status().isOk());
    }

    // =========================
    // DELETE OK
    // =========================
    @Test
    void eliminarProducto_ok() throws Exception {
        doNothing().when(productoService).eliminar(anyLong());

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    // =========================
    // DELETE NOT FOUND
    // =========================
    @Test
    void eliminarProducto_noExiste() throws Exception {
        doThrow(new RuntimeException())
                .when(productoService).eliminar(anyLong());

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());
    }
}
