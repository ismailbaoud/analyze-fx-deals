.PHONY: help build start stop restart logs test clean deploy import-sample health db-connect coverage

# Default target
.DEFAULT_GOAL := help

# Colors for output
CYAN := \033[0;36m
GREEN := \033[0;32m
YELLOW := \033[0;33m
RED := \033[0;31m
NC := \033[0m # No Color

## help: Show this help message
help:
	@echo "$(CYAN)FX Deals Analysis - Available Commands:$(NC)"
	@echo ""
	@grep -E '^## ' $(MAKEFILE_LIST) | sed 's/## /  $(GREEN)/' | sed 's/:/ -$(NC)/'
	@echo ""

## build: Build Docker images
build:
	@echo "$(CYAN)Building Docker images...$(NC)"
	docker-compose build

## start: Start all services
start:
	@echo "$(CYAN)Starting services...$(NC)"
	docker-compose up -d
	@echo "$(GREEN)Services started! Waiting for health check...$(NC)"
	@sleep 30
	@make health

## stop: Stop all services
stop:
	@echo "$(YELLOW)Stopping services...$(NC)"
	docker-compose down

## restart: Restart all services
restart: stop start

## logs: View application logs
logs:
	docker-compose logs -f app

## logs-all: View all services logs
logs-all:
	docker-compose logs -f

## test: Run unit tests
test:
	@echo "$(CYAN)Running tests...$(NC)"
	mvn test

## coverage: Generate test coverage report
coverage:
	@echo "$(CYAN)Generating coverage report...$(NC)"
	mvn clean test jacoco:report
	@echo "$(GREEN)Coverage report generated at: target/site/jacoco/index.html$(NC)"

## clean: Clean build artifacts and Docker volumes
clean:
	@echo "$(YELLOW)Cleaning up...$(NC)"
	mvn clean
	docker-compose down -v
	@echo "$(GREEN)Cleanup complete!$(NC)"

## deploy: Full deployment (clean, build, start)
deploy: clean build start
	@echo "$(GREEN)Deployment complete!$(NC)"

## quick-start: Quick start without rebuild
quick-start:
	@echo "$(CYAN)Quick starting services...$(NC)"
	docker-compose up -d
	@sleep 30
	@make health
	@make import-sample

## health: Check application health
health:
	@echo "$(CYAN)Checking application health...$(NC)"
	@curl -s http://localhost:8080/actuator/health | jq . || echo "$(RED)Health check failed!$(NC)"

## import-sample: Import sample CSV file
import-sample:
	@echo "$(CYAN)Importing sample deals...$(NC)"
	@curl -s -X POST http://localhost:8080/api/deals/import \
		-F "file=@sample-data/deals.csv" | jq .
	@echo "$(GREEN)Sample data imported!$(NC)"

## get-deals: Get all deals
get-deals:
	@echo "$(CYAN)Fetching all deals...$(NC)"
	@curl -s http://localhost:8080/api/deals | jq .

## get-deal: Get deal by ID (usage: make get-deal ID=DEAL001)
get-deal:
	@echo "$(CYAN)Fetching deal $(ID)...$(NC)"
	@curl -s http://localhost:8080/api/deals/$(ID) | jq .

## db-connect: Connect to PostgreSQL database
db-connect:
	@echo "$(CYAN)Connecting to database...$(NC)"
	docker exec -it fx-deals-postgres psql -U fx_user -d fx_deals_db

## db-query: Query all deals from database
db-query:
	@echo "$(CYAN)Querying database...$(NC)"
	docker exec -it fx-deals-postgres psql -U fx_user -d fx_deals_db -c "SELECT * FROM deals;"

## db-count: Count deals in database
db-count:
	@echo "$(CYAN)Counting deals...$(NC)"
	docker exec -it fx-deals-postgres psql -U fx_user -d fx_deals_db -c "SELECT COUNT(*) FROM deals;"

## status: Show service status
status:
	@echo "$(CYAN)Service Status:$(NC)"
	docker-compose ps

## create-sample: Create sample CSV file
create-sample:
	@echo "$(CYAN)Creating sample data...$(NC)"
	@mkdir -p sample-data
	@cat > sample-data/deals.csv << 'EOF'
dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount
DEAL001,USD,EUR,2024-01-15T10:30:00,1000.00
DEAL002,GBP,JPY,2024-01-15T11:00:00,2500.50
DEAL003,EUR,USD,2024-01-15T12:15:00,3200.75
DEAL004,JPY,GBP,2024-01-15T13:45:00,150000.00
DEAL005,USD,CHF,2024-01-15T14:20:00,5000.00
DEAL006,EUR,GBP,2024-01-15T15:30:00,8750.25
DEAL007,CHF,USD,2024-01-15T16:00:00,4500.00
DEAL008,CAD,USD,2024-01-16T09:00:00,6200.00
DEAL009,AUD,EUR,2024-01-16T10:30:00,7800.50
DEAL010,NZD,USD,2024-01-16T11:45:00,3500.00
EOF
	@echo "$(GREEN)Sample file created at sample-data/deals.csv$(NC)"

## package: Build JAR file
package:
	@echo "$(CYAN)Building JAR file...$(NC)"
	mvn clean package -DskipTests
	@echo "$(GREEN)JAR file created!$(NC)"

## run-local: Run application locally (requires PostgreSQL)
run-local:
	@echo "$(CYAN)Running application locally...$(NC)"
	mvn spring-boot:run

## docker-clean: Remove all Docker containers, images, and volumes
docker-clean:
	@echo "$(RED)Cleaning all Docker resources...$(NC)"
	docker-compose down -v --rmi all
	@echo "$(GREEN)Docker cleanup complete!$(NC)"

## full-test: Run tests with coverage and deploy
full-test: test coverage deploy
	@echo "$(GREEN)Full test cycle complete!$(NC)"
