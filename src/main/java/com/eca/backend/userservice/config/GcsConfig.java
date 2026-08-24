package com.eca.backend.userservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GcsConfig {

    private static final Logger logger = LoggerFactory.getLogger(GcsConfig.class);

    @Value("${gcp.storage.project-id:eca-enterprise-gcp-project}")
    private String projectId;

    @Bean
    public Storage googleCloudStorage() {
        try {
            logger.info("Initializing Google Cloud Storage client with ADC for GCP project: {}", projectId);
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build()
                    .getService();
        } catch (IOException e) {
            logger.warn("Application Default Credentials (ADC) not found locally: {}. Initializing unauthenticated / mock GCS client fallback.", e.getMessage());
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .build()
                    .getService();
        }
    }
}
