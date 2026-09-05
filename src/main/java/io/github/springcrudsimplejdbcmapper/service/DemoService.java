package io.github.springcrudsimplejdbcmapper.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import io.github.simplejdbcmapper.core.MultiEntity;
import io.github.simplejdbcmapper.core.SimpleJdbcMapper;
import io.github.simplejdbcmapper.core.SortBy;
import io.github.simplejdbcmapper.relationship.Relationship;
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
		logger.info("======== updateSpecificProperties in this case 'cost' property only ==========================");
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
		 * For custom queries use geSqlColumns() to get the columns sql and use it with
		 * EntityRowMapper. EntityRowMapper is the recommended row mapper for
		 * SimpleJdbcMapper. Note in this case the 'name' property is mapped to the
		 * 'product_name' column.
		 */
		String sql = "SELECT " + sjm.getSqlColumns(Product.class) + " FROM product WHERE sku = ? ";

		// Using Spring's JdbcTemplate api for sql above
		List<Product> products = sjm.getJdbcTemplate().query(sql, sjm.entityRowMapper(Product.class), "sku#1001");

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products));

	}

	public void toManyRelationship() {
		logger.info("============================================================================================");
		logger.info("======================= Relationshp: Order toMany OrderLine  ===============================");
		logger.info("============================================================================================");

		// Define the multiple mapped entities you want to select. Make sure the table
		// aliases match that in query.
		// Mapped Class | Table |Alias
		// ------------------------------------
		// Order.class | orders | "o"
		// OrderLine.class | order_line | "ol"
		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol");

		// Get the columns for your 'SELECT' using getSqlColumns().
		// Using java String blocks makes the queries more readable.
		String sql = """
				SELECT %s
				FROM orders o
				LEFT JOIN order_line ol ON  o.id = ol.order_id
				WHERE o.total_amount >= ?
				ORDER BY o.id, ol.id
				""".formatted(sjm.getSqlColumns(multiEntity));

		// Use the library ResultSetExtractor with JdbcTemplate to extract the
		// results. RelationshipMapper holds the query results
		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity),
				0);

		// define the relationship. Note that Relationship is thread safe and so it can
		// be used
		// with different query results which have the same relationship.
		Relationship orderToManyOrderLine = Relationship.type(Order.class).toMany(OrderLine.class)
				.joinOn("id", "orderId").populate("orderLines");

		// Assemble the relationship and getList() returns the orders
		List<Order> orders = relationshipMapper.assemble(orderToManyOrderLine).getList(Order.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders));

	}

	public void multipleRelationshipsToOneToManyWithSingleQuery() {
		logger.info(
				"==============================================================================================================");
		logger.info(
				"=== Populating multiple relationships with a single query. Order toMany OrderLine, OrderLine toOne Product ===");
		logger.info(
				"==============================================================================================================");

		// Define your entities. The aliases should exactly match the aliases used in
		// the query.
		MultiEntity multiEntity = new MultiEntity().add(Order.class, "o").add(OrderLine.class, "ol").add(Product.class,
				"p");

		// build your custom sql using the columns sql from
		// sjm.getSqlColumns(multiEntity)
		String sql = """
				SELECT %s
				FROM orders o
				LEFT JOIN order_line ol ON  o.id = ol.order_id
				LEFT JOIN product p ON ol.product_id = p.id
				WHERE o.total_amount >= ?
				ORDER BY o.id, ol.id
				""".formatted(sjm.getSqlColumns(multiEntity));

		// Use JdbcTemplate with the library extractor to execute the query and
		// extract results
		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity),
				0);

		// Define the toOne relationship between OrderLine and Product
		Relationship orderLineToOneProduct = Relationship.type(OrderLine.class).toOne(Product.class)
				.joinOn("productId", "id").populate("product");

		// Define the toMany relationship between Order and OrderLine
		Relationship orderToManyOrderLine = Relationship.type(Order.class).toMany(OrderLine.class)
				.joinOn("id", "orderId").populate("orderLines");

		// Assemble the relationships and getList() returns the orders
		List<Order> orders = relationshipMapper.assemble(orderLineToOneProduct, orderToManyOrderLine)
				.getList(Order.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders));

	}

	public void toManyThroughAnIntermediateTable() {
		logger.info("============================================================================================");
		logger.info("=== Relationship: Employee toMany Skill through intermediate table employee_skill ==========");
		logger.info("============================================================================================");

		// Define the entities. The intermediate table employe_skill (in this case
		// corresponds to EmpolyeeSkill class) needs to be selected also.
		MultiEntity multiEntity = new MultiEntity().add(Employee.class, "emp").add(EmployeeSkill.class, "es")
				.add(Skill.class, "s");

		// build your custom sql using the columns sql from
		// sjm.getSqlColumns(multiEntity)
		String sql = """
				SELECT %s
				FROM employee emp
				LEFT JOIN  employee_skill es ON emp.id = es.employee_id
				LEFT JOIN skill s ON es.skill_id = s.id
				WHERE emp.id <= 4
				ORDER BY emp.id, s.name
				""".formatted(sjm.getSqlColumns(multiEntity));

		// Use JdbcTemplate with the library extractor to extract results for the
		// entities.
		RelationshipMapper relationshipMapper = sjm.getJdbcTemplate().query(sql, sjm.resultSetExtractor(multiEntity));

		// Define the toMany relationship between Employee and Skill through
		// intermediate class EmployeeSkill
		Relationship employeeToManySkillThrough = Relationship.type(Employee.class).toMany(Skill.class)
				.through(EmployeeSkill.class, "employeeId", "skillId").populate("skills");

		// Assemble the relationship and getList() returns the employees
		List<Employee> employees = relationshipMapper.assemble(employeeToManySkillThrough).getList(Employee.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employees));

	}

	public void populatingRelationshipsFromMultipleQueries() {
		logger.info("============================================================================================");
		logger.info("=== populating relationships using multiple queries ==========================================");
		logger.info("============================================================================================");

		String orderSql = """
				   SELECT %s
				   FROM orders
				   ORDER BY orders.id
				   OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
				""".formatted(sjm.getSqlColumns(Order.class));

		// For a single entity use EntityRowMapper with JdbcTemplate to get the results.
		List<Order> orders = sjm.getJdbcTemplate().query(orderSql, sjm.entityRowMapper(Order.class), 0, 10);

		// get the order id list
		List<Integer> orderIdList = orders.stream().map(Order::getId).toList();

		// 2nd query. For IN clauses we have to use a named parameter
		MultiEntity multiEntity = new MultiEntity().add(OrderLine.class, "ol").add(Product.class, "p");
		String sql = """
				   SELECT %s
				   FROM order_line ol
				   LEFT JOIN product p ON ol.product_id = p.id
				   WHERE ol.order_id IN (:orderIdList)
				   ORDER BY ol.id
				""".formatted(sjm.getSqlColumns(multiEntity));

		MapSqlParameterSource param = new MapSqlParameterSource().addValue("orderIdList", orderIdList);
		// Since the query has a named parameter we are using NamedParameterJdbcTemplate
		// for this query
		RelationshipMapper relationshipMapper = sjm.getNamedParameterJdbcTemplate().query(sql, param,
				sjm.resultSetExtractor(multiEntity));

		// add orders to the relationshipMapper so that we can build a relationship
		// from it.
		relationshipMapper.addEntityResult(Order.class, orders, "id");

		// Define the toOne relationship between OrderLine and Product
		Relationship orderLineToOneProduct = Relationship.type(OrderLine.class).toOne(Product.class)
				.joinOn("productId", "id").populate("product");

		// Define the toMany relationship between Order and OrderLine
		Relationship orderToManyOrderLine = Relationship.type(Order.class).toMany(OrderLine.class)
				.joinOn("id", "orderId").populate("orderLines");

		// Assemble the relationships and getList() returns the orders
		orders = relationshipMapper.assemble(orderLineToOneProduct, orderToManyOrderLine).getList(Order.class);

		logger.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders));
	}

}
