package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.repository.CitaRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionRecordatorio {

    @Autowired
    CitaRepository citaRepository;

    @Autowired
    NotificacionService notificacionService;


    @Scheduled(fixedRate = 5000)
    public void revisarTiempo() throws MessagingException, IOException {
        List<Cita> citasBloqueadas = citaRepository.findByUltimaReprogramacionBloqueadaNotNull();

        for (Cita cita : citasBloqueadas){
            if(cita.getFechaHabilitarReprogramacion().isBefore(LocalDateTime.now())){

                notificacionService.notificacionReprogramarCitaHabilitado(cita);

                cita.setNumReprogramaciones(0);
                cita.setUltimaReprogramacionBloqueada(null);
                cita.setFechaHabilitarReprogramacion(null);
                citaRepository.save(cita);
            }
        }
    }
}
