USE	`salesman`;

CREATE TABLE `batches` (
	id					int primary key auto_increment,
    code				char(20) -- Please make here "unique"
);

CREATE TABLE `products` (
	id					int primary key auto_increment,
    code				char(20),
    name				varchar(50),
    status				char(12)
);

CREATE TABLE `purchases` (
	id					int primary key auto_increment,
    ref_no				varchar (20) unique,
    batch_id			int,
    product_id			int,
    description			varchar(150),
    cost				double
);

CREATE TABLE `price_schedules` (	
    id					int primary key auto_increment, 
	product_id			int,
    batch_id			int,
    cost				double,
    price				double
);

CREATE TABLE `sales` (
	id					int primary key auto_increment,
    ref_no				varchar (20) unique,
    batch_id			int,
    product_id			int,
    amount				double
);

CREATE TABLE `expenses` (
	id					int primary key auto_increment,
    batch_id			int,
    description			varchar(150),
    amount				double
);




drop table `purchases`;

USE `salesman`;
SELECT * FROM `purchases`;
 