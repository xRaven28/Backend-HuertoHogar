package com.huertohogar.huertohogar.Controller;

import com.huertohogar.huertohogar.Model.Compra;
import com.huertohogar.huertohogar.Service.CompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    // =========================
    // GET /api/compras
    // =========================
    @Test
    void listarCompras_ok() throws Exception {
        when(compraService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(compraService, times(1)).listarTodas();
    }

    // =========================
    // PUT /api/compras/{id}/estado OK
    // =========================
    @Test
    void cambiarEstadoCompra_ok() throws Exception {
        Compra compra = new Compra();
        compra.setEstado("ENTREGADO");

        when(compraService.actualizarEstado(1L, "ENTREGADO"))
                .thenReturn(compra);

        mockMvc.perform(
                put("/api/compras/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"ENTREGADO\"}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));

        verify(compraService).actualizarEstado(1L, "ENTREGADO");
    }

    // =========================
    // PUT /api/compras/{id}/estado → id NO existe
    // =========================
    @Test
    void cambiarEstadoCompra_idNoExiste() throws Exception {
        when(compraService.actualizarEstado(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Compra no encontrada"));

        mockMvc.perform(
                put("/api/compras/9999/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"ENTREGADO\"}")
        )
                .andExpect(status().isBadRequest());

        verify(compraService).actualizarEstado(9999L, "ENTREGADO");
    }

    // =========================
    // PUT sin body (400)
    // =========================
    @Test
    void cambiarEstadoCompra_sinBody() throws Exception {
        mockMvc.perform(
                put("/api/compras/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());

        verify(compraService, never()).actualizarEstado(anyLong(), anyString());
    }

    // =========================
    // PUT body vacío
    // =========================
    @Test
    void cambiarEstadoCompra_bodyVacio() throws Exception {
        mockMvc.perform(
                put("/api/compras/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
                .andExpect(status().isBadRequest());
    }

    // =========================
    // PUT estado null
    // =========================
    @Test
    void cambiarEstadoCompra_estadoNull() throws Exception {
        mockMvc.perform(
                put("/api/compras/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":null}")
        )
                .andExpect(status().isBadRequest());
    }
}
