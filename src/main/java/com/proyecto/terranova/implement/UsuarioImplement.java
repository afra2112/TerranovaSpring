package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Rol;
import com.proyecto.terranova.repository.RolRepository;
import com.proyecto.terranova.service.EmailService;
import com.proyecto.terranova.service.NotificacionService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.UsuarioService;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.dto.UsuarioDTO;
import com.proyecto.terranova.entity.Usuario;

@Service
public class UsuarioImplement implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificacionService notificacionService;

    @Override
    public boolean save(UsuarioDTO dto) {
        if(repository.existsByemail(dto.getEmail()) || repository.existsBycedula(dto.getCedula())){
            return false;
        }

        Usuario entidadUsuario = modelMapper.map(dto, Usuario.class);

        entidadUsuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));

        List<Rol> roles = new ArrayList<>();
        Rol rol = rolRepository.findBynombreRol(RolEnum.COMPRADOR);
        roles.add(rol);
        entidadUsuario.setRoles(roles);
        entidadUsuario.setFechaRegistro(LocalDate.now());
        repository.save(entidadUsuario);
        return true;
    }

    @Override
    public void update(Usuario usuario) {
    	repository.save(usuario);
    }

    @Override
    public Usuario findById(String cedula) {
     return repository.findById(cedula).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<UsuarioDTO> findAll() {

        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, UsuarioDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String cedula) {
        if(!repository.existsById(cedula)){
               return false;
        }
        repository.deleteById(cedula);
        return true;
    }

    @Override
    public boolean existsById(String cedula) {
        return repository.existsById(cedula);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void procesarOAuthPostLogin(String provider, String providerId, String email, String name) {
        Usuario usuario = null;

        if(email != null){
            usuario = repository.findByEmail(email);
        }

        if(usuario == null && providerId != null){
            usuario = repository.findByProviderAndProviderId(provider, providerId);
        }

        if(usuario == null){
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNombres(name);
            usuario.setProvider(provider);
            usuario.setProviderId(providerId);
            repository.save(usuario);
        } else {
            boolean cambiado = false;

            if (usuario.getNombres() == null && name != null) {
                usuario.setNombres(name);
                cambiado = true;
            }
            if (cambiado){
                repository.save(usuario);
            }
        }
    }

    @Override
    public void volverVendedor(String cedula) {
        Usuario usuario = repository.findById(cedula).orElseThrow();
        Rol rol = rolRepository.findBynombreRol(RolEnum.VENDEDOR);
        usuario.getRoles().add(rol);
        repository.save(usuario);
    }

    @Override
    public List<RolEnum> obtenerNombresRoles(Usuario usuario) {
        List<RolEnum> nombresRoles = new ArrayList<>();
        usuario.getRoles().forEach(rol -> {
            nombresRoles.add(rol.getNombreRol());
        });
        return nombresRoles;
    }

    @Override
    public void generarTokenYEnviarCorreoRecuperarContrasena(String email) throws MessagingException, IOException {
        Usuario usuario = repository.findByEmail(email);

        String token = UUID.randomUUID().toString();

        usuario.setResetToken(token);
        usuario.setResetTokenExpiracion(LocalDateTime.now().plusMinutes(30));
        repository.save(usuario);

        notificacionService.notificacionRecuperarContrasena(email);
    }

    @Override
    public String validarTokenYActualizarContrasena(String token, String nuevaContrasena) {
        Usuario usuario = repository.findByResetToken(token);
        if (usuario.getResetTokenExpiracion().isBefore(LocalDateTime.now())) {
            return "El enlace expiró.";
        }

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiracion(null);
        repository.save(usuario);

        return "Contraseña actualizada correctamente.";
    }

    public void actualizarPerfil(String cedula, String nuevoNombre, String nuevoCorreo, String nuevaFoto ){
        Usuario usuario = repository.findById(cedula)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        usuario.setNombres(nuevoNombre);
        usuario.setEmail(nuevoCorreo);
        usuario.setFoto(nuevaFoto);
        repository.save(usuario);
    }

}
