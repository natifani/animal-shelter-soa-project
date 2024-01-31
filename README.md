# animal-shelter-soa-project

This project represents a PoC application for managing shelter animals and adoptions showcasing a service oriented architecture.

The application consists of the following services:
- `service-discovery`: maintains a service registry using Eureka
- `api-gateway`: discovers the available services, routes and load balances the requests to them making the services accessible using one single port
- `auth-service`: provides API for authentication (login, register, refresh token)
- `animal-shelter`: provides API for managing shelter animals (create, list, delete, update)
- `adoption-service`: provides API for managing adoption requests (create, accept, decline)
- `email-service`: dummy application for simulating an email server

Each of the APIs has a separate MySQL database. In order to have consistency between them, the services' producers publish the executed CRUD operations in form of events to a Kafka server, and when the consumers receive a new event, they update the database according to the message.

The application uses RabbitMQ in order to simulate an email triggering. When an administrator reacts to an adoption request, the reaction is published to RabbitMQ from which the email-service reads the message and prints it out as the content of the email.

The frontend of the application is based on micro-frontend architecture. The management of animals and adoption requests are implemented as two different micro-frontends and are imported to the same main frontend project.

![c4 model](/diagrams/c4-model-level2.png)

## Deploy & test the application
Run the `docker-compose.yml` file and then go to  `http://localhost:9001` in your browser.
