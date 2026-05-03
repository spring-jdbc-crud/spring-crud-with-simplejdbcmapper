package io.github.springcrudsimplejdbcmapper.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import io.github.simplejdbcmapper.core.MultiEntity;
import io.github.simplejdbcmapper.core.ResultListMap;
import io.github.simplejdbcmapper.core.SimpleJdbcMapper;
import io.github.simplejdbcmapper.core.SortBy;
import io.github.simplejdbcmapper.relationship.Relationship;

@Service
public class CrudService {

	@Autowired
	private SimpleJdbcMapper sjm;

	public void crud() {

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
		assertTrue(!products.isEmpty());

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

		/*
		 * For custom queries use getBeanFriendlySqlColumns() to get the columns sql. It
		 * creates the appropriate column aliases when the column name does not match
		 * the corresponding underscore case property name. This allows the usage of
		 * Spring row mappers like BeanPropertyRowMapper, SimplePropertyRowMapper etc
		 * instead of writing custom row mappers. Note in this case the 'name' property
		 * is mapped to the 'product_name' column.
		 */
		String sql = "SELECT " + sjm.getBeanFriendlySqlColumns(Product.class) + " FROM product WHERE sku = ? ";

		// Using Spring's JdbcClient api for sql above. JdbcClient is using
		// SimplePropertyRowMapper internally
		List<Product> productList = sjm.getJdbcClient().sql(sql).param("sku-1").query(Product.class).list();
		assertTrue(!productList.isEmpty());
		assertEquals("Shoes", productList.get(0).getName());
		assertEquals("sku-1", productList.get(0).getSku());

		// Using Spring's JdbcTemplate api for sql above
		List<Product> productList2 = sjm.getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Product.class),
				"sku-1");
		assertTrue(!productList2.isEmpty());
		assertEquals("Shoes", productList2.get(0).getName());
		assertEquals("sku-1", productList2.get(0).getSku());

		String sql2 = "SELECT " + sjm.getEntitySqlColumns(Product.class) + " FROM product WHERE sku = ? ";

		List<Product> list1 = sjm.getJdbcTemplate().query(sql2, sjm.newEntityRowMapper(Product.class), "sku-1");
		assertTrue(!list1.isEmpty());
		assertEquals("Shoes", list1.get(0).getName());
		assertEquals("sku-1", list1.get(0).getSku());

		List<Product> productList3 = sjm.findByPropertyValue(Product.class, "sku", "sku-1");
		assertEquals(1, productList3.size());

		// insert 2nd product and do findByPropertyValues()
		Product p2 = new Product();
		p2.setName("Shoes2");
		p2.setSku("sku-2");
		p2.setCost(12.00);
		sjm.insert(p2);

		String[] skus = { "sku-1", "sku-2" };
		List<Product> productList4 = sjm.findByPropertyValues(Product.class, "sku", Arrays.asList(skus));
		assertEquals(2, productList4.size());

		// Using SortBy to generate the "ORDER BY" clause. Use it similarly with method
		// signatures which have varargs SortBy
		List<Product> productList5 = sjm.findAll(Product.class, new SortBy("cost", "DESC"), new SortBy("name"));
		assertTrue(productList5.size() > 0);

	}

	public List<Order> toManyRelationship() {

		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol");

		String sql = """
				SELECT %s
				FROM orders o
				LEFT JOIN order_line ol ON  o.id = ol.order_id
				WHERE o.total_amount >= ?
				ORDER BY o.id, ol.id
				""".formatted(sjm.getMultiEntitySqlColumns(multiEntity));

		ResultListMap resultListMap = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity), 0);

		List<Order> orders = resultListMap.getList(Order.class);
		List<OrderLine> orderLines = resultListMap.getList(OrderLine.class);

		Relationship.mainList(orders).toManyList(orderLines).joinOn("id", "orderId").populate("orderLines");

		return orders;
	}

	public List<Order> multipleRelationshipsWithSingleQuery() {

		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol").add(Product.class,
				"p");

		String sql = """
				SELECT %s
				FROM orders o
				LEFT JOIN order_line ol ON  o.id = ol.order_id
				LEFT JOIN product p ON ol.product_id = p.id
				WHERE o.total_amount >= ?
				ORDER BY o.id, ol.id
				""".formatted(sjm.getMultiEntitySqlColumns(multiEntity));

		ResultListMap resultListMap = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity), 0);

		List<Order> orders = resultListMap.getList(Order.class);
		List<OrderLine> orderLines = resultListMap.getList(OrderLine.class);
		List<Product> products = resultListMap.getList(Product.class);

		Relationship.mainList(orders).toManyList(orderLines).joinOn("id", "orderId").populate("orderLines");
		Relationship.mainList(orderLines).toOneList(products).joinOn("productId", "id").populate("product");

		return orders;

	}

	public List<Employee> toManyThrough() {

		MultiEntity multiEntity = new MultiEntity().add(Employee.class, "emp").add(EmployeeSkill.class, "es")
				.add(Skill.class, "s");

		String sql = """
				SELECT %s
				FROM employee emp
				LEFT JOIN  employee_skill es ON emp.id = es.employee_id
				LEFT JOIN skill s ON es.skill_id = s.id
				WHERE emp.id <= 4
				ORDER BY emp.id, s.name
				""".formatted(sjm.getMultiEntitySqlColumns(multiEntity));

		ResultListMap resultListMap = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity));

		List<Employee> employees = resultListMap.getList(Employee.class);
		List<EmployeeSkill> employeeSkillList = resultListMap.getList(EmployeeSkill.class);
		List<Skill> skills = resultListMap.getList(Skill.class);

		Relationship.mainList(employees).toManyList(skills).through(employeeSkillList, "employeeId", "skillId")
				.ids("id", "id").populate("skills");

		return employees;

	}

	public List<Order> mixAndMatchRelationshipFromMultipleQueries() {
		// first query
		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol");
		String sql = """
				   SELECT %s
				   FROM orders o
				   LEFT JOIN order_line ol ON  o.id = ol.order_id
				   WHERE o.total_amount >= ?
				   ORDER BY o.order_date DESC, ol.id
				""".formatted(sjm.getMultiEntitySqlColumns(multiEntity));
		ResultListMap resultListMap = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity), 0);
		List<Order> orders = resultListMap.getList(Order.class);
		List<OrderLine> orderLines = resultListMap.getList(OrderLine.class);

		// get the productId list from orderLines list
		List<Integer> productIdList = orderLines.stream().map(OrderLine::getProductId).toList();

		// Second query. findByPropertyValues() uses an IN clause so even if there are
		// duplicate product ids we are fine.
		List<Product> products = sjm.findByPropertyValues(Product.class, "id", productIdList);

		// The toMany relationship populates order.orderLines
		Relationship.mainList(orders).toManyList(orderLines).joinOn("id", "orderId").populate("orderLines");
		// The toOne relationship populates orderLine.product.
		Relationship.mainList(orderLines).toOneList(products).joinOn("productId", "id").populate("product");

		return orders;
	}

}
