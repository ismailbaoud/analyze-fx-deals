# FX Deals Data Warehouse System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

A production-ready Spring Boot application for managing Foreign Exchange (FX) deal transactions.

---

## Quick Start

```bash
# Clone the repository
cd /home/happy/Bureau/progressSoft/analyze-fx-deals

# Deploy with one command
make deploy

# Application is ready at http://localhost:8080

# Import sample data
make import-sample

# Get all deals
make get-deals
```

---

## Table of Contents

* Quick Start
* Features
* Prerequisites
* Installation
* API Documentation
* Makefile Commands
* Testing
* Test Coverage
* Troubleshooting
* Project Structure
* Technologies

---

## Features

* CSV Bulk Import: Import thousands of deals from CSV files
* Data Validation: Jakarta Bean Validation for all fields
* Duplicate Prevention: Automatic duplicate detection
* RESTful APIs: Clean, documented REST endpoints
* Audit Logging: Structured SLF4J logging
* Docker Ready: Fully containerized with Docker Compose
* Tested: High code coverage

---

## Prerequisites

### Required

* Docker (20.10+)
* Docker Compose (2.0+)

### Verify

```bash
docker --version
docker-compose --version
```

---

## Installation

```bash
# Navigate to project
cd /home/happy/Bureau/progressSoft/analyze-fx-deals

# Deploy everything
make deploy

# Check status
make status

# Check health
make health
```

---

## API Documentation

### Base URL

```
http://localhost:8080/api/deals
```

### Endpoints

| Method | Endpoint            | Description           |
| ------ | ------------------- | --------------------- |
| POST   | `/api/deals/import` | Import deals from CSV |
| GET    | `/api/deals`        | Get all deals         |
| GET    | `/api/deals/{id}`   | Get deal by ID        |
| GET    | `/actuator/health`  | Health check          |

### Import Deals

```bash
# Using curl
curl -X POST http://localhost:8080/api/deals/import \
  -F "file=@sample-data/deals.csv"

# Using Makefile
make import-sample
```

**CSV Format:**

```csv
dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount
DEAL001,USD,EUR,2024-01-15T10:30:00,1000.00
```

### Get All Deals

```bash
# Using curl
curl http://localhost:8080/api/deals

# Using Makefile
make get-deals
```

### Get Deal by ID

```bash
# Using curl
curl http://localhost:8080/api/deals/DEAL001

# Using Makefile
make get-deal ID=DEAL001
```

### Health Check

```bash
# Using curl
curl http://localhost:8080/actuator/health

# Using Makefile
make health
```

---

## Makefile Commands

### Deployment

```bash
make deploy          # Full deployment (clean + build + start)
make quick-start     # Start without rebuild
make start           # Start services
make stop            # Stop services
make restart         # Restart services
```

### Monitoring

```bash
make status          # Show service status
make logs            # View application logs
make logs-all        # View all services logs
make health          # Check application health
```

### Data Operations

```bash
make import-sample   # Import sample CSV data
make get-deals       # Get all deals via API
make get-deal ID=X   # Get specific deal
make db-connect      # Connect to PostgreSQL
make db-query        # Query all deals
make db-count        # Count deals
```

### Development

```bash
make test            # Run unit tests
make coverage        # Generate coverage report
make package         # Build JAR file
```

### Cleanup

```bash
make clean           # Clean build artifacts
make docker-clean    # Remove all Docker resources
```

---

## Testing

### Run Tests

```bash
make test
```

### Generate Coverage Report

```bash
make coverage
# Open: target/site/jacoco/index.html
```

---

## Test Coverage

The project uses JaCoCo to measure unit test coverage.

### Coverage Summary

* Exception Handler: 100%
* Service Layer: 95%
* Controller Layer: 88%
* Overall: 82%

### Screenshot

![JaCoCo Coverage](![alt text](image.png))

> The screenshot can be generated from `target/site/jacoco/index.html`

---

## Troubleshooting

### Services Won't Start

```bash
docker-compose down
docker-compose up --build --force-recreate
```

### Database Connection Error

```bash
make status
docker-compose restart postgres
```

### Port Already in Use

```bash
lsof -i :8080
make stop
```

### CSV Import Fails

Check CSV format:

* Headers must match exactly
* Date format: `YYYY-MM-DDTHH:mm:ss`
* Currency: Valid ISO 4217 codes (USD, EUR, GBP)
* Amount: Positive decimal

View logs:

```bash
make logs
```

---

## Project Structure

```
analyze-fx-deals/
├── src/
│   ├── main/java/              # Source code
│   └── test/java/              # Unit tests
├── sample-data/
│   └── deals.csv               # Sample CSV
├── docker-compose.yml          # Docker config
├── Dockerfile                  # Docker image
├── Makefile                    # Task automation
├── pom.xml                     # Maven config
└── README.md                   # This file
```

---

## Technologies

| Technology  | Version | Purpose                 |
| ----------- | ------- | ----------------------- |
| Java        | 17      | Programming language    |
| Spring Boot | 3.5.7   | Application framework   |
| PostgreSQL  | 15      | Database                |
| Docker      | 20.10+  | Containerization        |
| Maven       | 3.9+    | Build tool              |
| MapStruct   | 1.6.3   | DTO mapping             |
| Lombok      | 1.18.42 | Reduce boilerplate code |
