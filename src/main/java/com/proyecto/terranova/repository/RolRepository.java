package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.RolEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Rol;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    @Query(value = "SELECT u.nombres, r.nombre_rol FROM usuarios u JOIN usuario_rol ur ON u.cedula = ur.cedula JOIN roles r ON ur.id_rol = r.id_rol WHERE u.cedula = :cedula", nativeQuery = true)
    List<String> findByRolesAndCedula(@Param("cedula") String cedula);

    Rol findBynombreRol(RolEnum nombre);

    boolean existsByNombreRol(RolEnum nombre);
}
