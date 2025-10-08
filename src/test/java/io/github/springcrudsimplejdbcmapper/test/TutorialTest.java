package io.github.springcrudsimplejdbcmapper.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.simplejdbcmapper.core.SimpleJdbcMapper;
import io.github.springcrudsimplejdbcmapper.model.Product;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TutorialTest {

	@Autowired
	private SimpleJdbcMapper sjm;

	@Test
	void tutorial_test() {
		Product p = new Product();
		p.setName("Shoes");
		p.setSku("sku-1");
		p.setCost(10.25);

		// auto assigns id on insert since id configured as auto generated
		sjm.insert(p);
		assertNotNull(p.getId());

		p = sjm.findById(Product.class, p.getId());
		assertEquals("Shoes", p.getName());
		assertEquals("sku-1", p.getSku());
		assertEquals(10.25, p.getCost());

		List<Product> products = sjm.findAll(Product.class);
		assertTrue(products.size() > 0);

		p.setDescription("Shoe description");
		// issues an update for the full object
		sjm.update(p);

		p = sjm.findById(Product.class, p.getId());
		assertEquals("Shoe description", p.getDescription());

		p.setCost(11.99);
		p.setDescription("New Description");
		// sql update will be issued only for these 2 specified fields.
		sjm.updateSpecificProperties(p, "cost", "description");

		p = sjm.findById(Product.class, p.getId());
		assertEquals(11.99, p.getCost());
		assertEquals("New Description", p.getDescription());

		// getBeanFriendlySqlColumns() creates a string for the sql columns with
		// appropriate aliases. Note that in the Product model property 'name' is
		// mapped to 'product_name' column
		String sql = "SELECT " + sjm.getBeanFriendlySqlColumns(Product.class) + " FROM product WHERE sku = ? ";

		// Using Spring's JdbcClient api for sql above.
		List<Product> productList = sjm.getJdbcClient().sql(sql).param("sku-1").query(Product.class).list();
		assertTrue(productList.size() > 0);
		assertEquals("Shoes", productList.get(0).getName());
		assertEquals("sku-1", productList.get(0).getSku());

		// Using Spring's JdbcTemplate api for sql above
		List<Product> productList2 = sjm.getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Product.class),
				"sku-1");
		assertTrue(productList2.size() > 0);
		assertEquals("Shoes", productList2.get(0).getName());
		assertEquals("sku-1", productList2.get(0).getSku());

	}
}
