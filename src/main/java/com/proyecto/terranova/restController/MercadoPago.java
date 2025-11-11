package com.proyecto.terranova.restController;

import com.proyecto.terranova.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mercadopago")
public class MercadoPago {

    @Autowired
    MercadoPagoService mercadoPagoService;

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirNotificacion(@RequestBody Map<String, Object> payload){
        try {
            mercadoPagoService.procesarNotificacion(payload);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
        }
    }
}
