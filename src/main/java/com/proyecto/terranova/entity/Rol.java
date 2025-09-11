package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.RolEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Enumerated(EnumType.STRING)
    private RolEnum nombreRol;

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;
}
