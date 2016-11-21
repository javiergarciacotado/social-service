# Social Service

Service for handling social media stories. Currently, to keep track of likes and dislikes.

## Requirements

- Maven version >= 3
- JDK8

## Considerations

- Implemented Factory pattern to register Handlers for request method (GET,POST and PUT). If a handler is not registered, returns BAD REQUEST
- Implemented Singleton pattern for Stories. In the future, it will help to add Story attributes and build POJOs.
- Implemented a Connection Database pool to avoid expensive creation/shutdown connections.
- Implemented a Properties reader to define server and database properties


## Running the application

Clone the repository and use Maven to compile and run it:

 1. Clone the repository 
  ```
  git clone https://github.com/javiergarciacotado/social-service
  cd social-service
  ```  
 2. Run maven phases
  ```
  mvn clean verify site --> Should run unit and integration tests and generates reporting documentation 
  mvn package --> Should generate a JAR with dependencies
  ```
 3. Run the application
  ```
  java -jar target/social-service-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```
  or
  ```
  mvn exec:java
  ```
  Both will start a HttpServer with the port specified at application.properties
  
## Results

- It returns 200 OK, 201 CREATED, 400 BAD REQUEST, 404 NOT FOUND and 500 INTERNAL SERVER ERROR
- For the 201 CREATED it adds the Location Header

## Documentation

At `target/site/index.html` there is the Javadoc documentation, failsafe and surefire reports

