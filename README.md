# Building a CRUD application with Spring and SimpleJdbcMapper
This is a demo application on how to use SimpleJdbcMapper with Spring. SimpleJdbMapper is a wrapper around Spring JDBC libraries that makes database CRUD operations less verbose.

Note that the demo requires **java21+**.

Get the source code by cloning the repository or using the 'Download ZIP' option by clicking on the 'code' dropdown button on this screen.
Once you have the source code, use following commands to install and run the application. The 'install' step could take some time because maven may have to download a bunch of files. No configuration is needed to run the demo application since it goes against an embedded h2 database.

When you run the application you should see all the SQL being issued on the console.

```
# For Unix/Mac 
./mvnw clean install
./mvnw spring-boot:run

# For windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run

```


The code for CRUD is in class [CrudService.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/CrudService.java).

The model [Product.java](src/main/java/io/github/springcrudsimplejdbcmapper/core/Product.java).

The configuration is in class [SimpleJdbcMapperConfig.java](src/main/java/io/github/springcrudsimplejdbcmapper/config/SimpleJdbcMapperConfig.java)

Github project for [SimpleJdbcMapper](https://github.com/spring-jdbc-crud/simplejdbcmapper) library.


