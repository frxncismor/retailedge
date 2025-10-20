# Shared Models

TypeScript interfaces and types shared across RetailEdge applications.

## What/Why

The Shared Models library provides consistent type definitions across all frontend and backend applications. This ensures type safety and reduces duplication of interface definitions.

**Key Features:**
- Product and order type definitions
- API response types
- User and authentication types
- Common utility types
- Enum definitions

## Run locally

### Prerequisites
- Node.js 18+
- TypeScript 4.9+

### Installation
```bash
# Install dependencies
npm install

# Build the library
npm run build:shared-models
```

### Development
```bash
# Build with watch mode
npx nx build shared-models --watch

# Build for production
npx nx build shared-models --configuration=production
```

## Test

### Unit Tests
```bash
# Run unit tests
npm run test:shared-models

# Run with coverage
npx nx test shared-models --coverage
```

### Type Checking
```bash
# Check TypeScript types
npx tsc --noEmit

# Build and check types
npx nx build shared-models
```

## Deploy

### Build for Production
```bash
# Build the library
npx nx build shared-models --configuration=production

# Output will be in dist/libs/shared/models/
```

### Publish to NPM
```bash
# Build and publish
npm run build:shared-models
npm publish dist/libs/shared/models
```

## Usage

### Import Types
```typescript
import { Product, Order, OrderStatus, Customer } from '@retailedge/shared-models';

// Use in your application
const product: Product = {
  id: '1',
  name: 'Sample Product',
  description: 'Product description',
  price: 99.99,
  category: 'Electronics',
  inStock: true,
  createdAt: new Date(),
  updatedAt: new Date()
};
```

### API Response Types
```typescript
import { ApiResponse, PaginatedResponse } from '@retailedge/shared-models';

// API response wrapper
const response: ApiResponse<Product> = {
  data: product,
  message: 'Success',
  success: true,
  timestamp: new Date()
};

// Paginated response
const paginatedResponse: PaginatedResponse<Product> = {
  data: [product1, product2],
  total: 100,
  page: 1,
  limit: 10,
  hasNext: true,
  hasPrev: false
};
```

## Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `NODE_ENV` | Environment | `development` | No |
| `TSCONFIG_PATH` | TypeScript config path | `tsconfig.json` | No |

## Troubleshooting

### Common Issues

**Type errors after changes**
```bash
# Clean and rebuild
npx nx reset
npx nx build shared-models

# Check TypeScript configuration
npx tsc --showConfig
```

**Import path issues**
```bash
# Check tsconfig.base.json paths
cat tsconfig.base.json | grep shared-models

# Verify build output
ls -la dist/libs/shared/models/
```

**Test failures**
```bash
# Clear Jest cache
npx jest --clearCache

# Run tests with verbose output
npx nx test shared-models --verbose
```

### Debug Mode
```bash
# Run with debug logging
DEBUG=* npx nx build shared-models

# Run tests with debug output
npx nx test shared-models --verbose --detectOpenHandles
```