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
    
delimiter ;

DESCRIBE intake_recipe;