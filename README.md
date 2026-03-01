# Building a CRUD application with Spring and SimpleJdbcMapper
Spring Framework's JDBC core package, designed to simplify database interactions using JDBC, is a popular option for applications to persist data to a relational database. The central classes used are JdbcClient with its fluent API and JdbcTemplate with the older classic API.

When using these APIs, the CRUD operations tend to be verbose. The SimpleJdbcMapper mitigates this verbosity and also stays out of the way so you can keep using all the features of JdbcClient/JdbcTemplate. 

# Features

  1. One liners for CRUD.
  2. Auto assign properties for models:
      * auto assign created on, updated on.
      * auto assign created by, updated by.
      * optimistic locking feature for updates.
  3. Helper methods to get the SQL for the mapped objects that can be used with Spring row mappers like BeanPropertyRowMapper, SimplePropertyRowMapper etc.
  4. For transaction management use Spring transactions since its just a wrapper library.
  5. To log the SQL statements use the same SQL logging configurations as Spring. See the logging section further below.
  6. Tested against PostgreSQL, MySQL, Oracle, SQLServer. Should work with other databases.
  7. Only dependency is Spring JDBC libraries. No other external dependencies.

# Example CRUD Operations With SimpleJdbcMapper

```
@Table(name = "product")
public class Product {
	@Id(type = IdType.AUTO_GENERATED)
	private Integer Id;

	@Column
	private String sku;

	@Column(name = "product_name")
	private String name;

	@Column
	private String description;

	@Column
	private Double cost;

	private String nonDatabaseColumn;
	
	...
}


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
		// appropriate column aliases. Note that Product model property 'name' is
		// mapped to 'product_name' column
		String sql = "SELECT " + sjm.getBeanFriendlySqlColumns(Product.class) + " FROM product WHERE sku = ? ";

		// Using Spring's JdbcClient api for sql above.
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

```



## JDK and Spring version requirements

JDK **21+**

SpringBoot **3.2.3+** or Spring framework **6.1.4+**
 
## Maven coordinates

 ``` 
  <dependency>
    <groupId>io.github.spring-jdbc-crud</groupId>
    <artifactId>simplejdbcmapper</artifactId>
    <version>1.6.3</version>
 </dependency>
 ```
 
## Configuration

```
# application.properties
# H2 database configuration. For other database configurations see SimpleJdbcMapper documentation at:
# https://github.com/spring-jdbc-crud/SimpleJdbcMapper#spring-bean-configuration-for-simplejdbcmapper
spring.datasource.jdbc-url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

...

@Component
public class SimpleJdbcMapperConfig {
    // see application.properties for spring.datasource configuration
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource sqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SimpleJdbcMapper simpleJdbcMapper(DataSource dataSource) {
        // This configuration is for H2 database.
        // See SimpleJdbcMapper documentation for other database configurations.
        return new SimpleJdbcMapper(dataSource);
    }
}

```

## Accessing JdbcClient/JdbcTemplate
``` 
 JdbcClient jdbcClient = sjm.getJdbcClient();
 JdbcTemplate jdbcTemplate = sjm.getJdbcTemplate();
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = sjm.getNamedParameterJdbcTemplate();
```
You can also create your own JdbcClient/JdbcTemplate and use it. 


## Logging
 
Uses the same logging configurations as Spring. In application.properties:
 
 ``` 
 
 # log the SQL
 logging.level.org.springframework.jdbc.core.JdbcTemplate=TRACE
 
 # need this to log the INSERT statements
 logging.level.org.springframework.jdbc.core.simple.SimpleJdbcInsert=TRACE
 
 # log the parameters of SQL statement
 logging.level.org.springframework.jdbc.core.StatementCreatorUtils=TRACE

 ```
 
## Building and running the demo application
 
 Note that the demo requires **java21+**.

You can build and run the application with the following 3 steps. No other configuration is needed since the application goes against an embedded h2 database. When you run the application you should see all the SQL being issued on the console.

**1.Get source code**      
Clone the repository or use the 'Download ZIP' option by clicking on the 'code' dropdown button on this screen.

**2.Build the application**    
This step could take some time because the dependency files have to be downloaded

```
# For Unix/Mac 
./mvnw clean install  

# For windows
mvnw.cmd clean install
```
**3.Run the application**      
When you run the application you should see all the SQL being issued on the console.

```
# For Unix/Mac 
./mvnw spring-boot:run

# For windows
mvnw.cmd spring-boot:run
```

The code for CRUD is in class [CrudService.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/CrudService.java).

The model [Product.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/Product.java).

The configuration is in class [SimpleJdbcMapperConfig.java](src/main/java/io/github/springcrudsimplejdbcmapper/config/SimpleJdbcMapperConfig.java)

Github project for [SimpleJdbcMapper](https://github.com/spring-jdbc-crud/simplejdbcmapper) library.

 

