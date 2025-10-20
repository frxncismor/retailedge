# Catalog Service

Spring Boot microservice for product catalog management in RetailEdge platform.

## What/Why

The Catalog Service manages product information, categories, inventory, and search functionality. Built with Spring Boot 3.2+ and provides RESTful APIs for product operations.

**Key Features:**
- Product CRUD operations
- Category management
- Product search and filtering
- Inventory tracking
- Product recommendations
- Image management

## Run locally

### Prerequisites
- Java 17+
- Maven 3.6+
- H2 Database (embedded)

### Installation
```bash
# Install dependencies
mvn clean install

# Run the application
npm run serve:catalog-service
# Or directly with Maven
mvn spring-boot:run
```

The service will be available at `http://localhost:8081`.

### Development
```bash
# Run with Maven
mvn spring-boot:run

# Build the application
mvn clean package

# Run tests
mvn test
```

## Test

### Unit Tests
```bash
# Run unit tests
npm run test:catalog-service

# Run with Maven
mvn test

# Run with coverage
mvn test jacoco:report
```

### Integration Tests
```bash
# Run integration tests
mvn verify

# Run specific test class
mvn test -Dtest=ProductControllerTest
```

## Deploy

### Build for Production
```bash
# Build JAR file
mvn clean package -Pproduction

# JAR will be in target/catalog-service-1.0.0.jar
```

### Docker
```bash
# Build Docker image
docker build -f infra/docker/Dockerfile.catalog -t retailedge-catalog-service .

# Run container
docker run -p 8081:8081 retailedge-catalog-service
```

### Health Check
```bash
# Check service health
curl http://localhost:8081/actuator/health

# Check service info
curl http://localhost:8081/actuator/info
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SERVER_PORT` | Server port | `8081` | No |
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` | No |
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:h2:mem:testdb` | No |
| `SPRING_DATASOURCE_USERNAME` | Database username | `sa` | No |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `password` | No |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | DDL mode | `create-drop` | No |

## API Endpoints

### Products
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Categories
- `GET /api/categories` - List all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category

### Search
- `GET /api/products/search?q={query}` - Search products
- `GET /api/products/filter?category={id}` - Filter by category

## Troubleshooting

### Common Issues

**Port 8081 already in use**
```bash
# Kill process using port 8081
npx kill-port 8081
# Or change port in application.yml
```

**Database connection issues**
```bash
# Check H2 console
open http://localhost:8081/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: password
```

**Maven build fails**
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean package -DskipTests
```

### Debug Mode
```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Enable SQL logging
export SPRING_JPA_SHOW_SQL=true
mvn spring-boot:run
```
