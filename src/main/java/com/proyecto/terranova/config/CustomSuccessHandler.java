package com.proyecto.terranova.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        boolean esComprador = roles.stream().anyMatch(r -> r.getAuthority().equals("COMPRADOR"));
        boolean esVendedor = roles.stream().anyMatch(r -> r.getAuthority().equals("VENDEDOR"));

        if(esVendedor && esComprador){
            response.sendRedirect("/ambos/index");
        } else if (esVendedor) {
            response.sendRedirect("/vendedor/index");
        } else if (esComprador) {
            response.sendRedirect("/comprador/index");
        } else {
            response.sendRedirect("/usuarios/login");
        }
    }
}
