USE mealplanner;

delimiter //

	-- INTAKE PROCEDURES AND TRIGGERS --

	DROP FUNCTION IF EXISTS auto_increment_intake_id //
	CREATE FUNCTION auto_increment_intake_id()
    RETURNS INT DETERMINISTIC
	BEGIN
		DECLARE nextId INT;
        
        SELECT MAX(intake_id) + 1 INTO nextId
        FROM (SELECT MAX(intake_id) as 'intake_id' FROM intake_recipe
			UNION
            SELECT MAX(intake_id) as 'intake_id' FROM intake_stock) contenders;
		
        RETURN IFNULL(nextId, 1);
    END //
    
    DROP PROCEDURE IF EXISTS update_stock_after_intake //
    CREATE PROCEDURE update_stock_after_intake
    (
		user INT,
        food INT,
        consumedServings INT
    )
    BEGIN
		DECLARE stockServings INT;
        DECLARE stockItemId INT;
        DECLARE toRemove INT DEFAULT consumedServings;
		DECLARE stock_cursor CURSOR FOR 
			SELECT stock_item_id, quantity
            FROM product_stock JOIN stock_item USING (stock_id)
            WHERE user_id = user AND NDB_Number = food
            GROUP BY stock_item_id
            ORDER BY expiration_date;
		OPEN stock_cursor;
        consume_stock: LOOP
			IF toRemove <= 0 THEN
				LEAVE consume_stock;
			END IF;
			FETCH stock_cursor INTO stockItemId, stockServings;
            IF toRemove < stockServings THEN
				UPDATE stock_item SET quantity = stockServings - toRemove WHERE stock_item_id = stockItemId;
                SET toRemove = 0;
			ELSE
				DELETE FROM stock_item WHERE stock_item_id = stockItemId;
                SET toRemove = toRemove - stockServings;
            END IF;
        END LOOP;
        CLOSE stock_cursor;
    END //
    
    DROP PROCEDURE IF EXISTS update_stock_after_recipe_intake //
    CREATE PROCEDURE update_stock_after_recipe_intake
    (
		user INT,
        recipe INT,
        recipeServings INT
    )
    BEGIN
		DECLARE consumedStock INT;
        DECLARE ndb INT;
		DECLARE done TINYINT DEFAULT FALSE;
        DECLARE ingredient_cursor CURSOR FOR 
			SELECT NDB_Number, (servings / yield) * recipeServings
            FROM recipe_has_product JOIN recipe USING (recipe_id)
            WHERE recipe_id = recipe;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
        
        OPEN ingredient_cursor;
        consume_ingredient: LOOP
			FETCH ingredient_cursor INTO ndb, consumedStock;
            IF done THEN
				LEAVE consume_ingredient;
			END IF;
            CALL update_stock_after_intake(user, ndb, consumedStock);
        END LOOP;
        CLOSE ingredient_cursor;
    END //
    
    DROP TRIGGER IF EXISTS before_insert_intake_recipe //
    CREATE TRIGGER before_insert_intake_recipe BEFORE INSERT ON intake_recipe
    FOR EACH ROW
    BEGIN
		DECLARE notUnique TINYINT;
		SET NEW.intake_id = IFNULL(NEW.intake_id, auto_increment_intake_id());
        SET NEW.intake_time = IFNULL(NEW.intake_time, CURTIME());
		SELECT count(*) > 0 INTO notUnique
        FROM intake_recipe
        WHERE intake_id = NEW.intake_id;
        IF notUnique THEN
			SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'non-unique intake_id';
        END IF;
        IF recipeCanBeMade(NEW.user_id, NEW.recipe_id) < 1 THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'You do not have the necessary stock';
		END IF;
        CALL update_stock_after_recipe_intake(NEW.user_id, NEW.recipe_id, NEW.servings);
    END//
    
    DROP TRIGGER IF EXISTS before_insert_intake_stock //
    CREATE TRIGGER before_insert_intake_stock BEFORE INSERT ON intake_stock
    FOR EACH ROW
    BEGIN
		DECLARE haveOnHand TINYINT;
		DECLARE notUnique TINYINT;
        DECLARE ndb INT;
        
		SET NEW.intake_id = IFNULL(NEW.intake_id, auto_increment_intake_id());
        SET NEW.intake_time = IFNULL(NEW.intake_time, CURTIME());
        
        
		SELECT count(*) > 0 INTO notUnique
        FROM intake_recipe
        WHERE intake_id = NEW.intake_id;
        IF notUnique THEN
			SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'non-unique intake_id';
        END IF;

        SELECT NDB_Number INTO ndb FROM product_stock WHERE stock_id = NEW.stock_id;
        SELECT sum(quantity) >= NEW.servings INTO haveOnHand
        FROM stock_item JOIN product_stock USING (stock_id)
        WHERE stock_id = NEW.stock_id AND user_id = NEW.user_id
        GROUP BY stock_id;
		IF !haveOnHand THEN
			SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'You do not have enough stock on hand';
        END IF;
		CALL update_stock_after_intake(NEW.user_id, ndb, NEW.servings);
	END//
    
    DROP PROCEDURE IF EXISTS delete_intake //
    CREATE PROCEDURE delete_intake
    (
		IN intake INT,
        IN user INT,
        OUT rowsAffected INT
    )
    BEGIN
		SET rowsAffected = 0;
		DELETE FROM intake_stock WHERE intake_id = intake AND user_id = user;
        SET rowsAffected = rowsAffected + ROW_COUNT();
        DELETE FROM intake_recipe WHERE intake_id = intake AND user_id = user;
        SET rowsAffected = rowsAffected + ROW_COUNT();
        SELECT rowsAffected;
    END //
    
    DROP FUNCTION IF EXISTS recipeCanBeMade //
    CREATE FUNCTION recipeCanBeMade
    (
		user INT,
        recipe INT
    )
    RETURNS DOUBLE DETERMINISTIC
    BEGIN
		DECLARE possibleServings DOUBLE;
        SELECT MIN(IFNULL((quantity / servings), 0)) * yield INTO possibleServings
		FROM product JOIN recipe_has_product USING (NDB_Number)
			LEFT JOIN (SELECT NDB_Number, sum(quantity) as 'quantity'
				FROM product_stock JOIN stock_item USING (stock_id) 
                WHERE user_id = user
                GROUP BY stock_id) stocks USING (NDB_Number)
			JOIN recipe USING (recipe_id)
		WHERE recipe_id = recipe;
		RETURN possibleServings;
    END //
    
    -- END INTAKE PROCEDURES AND TRIGGERS --
    
delimiter ;
