package com.bbidoleMarket.bbidoleMarket.api.image.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GCPConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentialsPath;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        // classpath: 접두어 제거
        String resourcePath = credentialsPath.replace("classpath:", "");

        // 클래스패스에서 자격 증명 파일 로드
        ClassPathResource resource = new ClassPathResource(resourcePath);
        InputStream inputStream = resource.getInputStream();

        // 자격 증명 및 스토리지 옵션 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}