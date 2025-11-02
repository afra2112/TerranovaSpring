package com.proyecto.terranova.config;

import com.proyecto.terranova.implement.UserDetailServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    private UserDetailServiceImplement userDetailServiceImplement;

    @Autowired
    private PasswordEncoderConfig passwordEncoderConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login","/registro","/403", "/password-olvidada", "/recuperar-password", "/comprador/productos", "/oauth2/**").permitAll();
                    auth.requestMatchers("/comprador/**").hasAuthority("COMPRADOR");
                    auth.requestMatchers("/vendedor/**").hasAuthority("VENDEDOR");
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/login").permitAll();
                    login.loginProcessingUrl("/login");
                    login.defaultSuccessUrl("/comprador/explorar", true);
                    login.failureUrl("/login?error=true");
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout=true");
                })
                .exceptionHandling(ex -> {
                    ex.accessDeniedPage("/403");
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailServiceImplement)
                .passwordEncoder(passwordEncoderConfig.passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}
