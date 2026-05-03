INSERT INTO orders
(order_date, status, total_amount)
VALUES('2026-01-20 00:00:00','IN PROCESS', 1500.00);

INSERT INTO orders
(order_date, status, total_amount)
VALUES('2026-02-20 00:00:00','COMPLETE', 1000.00);

INSERT INTO orders
(order_date, status, total_amount)
VALUES('2026-03-20 00:00:00','COMPLETE', 500.00);

INSERT INTO orders
(order_date, status, total_amount)
VALUES('2026-04-20 00:00:00','HOLD', 0);


INSERT INTO order_line
(order_id, product_id, num_of_units)
VALUES(1, 1, 10);

INSERT INTO order_line
(order_id, product_id, num_of_units)
VALUES(1, 2, 5);

INSERT INTO order_line
(order_id, product_id, num_of_units)
VALUES(2, 3, 10);

INSERT INTO order_line
(order_id, product_id, num_of_units)
VALUES(3, 3, 5);

INSERT INTO product
(sku, product_name, cost, description)
VALUES('sku#1001', 'chair', 100.00, 'chair something');
INSERT INTO product
(sku, product_name, cost, description)
VALUES('sku#1002', 'table', 100.00, 'table something');
INSERT INTO product
(sku, product_name, cost, description)
VALUES('sku#1003', 'sofa', 100.00, 'sofa something');

INSERT INTO employee
(last_name, first_name)
VALUES ('emp1 last', 'emp1 first');

INSERT INTO employee
(last_name, first_name)
VALUES ('emp2 last', 'emp2 first');

INSERT INTO employee
(last_name, first_name)
VALUES ('emp3 last', 'emp3 first');

INSERT INTO employee
(last_name, first_name)
VALUES ('emp4 last', 'emp4 first');

INSERT INTO skill
(name)
VALUES ('java');

INSERT INTO skill
(name)
VALUES ('spring');

INSERT INTO skill
(name)
VALUES ('python');

INSERT INTO skill
(name)
VALUES ('ruby');


INSERT INTO employee_skill
(employee_id, skill_id)
VALUES (1, 1);

INSERT INTO employee_skill
(employee_id, skill_id)
VALUES (1, 2);

INSERT INTO employee_skill
(employee_id, skill_id)
VALUES (3, 2);

INSERT INTO employee_skill
(employee_id, skill_id)
VALUES (3, 3);

INSERT INTO employee_skill
(employee_id, skill_id)
VALUES (3, 4);


