# The demo application for SimpleJdbcMapper
This is a demo application for [SimpleJdbcMapper](https://github.com/spring-jdbc-crud/simplejdbcmapper), a library that simplifies Spring JdbcTemplate/JdbcClient CRUD operations and relationship queries by making them less verbose.


The application requires **java21+**.

The application has an embedded h2 database populated with table data. No configuration is required.  When you run the application you should see all the SQL being issued on the console and the related objects printed out in JSON format.


**1.Get source code**      
Clone the repository or use the 'Download ZIP' option by clicking on the 'code' dropdown button on this screen.

**2.Build the application**    
This will download the dependency files and then build the application.

```
# For Unix/Mac 
./mvnw clean install  

# For windows
mvnw.cmd clean install
```
**3.Run the application**      
When you run the application you should see all the SQL being issued on the console and the related objects printed out in JSON format.

```
# For Unix/Mac 
./mvnw spring-boot:run

# For windows
mvnw.cmd spring-boot:run
```

The example code is in class [DemoService.java](src/main/java/io/github/springcrudsimplejdbcmapper/service/DemoService.java).


The configuration is in class [SimpleJdbcMapperConfig.java](src/main/java/io/github/springcrudsimplejdbcmapper/config/SimpleJdbcMapperConfig.java)



