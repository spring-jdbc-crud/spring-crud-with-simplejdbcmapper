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
		System.out.println("Product classloader:" + Product.class.getClassLoader());
		return sjm.findAll(Product.class);
	}

	@GetMapping("/products/{id}")
	Product getProduct(@PathVariable Integer id) {
		System.out.println("SimpleJdbcMapper classloader:" + sjm.getClass().getClassLoader());

		Product p = sjm.findById(Product.class, id);
		System.out.println("Product classloader:" + p.getClass().getClassLoader());
		return p;
	}
}
