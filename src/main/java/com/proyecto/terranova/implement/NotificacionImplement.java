package com.proyecto.terranova.implement;

import com.proyecto.terranova.dto.NotificacionPeticion;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.service.EmailService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.repository.NotificacionRepository;
import com.proyecto.terranova.dto.NotificacionDTO;

@Service
public class NotificacionImplement implements NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailService emailService;

    @Override
    public NotificacionDTO save(NotificacionDTO dto) {
        Notificacion entidadNotificacion = modelMapper.map(dto, Notificacion.class);
        Notificacion entidadGuardada = repository.save(entidadNotificacion);
        return modelMapper.map(entidadGuardada, NotificacionDTO.class);
    }

    @Override
    public NotificacionDTO update(Long id, NotificacionDTO dto) {
        Notificacion entidadNotificacion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrado"));

        modelMapper.map(dto, entidadNotificacion);

        Notificacion entidadActualizada = repository.save(entidadNotificacion);
        return modelMapper.map(entidadActualizada, NotificacionDTO.class);
    }

    @Override
    public NotificacionDTO findById(Long id) {
        Notificacion entidadNotificacion = repository.findById(id).orElseThrow(() -> new RuntimeException("Notificacion no encontrado"));
        return modelMapper.map(entidadNotificacion, NotificacionDTO.class);
    }

    @Override
    public List<NotificacionDTO> findAll() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Long id) {
        if(!repository.existsById(id)){
               return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean validarSiEnviarNotificacionONo(Usuario usuario, String tipo) {
        boolean enviar = true;

        switch (tipo){
            case "Disponibilidades":
                enviar = usuario.isNotificacionesDisponibilidades();
                break;
            case "Productos":
                enviar = usuario.isNotificacionesProductos();
                break;
            case "Citas":
                enviar = usuario.isNotificacionesCitas();
                break;
            case "Ventas":
                enviar = usuario.isNotificacionesVentas();
                break;
            case "Sistema":
                enviar = usuario.isNotificacionesSistema();
                break;
        }
        return enviar;
    }

    @Override
    public void marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = repository.findById(idNotificacion).orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));

        if(!notificacion.isLeido()){
            notificacion.setLeido(true);
        }else {
            notificacion.setLeido(false);
        }
        repository.save(notificacion);
    }

    @Override
    public void marcarTodasComoLeidas(Usuario usuario) {
        List<Notificacion> notificaciones = repository.findByUsuarioAndLeidoFalseAndActivoOrderByFechaNotificacionDesc(usuario, true);
        notificaciones.forEach(noti -> noti.setLeido(true));
        repository.saveAll(notificaciones);
    }

    @Override
    public List<Notificacion> obtenerPorUsuarioYActivo(Usuario usuario, boolean activo) {
        return repository.findByUsuarioAndActivoOrderByFechaNotificacionDesc(usuario, activo);
    }

    @Override
    public List<Notificacion> obtenerPorUsuarioYLeido(Usuario usuario, boolean leido) {
        return repository.findByUsuarioAndLeidoFalseAndActivoOrderByFechaNotificacionDesc(usuario, true);
    }

    @Override
    public int contarPorUsuarioYTipo(Usuario usuario, String tipo) {
        return repository.countByUsuarioAndTipoAndActivo(usuario, tipo, true);
    }

    @Override
    public List<Notificacion> obtenerPorUsuarioYTipo(Usuario usuario, String tipo) {
        return repository.findByUsuarioAndTipoOrderByFechaNotificacionDesc(usuario, tipo);
    }

    @Override
    public int contarNoLeidasPorUsuario(Usuario usuario, boolean leido) {
        return repository.countByUsuarioAndLeidoAndActivo(usuario, false, true);
    }

    @Override
    public void borrarNotificacion(Long idNotificacion) {
        Notificacion notificacion = repository.findById(idNotificacion).orElseThrow();
        notificacion.setActivo(false);
        notificacion.setLeido(true);
        repository.save(notificacion);
    }

    @Override
    public void eliminarHistorial(Usuario usuario) {
        List<Notificacion> notificacionesBorradas = repository.findByUsuarioAndActivoOrderByFechaNotificacionDesc(usuario, false);
        for (Notificacion notificacion : notificacionesBorradas){
            repository.delete(notificacion);
        }
    }

    @Override
    public void crearNotificacionAutomatica(NotificacionPeticion peticion) throws MessagingException, IOException {
        Usuario usuario = peticion.getUsuarioNotificacion();

        if (validarSiEnviarNotificacionONo(usuario, peticion.getTipoNotificacion())){
            Notificacion notificacion = new Notificacion();
            notificacion.setTitulo(peticion.getTituloNotificacion());
            notificacion.setMensajeNotificacion(peticion.getMensajeNotificacion());
            notificacion.setTipo(peticion.getTipoNotificacion());
            notificacion.setLeido(false);
            notificacion.setFechaNotificacion(LocalDateTime.now());
            notificacion.setUsuario(usuario);
            notificacion.setReferenciaId(peticion.getIdReferenciaNotificacion());
            notificacion.setUrlAccion(peticion.getUrlAccionNotificacion());
            repository.save(notificacion);
        }
        if(usuario.isRecibirCorreos()){
            String html = emailService.generarHtmlParaCorreo(
                    peticion.getNombreUsuarioCorreo(),
                    peticion.getNombreTipoNotificacionCorreo(),
                    peticion.getLinkAccionCorreo(),
                    peticion.getNombreUsuarioContrarioCorreo(),
                    peticion.getNombreTemplateHtmlCorreo()
            );
            emailService.enviarEmailConHtml(usuario.isRecibirCorreos(),usuario.getEmail(), peticion.getAsuntoCorreo(),html);
        }
    }

    //HELPERS PARA CREAR NOTIFICACIONES
    private NotificacionPeticion buildNotificacion(
            Long idReferencia,
            String mensaje,
            String tipo,
            String titulo,
            Usuario usuario,
            String urlAccion,
            String asuntoCorreo,
            String linkAccion,
            String templateHtml,
            String tipoCorreo,
            String usuarioContrario,
            String usuarioCorreo
    ) {
        return NotificacionPeticion.builder()
                .idReferenciaNotificacion(idReferencia)
                .mensajeNotificacion(mensaje)
                .tipoNotificacion(tipo)
                .tituloNotificacion(titulo)
                .usuarioNotificacion(usuario)
                .urlAccionNotificacion(urlAccion)
                .asuntoCorreo(asuntoCorreo)
                .linkAccionCorreo(linkAccion)
                .nombreTemplateHtmlCorreo(templateHtml)
                .nombreTipoNotificacionCorreo(tipoCorreo)
                .nombreUsuarioContrarioCorreo(usuarioContrario)
                .nombreUsuarioCorreo(usuarioCorreo)
                .build();
    }


    @Override
    public void notificacionCitaCancelada(Cita cita, Usuario compradorOVendedor) throws MessagingException, IOException {
        Usuario comprador = cita.getComprador();
        Usuario vendedor = cita.getProducto().getVendedor();
        String producto = cita.getProducto().getNombreProducto();

        boolean fueComprador = compradorOVendedor.equals(comprador);

        NotificacionPeticion notifComprador = buildNotificacion(
                cita.getIdCita(),
                fueComprador
                        ? "Cancelaste tu cita para el producto: " + producto + " con el vendedor: " + vendedor.getNombres()
                        : "Tu cita para el producto: " + producto + " ha sido cancelada por el vendedor: " + vendedor.getNombres(),
                "Citas",
                "Actualización en tu cita: Cancelación",
                comprador,
                "/comprador/citas",
                (fueComprador ? "Cancelaste" : "Cancelaron") + " tu cita para el producto: " + producto,
                "http://localhost:8080/comprador/citas",
                "citaCancelada",
                cita.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                cita.getIdCita(),
                fueComprador
                        ? "El comprador " + comprador.getNombres() + " canceló la cita para el producto: " + producto
                        : "Cancelaste tu cita para el producto: " + producto + " con el comprador: " + comprador.getNombres(),
                "Citas",
                "Actualización en tu cita: Cancelación",
                vendedor,
                "/vendedor/citas",
                (fueComprador ? "El comprador canceló" : "Cancelaste") + " tu cita para el producto: " + producto,
                "http://localhost:8080/vendedor/citas",
                "citaCancelada",
                cita.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );

        crearNotificacionAutomatica(notifComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionCitaFinalizada(Cita cita) throws MessagingException, IOException {
        Usuario comprador = cita.getComprador();
        Usuario vendedor = cita.getProducto().getVendedor();
        String producto = cita.getProducto().getNombreProducto();

        NotificacionPeticion notifComprador = buildNotificacion(
                cita.getIdCita(),
                "La cita para el producto " + producto + " ha finalizado exitosamente. Ahora puedes proceder a generar la compra desde tu panel.",
                "Citas",
                "Cita finalizada",
                cita.getComprador(),
                "/comprador/citas",
                "Tu cita para el producto " + producto + " ha sido marcada como finalizada.",
                "http://localhost:8080/comprador/citas",
                "citaFinalizada",
                cita.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                cita.getIdCita(),
                "La cita con el comprador " + comprador.getNombres() + " para tu producto " + producto + " ha finalizado.",
                "Citas",
                "Cita finalizada",
                vendedor,
                "/vendedor/citas",
                "La cita para tu producto " + producto + " ha sido marcada como finalizada.",
                "http://localhost:8080/vendedor/citas",
                "citaFinalizadaVendedor",
                cita.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );


        crearNotificacionAutomatica(notifComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionCitaReservada(Cita cita) throws MessagingException, IOException {
        Usuario comprador = cita.getComprador();
        Usuario vendedor = cita.getProducto().getVendedor();
        String producto = cita.getProducto().getNombreProducto();

        NotificacionPeticion notifComprador = buildNotificacion(
                cita.getIdCita(),
                "Haz reservado una cita para el producto: " + producto + ". Con el vendedor: "+vendedor.getNombres()+". Puedes ir a Mis Citas para mas detalles.",
                "Citas",
                "Cita Reservada",
                cita.getComprador(),
                "/comprador/citas",
                "Tu cita para el producto " + producto + " ha sido reservada.",
                "http://localhost:8080/comprador/citas",
                "citaReservada",
                cita.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                cita.getIdCita(),
                "El comprador " + comprador.getNombres() + ". Ha reservado una cita para tu producto " + producto + ".",
                "Citas",
                "Cita Reservada",
                vendedor,
                "/vendedor/citas",
                "Han reservado una cita para tu producto: " + producto + ".",
                "http://localhost:8080/vendedor/citas",
                "citaReservadaVendedor",
                cita.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );


        crearNotificacionAutomatica(notifComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionCitaReprogramada(Cita cita, Usuario compradorOVendedor) throws MessagingException, IOException {
        Usuario comprador = cita.getComprador();
        Usuario vendedor = cita.getProducto().getVendedor();
        String producto = cita.getProducto().getNombreProducto();

        boolean fueComprador = compradorOVendedor.equals(comprador);

        NotificacionPeticion notifComprador = buildNotificacion(
                cita.getIdCita(),
                fueComprador
                        ? "Reprogramaste tu cita para el producto: " + producto + " con el vendedor: " + vendedor.getNombres()  + ". Para la fecha: " + cita.getDisponibilidad().getFecha()
                        : "Tu cita para el producto: " + producto + " ha sido reprogramada por el vendedor: " + vendedor.getNombres() + ". Para la fecha: " + cita.getDisponibilidad().getFecha(),
                "Citas",
                "Actualización en tu cita: Reprogramacion",
                comprador,
                "/comprador/citas",
                (fueComprador ? "Reprogramaste" : "Reprogramaron") + " tu cita para el producto: " + producto  + ". Para la fecha: ",
                "http://localhost:8080/comprador/citas",
                "reprogramacionCita",
                cita.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                cita.getIdCita(),
                fueComprador
                        ? "El comprador " + comprador.getNombres() + " reprogramo la cita para el producto: " + producto  + ". Para la fecha: " + cita.getDisponibilidad().getFecha()
                        : "Reprogramaste tu cita para el producto: " + producto + " con el comprador: " + comprador.getNombres()  + ". Para la fecha: " + cita.getDisponibilidad().getFecha(),
                "Citas",
                "Actualización en tu cita: Reprogramacion",
                vendedor,
                "/vendedor/citas",
                (fueComprador ? "El comprador reprogramo" : "Reprogramaste") + " tu cita para el producto: " + producto  + ". Para la fecha: ",
                "http://localhost:8080/vendedor/citas",
                "reprogramacionCita",
                cita.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );

        crearNotificacionAutomatica(notifComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionReprogramarCitaHabilitado(Cita cita) throws MessagingException, IOException {
        NotificacionPeticion notificacionPeticion = buildNotificacion(
                cita.getIdCita(),
                "Ya han pasado 24 horas desde tu ultima reprogramacion posible, ya puedes volver a reprogramar tu cita dos veces mas",
                "Citas",
                "Reprogramacion Disponible",
                cita.getComprador(),
                "/comprador/citas",
                "Reprogramacion Disponible",
                "http://localhost:8080/comprador/citas",
                "reprogramacionHabilitada",
                cita.getProducto().getNombreProducto(),
                cita.getProducto().getVendedor().getNombres(),
                cita.getComprador().getNombres()
        );

        crearNotificacionAutomatica(notificacionPeticion);
    }

    @Override
    public void notificacionVentaGenerada(Venta venta) throws MessagingException, IOException {
        Usuario comprador = venta.getComprador();
        Usuario vendedor = venta.getProducto().getVendedor();
        String producto = venta.getProducto().getNombreProducto();

        NotificacionPeticion notifiComprador = buildNotificacion(
                venta.getIdVenta(),
                "Se ha creado una nueva compra para el producto: " + producto + ". Ve al panel de compras para mas detalles. Recuerda que tu debes confirmar los datos obligatorios de la venta para finalizarla.",
                "Ventas",
                "Creacion de compra",
                venta.getComprador(),
                "/comprador/compras",
                "Creaste una compra para el producto: "+producto,
                "http://localhost:8080/comprador/compras",
                "ventaGenerada",
                venta.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                venta.getIdVenta(),
                "El comprador " + comprador.getNombres() +
                        " ha generado una venta para tu producto: " + producto +
                        ". Recuerda que debes actualizar los datos obligatorios para finalizar la venta.",
                "Ventas",
                "Nueva venta generada",
                vendedor,
                "/vendedor/ventas",
                "Se generó una venta para tu producto: " + producto,
                "http://localhost:8080/vendedor/ventas",
                "ventaGeneradaVendedor",
                venta.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );

        crearNotificacionAutomatica(notifiComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionVentaModificada(Venta venta) throws MessagingException, IOException {
        Usuario comprador = venta.getComprador();
        Usuario vendedor = venta.getProducto().getVendedor();
        String producto = venta.getProducto().getNombreProducto();

        NotificacionPeticion notifiComprador = buildNotificacion(
                venta.getIdVenta(),
                "El vendedor ha modificado los datos de tu compra para el producto: " + producto + ". Ve al panel de compras para mas detalles. Recuerda que tu debes confirmar los datos obligatorios de la venta para finalizarla.",
                "Ventas",
                "Modificacion de compra",
                venta.getComprador(),
                "/comprador/compras",
                "Modificacion en datos de compra para el producto: "+producto,
                "http://localhost:8080/comprador/compras",
                "ventaModificadaComprador",
                venta.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                venta.getIdVenta(),
                "Modificaste los datos de tu venta con el comprador: " + comprador.getNombres() +
                        " para el producto: " + producto +
                        ".",
                "Ventas",
                "Modificacion de datos de venta",
                vendedor,
                "/vendedor/ventas",
                "Actualizaste los datos de una venta",
                "http://localhost:8080/vendedor/ventas",
                "ventaModificada",
                venta.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );

        crearNotificacionAutomatica(notifiComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionPeticionFinalizacionVenta(Venta venta) throws MessagingException, IOException {
        Usuario comprador = venta.getComprador();
        Usuario vendedor = venta.getProducto().getVendedor();
        String producto = venta.getProducto().getNombreProducto();

        NotificacionPeticion notifiComprador = buildNotificacion(
                venta.getIdVenta(),
                "El vendedor ha hecho una peticion para finalizar tu compra para el producto: " + producto + ". Ve al panel de compras para mas detalles. Recuerda que tu debes confirmar los datos obligatorios de la venta para finalizarla.",
                "Ventas",
                "Peticion de finalizacion de compra",
                venta.getComprador(),
                "/comprador/compras",
                "Modificacion en datos de compra para el producto: "+producto,
                "http://localhost:8080/comprador/compras",
                "ventaFinalizacion",
                venta.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                venta.getIdVenta(),
                "Hiciste una peticion de finalizacion de venta con el comprador: " + comprador.getNombres() +
                        " para el producto: " + producto +
                        ". Espera a que el acepte o cancele la revision",
                "Ventas",
                "Peticion de finalizacion de venta",
                vendedor,
                "/vendedor/ventas",
                "Hiciste la peticion para finalizar una venta",
                "http://localhost:8080/vendedor/ventas",
                "ventaFinalizacion",
                venta.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );

        crearNotificacionAutomatica(notifiComprador);
        crearNotificacionAutomatica(notifVendedor);
    }

    @Override
    public void notificacionDisponibilidadRegistrada(Disponibilidad disponibilidad, String fecha, String hora) throws MessagingException, IOException {
        NotificacionPeticion notificacionPeticion = buildNotificacion(
                disponibilidad.getIdDisponibilidad(),
                "Has registrado una disponibilidad para el dia "+fecha+". A las "+hora,
                "Disponibilidades",
                "Agregaste una disponibilidad",
                disponibilidad.getProducto().getVendedor(),
                "/vendedor/mi-calendario",
                "Agregaste una disponibilidad",
                "http://localhost:8080/vendedor/mi-calendario",
                "disponibilidadCreada",
                disponibilidad.getProducto().getNombreProducto(),
                "ninguno",
                disponibilidad.getProducto().getVendedor().getNombres()
        );

        crearNotificacionAutomatica(notificacionPeticion);
    }

    @Override
    public void notificacionFotoPerfilCambiada(Usuario usuario) throws MessagingException, IOException {
        NotificacionPeticion notificacionPeticion = buildNotificacion(
                0L,
                "Cambiaste tu foto de perfil exitosamente.",
                "Sistema",
                "Cambio de foto de datos personales",
                usuario,
                "/usuarios/mi-perfil?id=1",
                "Cambiaste tu foto.",
                "http://localhost:8080/usuarios/mi-perfil?id=1",
                "fotoCambiada",
                "ninguno",
                "ninguno",
                usuario.getNombres()
        );

        crearNotificacionAutomatica(notificacionPeticion);
    }

    @Override
    public void notificacionDatosPersonalesActualizados(Usuario usuario) throws MessagingException, IOException {
        NotificacionPeticion notificacionPeticion = buildNotificacion(
                0L,
                "Cambiaste datos de tu informacion personal, puedes ir a Mi Perfil para verlos reflejados.",
                "Sistema",
                "Cambio de datos personales",
                usuario,
                "/usuarios/mi-perfil?id=1",
                "Cambiaste tu info personal.",
                "http://localhost:8080/usuarios/mi-perfil?id=1",
                "infoCambiada",
                "ninguno",
                "ninguno",
                usuario.getNombres()
        );

        crearNotificacionAutomatica(notificacionPeticion);
    }

    @Override
    public void notificacionPedirModificarVenta(Venta venta, String razon) throws MessagingException, IOException {
        Usuario comprador = venta.getComprador();
        Usuario vendedor = venta.getProducto().getVendedor();
        String producto = venta.getProducto().getNombreProducto();

        NotificacionPeticion notifiComprador = buildNotificacion(
                venta.getIdVenta(),
                "Has hecho una peticion para actualizar los datos de la venta para el producto: " + producto + ". Con la siguiente razon: "+ razon,
                "Ventas",
                "Peticion modificacion de compra",
                venta.getComprador(),
                "/comprador/compras",
                "Pediste una modificacion de compra para el producto: "+producto,
                "http://localhost:8080/comprador/compras",
                "pedirModificarVenta",
                venta.getProducto().getNombreProducto(),
                vendedor.getNombres(),
                comprador.getNombres()
        );

        NotificacionPeticion notifVendedor = buildNotificacion(
                venta.getIdVenta(),
                "El comprador: " + comprador.getNombres() +
                        "ha pedido que modifiques la venta para el producto: " + producto +
                        ". Razon: "+razon,
                "Ventas",
                "Peticion modificacion de datos de venta",
                vendedor,
                "/vendedor/ventas",
                "Te pidieron que actualices los datos de tu venta para: "+producto,
                "http://localhost:8080/vendedor/ventas",
                "pedirModificarVentaVendedor",
                venta.getProducto().getNombreProducto(),
                comprador.getNombres(),
                vendedor.getNombres()
        );

        crearNotificacionAutomatica(notifiComprador);
        crearNotificacionAutomatica(notifVendedor);
    }
}
