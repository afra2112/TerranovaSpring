package com.proyecto.terranova.service;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
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


    @Scheduled(cron = "0 */1 * * * *")
    public void actualizarEstadosCitas() {

        LocalDateTime ahora = LocalDateTime.now();

        List<Cita> programadas = citaRepository.findByEstadoCita(EstadoCitaEnum.PROGRAMADA);
        for (Cita cita : programadas) {
            LocalDateTime inicio = LocalDateTime.of(cita.getFecha(), cita.getHoraInicio());
            LocalDateTime fin = LocalDateTime.of(cita.getFecha(), cita.getHoraFin());

            if (!ahora.isBefore(inicio) && !ahora.isAfter(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.EN_CURSO);
                citaRepository.save(cita);
            }
        }

        List<Cita> enProceso = citaRepository.findByEstadoCita(EstadoCitaEnum.EN_CURSO);
        for (Cita cita : enProceso) {
            LocalDateTime fin = LocalDateTime.of(cita.getFecha(), cita.getHoraFin());
            if (ahora.isAfter(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.FINALIZADA);
                citaRepository.save(cita);
            }
        }
    }
}
