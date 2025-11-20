package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.GastoVenta;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.service.GastoVentaService;
import com.proyecto.terranova.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/vendedor/ventas/gasto")
public class GastoVentaController {

    @Autowired
    GastoVentaService gastoVentaService;

    @Autowired
    VentaService ventaService;

    @PostMapping("/agregar")
    @ResponseBody
    public Map<String, Object> agregarGasto(@RequestParam Long idVenta, @RequestParam String nombreGasto, @RequestParam Long valorGasto) {
        Map<String, Object> response = new HashMap<>();

        try {
            GastoVenta gastoVenta = gastoVentaService.save(idVenta, nombreGasto, valorGasto);

            response.put("success", true);
            response.put("idGasto", gastoVenta.getIdGasto());
            return response;

        } catch (Exception e) {
            response.put("success", false);
            return response;
        }
    }

    @PostMapping("/eliminar/{idGasto}")
    @ResponseBody
    public Map<String, Object> eliminarGasto(@PathVariable Long idGasto) {
        Map<String, Object> response = new HashMap<>();

        try {
            gastoVentaService.delete(idGasto);

            response.put("success", true);
            return response;

        } catch (Exception e) {
            response.put("success", false);
            return response;
        }
    }

    @PostMapping("/nota/actualizar")
    @ResponseBody
    public Map<String, Object> actualizarNota(@RequestParam Long idVenta, @RequestParam String nota) {
        Map<String, Object> response = new HashMap<>();

        try {
            Venta venta = ventaService.findById(idVenta);
            venta.setNota(nota);
            ventaService.save(venta);

            response.put("success", true);
            return response;

        } catch (Exception e) {
            response.put("success", false);
            return response;
        }
    }
}
