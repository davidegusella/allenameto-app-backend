package com.palestra.allenamentoapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // Abilita chiamate per tutte le API ( /api/** )
        registry.addMapping("/api/**")
                .allowedOrigins("172.16.229.76:8080")  // Indica il dominio del frontend (modifica se necessario)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Specifica i metodi HTTP permessi
                .allowedHeaders("*")  // Permette tutti gli header
                .allowCredentials(true);  // Permette l'invio di credenziali (ad esempio, i cookie o l'autenticazione)
    }
}
//