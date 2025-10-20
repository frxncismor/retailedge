# Admin React

React admin dashboard for RetailEdge e-commerce platform management.

## What/Why

The Admin React application provides administrative interface for managing products, orders, users, and system configuration. Built with React 18+ and designed for efficient data management and analytics.

**Key Features:**
- Product management (CRUD operations)
- Order management and status updates
- User management and permissions
- Analytics dashboard
- System configuration
- Real-time notifications

## Run locally

### Prerequisites
- Node.js 18+
- npm or pnpm

### Installation
```bash
# Install dependencies
npm install

# Serve the application
npm run serve:admin-react
```

The application will be available at `http://localhost:3000`.

### Development
```bash
# Run with watch mode
npx nx serve admin-react

# Build for production
npx nx build admin-react
```

## Test

### Unit Tests
```bash
# Run unit tests
npm run test:admin-react

# Run with coverage
npx nx test admin-react --coverage
```

### E2E Tests
```bash
# Run e2e tests
npm run test:e2e:admin-react

# Run e2e tests in headed mode
npx nx e2e admin-react-e2e --headed
```

## Deploy

### Build for Production
```bash
# Build the application
npx nx build admin-react --configuration=production

# Output will be in dist/apps/clients/admin-react/
```

### Docker
```bash
# Build Docker image
docker build -f infra/docker/Dockerfile.admin-react -t retailedge-admin-react .

# Run container
docker run -p 3000:80 retailedge-admin-react
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `API_BASE_URL` | Backend API base URL | `http://localhost:8081` | No |
| `CATALOG_SERVICE_URL` | Catalog service URL | `http://localhost:8081` | No |
| `ORDERS_SERVICE_URL` | Orders service URL | `http://localhost:8082` | No |
| `USERS_SERVICE_URL` | Users service URL | `http://localhost:8083` | No |
| `ADMIN_API_KEY` | Admin API authentication key | - | Yes |

## Troubleshooting

### Common Issues

**Port 3000 already in use**
```bash
# Kill process using port 3000
npx kill-port 3000
# Or use different port
npx nx serve admin-react --port 3001
```

**Build fails with memory issues**
```bash
# Increase Node.js memory
export NODE_OPTIONS="--max-old-space-size=4096"
npx nx build admin-react
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
DEBUG=* npx nx serve admin-react

# Run tests with verbose output
npx nx test admin-react --verbose
```
