# GameChallenge

### This repository consists of below files:
- `Main Application` Consists of the main function and configuration of swagger API. 
- `Controller` - Consists of end-point urls.
- `Model` file - Consists of an input number to be provided by user (manual) or random generated (auto generated).
- `Service` file - Logic to play the game and communication between the two microservices are written.
- `JUnit Test Cases` file - To perform the TDD using JUnit (Basic Testing).
- `Properties` file - Consisits of port details and http urls for two standalone spring boot applications.

## Standalone Game Application
It plays the game between two players when input as a number is provided by the user.

## Prerequisites
- [Java]
- [Lombok Java Library]
- [Message Queue] 
- [Tomcat 8.5] 

## Technologies Used

- Spring Boot
- Spring MVC Test framework and JUnits
- Lombok Java Library
- In-memory ActiveMQ
- Swagger API
- Maven

## Installation Steps
1. Clone the Git Repository.
2. Build the project using Maven.

## Build Projects

`mvn clean install` - under \GameChallenge

3. The `jar` file will be created under the target folder - \target\GameTakeaway-0.0.1-SNAPSHOT.jar

## Usage
Now execute the jar file using below two commands to run the two standalone spring boot applications
1. `java -jar GameTakeaway-0.0.1-SNAPSHOT.jar --spring.config.name=application`  - First Service (Player 1)
2. `java -jar GameTakeaway-0.0.1-SNAPSHOT.jar --spring.config.name=application1` - Second Service (Player 2)

Both have embedded Tomcat and In-memory ActiveMQ. The application can be accessible using the below URL.

### Check Status of Service
`http://localhost:8080/api/GameTakeaway/checkServiceStatus` -> Check the Status of Player 1
`http://localhost:8081/api/GameTakeaway/checkServiceStatus` -> Check the Status of Player 2

### Automatic or Manually Start the Game
`http://localhost:8080/api/GameTakeaway/game/true` -> Automatic Actvity for Player 1                                            
`http://localhost:8081/api/GameTakeaway/game/true` -> Automatic Actvity for Player 2

`http://localhost:8080/api/GameTakeaway/game/false?number=185642` -> Manual Activity (Num Input here is 185642) for P1
`http://localhost:8080/api/GameTakeaway/game/false?number=185642` -> Manual Activity (Num Input here is 185642) for P2

## The Request can be initiated using POSTMAN or SWAGGER console.
 `http://localhost:8080/api/GameTakeaway/swagger-ui.html#!/GameTakeaway/` - SWAGGER URL for initiating GET and POST Request.
 
 ## Communication
 - RestTemplate for communication between Microservices.
 - ActiveMQ for sending the number in queue which is listened by other service.
