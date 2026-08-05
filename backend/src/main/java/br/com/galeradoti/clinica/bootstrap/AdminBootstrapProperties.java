package br.com.galeradoti.clinica.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.bootstrap.admin")
public record AdminBootstrapProperties(
    boolean enabled,
    String name,
    String email,
    String password
) {
}