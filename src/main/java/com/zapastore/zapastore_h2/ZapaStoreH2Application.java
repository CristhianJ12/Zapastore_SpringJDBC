package com.zapastore.zapastore_h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement; // 👈 NUEVO

@SpringBootApplication
@EnableTransactionManagement // 👈 ESTO HABILITA EL @Transactional EN LOS SERVICIOS
public class ZapaStoreH2Application {

    public static void main(String[] args) {
        SpringApplication.run(ZapaStoreH2Application.class, args);
    }
}