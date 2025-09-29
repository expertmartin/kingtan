--  CREATE DATABASE db_users;

--  CREATE USER app_user WITH PASSWORD 'securepassword';
--  GRANT ALL PRIVILEGES ON DATABASE user_management TO app_user;

  -- Users table
  CREATE TABLE IF NOT EXISTS users (
      id BIGSERIAL PRIMARY KEY,
      username VARCHAR(50) UNIQUE NOT NULL,
      email VARCHAR(100) UNIQUE NOT NULL,
      password VARCHAR(255) NOT NULL,
      enabled BOOLEAN NOT NULL DEFAULT TRUE
  );

  -- Roles table
  CREATE TABLE IF NOT EXISTS roles (
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(50) UNIQUE NOT NULL
  );

  -- Junction table for user-role relationship
  CREATE TABLE IF NOT EXISTS user_roles (
      user_id BIGINT NOT NULL,
      role_id BIGINT NOT NULL,
      PRIMARY KEY (user_id, role_id),
      FOREIGN KEY (user_id) REFERENCES users(id),
      FOREIGN KEY (role_id) REFERENCES roles(id)
  );

  CREATE TABLE IF NOT EXISTS password_reset_token (
      id BIGSERIAL PRIMARY KEY,
      token VARCHAR(255) NOT NULL,
      user_id BIGINT NOT NULL,
      expiry_date TIMESTAMP NOT NULL,
      FOREIGN KEY (user_id) REFERENCES users(id)
  );