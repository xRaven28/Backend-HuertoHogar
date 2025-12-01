package com.huertohogar.huertohogar.Service;

import com.huertohogar.huertohogar.Model.Compra;
import com.huertohogar.huertohogar.Model.ItemCompra;
import com.huertohogar.huertohogar.Repository.CompraRepository;
import com.huertohogar.huertohogar.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepo;
    private final UsuarioRepository usuarioRepo;

    public CompraService(CompraRepository compraRepo, UsuarioRepository usuarioRepo) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public Compra registrarCompra(Compra compra) {
        System.out.println("\n=== COMPRA RECIBIDA ===");

        System.out.println("Usuario ID recibido: " +
                (compra.getUsuario() != null ? compra.getUsuario().getId() : "NULL"));

        System.out.println("Items recibidos: " +
                (compra.getItems() != null ? compra.getItems().size() : 0));

        System.out.println("Total recibido: " + compra.getTotal());
        System.out.println("Método pago: " + compra.getMetodoPago());
        System.out.println("Código: " + compra.getCodigo());
        System.out.println("=========================\n");

        if (compra.getUsuario() == null || compra.getUsuario().getId() == null) {
            throw new RuntimeException("La compra debe incluir un usuario válido");
        }

        Long usuarioId = compra.getUsuario().getId();

        // Estado por defecto
        if (compra.getEstado() == null || compra.getEstado().isBlank()) {
            compra.setEstado("PREPARANDO");
        }

        return usuarioRepo.findById(usuarioId)
                .map(usuario -> {
                    compra.setUsuario(usuario);
                    for (ItemCompra item : compra.getItems()) {
                        item.setCompra(compra);
                    }
                    Compra guardada = compraRepo.save(compra);
                    usuario.getCompras().add(guardada);

                    return guardada;
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    public List<Compra> comprasDeUsuario(Long idUsuario) {
        return compraRepo.findByUsuarioId(idUsuario);
    }

    public List<Compra> listarTodas() {
        return compraRepo.findAll();
    }

    public Compra actualizarEstado(Long idCompra, String estado) {
        return compraRepo.findById(idCompra)
                .map(c -> {
                    c.setEstado(estado);
                    return compraRepo.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }
}
