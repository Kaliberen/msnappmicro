# MSNappMicro - Microservices Messaging System

## Overview

#### MSNappMicro is a microservice-based messaging system inspired by the classic MSN Messenger.

The system consist of four services:

- **gateway-service** - single entry point to the system (Spring Cloud Gateway)
- **user-service** - manages users (CRUD operations)
- **message-service** - sends and stores messages
- **notification-service** - creates notifications when messages are sent
- **rabbitmq** - message broker for asynchronous communication
- - -
## Architecture
### Communication pattern

#### Synchronous communication

- `message-service` calls `user-service` via REST
- Used to validate sender and receiver before saving a message

### Asynchronous communication
- `message-service` publishes `MessageSentEvent` to RabbitMQ
- `notification-service` consumes the event and stores a notification
- - -
## Tech stack
- **Java 17** 
- **Spring Boot 3.5**
- **Spring Cloud 2025**
- **Maven**
- **Docker Compose**
- **RabbitMQ**
- **H2 (in memory database)**
- **Actuator (health monitoring)**
- - -
## How to run 
### Prerequisites

- **Docker Desktop (with Compose v2)**
- **Java 17** (only required if running locally without Docker)
- - -
## Run with Docker Compose (Recommended)

### 1. From the project root folder:
```bash
docker compose up --build -d
```

### 2. Check running services
```bash
docker compose ps
```

### 3. View logs
```bash
docker compose logs -f
```

### 4. Stop the system
```bash
docker compose down
```

### 5. Full reset (delete volumes and data)
```bash
docker compose down -v
```
- - -
## Health checks (Actuator)
- `gateway-service`: http://localhost:8080/actuator/health
- `user-service`: http://localhost:8000/actuator/health
- `message-service`: http://localhost:8001/actuator/health
- `notification-service`: http://localhost:8002/actuator/health
- - -

## Ports
### Service

- `gateway-service` - http://localhost:8080
- `user-service` - http://localhost:8000 
- `message-service` - http://localhost:8001
- `notification-service` - http://localhost:8002
- **RabbitMQ UI login** - http://localhost:15672
- - **username:** guest
- - **password:** guest
- - -

## Functional Test Scenarios
### All request should be sent via the Gateway (port 8080).
### OBS: Must create two users before test messaging

### 1. Create Users

POST `/users`

**Example:**
```
{
   "username": "Ola",
   "displayName": "Ola Nordmann",
   "email": "ola@kristiania.no",
   "friendIds": []
}
```
```
{
   "username": "Dan",
   "displayName": "Bog",
   "email": "Dan@kristiania.no",
   "friendIds": []
}
```
Expected:
- HTTP 200
- User object with generated `id`

- - -

### 2. Send Message

POST `/messages`
``` 
{
   "senderId": 1,
   "receiver": 2,
   "content": "Hello Dan!"
}
```

Expected:
- HTTP 200
- Message object with `id` and `createdAt`

- - - 

### 3. Event Verification (Async)

After sending a message:
- `message-service` publishes `MessageSentEvent`
- `notification-service` consumes it
- Check logs:
```bash
docker compose logs notification-service
```

You should see event handling logs.

- - -

## Assumptions & Simplifications
- H2 in-memory databases are used (data resets on restart)
- No authentication implemented
- Notification service stores basic notifications only
- No frontend included

- - -

## CI/CD Ready

**The project:**
- Builds with `mvn clean install`
- Starts with `docker compose up`
- Uses health endpoints
- Is structured for integration into a CI/CD pipeline

- - -

## Project structure
```
msnappmicro/
|---gateway-service/
|      |---src/
|      |---Dockerfile
|      |---pom.xml
|---user-service/
|      |---src/
|      |---Dockerfile
|      |---pom.xml
|---message-service/
|      |---src/
|      |---Dockerfile
|      |---pom.xml
|---notification-service/
|      |---src/
|      |---Dockerfile
|      |---pom.xml
|---docker-compose.yml
|---pom.xml
|---README.md
```



