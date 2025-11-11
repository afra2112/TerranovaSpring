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
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }

    public void procesarNotificacion(Map<String, Object> payload) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accesToken);

        String tipo = (String) payload.get("type");
        Map<String, Object> datos = (Map<String, Object>) payload.get("data");
        Long paymentId = Long.valueOf(datos.get("id").toString());

        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(paymentId);

        String estado = payment.getStatus();           //LOS ESTADOS PUEDEN SER ESTOS: approved, pending, rejected
        BigDecimal monto = payment.getTransactionAmount();
        String moneda = payment.getCurrencyId();
        String metodoPago = payment.getPaymentMethodId();
        String externalReference = payment.getExternalReference();
        Long idVenta = Long.valueOf(externalReference);

        Pago pago = new Pago();
        pago.setVenta(ventaRepository.findById(idVenta).orElseThrow());
        pago.setIdPaymentMercadoPago(paymentId);
        pago.setMontoPagado(monto.toBigInteger().longValue());
        pago.setMoneda(moneda);
        pago.setEstado(estado);
        pago.setMetodoPago(metodoPago);
        pago.setFechaRegistroPago(LocalDateTime.now());

        pagoRepository.save(pago);
    }
}
