package com.proyecto.terranova.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.proyecto.terranova.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    MercadoPagoService mercadoPagoService;

    @GetMapping("iniciar-pago")
    public String iniciarPago(@RequestParam Long idProducto, @RequestParam Long idVenta, Model model) throws MPException, MPApiException {
        String puntoInicio = mercadoPagoService.crearPreferencia(idProducto, idVenta);
        return "redirect:" + puntoInicio;
    }

    @GetMapping("exitoso")
    public String pagoExitoso(Model model){
        return "pago-exitoso";
    }

    @GetMapping("fallido")
    public String pagoFallido(Model model){
        return "pago-fallido";
    }

    @GetMapping("pendiente")
    public String pagoPendiente(Model model){
        return "pago-pendiente";
    }
}
