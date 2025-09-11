package com.proyecto.terranova.repository;

import com.proyecto.terranova.dto.RolDTO;
import com.proyecto.terranova.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByEmail(String email);

    boolean existsByemail(String email);

    boolean existsBycedula(String cedula);

    Usuario findByProviderAndProviderId(String provider, String providerId);
}
