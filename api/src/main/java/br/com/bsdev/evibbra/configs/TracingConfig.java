package br.com.bsdev.evibbra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.tracing.exporter.SpanExportingPredicate;

@Configuration
public class TracingConfig {

	@Bean
	SpanExportingPredicate noActuator() {
		return span -> span.getTags().get("uri") == null || !span.getTags().get("uri").startsWith("/actuator");
	}

	@Bean
	SpanExportingPredicate noSwagger() {
		return span -> span.getTags().get("uri") == null || !span.getTags().get("uri").startsWith("/swagger");
	}

	@Bean
	SpanExportingPredicate noApiDocs() {
		return span -> span.getTags().get("uri") == null || !span.getTags().get("uri").startsWith("/v3/api-docs");
	}
	
	@Bean
	SpanExportingPredicate noSecurity() {
		return span -> !span.getName().startsWith("security filterchain");
	}
}