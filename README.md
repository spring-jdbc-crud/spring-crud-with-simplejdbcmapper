# Using SimpleJdbcMapper With Spring #
This is a demo application on how to use SimpleJdbcMapper with Spring. SimpleJdbMapper is a wrapper around Spring JDBC libraries that makes database CRUD operations less verbose.

No configuration is needed since application goes against an embedded h2 database.

Note that the demo requires **java21+**.

Get the source code by cloning the repository or using the 'Download ZIP' option by clicking on the 'code' dropdown button on this screen.
Once you have the source code, use following commands to install and run the application. When you run the application you should see all the SQL being issued on the console.

```
# For Unix/Mac 
./mvnw clean install
./mvnw spring-boot:run

# For windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run

```


The example code for this demo is in class [DemoService.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/DemoService.java).

The model [Product.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/Product.java).

The configuration is in class [SimpleJdbcMapperConfig.java](src/main/java/io/github/springcrudsimplejdbcmapper/config/SimpleJdbcMapperConfig.java)

Github project for [SimpleJdbcMapper](https://github.com/spring-jdbc-crud/simplejdbcmapper) library.


