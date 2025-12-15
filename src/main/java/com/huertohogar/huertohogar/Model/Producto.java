package com.huertohogar.huertohogar.Model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer precio;

    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String compania;

    @Column(columnDefinition = "TEXT")
    private String img;

    private boolean habilitado = true;

    private boolean oferta = false;

    private Integer descuento = 0;
    
    private int stock;

}
