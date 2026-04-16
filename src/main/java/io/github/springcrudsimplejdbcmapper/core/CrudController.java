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

	@GetMapping("/products")
	List<Product> all() {
		System.out.println("SimpleJdbcMapper classloader:" + SimpleJdbcMapper.class.getClassLoader());
		System.out.println("Product classloader:" + Product.class.getClassLoader());
		return sjm.findAll(Product.class);
	}

	@GetMapping("/products/{id}")
	Product getProduct(@PathVariable Integer id) {

		return sjm.findById(Product.class, id);
	}
}
