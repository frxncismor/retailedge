# Multi-stage build for Angular Storefront
FROM node:20-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json pnpm-lock.yaml ./
COPY nx.json tsconfig.base.json ./

# Install pnpm
RUN npm install -g pnpm

# Install dependencies
RUN pnpm install --frozen-lockfile

# Copy source code
COPY . .

# Build the Angular application
RUN pnpm run build:store-angular

# Production stage
FROM nginx:alpine

# Copy built application
COPY --from=builder /app/dist/apps/clients/store-angular /usr/share/nginx/html

# Copy nginx configuration
COPY apps/clients/store-angular/nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
