package io.github.springcrudsimplejdbcmapper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Since it implements interface CommandLineRunner the 'run' method is executed
 * on Spring application startup
 */
@Component
public class DemoCommandLineRunner implements CommandLineRunner {
	@Autowired
	CrudService crudService;

	@Override
	public void run(String... args) throws Exception {
		crudService.crud();
	}
}
