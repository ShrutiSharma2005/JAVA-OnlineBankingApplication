CREATE DATABASE IF NOT EXISTS online_bank;

USE online_bank;

-- Drop tables if they exist to allow clean re-initialization
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    salt VARCHAR(32) NOT NULL,
    balance DOUBLE DEFAULT 0.0,
    savings_goal_name VARCHAR(100) DEFAULT NULL,
    savings_goal_target DOUBLE DEFAULT 0.0,
    savings_goal_current DOUBLE DEFAULT 0.0
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    type VARCHAR(20) NOT NULL, -- 'Deposit', 'Withdraw', 'Transfer Out', 'Transfer In', 'Savings'
    amount DOUBLE NOT NULL,
    category VARCHAR(50) DEFAULT 'Others', -- 'Food', 'Shopping', 'Utilities', 'Investment', 'Entertainment', 'Others'
    description VARCHAR(255) DEFAULT '',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

