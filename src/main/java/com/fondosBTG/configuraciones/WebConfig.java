package com.fondosBTG.configuraciones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la aplicación. Permite solicitudes desde dominios específicos a través de los métodos HTTP
 * especificados.
 *
 * @author Guillermo Ramirez
 */
@Configuration
public class WebConfig {

    /**
     * Configura las reglas de CORS para la aplicación.
     *
     * @return un bean de {@link WebMvcConfigurer} que define la política de CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new StaticCorsConfigurer();
    }

    /**
     * Clase interna estática para configurar CORS.
     */
    static class StaticCorsConfigurer implements WebMvcConfigurer {

        /**
         * Configura los mapeos de CORS para permitir solicitudes desde las URLs y métodos
         * especificados.
         *
         * @param registry el registro de CORS donde se añaden los mapeos.
         */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://039612874504-app-bucket.s3-website-us-east-1.amazonaws.com",
                            "http://localhost:4200")
                    // Cambia "*" por las URLs específicas permitidas
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*");
        }
    }
}
