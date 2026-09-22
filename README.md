# Scalable E-Commerce Platform

A microservices-based e-commerce backend, built as a learning project to gain experience with distributed systems, service discovery, event-driven architecture, and containerization.

project idea: [roadmap.sh/projects/scalable-ecommerce-platform](https://roadmap.sh/projects/scalable-ecommerce-platform)

## Overview

The platform is being built as a set of independently deployable Spring Boot microservices, each owning its own data and responsibilities, communicating through a combination of REST (via an API gateway), service discovery, and asynchronous events over Kafka.

## Tech Stack

- **Language / Framework:** Java 17, Spring Boot 4.1.0, Spring Cloud 2025.1.2
- **Service Discovery:** Netflix Eureka
- **API Gateway:** Spring Cloud Gateway (WebFlux)
- **Messaging:** Apache Kafka
- **Databases:** PostgreSQL (one database per service), Redis
- **Auth:** JWT-based stateless authentication
- **Build:** Maven, multi-module project with a shared parent POM
- **Containerization:** Docker & Docker Compose (infrastructure: Postgres, Redis, Kafka, Zookeeper)

## Architecture

The project is structured as a Maven multi-module build under a single parent POM (`microservices`), with a shared `common-events` module holding Kafka event contracts used across services.

### Services

| Service | Status   | Responsibility |
|---|----------|---|
| `eureka-server` | ✅ Running | Service discovery registry |
| `api-gateway` | ✅ Running | Single entry point, routes requests to services via Eureka |
| `user-service` | ✅ Core auth complete | Registration, login, JWT issuance, profile lookup |
| `product-catalog-service` | 🔲 TODO  | Product listings, categories, inventory |
| `shopping-cart-service` | 🔲 TODO  | Per-user shopping cart (Redis-backed) |
| `order-service` | 🔲 TODO  | Order placement, status tracking, publishes `OrderCreatedEvent` |
| `payment-service` | 🔲 TODO  | Payment processing, publishes payment result events |
| `notification-service` | 🔲 TODO  | Consumes order/payment events, sends email/SMS notifications |
| `common-events` | ✅ Complete | Shared Kafka event classes (`OrderCreatedEvent`, `PaymentSucceededEvent`, `PaymentFailedEvent`) |

**Legend:** ✅ implemented and verified · 🔲 Test endpoint only (registers with Eureka, connects to its infra) but business logic not yet built

## Current Progress

- All services are wired into a single multi-module Maven build with per-module dependency control via a shared parent POM.
- Infrastructure (Postgres with one database per service, Redis, Kafka + Zookeeper) runs via Docker Compose.
- `eureka-server` and `api-gateway` are running, with services registering successfully.
- `user-service` has full registration and login flows, password hashing (BCrypt), and stateless JWT-based authentication including a custom `JwtAuthenticationFilter` that validates bearer tokens on protected endpoints.
- Kafka connectivity confirmed end-to-end (producer → topic → consumer) using `order-service` as the test case.
- Shared event contracts (`common-events` module) defined for the order → payment → notification event flow.

## End Goal

The finished platform will support a full, working e-commerce flow:

1. A user registers and logs in via `user-service`, receiving a JWT.
2. They browse products via `product-catalog-service` and add items to their cart via `shopping-cart-service`.
3. Placing an order triggers `order-service`, which persists the order and publishes an `OrderCreatedEvent` to Kafka.
4. `payment-service` consumes that event, processes payment (integrating with a provider such as Stripe), and publishes a `PaymentSucceededEvent` or `PaymentFailedEvent`.
5. `order-service` consumes the payment result to update order status, and `notification-service` independently consumes events at each stage to send the user email/SMS updates.
6. All external traffic flows through `api-gateway`, which resolves services dynamically via `eureka-server` and forwards authenticated requests downstream.

Remaining work: business logic for the product, cart, order, payment, and notification services; gateway-level request authentication forwarding; and eventually a production-style deployment step (Kubernetes/Swarm, centralized logging, CI/CD) per the original project roadmap.
