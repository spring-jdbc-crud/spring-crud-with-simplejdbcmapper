# Building a CRUD application with Spring and SimpleJdbcMapper
This is a demo application on how to use SimpleJdbcMapper with Spring. SimpleJdbMapper is a wrapper around Spring JDBC libraries that makes database CRUD operations less verbose.

Note that the demo requires **java21+**.

Get the source code by cloning the repository or using the 'Download ZIP' option by clicking on the 'code' dropdown button on this screen. Once you have the source code, use following commands to install and run the application. No configuration is needed to run the demo application since it goes against an embedded h2 database. 

You can build and run the application with the following 3 steps. No other configuration is needed since applicationn goes against an embedded h2 database. When you run the application you should see all the SQL being issued on the console.

* 1) Get source code **  
Clone the repository or use the 'Download ZIP' option by clicking on the 'code' dropdown button on this screen.

** 2) Build the application **  
This step could take some time because the dependency files have to be downloaded

```
# For Unix/Mac 
./mvnw clean install  

# For windows
mvnw.cmd clean install # This step could take some time because the dependency files have to be downloaded

```
** 3) Run the application **  
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


