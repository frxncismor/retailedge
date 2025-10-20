# RetailEdge Docker Setup

This document describes how to run the RetailEdge application stack using Docker and Docker Compose.

## Architecture

The stack includes:
- **PostgreSQL**: Database for all services
- **Traefik**: Reverse proxy with automatic service discovery
- **Catalog Service**: Product management (Port 8081)
- **Orders Service**: Order management (Port 8082)  
- **Users Service**: Customer management (Port 8083)

## Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- Make (optional, for using Makefile commands)

## Quick Start

### Using Make (Recommended)

```bash
# Build and start all services
make up

# View logs
make logs

# Check status
make status

# Test endpoints
make test

# Stop services
make down
```

### Using npm scripts

```bash
# Build and start all services
npm run docker:build
npm run docker:up

# View logs
npm run docker:logs

# Check status
npm run docker:status

# Test endpoints
npm run docker:test

# Stop services
npm run docker:down
```

### Using Docker Compose directly

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Check status
docker-compose ps

# Stop services
docker-compose down
```

## Service Endpoints

Once the stack is running, you can access:

- **Traefik Dashboard**: http://localhost:8080
- **Catalog API**: http://localhost/api/catalog
- **Orders API**: http://localhost/api/orders
- **Users API**: http://localhost/api/users

### API Documentation

Each service provides Swagger/OpenAPI documentation:
- Catalog: http://localhost/api/catalog/swagger-ui.html
- Orders: http://localhost/api/orders/swagger-ui.html
- Users: http://localhost/api/users/swagger-ui.html

### Health Checks

- All services: http://localhost/api/{service}/actuator/health
- Traefik: http://localhost:8080/api/rawdata

## Database

PostgreSQL is configured with:
- **Host**: localhost:5432
- **Database**: retailedge
- **Username**: retailedge
- **Password**: retailedge123

### Database Operations

```bash
# Connect to database shell
make db-shell

# Create backup
make db-backup

# Or using docker-compose directly
docker-compose exec postgres psql -U retailedge -d retailedge
```

## Development

### Rebuilding Services

```bash
# Rebuild all services
make dev-build

# Rebuild specific service
docker-compose build catalog-service
```

### Viewing Logs

```bash
# All services
make logs

# Specific service
make logs-catalog
make logs-orders
make logs-users
make logs-traefik
make logs-postgres
```

### Testing

```bash
# Test all endpoints
make test

# Test specific service
curl http://localhost/api/catalog/actuator/health
curl http://localhost/api/orders/actuator/health
curl http://localhost/api/users/actuator/health
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: Make sure ports 80, 443, 5432, and 8080 are available
2. **Database connection**: Wait for PostgreSQL to be ready before services start
3. **Service not responding**: Check logs with `make logs-{service}`

### Clean Up

```bash
# Stop and remove containers, networks, volumes
make clean

# Remove everything including images
make clean-all
```

### Health Checks

```bash
# Check all service health
make health
```

## Environment Variables

Services can be configured using environment variables:

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SERVER_PORT`: Service port (default: 8081, 8082, 8083)
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Database schema management

## Production Considerations

For production deployment:

1. Use proper secrets management for database credentials
2. Configure SSL/TLS certificates for Traefik
3. Set up proper logging and monitoring
4. Use external PostgreSQL instance
5. Configure resource limits and health checks
6. Set up backup strategies
