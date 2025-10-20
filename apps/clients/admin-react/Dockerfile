# Multi-stage build for React Admin
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

# Build the React application
RUN pnpm run build:admin-react

# Production stage
FROM nginx:alpine

# Copy built application
COPY --from=builder /app/dist/apps/clients/admin-react /usr/share/nginx/html

# Copy nginx configuration
COPY apps/clients/admin-react/nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
