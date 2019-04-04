use mealplanner;

delimiter //
	DROP PROCEDURE IF EXISTS wipe_db //
    CREATE PROCEDURE wipe_db(thresh INT)
    BEGIN
		SET SQL_SAFE_UPDATES = 0;
        DELETE FROM intake_stock WHERE user_id < thresh;
        DELETE FROM intake_recipe WHERE user_id < thresh;
        DELETE FROM product_stock WHERE user_id < thresh;
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
		(1, 1, 4, '2019-12-26 23:59:59'),
		(2, 2, 8, '2019-6-20 23:59:59'),
		(3, 3, 10, '2019-11-15 23:59:59'),
		(4, 4, 20, '2018-12-29 23:59:59'),
		(5, 5, 25, '2020-5-06 23:59:59'),
		(6, 6, 3, '2019-01-12 23:59:59'),
		(7, 7, 8, '2019-02-10 23:59:59'),
		(8, 8, 10, '2021-12-02 23:59:59'),
		(9, 9, 2, '2018-09-16 23:59:59'),
		(10, 10, 6, '2018-03-20 23:59:59');

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

    END //
    
delimiter ;
CALL init_db();
CALL wipe_db(12);
