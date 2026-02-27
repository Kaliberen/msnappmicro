[![CI](https://github.com/Kaliberen/msnappmicro/actions/workflows/ci.yml/badge.svg)](https://github.com/Kaliberen/msnappmicro/actions/workflows/ci.yml)
[![CD](https://github.com/Kaliberen/msnappmicro/actions/workflows/cd.yml/badge.svg)](https://github.com/Kaliberen/msnappmicro/actions/workflows/cd.yml)

# MSNappMicro - Microservices Messaging System

## Overview

#### MSNappMicro is a microservice-based messaging system inspired by the classic MSN Messenger.

The system consists of four services:

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
##  Run with Docker Compose (Recommended)

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

## Run locally (without Docker)

### 1. From the project root folder:

`mvn clean install` 

**Then start each service individually**

**user-service:** 
```bash
cd user-service
mvn spring-boot:run
```

**message-service:**
```bash
cd message-service
mvn spring-boot:run
```

**notification-service:**
```bash
cd notification-service
mvn spring-boot:run
```

**gateway-service:**
```bash
cd gateway-service
mvn spring-boot:run
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
### All requests should be sent via the Gateway (port 8080).
### OBS: Must create two users before test messaging

### With postman:

### 1. Create Users

POST `/users`

http://localhost:8080/users

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
GET http://localhost:8080/users

- show all users 

Expected:
- HTTP 200
- User object with generated `id`



- - -

### 2. Send Message


POST `/messages`

http://localhost:8080/messages
``` 
{
   "senderId": 1,
   "receiverId": 2,
   "content": "Hello Dan!"
}
```

Expected:
- HTTP 200
- Message object with `id` and `createdAt`


#### Check messages
GET http://localhost:8080/messages/inbox/2

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

### Testing notifications (asynchronous communication)
```
1. Send a message:
   POST /messages
   {
   "senderId": 1,
   "receiverId": 2,
   "content": "Hello"
   }

2. Fetch notifications:
   GET /notifications/2

This verifies:
- Asynchronous event-based communication (RabbitMQ)
- MessageService publishes event
- NotificationService consumes event
- Gateway routing works correctly

```

## Testing

### Run all tests (root)

`mvn test`

### Run tests for a single service

- **user-service**
- - `mvn -pl user-service test`
- **message-service**
- - `mvn -pl message-service test`
- **notification-service**
- - `mvn -pl notification-service test`
- **gateway-service**
- - `mvn -pl gateway-service test`


- - -

## Assumptions & Simplifications
- H2 in-memory databases are used (data resets on restart)
- No authentication implemented
- Notification service stores basic notifications only
- No frontend included

- - -

## CI/CD (GitHub Actions)

This repository uses GitHub Actions to ensure the project is buildable and testable, and support deployment automation.

### Continuos Integration (CI)
**Workflow:** `.github/workflows/ci.yml`

**Triggered on:**
- every `push`
- every `pull request`

**What it does:**
- sets up Java 17
- runs `mvn -B clean verify` (build + tests)
- runs `docker compose build` (verifies Docker images can be built)

**How to verify:**
- Go to GitHub -> **Actions** -> **CI** and check the latest run logs.

### Continuous Delivery (CD)
**Workflow:** `.github/workflows/cd.yml`

**Triggered on:**
- push to the default branch `main`

**What it does:**
- logs in to DockerHub using GitHub Secrets
- builds Docker images using `docker compose build` 
- tags and pushes images to DockerHub:
- `msnappmicro-gateway-service:latest`
- `msnappmicro-user-service:latest`
- `msnappmicro-message-service:latest`
- `msnappmicro-notification-service:latest`

**Required GitHub Secrets:**
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN` (DockerHub access token with **Read & Write** permissions)

**How to verify:**
- Go to GitHub -> **Actions -> **CD** and check the logs.
- Check DockerHub repositories for updated image tags.

- - -

## Project structure
```
msnappmicro/
|---.github/
|      |---workflows/
|             |---cd.yml
|             |---ci.yml
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



