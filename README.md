# Using SimpleJdbcMapper With Spring #
This is a demo application on how to use SimpleJdbcMapper with Spring. SimpleJdbMapper is a wrapper around Spring JDBC libraries that makes database CRUD operations less verbose.

Use git to clone the repository and then run it with maven. No configuration is needed since application goes against an embedded h2 database.

Note that the demo requires **java21+**.

```
# clone the repository
git clone https://github.com/spring-jdbc-crud/using-simplejdbcmapper-with-spring.git

# Use following command to run the application. You should see all the SQL being issued on the console.
# For Unix/Mac 
./mvnw clean install
./mvnw spring-boot:run

#for windows:
./mvnw.cmd clean install
./mvnw.cmd spring-boot:run

```
You will see all the SQL being issued on the console.



The example code for this demo is in class [DemoService.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/DemoService.java).

The model [Product.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/Product.java).

The configuration is in class [SimpleJdbcMapperConfig.java](src/main/java/io/github/springcrudsimplejdbcmapper/config/SimpleJdbcMapperConfig.java)

Github project for [SimpleJdbcMapper](https://github.com/spring-jdbc-crud/simplejdbcmapper) library.


