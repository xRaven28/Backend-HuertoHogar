package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Compra;
import com.huertohogar.huertohogar.Model.ItemCompra;
import com.huertohogar.huertohogar.Model.Usuario;
import com.huertohogar.huertohogar.Repository.CompraRepository;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
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
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CompraService compraService;

    // =========================
    // listarTodas()
    // =========================
    @Test
    void listarTodas_noFalla() {
        when(compraRepository.findAll()).thenReturn(List.of());

        assertDoesNotThrow(() -> compraService.listarTodas());
        verify(compraRepository).findAll();
    }

    // =========================
    // comprasDeUsuario()
    // =========================
    @Test
    void comprasDeUsuario_noFalla() {
        when(compraRepository.findByUsuarioId(1L))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> compraService.comprasDeUsuario(1L));
        verify(compraRepository).findByUsuarioId(1L);
    }

    // =========================
    // actualizarEstado OK
    // =========================
    @Test
    void actualizarEstado_ok() {
        Compra compra = new Compra();
        compra.setEstado("PREPARANDO");

        when(compraRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        when(compraRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Compra actualizada = compraService.actualizarEstado(1L, "ENTREGADO");

        assertEquals("ENTREGADO", actualizada.getEstado());
        verify(compraRepository).save(compra);
    }

    // =========================
    // actualizarEstado NO existe
    // =========================
    @Test
    void actualizarEstado_compraNoExiste_lanzaError() {
        when(compraRepository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> compraService.actualizarEstado(999L, "ENTREGADO"));

        assertEquals("Compra no encontrada", ex.getMessage());
    }

    // =========================
    // registrarCompra usuario null
    // =========================
    @Test
    void registrarCompra_usuarioNull_lanzaError() {
        Compra compra = new Compra();

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> compraService.registrarCompra(compra));

        assertEquals("La compra debe incluir un usuario válido", ex.getMessage());
    }

    // =========================
    // registrarCompra usuario NO existe
    // =========================
    @Test
    void registrarCompra_usuarioNoExiste_lanzaError() {
        Usuario usuario = new Usuario();
        usuario.setId(99L);

        Compra compra = new Compra();
        compra.setUsuario(usuario);

        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> compraService.registrarCompra(compra));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // =========================
    // registrarCompra OK
    // =========================
    @Test
    void registrarCompra_ok() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        ItemCompra item = new ItemCompra();
        item.setCantidad(2);
        item.setPrecio(1000);

        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setItems(List.of(item));
        compra.setTotal(2000);
        compra.setMetodoPago("EFECTIVO");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(compraRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Compra guardada = compraService.registrarCompra(compra);

        assertNotNull(guardada);
        assertEquals("PREPARANDO", guardada.getEstado());
        assertEquals(usuario, guardada.getUsuario());
        assertEquals(1, guardada.getItems().size());
        assertEquals(guardada, guardada.getItems().get(0).getCompra());
    }
}
