package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.repository.CitaRepository;
import com.proyecto.terranova.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionRecordatorio {

    @Autowired
    CitaRepository citaRepository;

    @Autowired
    NotificacionService notificacionService;

    @Scheduled(fixedRate = 5000)
    public void revisarTiempo(){
        List<Cita> citasBloqueadas = citaRepository.findByUltimaReprogramacionBloqueadaNotNull();

        for (Cita cita : citasBloqueadas){
            if(cita.getFechaHabilitarReprogramacion().isBefore(LocalDateTime.now())){

                String titulo = "Reprogramacion Disponibile";
                String mensaje = "Ya han pasado 24 horas desde tu ultima reprogramacion posible, ya puedes volver a reprogramar tu cita dos veces mas";
                notificacionService.crearNotificacionAutomatica(titulo, mensaje, "Citas", cita.getComprador(), cita.getIdCita(), "/comprador/citas");

                cita.setNumReprogramaciones(0);
                cita.setUltimaReprogramacionBloqueada(null);
                cita.setFechaHabilitarReprogramacion(null);
                citaRepository.save(cita);
            }
        }
    }
}
