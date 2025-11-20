package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Transporte;
import com.proyecto.terranova.entity.Venta;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public interface TransporteService {

    Transporte programarTransporte(
            Long idVenta,
            LocalDate fechaTransporte,
            LocalTime horaTransporte,
            String empresaTransporte,
            String placaVehiculo,
            String nombreConductor,
            String telefonoConductor,
            String cedulaConductor,
            String puntoEntrega,
            String observacionesComprador
    );

    void confirmarRecepcion(
            Long idTransporte,
            MultipartFile fotoGanadoDescargado,
            MultipartFile fotoGanadoNuevoLote
    ) throws IOException;

    Transporte encontrarPorId(Long idTransporte);

    void confirmarCarga(
            Long idTransporte,
            MultipartFile fotoCamionVacio,
            MultipartFile fotoCargaGanado,
            MultipartFile fotoGsmiConductor,
            String observacionesVendedor
    ) throws IOException;

    Transporte obtenerPorVenta(Venta venta);
}
