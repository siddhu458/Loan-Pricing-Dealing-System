# Corporate Banking – Loan Pricing & Deal Approval System

## Project Overview

Corporate banking teams process loan requests for enterprise and mid-market clients. Traditional workflows rely on spreadsheets and emails, leading to inconsistent pricing, lack of audit trails, unauthorized edits, and poor visibility.

This project delivers a **secure, role-based, centralized Loan Pricing & Deal Approval System** built using **Angular, Spring Boot, MongoDB, and Docker**. It supports end-to-end loan lifecycle management with enterprise-grade security and auditability.

---

## Technology Stack

* **Frontend:** Angular 18 (SPA)
* **Backend:** Spring Boot 3.x (Java 17)
* **Database:** MongoDB
* **Security:** JWT Authentication + Role-Based Access Control (RBAC)
* **Containerization:** Docker & Docker Compose

---

## System Architecture

* Angular SPA communicates with Spring Boot REST APIs
* Spring Boot handles business logic, validations, and security
* MongoDB stores users, loans, and audit history
* JWT provides stateless authentication
* Docker Compose orchestrates frontend, backend, and database containers

---

## User Roles

### USER (Relationship Manager)

* Create loan requests (DRAFT)
* Edit non-sensitive fields while in DRAFT
* Perform loan pricing calculation
* Submit loan for approval
* View loan list and details

### ADMIN (Credit Manager)

* Review submitted loans
* Approve or reject loans
* Edit sensitive fields (sanctioned amount, approved interest rate)
* Soft delete loans
* Manage users
* View complete audit trail

---

## Authentication & Authorization

* Login using email and password
* Passwords hashed using BCrypt
* JWT contains userId, role, and expiration
* Angular HTTP Interceptor attaches JWT to all requests
* AuthGuard and RoleGuard protect routes on frontend
* Backend enforces role and status-based authorization

---

## Loan Workflow

### Status Flow

* DRAFT
* SUBMITTED
* UNDER_REVIEW
* APPROVED
* REJECTED

### Transitions

* USER: DRAFT → SUBMITTED
* ADMIN:

  * SUBMITTED → UNDER_REVIEW
  * UNDER_REVIEW → APPROVED / REJECTED

All transitions are validated in the backend service layer.

---

## Loan Pricing

* Pricing calculated using predefined formula
* Based on base rate, credit rating, and tenure
* USER cannot override pricing values
* Backend re-validates pricing to prevent tampering

---

## Audit Trail & Soft Delete

### Audit Trail

Each loan maintains an audit history containing:

* Action performed
* Performed by
* Timestamp
* Optional comments

### Soft Delete

* Loans are marked as deleted instead of being removed
* Preserves historical data for compliance
* Deleted loans hidden from standard views

---

## Backend Design

* Layered architecture: Controller → Service → Repository
* DTOs used to avoid exposing entities
* Bean Validation for input validation
* Global exception handling using `@ControllerAdvice`
* Pagination implemented using Spring Data `Pageable`

---

## Frontend Design

* Feature-based modules: auth, loans, admin, shared
* Reactive Forms for authentication
* Role-based UI rendering using `*ngIf`
* JWT interceptor for API calls
* Guards for route protection

---

## Testing

### Backend

* JUnit 5 & Mockito
* Service and controller tests
* JaCoCo coverage ≥ 80%

### Frontend

* Jasmine & Karma
* Tests for services, guards, and forms
* Coverage ≥ 70%

---

## Prerequisites (Local Setup)

Before running the project, ensure the following are installed:

* Node.js (v18+ recommended)
* Angular CLI (v18)
* Java JDK 17
* Maven 3.9+
* MongoDB (local) OR Docker
* Docker & Docker Compose

---

## Getting Started (After Cloning the Repository)

Follow these steps **after downloading or cloning the GitHub repository**.

### 1. Clone the Repository

```bash
git clone https://github.com/SiddhuChaparthi/LoanPricing-and-Dealing.git
cd Loan-Pricing-Dealing
```

### 2. Verify Project Structure

Ensure the following structure exists:

```
frontend/
backend/
docker-compose.yml
.gitignore
```

If this structure is present, you are ready to proceed.

### 3. Choose How You Want to Run the Project

You can run the project in **two ways**:

* **Option 1:** Run services individually (without Docker)
* **Option 2:** Run everything using Docker Compose (recommended)

---

## Running the Project (Option 1: Without Docker)

### 1. Run MongoDB

Start MongoDB locally on default port:

```
mongodb://localhost:27017
```

### 2. Run Backend (Spring Boot)

```
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start on:

```
http://localhost:9999
```

### 3. Run Frontend (Angular)

```
cd frontend
npm install
ng serve
```

Frontend will be available at:

```
http://localhost:4200
```

---

## Running the Project (Option 2: Dockerized – Recommended)

### 1. Build and Run Containers

From project root:

```
docker compose up --build
```

### 2. Access Applications

* Frontend: [http://localhost:4200](http://localhost:4200)
* Backend APIs: [http://localhost:9999](http://localhost:9999)
* MongoDB: internal Docker network

### 3. Stop Containers

```
docker compose down
```

---

## Docker Setup Summary

* Angular served via Nginx container
* Spring Boot runs in its own container
* MongoDB container with volume persistence
* Docker Compose manages service networking

---

## Environment Configuration

* Application properties managed via `application.properties`
* Environment variables supported for Docker deployments
* JWT secrets configurable via environment variables

---

## Production Considerations

* HTTPS using reverse proxy (Nginx)
* Secure secret management
* Health checks and restart policies
* Centralized logging and monitoring
* CI/CD-ready Docker setup

---

## Key Highlights

* Enterprise-grade approval workflow
* Strong backend security with RBAC
* Complete audit trail
* Dockerized deployment
* Interview-ready full-stack system

---

## Interview Note

This project demonstrates real-world enterprise patterns including:

* Secure authentication and authorization
* Workflow-based state management
* Clean architecture and DTO usage
* Docker-based deployment