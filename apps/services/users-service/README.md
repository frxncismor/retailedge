# Users Service

Spring Boot microservice for user authentication and management in RetailEdge platform.

## What/Why

The Users Service handles user registration, authentication, authorization, and profile management. Built with Spring Boot 3.2+ and provides RESTful APIs for user operations.

**Key Features:**
- User registration and authentication
- JWT token management
- User profile management
- Role-based access control
- Password reset functionality
- User analytics and tracking

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
npm run serve:users-service
# Or directly with Maven
mvn spring-boot:run
```

The service will be available at `http://localhost:8083`.

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
npm run test:users-service

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
mvn test -Dtest=UserControllerTest
```

## Deploy

### Build for Production
```bash
# Build JAR file
mvn clean package -Pproduction

# JAR will be in target/users-service-1.0.0.jar
```

### Docker
```bash
# Build Docker image
docker build -f infra/docker/Dockerfile.users -t retailedge-users-service .

# Run container
docker run -p 8083:8083 retailedge-users-service
```

### Health Check
```bash
# Check service health
curl http://localhost:8083/actuator/health

# Check service info
curl http://localhost:8083/actuator/info
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SERVER_PORT` | Server port | `8083` | No |
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` | No |
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:h2:mem:testdb` | No |
| `SPRING_DATASOURCE_USERNAME` | Database username | `sa` | No |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `password` | No |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | DDL mode | `create-drop` | No |
| `JWT_SECRET` | JWT signing secret | - | Yes |
| `JWT_EXPIRATION` | JWT expiration time | `86400000` | No |
| `EMAIL_SERVICE_URL` | Email service URL | - | Yes |

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password

### Users
- `GET /api/users` - List all users (admin only)
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user profile
- `DELETE /api/users/{id}` - Delete user (admin only)

### User Profile
- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update current user profile
- `PUT /api/users/profile/password` - Change password

### User Roles
- `GET /api/users/{id}/roles` - Get user roles
- `PUT /api/users/{id}/roles` - Update user roles (admin only)

## Troubleshooting

### Common Issues

**Port 8083 already in use**
```bash
# Kill process using port 8083
npx kill-port 8083
# Or change port in application.yml
```

**Database connection issues**
```bash
# Check H2 console
open http://localhost:8083/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: password
```

**JWT token issues**
```bash
# Check JWT secret is set
echo $JWT_SECRET

# Test token generation
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'
```

### Debug Mode
```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5007"

# Enable SQL logging
export SPRING_JPA_SHOW_SQL=true
mvn spring-boot:run
```
