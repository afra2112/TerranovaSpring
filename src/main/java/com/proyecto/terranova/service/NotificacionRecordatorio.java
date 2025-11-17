package com.proyecto.terranova.service;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionRecordatorio {

    @Autowired
    private CitaRepository citaRepository;

    @Scheduled(cron = "*/30 * * * * *") // Cada 30 segundos
    @Transactional
    public void actualizarEstadosCitas() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Cita> citasActualizadas = new ArrayList<>();

        // 1. PROGRAMADA -> EN_CURSO
        List<Cita> programadas = citaRepository.findByEstadoCita(EstadoCitaEnum.PROGRAMADA);
        for (Cita cita : programadas) {
            LocalDateTime inicio = LocalDateTime.of(cita.getFecha(), cita.getHoraInicio());
            LocalDateTime fin = LocalDateTime.of(cita.getFecha(), cita.getHoraFin());

            // ✅ Cita ya finalizó (nunca se marcó como EN_CURSO)
            if (ahora.isAfter(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.FINALIZADA);
                citasActualizadas.add(cita);
            }
            // ✅ Cita está en curso (entre inicio y fin)
            else if (!ahora.isBefore(inicio) && ahora.isBefore(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.EN_CURSO);
                citasActualizadas.add(cita);
            }
        }

        // 2. EN_CURSO -> FINALIZADA
        List<Cita> enCurso = citaRepository.findByEstadoCita(EstadoCitaEnum.EN_CURSO);
        for (Cita cita : enCurso) {
            LocalDateTime fin = LocalDateTime.of(cita.getFecha(), cita.getHoraFin());

            // ✅ Cita terminó
            if (ahora.isAfter(fin) || ahora.isEqual(fin)) {
                cita.setEstadoCita(EstadoCitaEnum.FINALIZADA);
                citasActualizadas.add(cita);
            }
        }

        // ✅ Guardar todos los cambios de una vez
        if (!citasActualizadas.isEmpty()) {
            citaRepository.saveAll(citasActualizadas);
            System.out.println("✅ Actualizadas " + citasActualizadas.size() + " citas");
        }
    }
}