package io.github.springcrudsimplejdbcmapper.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * This creates new database table each time the application runs.
 */

@Component
public class FlywayMigrationConfig {

	@Bean
	public static FlywayMigrationStrategy cleanMigrateStrategy() {
		return new FlywayMigrationStrategy() {
			@Override
			public void migrate(Flyway flyway) {
				flyway.clean();
				flyway.migrate();
			}
		};
	}
}
