# Orders Service

Spring Boot microservice for order management and processing in RetailEdge platform.

## What/Why

The Orders Service handles order lifecycle, payment processing, inventory updates, and order tracking. Built with Spring Boot 3.2+ and provides RESTful APIs for order operations.

**Key Features:**
- Order creation and management
- Order status tracking
- Payment processing integration
- Inventory updates
- Order history and analytics
- Notification triggers

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
npm run serve:orders-service
# Or directly with Maven
mvn spring-boot:run
```

The service will be available at `http://localhost:8082`.

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
npm run test:orders-service

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
mvn test -Dtest=OrderControllerTest
```

## Deploy

### Build for Production
```bash
# Build JAR file
mvn clean package -Pproduction

# JAR will be in target/orders-service-1.0.0.jar
```

### Docker
```bash
# Build Docker image
docker build -f infra/docker/Dockerfile.orders -t retailedge-orders-service .

# Run container
docker run -p 8082:8082 retailedge-orders-service
```

### Health Check
```bash
# Check service health
curl http://localhost:8082/actuator/health

# Check service info
curl http://localhost:8082/actuator/info
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SERVER_PORT` | Server port | `8082` | No |
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` | No |
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:h2:mem:testdb` | No |
| `SPRING_DATASOURCE_USERNAME` | Database username | `sa` | No |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `password` | No |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | DDL mode | `create-drop` | No |
| `PAYMENT_SERVICE_URL` | Payment service URL | - | Yes |
| `INVENTORY_SERVICE_URL` | Inventory service URL | - | Yes |

## API Endpoints

### Orders
- `GET /api/orders` - List all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}` - Update order
- `PUT /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

### Order Items
- `GET /api/orders/{id}/items` - Get order items
- `POST /api/orders/{id}/items` - Add item to order
- `PUT /api/orders/{id}/items/{itemId}` - Update order item
- `DELETE /api/orders/{id}/items/{itemId}` - Remove item from order

### Order Status
- `GET /api/orders/{id}/status` - Get order status
- `PUT /api/orders/{id}/status` - Update order status

## Troubleshooting

### Common Issues

**Port 8082 already in use**
```bash
# Kill process using port 8082
npx kill-port 8082
# Or change port in application.yml
```

**Database connection issues**
```bash
# Check H2 console
open http://localhost:8082/h2-console
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
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5006"

# Enable SQL logging
export SPRING_JPA_SHOW_SQL=true
mvn spring-boot:run
```
