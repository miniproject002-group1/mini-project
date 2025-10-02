# 🚀 Microservices Project Setup

This project demonstrates a microservices architecture using **Spring Boot**, **Spring Cloud**, and **Keycloak** for authentication.  
The services are orchestrated to run both locally and in Docker.

---

## 📌 Services Overview

| Service              | Description |
|----------------------|-------------|
| **Eureka Server**    | Service discovery for all microservices. |
| **Config Server**    | Centralized configuration for all services. |
| **API Gateway**      | Entry point to the microservices, secured with Keycloak. |
| **Keycloak**         | Authentication & Authorization (already deployed externally). |
| **Product Service**  | Sample business service registered in Eureka. |
| **Category Service** | Another business service registered in Eureka. |

---

## ⚡ Prerequisites

Make sure you have installed:

- **Java 17+**
- **Gradle**
- **Docker & Docker Compose**
- **Keycloak** (already deployed — no need to start locally)

---

## ▶️ Start Services in Correct Order

###🌐 Service URLs

Eureka Dashboard → http://localhost:8761

Config Server → http://localhost:8888

API Gateway → http://localhost:8080

Product Service (via Gateway) → http://localhost:8080/product

Category Service (via Gateway) → http://localhost:8080/category

Keycloak (external) → Already deployed

#### you also need to run docker compose up

📖 Notes

Make sure to start Eureka Server and Config Server first, otherwise other services will fail to register or fetch configuration.

Keycloak configuration (realm, client, roles) must be set up before testing authentication.

Use Postman or curl with a valid JWT token for testing secured endpoints.
```bash


