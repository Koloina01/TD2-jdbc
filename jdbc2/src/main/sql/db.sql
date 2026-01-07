CREATE DATABASE mini_football_db;
\c mini_football_db;

CREATE USER mini_football_db_manager WITH PASSWORD '123456';
GRANT CONNECT ON DATABASE mini_football_db TO mini_football_db_manager;
GRANT USAGE ON SCHEMA public TO mini_football_db_manager;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO mini_football_db_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO mini_football_db_manager;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO mini_football_db_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL PRIVILEGES ON SEQUENCES TO mini_football_db_manager;