package com.proyecto.terranova.dto;

import com.proyecto.terranova.entity.Usuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificacionPeticion {
    //datos para la notificacion
    private String tituloNotificacion;
    private String mensajeNotificacion;
    private String tipoNotificacion;
    private Usuario usuarioNotificacion;
    private Long idReferenciaNotificacion;
    private String urlAccionNotificacion;

    //datos para el html del correo
    private String nombreUsuarioCorreo;
    private String nombreTipoNotificacionCorreo;
    private String nombreUsuarioContrarioCorreo;
    private String linkAccionCorreo;
    private String nombreTemplateHtmlCorreo;
    private String asuntoCorreo;
}
