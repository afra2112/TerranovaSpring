package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.*;
import com.proyecto.terranova.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompradorImplement implements CompradorService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CitaRepository citaRepository;

    @Autowired
    NotificacionRepository notificacionRepository;

    @Autowired
    HistorialVistosRepository historialVistosRepository;

    @Override
    public Map<String, Integer> prepararIndex(String cedula) {
        Usuario usuario = usuarioRepository.findById(cedula).orElseThrow();

        int favoritos = usuario.getFavoritos().size();
        int citas = citaRepository.findByCompradorAndActivoOrderByDisponibilidad_FechaAscDisponibilidad_HoraAsc(usuario, true).size();
        int notificaciones = notificacionRepository.findByUsuarioAndLeidoFalseAndActivoOrderByFechaNotificacionDesc(usuario, true).size();
        int visitados = historialVistosRepository.findByUsuario(usuario).size();

        Map<String, Integer> estadisticas = new HashMap<>();

        estadisticas.put("favoritos", favoritos);
        estadisticas.put("citas", citas);
        estadisticas.put("notificaciones", notificaciones);
        estadisticas.put("visitados", visitados);

        return estadisticas;
    }
}
