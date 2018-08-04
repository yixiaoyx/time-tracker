CREATE USER 'trackattack'@'localhost' IDENTIFIED BY '4920';
CREATE DATABASE IF NOT EXISTS TrackerDB;
GRANT ALL PRIVILEGES ON TrackerDB.* TO 'trackattack'@'localhost';
FLUSH PRIVILEGES;
USE TrackerDB;
