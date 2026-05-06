package io.github.springcrudsimplejdbcmapper.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.simplejdbcmapper.core.MultiEntity;
import io.github.simplejdbcmapper.core.SimpleJdbcMapper;
import io.github.simplejdbcmapper.core.SortBy;
import io.github.simplejdbcmapper.relationship.RelationshipMapper;
import io.github.springcrudsimplejdbcmapper.model.Employee;
import io.github.springcrudsimplejdbcmapper.model.EmployeeSkill;
import io.github.springcrudsimplejdbcmapper.model.Order;
import io.github.springcrudsimplejdbcmapper.model.OrderLine;
import io.github.springcrudsimplejdbcmapper.model.Product;
import io.github.springcrudsimplejdbcmapper.model.Skill;
import tools.jackson.databind.json.JsonMapper;

@Service
public class DemoService {
	@Autowired
	JsonMapper jsonMapper;
	private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

	@Autowired
	private SimpleJdbcMapper sjm;

	public void crud() {
		logger.info("============================================================================================");
		logger.info("======================= insert =============================================================");
		logger.info("============================================================================================");

		Product p = new Product();
		p.setSku("sku-2001");
		p.setCost(9.25);
		p.setDescription("some candy");
		p.setName("candy");
		// auto assigns id on insert since id configured as auto generated
		sjm.insert(p);
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p));

		logger.info("============================================================================================");
		logger.info("======================= findById                     =======================================");
		logger.info("============================================================================================");

		p = sjm.findById(Product.class, p.getId());
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p));

		logger.info("============================================================================================");
		logger.info("======================= update                       =======================================");
		logger.info("============================================================================================");

		p.setDescription("some other candy");
		// issues an update for the full object
		sjm.update(p);
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p));

		logger.info("============================================================================================");
		logger.info("========updateSpecificProperties in this case 'cost' property only ==========================");
		logger.info("============================================================================================");

		p.setCost(11.99);
		// sql update will be issued only for cost field.
		sjm.updateSpecificProperties(p, "cost");
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p));

		logger.info("============================================================================================");
		logger.info("======================= findAll with Sort            =======================================");
		logger.info("============================================================================================");

		// Using SortBy to generate the "ORDER BY" clause. Use it similarly with method
		// signatures which have varargs SortBy
		List<Product> products = sjm.findAll(Product.class, new SortBy("cost", "DESC"), new SortBy("name"));
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products));
	}

	public void findByProperty() {
		logger.info("============================================================================================");
		logger.info("======================= findByPropertyValue()        =======================================");
		logger.info("============================================================================================");

		List<Product> products = sjm.findByPropertyValue(Product.class, "sku", "sku#1001");
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products));

		logger.info("============================================================================================");
		logger.info("======================= findByPropertyValues()        ======================================");
		logger.info("============================================================================================");

		String[] skus = { "sku#1001", "sku#1002" };
		List<Product> list = sjm.findByPropertyValues(Product.class, "sku", Arrays.asList(skus));
		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list));
	}

	public void customQueriesForSingleEntity() {
		logger.info("============================================================================================");
		logger.info("=========== Custom query for a single entity         =======================================");
		logger.info("============================================================================================");

		/*
		 * For custom queries use getEntitySqlColumns() to get the columns sql and use
		 * it with EntityRowMapper (please refer to its javadocs. EntityRowMapper added
		 * in v2.2.0). Note in this case the 'name' property is mapped to the
		 * 'product_name' column.
		 */
		String sql = "SELECT " + sjm.getEntitySqlColumns(Product.class) + " FROM product WHERE sku = ? ";

		// Using Spring's JdbcTemplate api for sql above
		List<Product> products = sjm.getJdbcTemplate().query(sql, sjm.newEntityRowMapper(Product.class), "sku#1001");

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products));

	}

	public List<Order> toManyRelationship() {
		logger.info("============================================================================================");
		logger.info("======================= Order toMany OrderLine  ============================================");
		logger.info("============================================================================================");

		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol");

		String sql = """
				SELECT %s
				FROM orders o
				LEFT JOIN order_line ol ON  o.id = ol.order_id
				WHERE o.total_amount >= ?
				ORDER BY o.id, ol.id
				""".formatted(sjm.getMultiEntitySqlColumns(multiEntity));

		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity),
				0);

		List<Order> orders = relationshipMapper.type(Order.class).toMany(OrderLine.class).joinOn("id", "orderId")
				.populate("orderLines").getList(Order.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders));

		return orders;
	}

	public List<Order> multipleRelationshipsToOneToManyWithSingleQuery() {
		logger.info(
				"==============================================================================================================");
		logger.info(
				"=== Populating multiple relationships with a single query. Order toMany OrderLine, OrderLine toOne Product ===");
		logger.info(
				"==============================================================================================================");

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

		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity),
				0);

		relationshipMapper.type(OrderLine.class).toOne(Product.class).joinOn("productId", "id").populate("product");
		List<Order> orders = relationshipMapper.type(Order.class).toMany(OrderLine.class).joinOn("id", "orderId")
				.populate("orderLines").getList(Order.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders));

		return orders;

	}

	public List<Employee> toManyThroughAnIntermediateTable() {
		logger.info("============================================================================================");
		logger.info("=== Employee toMany Skill through intermediate table employee_skill ========================");
		logger.info("============================================================================================");

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

		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity));

		List<Employee> employees = relationshipMapper.type(Employee.class).toMany(Skill.class)
				.through(EmployeeSkill.class, "employeeId", "skillId").populate("skills").getList(Employee.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employees));

		return employees;
	}

	public List<Order> populatingRelationshipsFromMultipleQueries() {
		logger.info("============================================================================================");
		logger.info("=== populating relationships using multiple queries ==========================================");
		logger.info("============================================================================================");

		// first query
		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol");
		String sql = """
				   SELECT %s
				   FROM orders o
				   LEFT JOIN order_line ol ON  o.id = ol.order_id
				   WHERE o.total_amount >= ?
				   ORDER BY o.order_date DESC, ol.id
				""".formatted(sjm.getMultiEntitySqlColumns(multiEntity));
		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity),
				0);

		// the the orderLines so we can get its related productId
		List<OrderLine> orderLines = relationshipMapper.getList(OrderLine.class);

		// get the productId list from orderLines list
		List<Integer> productIdList = orderLines.stream().map(OrderLine::getProductId).toList();

		// Second query. findByPropertyValues() uses an IN clause so even if there are
		// duplicate product ids we are fine.
		List<Product> products = sjm.findByPropertyValues(Product.class, "id", productIdList);

		// add products to the relationship mapper
		relationshipMapper.addEntityResult(Product.class, products, "id");

		// The toOne relationship populates orderLine.product.
		relationshipMapper.type(OrderLine.class).toOne(Product.class).joinOn("productId", "id").populate("product");

		// The toMany relationship populates order.orderLines
		List<Order> orders = relationshipMapper.type(Order.class).toMany(OrderLine.class).joinOn("id", "orderId")
				.populate("orderLines").getList(Order.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders));
		return orders;
	}

}
