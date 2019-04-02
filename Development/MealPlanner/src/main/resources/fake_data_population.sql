use mealplanner;

INSERT INTO user
(email, username, password)
VALUES
("bono@republicofireland.gov", "bono", "atomicbomb551"),
("vladdyp@ru.gov", "puut", "wegotdonny410"),
("realg@compton.ca", "homies", "theirfrickinplace412"),
("god@god.god", "god", "pass123"),
("chicken@wing.aint", "nothing", "buta0910"),
("fightthepower@truth.org", "tinfoil", "hat911insidejob"),
("bob@hotmail.com", "iam", "veryold722"),
("dannyd@gmail.com", "danny", "kickit001"),
("whatwedoing@tpb.org", "pirateparty", "itslegalbro808"),
("malaysian@airlinehackers.where", "arethey", "rightnow222"),
("donotreply@ashleymadison.com", "sorry", "yougothacked404");

select * from user;

INSERT INTO product_stock
(NDB_Number, user_id)
VALUES
(45102352, 1),
(45102152, 9),
(45102120, 4),
(45112120, 11),
(45112199, 8),
(45112419, 4),
(45112459, 3),
(45112439, 1),
(45112639, 6),
(45122639, 10);

select * from product_stock;

INSERT INTO intake_stock
(intake_id, stock_id, user_id, intake_time, servings)
VALUES
(1, 1, 1, '2018-12-26 23:50:30', 1),
(2, 2, 9, '2019-02-21 13:30:00', 3),
(3, 3, 4, '2019-03-08 8:45:30', 5),
(4, 4, 11, '2018-12-25 15:10:00', 1),
(5, 5, 8, '2019-04-01 7:15:30', 2),
(6, 6, 4, '2019-01-06 22:10:00', 6),
(7, 7, 3, '2018-12-08 3:00:00', 1),
(8, 8, 1, '2019-01-29 20:00:30', 4),
(9, 9, 6, '2018-11-03 9:55:00', 1),
(10, 10, 10, '2019-3-16 10:30:30', 1);

select * from intake_stock;

INSERT INTO stock_item
(quantity, expiration_date)
VALUES
(10, '2019-12-26 23:59:59'),
(25, '2019-6-20 23:59:59'),
(5, '2019-11-15 23:59:59'),
(10, '2018-12-29 23:59:59'),
(40, '2020-5-06 23:59:59'),
(6, '2019-01-12 23:59:59'),
(4, '2019-02-10 23:59:59'),
(5, '2021-12-02 23:59:59'),
(10, '2018-09-16 23:59:59'),
(1, '2018-03-20 23:59:59');

select * from stock_item;

INSERT INTO follow
(follower_id, followee_id)
VALUES
(1, 10),
(6, 8),
(8, 6),
(4, 2),
(1, 9),
(11, 3),
(7, 1),
(4, 9),
(1, 5),
(5, 1);

select * from follow;
