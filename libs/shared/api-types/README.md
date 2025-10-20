# API Types

This library contains TypeScript types generated from OpenAPI specifications for all RetailEdge services.

## Generated Types

- **Catalog Service** (`catalog.ts`) - Product management types
- **Orders Service** (`orders.ts`) - Order and order item types
- **Users Service** (`users.ts`) - Customer and authentication types

## Usage

Import types from the main index:

```typescript
import { Product, Order, Customer } from '@retailedge/api-types';
```

Or import from specific service modules:

```typescript
import { Product, ProductRequest } from '@retailedge/api-types/catalog';
import { Order, OrderStatus } from '@retailedge/api-types/orders';
import { Customer, Address } from '@retailedge/api-types/users';
```

## Regenerating Types

To regenerate types from live OpenAPI specifications:

```bash
# Generate all types
npm run generate:api-types

# Generate specific service types
npm run generate:api-types:catalog
npm run generate:api-types:orders
npm run generate:api-types:users
```

**Note:** Services must be running for type generation to work.

## Development

The types in this library are automatically generated from OpenAPI specifications. Manual modifications will be overwritten during regeneration.

For development, use the temporary type files until services are available, then regenerate from live specifications.
