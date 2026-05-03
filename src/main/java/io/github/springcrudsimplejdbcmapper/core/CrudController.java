package io.github.springcrudsimplejdbcmapper.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.simplejdbcmapper.core.SimpleJdbcMapper;

@RestController
public class CrudController {
	@Autowired
	SimpleJdbcMapper sjm;

	@Autowired
	CrudService crudService;

	@GetMapping("/tomany")
	List<Order> toMany() {
		return crudService.toManyRelationship();
	}

	@GetMapping("/tomany-through")
	List<Employee> toManyThrough() {
		return crudService.toManyThrough();
	}

	@GetMapping("/multiple-relationships")
	List<Order> multipleRelationships() {
		return crudService.multipleRelationshipsWithSingleQuery();
	}

	@GetMapping("/mixandmatch")
	List<Order> mixandmatch() {
		return crudService.mixAndMatchRelationshipFromMultipleQueries();
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
