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
code;
this.description = description;
this.unitPrice = unitPrice;
this.qtyOnHand = qtyOnHand;

SHOW TABLES ;
CREATE TABLE IF NOT EXISTS Item(
    code VARCHAR(45),
    description VARCHAR(100),
    unitPrice DOUBLE,
    qtyOnHand INTEGER,
    CONSTRAINT PRIMARY KEY (code)
);
DESC Item;
SELECT * FROM Item;