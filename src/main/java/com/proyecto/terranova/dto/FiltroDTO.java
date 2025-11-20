package com.proyecto.terranova.dto;

import java.time.LocalDate;

public class FiltroDTO {

    //Campos Producto
    private String nombreProducto;
    private Long idUbicacion;
    private LocalDate fechaPublicacion;
    private String descripcion;
    private Long precioProducto;
    private String tipoProducto;

    //Campos Terreno
    private double tamanoTerreno;
    private String tipoTerreno;
    private String topografiaTerreno;
    private String acceso;
    private String servicios;
    private String usoActual;

    //Campos Ganado
    private String razaGanado;
    private int pesoGanado;
    private int edadGanado;
    private String generoGanado;
    private String tipoGanado;

    //Campos fincas
    private String espacioTotal;
    private String espacioConstruido;
    private int estratoFinca;
    private int numeroHabitaciones;
    private int numeroBanos;
}
