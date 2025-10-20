# RetailEdge

RetailEdge is an Nx monorepo with Angular storefront, React admin, and Spring Boot microservices for a retail e-commerce platform.

## 🏗️ Architecture

```mermaid
graph TB
    subgraph "Frontend Applications"
        A[Store Angular<br/>Port 4200]
        B[Admin React<br/>Port 3000]
    end
    
    subgraph "Backend Services"
        C[Catalog Service<br/>Port 8081]
        D[Orders Service<br/>Port 8082]
        E[Users Service<br/>Port 8083]
    end
    
    subgraph "Shared Libraries"
        F[Shared Models<br/>TypeScript Types]
    end
    
    subgraph "Infrastructure"
        G[Docker Compose]
        H[Traefik Proxy]
    end
    
    A --> C
    A --> D
    A --> E
    B --> C
    B --> D
    B --> E
    A --> F
    B --> F
    C --> F
    D --> F
    E --> F
    G --> A
    G --> B
    G --> C
    G --> D
    G --> E
    H --> A
    H --> B
```

### Frontend Applications

- **store-angular** (Port 4200) - Angular customer-facing application
- **admin-react** (Port 3000) - React admin dashboard

### Backend Services

- **catalog-service** (Port 8081) - Product catalog management
- **orders-service** (Port 8082) - Order processing and management
- **users-service** (Port 8083) - User authentication and management

### Shared Libraries

- **shared-models** - TypeScript interfaces and types shared across applications

### Infrastructure

- **Docker** - Service containerization
- **Traefik** - Reverse proxy and load balancer
- **CI/CD** - GitHub Actions for automation

## 🚀 Available Commands

```bash
# Install dependencies
npm install
# or with pnpm
pnpm install

# Run all tests
npm run test

# Run specific project tests
npm run test:storefront
npm run test:admin
npm run test:shared-models

# Run e2e tests
npm run test:e2e

# Run linting
npm run lint

# Format code
npm run format

# Build all applications
npm run build

# Build specific projects
npm run build:store-angular
npm run build:admin-react
npm run build:shared-models
npm run build:catalog-service
npm run build:orders-service
npm run build:users-service

# Serve applications
npm run serve:store-angular  # Angular app on port 4200
npm run serve:admin-react    # React app on port 3000
npm run serve:catalog-service # Spring Boot on port 8081
npm run serve:orders-service  # Spring Boot on port 8082
npm run serve:users-service   # Spring Boot on port 8083
```

## 📁 Project Structure

```
retailedge/
├── .cursor/
│   └── rules/                      # Cursor rules
├── .github/workflows/
│   └── ci.yml                      # CI/CD workflows
├── apps/
│   ├── clients/                    # Frontend applications
│   │   ├── store-angular/          # Angular customer app
│   │   │   ├── project.json
│   │   │   └── src/...
│   │   ├── store-angular-e2e/      # E2E tests for store-angular
│   │   ├── admin-react/            # React admin app
│   │   │   ├── project.json
│   │   │   └── src/...
│   │   └── admin-react-e2e/        # E2E tests for admin-react
│   └── services/                   # Backend services
│       ├── catalog-service/        # Spring Boot
│       │   ├── project.json        # Nx wrapper for mvn
│       │   ├── pom.xml
│       │   └── src/main/java/...
│       ├── orders-service/
│       │   ├── project.json
│       │   ├── pom.xml
│       │   └── src/main/java/...
│       └── users-service/
│           ├── project.json
│           ├── pom.xml
│           └── src/main/java/...
├── infra/
│   ├── docker/
│   │   ├── docker-compose.yml
│   │   ├── Dockerfile.catalog
│   │   ├── Dockerfile.orders
│   │   └── Dockerfile.users
│   └── k8s/                        # Kubernetes configs
├── libs/
│   └── shared/
│       └── models/                 # Shared TypeScript types
│           ├── project.json
│           └── src/index.ts
├── docs/
│   └── adrs/                       # Architecture Decision Records
├── nx.json                         # Nx configuration
├── package.json
├── tsconfig.base.json
└── README.md
```

## 🛠️ Technologies

- **Nx** - Monorepo management
- **Angular** - Customer-facing frontend
- **React** - Admin dashboard frontend
- **Spring Boot** - Backend microservices
- **TypeScript** - Type-safe development
- **Jest** - Unit testing
- **Cypress** - E2E testing
- **ESLint** - Code linting
- **Prettier** - Code formatting
- **pnpm** - Package management
- **Docker** - Containerization

## 🚀 Quick Start

### Prerequisites
- Node.js 18+
- Java 17+
- Maven 3.6+
- Docker (optional)

### Installation
```bash
# Clone the repository
git clone <repository-url>
cd retailedge

# Install dependencies
npm install

# Start all services
npm run serve
```

### Individual Services
```bash
# Frontend applications
npm run serve:store-angular  # http://localhost:4200
npm run serve:admin-react    # http://localhost:3000

# Backend services
npm run serve:catalog-service # http://localhost:8081
npm run serve:orders-service  # http://localhost:8082
npm run serve:users-service   # http://localhost:8083
```

### Docker
```bash
# Start all services with Docker
cd infra/docker
docker-compose up

# Or build and run individual services
docker-compose up catalog-service orders-service users-service
```

## 📚 Documentation

Each application and service has its own README with detailed instructions:

- [Store Angular](./apps/clients/store-angular/README.md) - Customer-facing application
- [Admin React](./apps/clients/admin-react/README.md) - Admin dashboard
- [Catalog Service](./apps/services/catalog-service/README.md) - Product management
- [Orders Service](./apps/services/orders-service/README.md) - Order processing
- [Users Service](./apps/services/users-service/README.md) - User management
- [Shared Models](./libs/shared/models/README.md) - Shared TypeScript types

## 📋 Architecture Decision Records

- [ADR-0001: Monorepo Architecture](./docs/adrs/0001-monorepo-architecture.md)
- [ADR-0002: Technology Stack](./docs/adrs/0002-technology-stack.md)
