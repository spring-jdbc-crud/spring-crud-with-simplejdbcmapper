package io.github.springcrudsimplejdbcmapper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Because it implements interface CommandLineRunner the 'run' method is
 * executed on Spring application startup
 */
@Component
public class DemoCommandLineRunner implements CommandLineRunner {
	@Autowired
	DemoService demoService;

	@Override
	public void run(String... args) throws Exception {
		demoService.runCrud();
	}
}
