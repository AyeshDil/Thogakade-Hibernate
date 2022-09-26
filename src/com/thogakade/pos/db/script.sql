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
# ==========================


CREATE TABLE If NOT EXISTS `Order`(
    orderId VARCHAR(45),
    date VARCHAR(250),
    totalCost DOUBLE,
    customer VARCHAR(45),
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (customer) REFERENCES Customer(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `Order Details`(
    itemCode VARCHAR(45),
    orderId VARCHAR(45),
    unitPrice DOUBLE,
    qty INT,
    CONSTRAINT PRIMARY KEY (itemCode,orderId),
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES Item(code)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES `Order`(orderId)
        ON DELETE CASCADE ON UPDATE CASCADE
);

SELECT * FROM `Order`;
SELECT * FROM `Order Details`;