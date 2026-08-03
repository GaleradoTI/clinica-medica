package br.com.galeradoti.clinica.health;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(
            Map.of(
                "status", "UP",
                "application", "clinica-medica-api",
                "timestamp", OffsetDateTime.now(ZoneOffset.UTC)
            )
        );
    }
}