# Using SimpleJdbcMapper With Spring #
This is a example application on how to use SimpleJdbcMapper with Spring. SimpleJdbMapper is a wrapper around Spring JDBC libraries that makes database CRUD operations less verbose.

Use git to clone the repository and then run it with maven. No configuration is needed since application goes against an embedded h2 database.

If you plan to run it, it requires **java21+**, git and maven installed on your local.

```
# clone the repository
git clone https://github.com/spring-jdbc-crud/using-simplejdbcmapper-with-spring.git

# Use following command to run the application. You should see all the SQL being issued on the console.
mvn clean package
```
You will see all the SQL being issued on the console.



The example code for this tutorial is in class [TutorialTest.java](src/test/java/io/github/springcrudsimplejdbcmapper/test/TutorialTest.java).

The model [Product.java](src/test/java/io/github/springcrudsimplejdbcmapper/model/Product.java).

The configuration is in class [SimpleJdbcMapperConfig.java](src/test/java/io/github/springcrudsimplejdbcmapper/config/SimpleJdbcMapperConfig.java)

Github project for [SimpleJdbcMapper](https://github.com/spring-jdbc-crud/simplejdbcmapper) library.


