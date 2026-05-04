package io.github.springcrudsimplejdbcmapper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.simplejdbcmapper.core.SimpleJdbcMapper;
import io.github.springcrudsimplejdbcmapper.model.Employee;
import io.github.springcrudsimplejdbcmapper.model.Order;
import io.github.springcrudsimplejdbcmapper.model.Product;
import io.github.springcrudsimplejdbcmapper.service.DemoService;

@RestController
public class CrudController {
	@Autowired
	SimpleJdbcMapper sjm;

	@Autowired
	DemoService demoService;

	@GetMapping("/tomany")
	List<Order> toMany() {
		return demoService.toManyRelationship();
	}

	@GetMapping("/tomany-through")
	List<Employee> toManyThrough() {
		return demoService.toManyThroughAnIntermediateTable();
	}

	@GetMapping("/multiple-relationships")
	List<Order> multipleRelationships() {
		return demoService.multipleRelationshipsToOneToManyWithSingleQuery();
	}

	@GetMapping("/mixandmatch")
	List<Order> mixandmatch() {
		return demoService.populatingRelationshipsFromMultipleQueries();
	}

	@GetMapping("/products")
	List<Product> all() {
		return sjm.findAll(Product.class);
	}

	@GetMapping("/products/{id}")
	Product getProduct(@PathVariable Integer id) {
		Product p = sjm.findById(Product.class, id);
		return p;
	}

}
