package com.proyecto.terranova.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.proyecto.terranova.entity.Pago;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.repository.PagoRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    PagoRepository pagoRepository;

    @Value("${MERCADO_PAGO_ACCESS_TOKEN}")
    private String accesToken;

    public String crearPreferencia(Long idProducto, Long idVenta) throws MPException, MPApiException {
        Producto producto = productoRepository.findById(idProducto).orElseThrow();

        MercadoPagoConfig.setAccessToken(accesToken);

        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(producto.getIdProducto().toString())
                        .title(producto.getNombreProducto())
                        .description(producto.getDescripcion())
                        .quantity(1)
                        .currencyId("COP")
                        .unitPrice(new BigDecimal(producto.getPrecioProducto()))
                        .build();

        List<PreferenceItemRequest> items = new ArrayList<>();

        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .externalReference(String.valueOf(idVenta))
                .notificationUrl("https://terranovaspring-production.up.railway.app/api/mercadopago/webhook")
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }

    public void procesarNotificacion(Map<String, Object> payload) {
        try {
            MercadoPagoConfig.setAccessToken(accesToken);

            String tipo = (String) payload.get("type");
            if (!"payment".equalsIgnoreCase(tipo)) {
                return;
            }

            System.out.println("paso el primer filtro");

            Map<String, Object> datos = (Map<String, Object>) payload.get("data");
            if (datos == null || datos.get("id") == null) {
                return;
            }

            System.out.println("paso el segundo filtro");

            Long paymentId = Long.valueOf(datos.get("id").toString());

            System.out.println("ID PAYMENTID: " + paymentId);

            if (pagoRepository.existsByIdPaymentMercadoPago(paymentId)) {
                return;
            }

            System.out.println("paso el tercer filtro");

            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(paymentId);

            String estado = payment.getStatus(); //ESTAS SON LAS POSIBLES REPUESTAS approved, pending, rejected
            BigDecimal monto = payment.getTransactionAmount();
            String moneda = payment.getCurrencyId();
            String metodoPago = payment.getPaymentMethodId();
            String externalReference = payment.getExternalReference();

            if (externalReference == null) {
                System.err.println("Pago sin externalReference entonces no se puede asociar a una venta");
                return;
            }

            System.out.println("paso el cuarto filtro");

            Long idVenta = Long.valueOf(externalReference);

            Pago pago = new Pago();
            pago.setVenta(ventaRepository.findById(idVenta).orElseThrow());
            pago.setIdPaymentMercadoPago(paymentId);
            pago.setMontoPagado(monto.toBigInteger().longValue());
            pago.setMoneda(moneda);
            pago.setEstado(estado);
            pago.setMetodoPago(metodoPago);
            pago.setFechaRegistroPago(LocalDateTime.now());

            Pago pagoHecho = pagoRepository.save(pago);
            System.out.println("CREO EL PAGO");

            Venta venta = ventaRepository.findById(pagoHecho.getVenta().getIdVenta()).orElseThrow();
            venta.setMetodoPago(metodoPago);
            venta.setPagado(true);
            venta.setPasoActual(5);
            ventaRepository.save(venta);

        } catch (MPApiException e) {
            System.err.println("Error procesando webhook de Mercado Pago: " + e.getMessage());
            var apiResponse = e.getApiResponse();
            var content = apiResponse.getContent();
            System.out.println(content);
            e.printStackTrace();
        } catch (MPException e) {
            throw new RuntimeException(e);
        }
    }

}
