SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS Thogakade;
SHOW DATABASES ;
Use Thogakade;
#===================
CREATE TABLE IF NOT EXISTS Customer(
    id VARCHAR(45),
    name VARCHAR(45),
    address TEXT,
    salary DOUBLE,
    CONSTRAINT PRIMARY KEY (id)
);
DESC Customer;
#====================
SELECT * FROM Customer;