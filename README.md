# RetailEdge

RetailEdge is an Nx monorepo with Angular storefront, React admin, and shared models library for a retail application.

## 🏗️ Architecture

### Frontend Applications

- **storefront** (Port 4200) - Angular customer-facing application
- **admin** (Port 3000) - React admin dashboard

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
npm run build:storefront
npm run build:admin
npm run build:shared-models

# Serve applications
npm run serve:storefront  # Angular app on port 4200
npm run serve:admin       # React app on port 3000
```

## 📁 Project Structure

```
retailedge/
├── apps/
│   ├── storefront/        # Angular customer app
│   ├── storefront-e2e/    # E2E tests for storefront
│   ├── admin/             # React admin app
│   └── admin-e2e/         # E2E tests for admin
├── libs/
│   └── shared-models/     # Shared TypeScript types
├── .npmrc                 # pnpm configuration
└── nx.json               # Nx configuration
```

## 🛠️ Technologies

- **Nx** - Monorepo management
- **Angular** - Customer-facing frontend
- **React** - Admin dashboard frontend
- **TypeScript** - Type-safe development
- **Jest** - Unit testing
- **Cypress** - E2E testing
- **ESLint** - Code linting
- **Prettier** - Code formatting
- **pnpm** - Package management
