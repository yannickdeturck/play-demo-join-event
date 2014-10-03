# --- Initial creation of the database schema

# --- !Ups
CREATE TABLE PIZZA (
  pizzaID int primary key AUTO_INCREMENT NOT NULL,
  pizzaName varchar(25) NOT NULL,
  hasTomato boolean NOT NULL,
  hasMozzarella boolean NOT NULL,
  hasCheese boolean NOT NULL,
  hasHam boolean NOT NULL,
  hasSalami boolean NOT NULL
);

CREATE TABLE PIZZAORDER (
  orderID int primary key AUTO_INCREMENT NOT NULL,
  pizzaID int NOT NULL,
  quantity int NOT NULL,
  orderDate date NOT NULL,
  customerName varchar(25) NOT NULL,
  remarks varchar(255),
  isSent boolean NOT NULL,
  sentDate date,
  FOREIGN KEY (pizzaID) REFERENCES PIZZA(pizzaID)
);

# --- !Downs
DROP TABLE IF EXISTS TITEL;
DROP TABLE IF EXISTS ROOSTER;

