-- Reset / create dummy data
USE expense_tracker;

TRUNCATE TABLE `expenses`;

INSERT INTO `expenses` (`id`,`title`,`amount`,`category`,`date`,`description`,`user_id`,`created_at`) VALUES (1,'Dinner at Pho House',550.00,'FOOD','2025-11-20','Dinner with friends',NULL,'2025-11-27 03:41:37');

INSERT INTO `expenses` (`id`,`title`,`amount`,`category`,`date`,`description`,`user_id`,`created_at`) VALUES (2,'Lunch Delivery',235.50,'FOOD','2025-11-21','Grab',NULL,'2025-11-27 12:41:37');

INSERT INTO `expenses` (`id`,`title`,`amount`,`category`,`date`,`description`,`user_id`,`created_at`) VALUES (3,'Gaming Purchase',999.00,'ENT','2025-11-22','Hades 2',NULL,'2025-11-27 19:41:37');

INSERT INTO `expenses` (`id`,`title`,`amount`,`category`,`date`,`description`,`user_id`,`created_at`) VALUES (4,'Rent November',20000.00,'ENT','2025-11-30','Rent',NULL,'2025-11-27 19:41:37');