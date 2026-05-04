package io.github.springcrudsimplejdbcmapper.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.github.springcrudsimplejdbcmapper.service.DemoService;

/**
 * Since it implements interface CommandLineRunner the 'run' method is executed
 * on Spring application startup
 */
@Component
public class DemoCommandLineRunner implements CommandLineRunner {
	@Autowired
	DemoService demoService;

	@Override
	public void run(String... args) throws Exception {
		demoService.crud();

		demoService.findByProperty();

		demoService.customQueriesForSingleEntity();

		demoService.toManyRelationship();

		demoService.multipleRelationshipsToOneToManyWithSingleQuery();

		demoService.toManyThroughAnIntermediateTable();

		demoService.populatingRelationshipsFromMultipleQueries();

	}
}
