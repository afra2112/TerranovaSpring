package com.proyecto.terranova.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${MERCADO_PAGO_ACCESS_TOKEN}")
    private String accesToken;

    public String crearPreferencia() throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accesToken);

        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id("1")
                        .title("Compra De Ganado")
                        .description("descripcion prueba")
                        .quantity(1)
                        .currencyId("COP")
                        .unitPrice(new BigDecimal("5000000"))
                        .build();

        List<PreferenceItemRequest> items = new ArrayList<>();

        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items).build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }
}
