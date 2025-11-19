package com.proyecto.terranova.service;

import java.io.IOException;
import java.util.List;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.dto.UsuarioDTO;
import com.proyecto.terranova.entity.Usuario;

public interface UsuarioService {
    boolean save(UsuarioDTO dto);
    void update(Usuario usuario);
    Usuario findById(String id);
    Usuario findByEmail(String email);
    List<UsuarioDTO> findAll();
    boolean delete(String id);
    boolean existsById(String id);
    long count();
    void procesarOAuthPostLogin(String registrationId, String providerId, String email, String name);
    void volverVendedor(String cedula);
    List<RolEnum> obtenerNombresRoles(Usuario usuario);
    void generarTokenYEnviarCorreoRecuperarContrasena(String email) throws IOException;
    String validarTokenYActualizarContrasena(String token, String nuevaContrasena);
}
