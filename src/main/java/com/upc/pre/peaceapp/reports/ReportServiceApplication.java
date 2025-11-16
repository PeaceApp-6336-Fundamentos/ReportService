package com.upc.pre.peaceapp.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.upc.pre.peaceapp.reports",
        "com.upc.pre.peaceapp.shared.documentation"
})
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@EntityScan(basePackages = "com.upc.pre.peaceapp.reports.domain.model.aggregates")
@EnableJpaRepositories(basePackages = "com.upc.pre.peaceapp.reports.infrastructure.persistence.jpa")
public class ReportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportServiceApplication.class, args);
    }
}
