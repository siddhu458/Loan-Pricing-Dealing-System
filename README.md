# Corporate Banking – Loan Pricing & Deal Approval System

---

## Project Overview

Corporate banking teams process high‑value loan requests for enterprise and mid‑market clients. Traditional workflows rely on spreadsheets, emails, and manual approvals, leading to:

* Inconsistent loan pricing
* Lack of audit trails
* Unauthorized data modifications
* Poor visibility into deal status

This project delivers a **secure, centralized, role‑based Loan Pricing & Deal Approval System** built using **Angular, Spring Boot, MongoDB, Apache Kafka, Docker, and AWS**. The system supports the complete loan lifecycle with **enterprise‑grade security, auditability, scalability, and cloud deployment readiness**.

---

## Technology Stack

### Frontend
* Angular 18 (Single Page Application)
* NGINX (Dockerized static hosting)

### Backend
* Spring Boot 3.x (Java 17)
* Spring Security + JWT
* RESTful APIs

### Database
* MongoDB

### Messaging & Events
* Apache Kafka
* Zookeeper

### Security
* JWT Authentication
* Role‑Based Access Control (RBAC)

### Cloud & DevOps
* Docker & Docker Compose
* AWS EC2
* AWS ECR (Elastic Container Registry)

---

## System Architecture

```
User
  ↓
Angular SPA (NGINX)
  ↓
Spring Boot REST APIs
  ↓
MongoDB
  ↓
Kafka (Event Streaming)
```

* Angular communicates with Spring Boot via REST APIs
* Spring Boot enforces business rules, workflow validation, and security
* MongoDB stores users, loans, and audit data
* Kafka publishes deal lifecycle events
* AWS hosts the complete containerized system

---

## User Roles

### USER (Relationship Manager)

* Create loan requests (DRAFT)
* Edit non‑sensitive fields in DRAFT
* Perform loan pricing calculation
* Submit loans for approval
* View loan list and loan details

### ADMIN (Credit Manager)

* Review submitted loans
* Move loans through approval stages
* Approve or reject deals
* Edit sensitive fields (sanctioned amount, approved interest rate)
* Soft delete loans
* Manage users
* View complete audit trail

---

## Authentication & Authorization

* Login using email and password
* Passwords encrypted using BCrypt
* JWT token contains userId, role, and expiration
* Angular HTTP Interceptor attaches JWT to every request
* AuthGuard and RoleGuard protect frontend routes
* Backend enforces role‑based and status‑based authorization

---

## Loan Workflow

### Loan Status Flow

* DRAFT
* SUBMITTED
* UNDER_REVIEW
* APPROVED
* REJECTED

### Allowed Transitions

* **USER:** DRAFT → SUBMITTED
* **ADMIN:**
  * SUBMITTED → UNDER_REVIEW
  * UNDER_REVIEW → APPROVED / REJECTED

All transitions are strictly validated in the backend service layer.

---

## Loan Pricing Engine

* Pricing calculated using a predefined formula
* Inputs include base rate, credit rating, and tenure
* USER cannot override pricing values
* Backend re‑calculates pricing to prevent tampering

---

## Kafka Integration (Event‑Driven Architecture)

### Kafka Topics

* `deal-events`

### Published Events

* `DEAL_CREATED`
* `DEAL_SUBMITTED`
* `DEAL_STAGE_UPDATED`
* `DEAL_APPROVED`
* `DEAL_REJECTED`

### Event Flow

* Events are published **after successful database commits**
* Producer sends deal lifecycle events to Kafka
* Consumer listens to events and logs / processes them

This design enables:

* Asynchronous processing
* Future integrations (notifications, analytics, reporting)
* Decoupled microservice‑ready architecture

---

## Audit Trail & Soft Delete

### Audit Trail

Each loan maintains a complete audit history containing:

* Action performed
* Performed by (user)
* Timestamp
* Optional comments

### Soft Delete

* Loans are marked as deleted (not physically removed)
* Ensures regulatory compliance
* Deleted loans hidden from normal views
* Full history preserved

---

## Backend Design

* Layered architecture: Controller → Service → Repository
* DTO‑based request/response handling
* Bean Validation (`@Valid`)
* Global exception handling using `@ControllerAdvice`
* Pagination and sorting using Spring Data `Pageable`

---

## Frontend Design

* Feature‑based modules: auth, loans, admin, shared
* Reactive Forms for authentication and data entry
* Role‑based UI rendering using `*ngIf`
* JWT interceptor for API calls
* Route Guards for authentication and authorization

---

## Testing

### Backend

* JUnit 5 & Mockito
* Service and controller tests
* Code coverage ≥ 80% (JaCoCo)

### Frontend

* Jasmine & Karma
* Unit tests for services, guards, and forms
* Coverage ≥ 70%

---

## Prerequisites (Local Setup)

Ensure the following are installed:

* Node.js 18+
* Angular CLI 18
* Java JDK 17
* Maven 3.9+
* MongoDB (local) or Docker
* Docker & Docker Compose

---

## Getting Started

### 1. Clone the Repository

```bash
https://github.com/siddhu458/Loan-Pricing-Dealing-System.git
```

### 2. Project Structure

```
frontend/
backend/
docker-compose.yml
.gitignore
README.md
```

---

## Running the Project (Without Docker)

### 1. Start MongoDB

```
mongodb://localhost:27017
```

### 2. Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:9999
```

### 3. Run Frontend

```bash
cd frontend
npm install
ng serve
```

Frontend runs on:

```
http://localhost:4200
```

---

## Running the Project (Dockerized – Recommended)

### 1. Build & Start Containers

```bash
docker compose up --build
```

### 2. Access Services

* Frontend: http://localhost:4200
* Backend APIs: http://localhost:9999
* MongoDB: Docker internal network
* Kafka & Zookeeper: Docker internal network

### 3. Stop Containers

```bash
docker compose down
```

---

## AWS Deployment

### AWS Services Used

* EC2 – Application hosting
* ECR – Docker image registry

### Deployment Flow

1. Build Docker images locally
2. Push images to Amazon ECR
3. Pull images on EC2 instance
4. Run containers using Docker Compose

The system is fully cloud‑ready and scalable.

---

## Environment Configuration

* `application.properties` supports environment variables
* JWT secrets externalized for production
* Kafka broker and MongoDB URLs configurable

---

## Production Considerations

* HTTPS via reverse proxy (NGINX)
* Secure secret management
* Health checks and restart policies
* Centralized logging
* CI/CD pipeline compatibility

---

## Key Highlights

* Enterprise‑grade approval workflow
* Secure JWT‑based RBAC
* Event‑driven architecture with Kafka
* Full audit trail and compliance support
* Dockerized & AWS‑deployable
* Interview‑ready real‑world system

---

## Interview Note

This project demonstrates real‑world enterprise patterns including:

* Secure authentication and authorization
* Workflow‑driven state management
* Event‑driven systems with Kafka
* Clean layered architecture with DTOs
* Docker & AWS cloud deployment

---

**Author:** Siddhu Chaparthi

