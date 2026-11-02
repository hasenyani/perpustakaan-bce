package id.ac.ub.filkom.contoh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Titik masuk aplikasi. @SpringBootApplication memindai seluruh sub-package:
 * boundary (UI), control (logika bisnis), entity + repository (data).
 */
@SpringBootApplication
public class PerpustakaanApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerpustakaanApplication.class, args);
    }
}
