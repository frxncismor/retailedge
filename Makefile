# RetailEdge Docker Management Makefile

.PHONY: help build up down logs clean restart status test

# Default target
help: ## Show this help message
	@echo "RetailEdge Docker Management"
	@echo "=========================="
	@echo ""
	@echo "Available commands:"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# Build all services
build: ## Build all Docker images
	@echo "Building all services..."
	docker-compose build

# Start all services
up: ## Start all services
	@echo "Starting RetailEdge stack..."
	docker-compose up -d
	@echo "Services started! Access Traefik dashboard at http://localhost:8080"
	@echo "API endpoints:"
	@echo "  - Catalog: http://localhost/api/catalog"
	@echo "  - Orders:  http://localhost/api/orders"
	@echo "  - Users:   http://localhost/api/users"

# Start services in foreground
up-logs: ## Start all services with logs
	@echo "Starting RetailEdge stack with logs..."
	docker-compose up

# Stop all services
down: ## Stop all services
	@echo "Stopping RetailEdge stack..."
	docker-compose down

# Show logs
logs: ## Show logs for all services
	docker-compose logs -f

# Show logs for specific service
logs-catalog: ## Show logs for catalog service
	docker-compose logs -f catalog-service

logs-orders: ## Show logs for orders service
	docker-compose logs -f orders-service

logs-users: ## Show logs for users service
	docker-compose logs -f users-service

logs-traefik: ## Show logs for Traefik
	docker-compose logs -f traefik

logs-postgres: ## Show logs for PostgreSQL
	docker-compose logs -f postgres

# Restart services
restart: ## Restart all services
	@echo "Restarting RetailEdge stack..."
	docker-compose restart

# Show service status
status: ## Show status of all services
	@echo "RetailEdge Services Status:"
	@echo "=========================="
	docker-compose ps

# Clean up (remove containers, networks, volumes)
clean: ## Remove all containers, networks, and volumes
	@echo "Cleaning up RetailEdge stack..."
	docker-compose down -v --remove-orphans
	docker system prune -f

# Clean everything including images
clean-all: ## Remove everything including Docker images
	@echo "Cleaning up everything..."
	docker-compose down -v --remove-orphans
	docker system prune -af

# Test endpoints
test: ## Test all API endpoints
	@echo "Testing API endpoints..."
	@echo "Testing Catalog Service..."
	@curl -s http://localhost/api/catalog/actuator/health | jq . || echo "Catalog service not ready"
	@echo "Testing Orders Service..."
	@curl -s http://localhost/api/orders/actuator/health | jq . || echo "Orders service not ready"
	@echo "Testing Users Service..."
	@curl -s http://localhost/api/users/actuator/health | jq . || echo "Users service not ready"

# Database operations
db-shell: ## Connect to PostgreSQL shell
	docker-compose exec postgres psql -U retailedge -d retailedge

db-backup: ## Backup database
	@echo "Creating database backup..."
	docker-compose exec postgres pg_dump -U retailedge retailedge > backup_$(shell date +%Y%m%d_%H%M%S).sql
	@echo "Backup created: backup_$(shell date +%Y%m%d_%H%M%S).sql"

# Development helpers
dev-build: ## Build only changed services
	@echo "Building changed services..."
	docker-compose build --no-cache

dev-logs: ## Follow logs for development
	docker-compose logs -f --tail=100

# Health checks
health: ## Check health of all services
	@echo "Checking service health..."
	@echo "PostgreSQL:"
	@docker-compose exec postgres pg_isready -U retailedge || echo "PostgreSQL not ready"
	@echo "Traefik:"
	@curl -s http://localhost:8080/api/rawdata | jq . || echo "Traefik not ready"
	@echo "Services:"
	@make test
