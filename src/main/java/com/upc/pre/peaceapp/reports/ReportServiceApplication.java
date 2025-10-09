package com.upc.pre.peaceapp.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.upc.pre.peaceapp.reports",
        "com.upc.pre.peaceapp.shared.documentation"
})
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
public class ReportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportServiceApplication.class, args);
    }

}
