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

DELETE from recipe;

INSERT INTO recipe
(creator_id, instructions, name, description, yield, created_at)
VALUES
(7, "Place ice cream scoop in glass. Pour root beer over ice cream.", "Root Beer Float", "Root beer, ice cream", 1, '2018-12-08'),
(4, "Mix dry ingredients and wet ingredients separately. Slowly add wet ingredients to dry ingredients. 
Place in an oiled pan and bake at 350 degrees until a knife comes out cleanly", "Brownies", "Brownie mix, egg, water, butter", 12, '2019-03-25'),
(11, "Lightly toast garlic and chilis in pan with olive oil. Turn off the heat and add pasta. 
Squeeze lemon juice over top and garnish with parsley", "Pasta Aglio Olio", "Spaghetti, olive oil, garlic, chili pepper, lemon, parsley", 
3, '2019-02-21'),
(8, "Place sugar in pot on medium heat. When sugar reaches 150 degrees, add cream and reduce heat. 
Take off stove and allow to cool and harden.", "Caramel", "White sugar, cream", 8, '2018-10-09'),
(5, "Dip chicken first in egg, then in flour. Drop into oil at 350 degrees. Remove when exterior is crispy and golden brown",
"Fried Chicken", "Boneless chicken, egg, wheat flour, canola oil", 4, '2019-01-02');

select * from recipe;

INSERT INTO recipe_has_product
(recipe_id, NDB_Number, servings)
VALUES
(11, 45137355, 1),
(11, 45181550, 1),
(12, 45085287, 1),
(12, 45263540, 1),
(12, 45006044, 1),
(13, 45028750, 1),
(13, 45015429, 1),
(13, 45182100, 4),
(13, 45209597, 2),
(13, 45060440, 1),
(14, 45012164, 4),
(14, 45148284, 1),
(15, 45120912, 4),
(15, 45362972, 3),
(15, 45018668, 3),
(15, 45001645, 2);

INSERT INTO rating
(recipe_id, user_id, score)
VALUES
(11, 8, '5'),
(11, 4, '4'),
(11, 11, '3'),
(12, 4, '2'),
(12, 1, '4'),
(13, 3, '5'),
(13, 10, '4'),
(14, 1, '1'),
(14, 9, '2'),
(15, 4, '3');

delete from rating;

select * from recipe_has_product;

select * from rating;
