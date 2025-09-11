package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.RolEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UsuarioDTO {

    private String cedula;

    private String nombres;

    private String apellidos;

    private String email;

    private String contrasena;

    private String telefono;

    private LocalDate nacimiento;

    private List<RolEnum> roles;
}
