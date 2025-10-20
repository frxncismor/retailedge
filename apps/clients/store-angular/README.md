# Store Angular

Angular customer-facing application for RetailEdge e-commerce platform.

## What/Why

The Store Angular application provides the customer interface for browsing products, managing cart, and placing orders. Built with Angular 17+ and designed for optimal user experience across desktop and mobile devices.

**Key Features:**
- Product catalog browsing and search
- Shopping cart management
- User authentication and profile
- Order placement and tracking
- Responsive design

## Run locally

### Prerequisites
- Node.js 18+
- npm or pnpm

### Installation
```bash
# Install dependencies
npm install

# Serve the application
npm run serve:store-angular
```

The application will be available at `http://localhost:4200`.

### Development
```bash
# Run with watch mode
npx nx serve store-angular

# Build for production
npx nx build store-angular
```

## Test

### Unit Tests
```bash
# Run unit tests
npm run test:store-angular

# Run with coverage
npx nx test store-angular --coverage
```

### E2E Tests
```bash
# Run e2e tests
npm run test:e2e:store-angular

# Run e2e tests in headed mode
npx nx e2e store-angular-e2e --headed
```

## Deploy

### Build for Production
```bash
# Build the application
npx nx build store-angular --configuration=production

# Output will be in dist/apps/clients/store-angular/
```

### Docker
```bash
# Build Docker image
docker build -f infra/docker/Dockerfile.store-angular -t retailedge-store-angular .

# Run container
docker run -p 4200:80 retailedge-store-angular
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `API_BASE_URL` | Backend API base URL | `http://localhost:8081` | No |
| `CATALOG_SERVICE_URL` | Catalog service URL | `http://localhost:8081` | No |
| `ORDERS_SERVICE_URL` | Orders service URL | `http://localhost:8082` | No |
| `USERS_SERVICE_URL` | Users service URL | `http://localhost:8083` | No |

## Troubleshooting

### Common Issues

**Port 4200 already in use**
```bash
# Kill process using port 4200
npx kill-port 4200
# Or use different port
npx nx serve store-angular --port 4201
```

**Build fails with memory issues**
```bash
# Increase Node.js memory
export NODE_OPTIONS="--max-old-space-size=4096"
npx nx build store-angular
```

**E2E tests fail**
```bash
# Clear Cypress cache
npx cypress cache clear
# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
```

### Debug Mode
```bash
# Run with debug logging
DEBUG=* npx nx serve store-angular

# Run tests with verbose output
npx nx test store-angular --verbose
```
