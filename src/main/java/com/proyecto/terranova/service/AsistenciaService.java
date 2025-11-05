package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AsistenciaService {
    List<Asistencia> encontrarPorComprador(Usuario comprador);
    boolean yaTieneAsistencia(Usuario comprador, Long idProducto);
    Asistencia save(Asistencia asistencia);
}
