CREATE TABLE orders (
	id integer NOT NULL AUTO_INCREMENT,
	order_date timestamp NULL,
	status varchar(100) NULL,
	total_amount numeric(10,3),
	CONSTRAINT order_pk PRIMARY KEY (id)
);

CREATE TABLE order_line (
	id integer NOT NULL AUTO_INCREMENT,
	order_id bigint NOT NULL,
	product_id integer,
	num_of_units integer,
	CONSTRAINT order_line_pk PRIMARY KEY (id)
);

CREATE TABLE product (
	id integer NOT NULL auto_increment,
	sku varchar(100),
	product_name varchar(100),
	description varchar(255),
	cost numeric(10,3) NULL,
	primary key(id)
);


CREATE TABLE employee (
	id integer NOT NULL AUTO_INCREMENT,
	first_name varchar(100) NOT NULL,
	last_name varchar(100) NOT NULL,
	CONSTRAINT employee_pk PRIMARY KEY (id)
);

CREATE TABLE skill (
	id integer NOT NULL AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	CONSTRAINT skill_pk PRIMARY KEY (id)
);

CREATE TABLE employee_skill (
	id integer NOT NULL AUTO_INCREMENT,
	employee_id integer,
	skill_id integer,
	CONSTRAINT employee_skill_pk PRIMARY KEY (id)
);











