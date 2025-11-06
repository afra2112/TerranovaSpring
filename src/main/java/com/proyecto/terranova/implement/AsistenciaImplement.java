package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsistenciaImplement implements AsistenciaService {

    @Autowired
    AsistenciaRepository asistenciaRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<Asistencia> encontrarPorComprador(Usuario comprador) {
        return asistenciaRepository.findByUsuarioOrderByCita_FechaAscCita_HoraInicioAsc(comprador);
    }

    @Override
    public boolean yaTieneAsistencia(Usuario comprador, Long idProducto) {
        return asistenciaRepository.existsByCita_ProductoAndUsuario(productoRepository.findById(idProducto).orElseThrow(), comprador);
    }

    @Override
    public Asistencia save(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }
}
