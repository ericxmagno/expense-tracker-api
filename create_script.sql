-- schema.sql
CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

-- Users (optional)
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories table (optional, but helps validation)
CREATE TABLE IF NOT EXISTS categories (
  code VARCHAR(50) PRIMARY KEY,
  display_name VARCHAR(100) NOT NULL
);

INSERT INTO categories (code, display_name) VALUES
('FOOD', 'Food'),
('TRAVEL', 'Travel'),
('RENT', 'Rent'),
('ENT', 'Entertainment'),
('OTHER', 'Other');

-- Expenses
CREATE TABLE IF NOT EXISTS expenses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  amount DECIMAL(13,2) NOT NULL,
  category VARCHAR(50) NOT NULL,
  date DATE NOT NULL,
  description TEXT,
  user_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_category CHECK (category IN ('FOOD','TRAVEL','RENT','ENT','OTHER'))
);
