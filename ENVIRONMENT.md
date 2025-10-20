# Environment Configuration

This document explains how to configure environment variables for the RetailEdge project.

## Environment Files

### `env.example`

Template file containing all available environment variables with default values. Copy this to `.env` for local development:

```bash
cp env.example .env
```

### `docker.env`

Environment variables specifically for Docker Compose. This file is used by `docker-compose.yml` to configure all services.

## Environment Variables

### Database Configuration

- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_DATASOURCE_DRIVER`: JDBC driver class

### Service Configuration

- `SERVER_PORT`: Port for each service (8081, 8082, 8083)
- `SPRING_APPLICATION_NAME`: Application name for each service

### JPA Configuration

- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL mode (update, create-drop, etc.)
- `SPRING_JPA_SHOW_SQL`: Enable SQL logging

### Management & Monitoring

- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE`: Actuator endpoints to expose
- `MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS`: Health check detail level

### SpringDoc OpenAPI

- `SPRINGDOC_API_DOCS_PATH`: API docs endpoint path
- `SPRINGDOC_SWAGGER_UI_PATH`: Swagger UI path

### Logging

- `LOGGING_LEVEL_*`: Log levels for different packages

### CORS Configuration

- `CORS_ALLOWED_ORIGINS`: Allowed origins for CORS
- `CORS_ALLOWED_METHODS`: Allowed HTTP methods
- `CORS_ALLOWED_HEADERS`: Allowed headers

### Security

- `JWT_SECRET`: JWT signing secret
- `JWT_EXPIRATION`: JWT token expiration time

## Usage

### Local Development

1. Copy `env.example` to `.env`
2. Modify values as needed
3. Run services with `nx serve` commands

### Docker Development

1. Modify `docker.env` if needed
2. Run `docker-compose up` or `make up`

### Production

1. Set environment variables in your deployment platform
2. Ensure sensitive values are properly secured
3. Use strong passwords and secrets

## Security Notes

- Never commit `.env` files to version control
- Use strong, unique passwords for production
- Rotate JWT secrets regularly
- Limit CORS origins to known domains
- Use environment-specific configurations
