-- Initialize RetailEdge Database
-- This script runs when PostgreSQL container starts for the first time

-- Create database if it doesn't exist (already created by POSTGRES_DB)
-- CREATE DATABASE IF NOT EXISTS retailedge;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schemas for each service (optional, for better organization)
-- CREATE SCHEMA IF NOT EXISTS catalog;
-- CREATE SCHEMA IF NOT EXISTS orders;
-- CREATE SCHEMA IF NOT EXISTS users;

-- The tables will be created automatically by Hibernate
-- when the Spring Boot applications start with ddl-auto=update

-- Insert some sample data (optional)
-- This will be populated by the applications or data migration scripts
