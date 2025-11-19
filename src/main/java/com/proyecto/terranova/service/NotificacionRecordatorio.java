package com.proyecto.terranova.service;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionRecordatorio {

    @Autowired
    private CitaRepository citaRepository;

    private static final ZoneId COLOMBIA_ZONE = ZoneId.of("America/Bogota");

    @Scheduled(cron = "*/30 * * * * *")    @Transactional
    public void actualizarEstadosCitas() {
        LocalDateTime ahora = LocalDateTime.now(COLOMBIA_ZONE);
        LocalDate hoy = LocalDate.now(COLOMBIA_ZONE);

        List<Cita> citasActualizadas = new ArrayList<>();

        List<Cita> programadas = citaRepository.findByEstadoCita(EstadoCitaEnum.PROGRAMADA);

        for (Cita cita : programadas) {
            LocalDateTime inicio = LocalDateTime.of(cita.getFecha(), cita.getHoraInicio());
            LocalDateTime fin = LocalDateTime.of(cita.getFecha(), cita.getHoraFin());

            if (cita.getFecha().isAfter(hoy)) {
                continue;
            }

            if (ahora.isAfter(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.FINALIZADA_AUTOMATICAMENTE);
                citasActualizadas.add(cita);
            }

            else if (!ahora.isBefore(inicio) && ahora.isBefore(fin)) {
                if (!cita.getAsistencias().isEmpty()) {
                    cita.setEstadoCita(EstadoCitaEnum.EN_CURSO);
                } else {
                    cita.setEstadoCita(EstadoCitaEnum.FINALIZADA_AUTOMATICAMENTE);
                }

                citasActualizadas.add(cita);
            }
        }

        List<Cita> enCurso = citaRepository.findByEstadoCita(EstadoCitaEnum.EN_CURSO);

        for (Cita cita : enCurso) {
            LocalDateTime fin = LocalDateTime.of(cita.getFecha(), cita.getHoraFin());

            if (ahora.isAfter(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.FINALIZADA);
                citasActualizadas.add(cita);
            }

            if (cita.getAsistencias().isEmpty()){
                cita.setEstadoCita(EstadoCitaEnum.FINALIZADA_AUTOMATICAMENTE);
                citasActualizadas.add(cita);
            }
        }

        if (!citasActualizadas.isEmpty()) {
            citaRepository.saveAll(citasActualizadas);
            }
    }
}