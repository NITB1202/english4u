# Instructions
Before starting, create an .env file based on the .env.example template.

### Running Locally: For Testing
* Run server-registry, followed by services, and then API Gateway.
* Access the H2 console at localhost:{service-port}/h2-console.
* Use Postman or Swagger to test the API.

### Running in Production: For the Project
* Build the container.
* Run the container in the following order: server-registry → services → API Gateway.
* Connect to the Frontend (FE) server.

#### Swagger URL: localhost:8080/swagger (Api Gateway)
