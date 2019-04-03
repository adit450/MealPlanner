USE mealplanner;

delimiter //

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
    
    DROP TRIGGER IF EXISTS auto_increment_recipe_intake_id //
    CREATE TRIGGER auto_increment_recipe_intake_id BEFORE INSERT ON intake_recipe
    FOR EACH ROW
    BEGIN
		DECLARE notUnique TINYINT;
		SET NEW.intake_id = IFNULL(NEW.intake_id, auto_increment_intake_id());
		SELECT count(*) > 0 INTO notUnique
        FROM intake_recipe
        WHERE intake_id = NEW.intake_id;
        IF notUnique THEN
			SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'non-unique intake_id';
        END IF;
    END//
    
    DROP TRIGGER IF EXISTS auto_increment_stock_intake_id //
    CREATE TRIGGER auto_increment_stock_intake_id BEFORE INSERT ON intake_stock
    FOR EACH ROW
    BEGIN
		DECLARE notUnique TINYINT;
		SET NEW.intake_id = IFNULL(NEW.intake_id, auto_increment_intake_id());
		SELECT count(*) > 0 INTO notUnique
        FROM intake_recipe
        WHERE intake_id = NEW.intake_id;
        IF notUnique THEN
			SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'non-unique intake_id';
        END IF;
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
    
    DROP FUNCTION IF EXISTS can_be_made //
    CREATE FUNCTION can_be_made
    (
		user INT,
        recipe INT
    )
    RETURNS INT DETERMINISTIC
    BEGIN
		DECLARE possibleServings INT;
        SELECT MIN(IFNULL((quantity / servings), 0)) INTO possibleServings
		FROM product JOIN recipe_has_product USING (NDB_Number)
			LEFT JOIN product_stock s USING (NDB_Number)
			JOIN stock_item si ON (s.stock_id = si.stock_item_id)
		WHERE recipe_id = recipe AND user_id = user;
	
    END //
    
delimiter ;

DESCRIBE recipe;
DESCRIBE recipe_has_product;
DESCRIBE stock_item;
DESCRIBE product_stock;
    
SELECT * FROM stock_item;
SELECT * FROM product_stock;

SELECT NDB_Number, quantity, servings
FROM product JOIN recipe_has_product USING (NDB_Number)
	LEFT JOIN product_stock s USING (NDB_Number)
	JOIN stock_item si ON (s.stock_id = si.stock_item_id)
WHERE recipe_id = recipe AND user_id = user;
		