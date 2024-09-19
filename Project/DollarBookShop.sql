create database dollarbookshop;
use dollarbookshop;

create table products(
  ProductID char(5) primary key,
  Name varchar(50) not null,
  Genre varchar(50) not null,
  Stock int not null,
  Price int not null);

create table users(
    UserID char(5) primary key,
    Email varchar(50) not null,
    Username varchar(50) not null,
    Password varchar(50) not null,
    DOB date not null,
    Role varchar(10) not null);

create table carts(
  UserID char(5) not null,
  ProductID char(5) not null,
  Quantity int not null,
  primary key (UserID,ProductID),
  foreign key (UserID) references users(UserID),
  foreign key (ProductID) references products(ProductID));

create table transaction_header(
  TransactionID char(5) primary key,
  UserID char(5) not null,
  TransactionDate date not null,
  foreign key (UserID) references users(UserID));

create table transaction_details(
  TransactionID char(5) not null,
  ProductID char(5) not null,
  Quantity int not null,
  primary key (TransactionID, ProductID),
  foreign key (TransactionID) references transaction_header(TransactionID),
  foreign key (ProductID) references products(ProductID));