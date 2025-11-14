# FX Deals Analysis - Docker Deployment Guide

## Prerequisites

- Docker Desktop or Docker Engine (20.10+)
- Docker Compose (2.0+)

## Quick Start

### 1. Build and Start Services

```bash
# Start all services
docker-compose up -d

# Or build and start
docker-compose up --build -d
```

### 2. Check Service Health

```bash
# View logs
docker-compose logs -f app

# Check container status
docker-compose ps

# Check health
curl http://localhost:8080/actuator/health
```

### 3. Access the Application

- **Application URL**: http://localhost:8080
- **PostgreSQL**: localhost:5432
  - Database: `fxdeals`
  - Username: `fxuser`
  - Password: `fxpass123`

## API Endpoints

### Import Deals from CSV

```bash
# Import sample file
curl -X POST http://localhost:8080/api/deals/import \
  -F "file=@samples/deals-sample.csv"
```

### Get All Deals

```bash
curl http://localhost:8080/api/deals
```

### Get Deal by ID

```bash
curl http://localhost:8080/api/deals/DEAL001
```

## Sample Files

Sample CSV files are located in the `samples/` directory:

- `deals-sample.csv` - Valid deals for testing
- `deals-invalid.csv` - Invalid deals to test validation

## Testing the Application

### 1. Import Valid Deals

```bash
curl -X POST http://localhost:8080/api/deals/import \
  -F "file=@samples/deals-sample.csv"
```

Expected response: List of successfully imported deals

### 2. Try Importing Invalid Deals

```bash
curl -X POST http://localhost:8080/api/deals/import \
  -F "file=@samples/deals-invalid.csv"
```

Invalid deals will be skipped with warnings in logs.

### 3. Retrieve All Deals

```bash
curl http://localhost:8080/api/deals | jq .
```

### 4. Search Specific Deal

```bash
curl http://localhost:8080/api/deals/DEAL001 | jq .
```

## Docker Commands

### Stop Services

```bash
docker-compose down
```

### Stop and Remove Volumes (Clean Database)

```bash
docker-compose down -v
```

### View Logs

```bash
# All services
docker-compose logs -f

# Only app
docker-compose logs -f app

# Only database
docker-compose logs -f postgres
```

### Restart Services

```bash
docker-compose restart
```

### Rebuild Application

```bash
docker-compose up --build -d app
```

## Database Access

### Connect to PostgreSQL

```bash
# Using docker exec
docker exec -it fx-deals-postgres psql -U fxuser -d fxdeals

# Or use connection string
psql postgresql://fxuser:fxpass123@localhost:5432/fxdeals
```

### Common SQL Queries

```sql
-- View all deals
SELECT * FROM deals;

-- Count deals by currency
SELECT from_currency_iso_code, COUNT(*) 
FROM deals 
GROUP BY from_currency_iso_code;

-- Find deals above amount
SELECT * FROM deals WHERE deal_amount > 5000;

-- Delete all deals (for testing)
DELETE FROM deals;
```

## Troubleshooting

### Application won't start

```bash
# Check logs
docker-compose logs app

# Rebuild containers
docker-compose down
docker-compose up --build
```

### Database connection issues

```bash
# Check postgres health
docker-compose ps postgres

# Restart database
docker-compose restart postgres
```I'll create a complete Docker deployment setup for your project with docker-compose, Dockerfile, and sample CSV file.

### /home/happy/Bureau/progressSoft/analyze-fx-deals/Dockerfile

````dockerfile
# filepath: /home/happy/Bureau/progressSoft/analyze-fx-deals/Dockerfile
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
````

### /home/happy/Bureau/progressSoft/analyze-fx-deals/docker-compose.yml

````yaml
# filepath: /home/happy/Bureau/progressSoft/analyze-fx-deals/docker-compose.yml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: fx-deals-postgres
    environment:
      POSTGRES_DB: fx_deals_db
      POSTGRES_USER: fx_user
      POSTGRES_PASSWORD: fx_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - fx-deals-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U fx_user -d fx_deals_db"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: fx-deals-app
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/fx_deals_db
      SPRING_DATASOURCE_USERNAME: fx_user
      SPRING_DATASOURCE_PASSWORD: fx_password
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_JPA_SHOW_SQL: "true"
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - fx-deals-network
    restart: unless-stopped

volumes:
  postgres_data:
    driver: local

networks:
  fx-deals-network:
    driver: bridge
````

### /home/happy/Bureau/progressSoft/analyze-fx-deals/.dockerignore

