package com.fondosBTG.configuraciones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

/**
 * Configuración de AWS SNS (Simple Notification Service) para la aplicación.
 *
 * @author Guillermo Ramirez
 */
@Configuration
public class SnsConfig {

    private final String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
    private final String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");

    /**
     * Crea un bean SnsClient configurado con credenciales de AWS.
     *
     * @return Un cliente SNS configurado que se puede utilizar para interactuar con el servicio de SNS de AWS.
     */
    @Bean
    public SnsClient snsClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return SnsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.US_EAST_1)
                .build();
    }
}
