package io.github.springcrudsimplejdbcmapper.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.github.simplejdbcmapper.core.SimpleJdbcMapper;

@Component
public class SimpleJdbcMapperConfig {
	// see application.properties for spring.datasource configuration
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource sqlDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public SimpleJdbcMapper simpleJdbcMapper(DataSource dataSource) {
		// This configuration is for H2 database.
		// See SimpleJdbcMapper documentation for other database configurations.
		return new SimpleJdbcMapper(dataSource);
	}
}
