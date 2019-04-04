use mealplanner;

delimiter //

    DROP PROCEDURE IF EXISTS wipe_db //
    CREATE PROCEDURE wipe_db(thresh INT)
    BEGIN

        SET SQL_SAFE_UPDATES = 0;
        DELETE FROM intake_stock WHERE user_id < thresh;
        DELETE FROM intake_recipe WHERE user_id < thresh;
        DELETE FROM product_stock WHERE user_id < thresh;
        DELETE FROM recipe_has_product WHERE recipe_id < 6;
        DELETE FROM rating WHERE recipe_id < 6;
        DELETE FROM recipe WHERE creator_id < thresh;
        DELETE FROM follow WHERE follower_id < thresh OR followee_id < thresh;
        DELETE FROM user WHERE user_id < thresh;
        SET SQL_SAFE_UPDATES = 1;

    END //

    

    DROP PROCEDURE IF EXISTS init_db //
    CREATE PROCEDURE init_db()

    BEGIN

        CALL wipe_db(12);

        INSERT INTO user
        (user_id, email, username, password)
        VALUES
        (1, "bono@republicofireland.gov", "bono", "atomicbomb551"),
        (2, "vladdyp@ru.gov", "puut", "wegotdonny410"),
        (3, "realg@compton.ca", "homies", "theirfrickinplace412"),
        (4, "god@god.god", "god", "pass123"),
        (5, "chicken@wing.aint", "nothing", "buta0910"),
        (6, "fightthepower@truth.org", "tinfoil", "hat911insidejob"),
        (7, "bob@hotmail.com", "iam", "veryold722"),
        (8, "dannyd@gmail.com", "danny", "kickit001"),
        (9, "whatwedoing@tpb.org", "pirateparty", "itslegalbro808"),
        (10, "malaysian@airlinehackers.where", "arethey", "rightnow222"),
        (11, "donotreply@ashleymadison.com", "sorry", "yougothacked404");

        INSERT INTO product_stock
        (stock_id, NDB_Number, user_id)
        VALUES
        (1, 45102352, 1),
        (2, 45102152, 9),
        (3, 45102120, 4),
        (4, 45112120, 11),
        (5, 45112199, 8),
        (6, 45112419, 4),
        (7, 45112459, 3),
        (8, 45112439, 1),
        (9, 45112639, 6),
        (10, 45122639, 10);
        
        INSERT INTO stock_item
        (stock_id, stock_item_id, quantity, expiration_date)
        VALUES
        (1, 1, 14, '2019-12-26'),
        (2, 2, 18, '2019-6-20'),
        (3, 3, 20, '2019-11-15'),
        (4, 4, 30, '2018-12-29'),
        (5, 5, 45, '2020-5-06'),
        (6, 6, 12, '2019-01-12'),
        (7, 7, 4, '2019-02-10'),
        (8, 8, 10, '2021-12-02'),
        (9, 9, 9, '2018-09-16'),
        (10, 10, 18, '2018-03-20');

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
        
        INSERT INTO recipe
		(recipe_id, creator_id, instructions, name, description, yield, created_at)
		VALUES
		(1, 7, "Place ice cream scoop in glass. Pour root beer over ice cream.", "Root Beer Float", "Root beer, ice cream", 1, '2018-12-08'),
		(2, 4, "Mix dry ingredients and wet ingredients separately. Slowly add wet ingredients to dry ingredients. 
		Place in an oiled pan and bake at 350 degrees until a knife comes out cleanly", "Brownies", "Brownie mix, egg, water, butter", 12, '2019-03-25'),
		(3, 11, "Lightly toast garlic and chilis in pan with olive oil. Turn off the heat and add pasta. 
		Squeeze lemon juice over top and garnish with parsley", "Pasta Aglio Olio", "Spaghetti, olive oil, garlic, chili pepper, lemon, parsley", 
		3, '2019-02-21'),
		(4, 8, "Place sugar in pot on medium heat. When sugar reaches 150 degrees, add cream and reduce heat. 
		Take off stove and allow to cool and harden.", "Caramel", "White sugar, cream", 8, '2018-10-09'),
		(5, 5, "Dip chicken first in egg, then in flour. Drop into oil at 350 degrees. Remove when exterior is crispy and golden brown",
		"Fried Chicken", "Boneless chicken, egg, wheat flour, canola oil", 4, '2019-01-02');
        
        INSERT INTO product_stock
        (stock_id, NDB_Number, user_id)
        VALUES
        (11, 45137355, 1),
		(12, 45181550, 1),
		(13, 45085287, 2),
		(14, 45263540, 2),
		(15, 45006044, 2),
		(16, 45028750, 1),
		(17, 45015429, 1),
		(18, 45182100, 1),
		(19, 45209597, 1),
		(20, 45060440, 1);
        
        INSERT INTO stock_item
        (stock_id, stock_item_id, quantity, expiration_date)
        VALUES
        (11, 11, 4, '2019-12-26'),
        (12, 12, 8, '2019-6-20'),
        (13, 13, 3, '2019-11-15'),
        (14, 14, 9, '2018-12-29'),
        (15, 15, 5, '2020-5-06'),
        (16, 16, 12, '2019-01-12'),
        (17, 17, 8, '2019-02-10'),
        (18, 18, 7, '2021-12-02'),
        (19, 19, 4, '2018-09-16'),
        (20, 20, 8, '2018-03-20');

		INSERT INTO recipe_has_product
		(recipe_id, NDB_Number, servings)
		VALUES
		(1, 45137355, 1),
		(1, 45181550, 1),
		(2, 45085287, 1),
		(2, 45263540, 1),
		(2, 45006044, 1),
		(3, 45028750, 1),
		(3, 45015429, 1),
		(3, 45182100, 4),
		(3, 45209597, 2),
		(3, 45060440, 1),
		(4, 45012164, 4),
		(4, 45148284, 1),
		(5, 45120912, 4),
		(5, 45362972, 3),
		(5, 45018668, 3),
		(5, 45001645, 2);
        
        INSERT INTO intake_recipe
        (intake_id, user_id, recipe_id, servings, intake_time) VALUES
        (11, 1, 1, 1, CURTIME()),
        (12, 1, 3, 12, '2019-02-21 13:30:00'),
        (13, 2, 2, 3, '2019-04-21 18:30:00');

		INSERT INTO rating
		(recipe_id, user_id, score)
		VALUES
		(1, 8, '5'),
		(1, 4, '4'),
		(1, 11, '3'),
		(2, 4, '2'),
		(2, 1, '4'),
		(3, 3, '5'),
		(3, 10, '4'),
		(4, 1, '1'),
		(4, 9, '2'),
		(5, 4, '3');

    END //

delimiter ;

CALL init_db();