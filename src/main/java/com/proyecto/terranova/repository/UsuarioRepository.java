package com.proyecto.terranova.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Usuario;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByEmail(String email);

    boolean existsByemail(String email);

    boolean existsBycedula(String cedula);

    Usuario findByProviderAndProviderId(String provider, String providerId);

    Optional<Usuario> findByResetToken(String resetToken);
}
