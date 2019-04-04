-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mealplanner
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mealplanner
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mealplanner` DEFAULT CHARACTER SET utf8 ;
USE `mealplanner` ;

-- -----------------------------------------------------
-- Table `mealplanner`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`product` (
  `NDB_Number` INT NOT NULL,
  `long_name` VARCHAR(255) NOT NULL,
  `expr_rate` INT NOT NULL,
  `data_source` VARCHAR(64) NOT NULL,
  `gtin_upc` BIGINT NOT NULL,
  `manufacturer` VARCHAR(64),
  `ingredients_english` LONGTEXT NOT NULL,
  PRIMARY KEY (`NDB_NUMBER`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mealplanner`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `username_UNIQUE` ON `mealplanner`.`user` (`username` ASC);

CREATE UNIQUE INDEX `email_UNIQUE` ON `mealplanner`.`user` (`email` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`product_stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`product_stock` (
  `stock_id` INT NOT NULL AUTO_INCREMENT,
  `NDB_Number` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`stock_id`),
  CONSTRAINT `fk_stock_item_product`
    FOREIGN KEY (`NDB_Number`)
    REFERENCES `mealplanner`.`product` (`NDB_Number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_stock_item_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_stock_item_product_idx` ON `mealplanner`.`product_stock` (`NDB_Number` ASC);

CREATE INDEX `fk_stock_item_user1_idx` ON `mealplanner`.`product_stock` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`recipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`recipe` (
  `recipe_id` INT NOT NULL AUTO_INCREMENT,
  `creator_id` INT NOT NULL,
  `instructions` TEXT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` TINYTEXT NULL,
  `yield` INT NOT NULL,
  `image` BLOB NULL,
  `created_at` DATE NULL,
  PRIMARY KEY (`recipe_id`),
  CONSTRAINT `fk_recipe_user1`
    FOREIGN KEY (`creator_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_recipe_user1_idx` ON `mealplanner`.`recipe` (`creator_id` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`intake_recipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`intake_recipe` (
  `intake_id` INT NOT NULL DEFAULT 0,
  `user_id` INT NOT NULL,
  `recipe_id` INT NOT NULL,
  `servings` INT NOT NULL,
  `intake_time` DATETIME NULL,
  PRIMARY KEY (`intake_id`),
  CONSTRAINT `fk_intake_recipe_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_intake_recipe_recipe1`
    FOREIGN KEY (`recipe_id`)
    REFERENCES `mealplanner`.`recipe` (`recipe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_intake_recipe_user1_idx` ON `mealplanner`.`intake_recipe` (`user_id` ASC);

CREATE INDEX `fk_intake_recipe_recipe1_idx` ON `mealplanner`.`intake_recipe` (`recipe_id` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`rating` (
  `recipe_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `score` ENUM('0', '1', '2', '3', '4', '5') NOT NULL,
  PRIMARY KEY (`recipe_id`, `user_id`),
  CONSTRAINT `fk_recipe_has_user_recipe1`
    FOREIGN KEY (`recipe_id`)
    REFERENCES `mealplanner`.`recipe` (`recipe_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_recipe_has_user_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_recipe_has_user_user1_idx` ON `mealplanner`.`rating` (`user_id` ASC);

CREATE INDEX `fk_recipe_has_user_recipe1_idx` ON `mealplanner`.`rating` (`recipe_id` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`recipe_has_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`recipe_has_product` (
  `recipe_id` INT NOT NULL,
  `NDB_Number` INT NOT NULL,
  `servings` INT NOT NULL,
  PRIMARY KEY (`recipe_id`, `NDB_Number`),
  CONSTRAINT `fk_recipe_has_product_recipe1`
    FOREIGN KEY (`recipe_id`)
    REFERENCES `mealplanner`.`recipe` (`recipe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_recipe_has_product_product1`
    FOREIGN KEY (`NDB_Number`)
    REFERENCES `mealplanner`.`product` (`NDB_Number`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_recipe_has_product_product1_idx` ON `mealplanner`.`recipe_has_product` (`NDB_Number` ASC);

CREATE INDEX `fk_recipe_has_product_recipe1_idx` ON `mealplanner`.`recipe_has_product` (`recipe_id` ASC);



-- -----------------------------------------------------
-- Table `mealplanner`.`intake_stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`intake_stock` (
  `user_id` INT NOT NULL,
  `stock_id` INT NOT NULL,
  `intake_id`INT NOT NULL DEFAULT 0,
  `intake_time` DATETIME NULL,
  `servings` INT NOT NULL,
  PRIMARY KEY (`intake_id`),
  CONSTRAINT `fk_intake_stock_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_intake_stock_product_stock1`
    FOREIGN KEY (`stock_id`)
    REFERENCES `mealplanner`.`product_stock` (`stock_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_intake_stock_user1_idx` ON `mealplanner`.`intake_stock` (`user_id` ASC);
CREATE INDEX `fk_intake_stock_product_stock1_idx` ON `mealplanner`.`intake_stock` (`stock_id` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`stock_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`stock_item` (
  `stock_item_id` INT NOT NULL AUTO_INCREMENT,
  `stock_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `expiration_date` DATE NOT NULL,
  PRIMARY KEY(`stock_item_id`),
  CONSTRAINT `fk_stock_item_product_stock1`
    FOREIGN KEY (`stock_id`)
    REFERENCES `mealplanner`.`product_stock` (`stock_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_stock_item_product_stock1_idx` ON `mealplanner`.`stock_item` (`stock_id` ASC);

-- -----------------------------------------------------
-- Table `mealplanner`.`derivation_code_description`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`derivation_code_description` (
	`derivation_code` VARCHAR(10) NOT NULL,
    `Derivation_Descript` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`derivation_code`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mealplanner`.`follow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`follow` (
  `follower_id` INT NOT NULL,
  `followee_id` INT NOT NULL,
  PRIMARY KEY (`follower_id`, `followee_id`),
  CONSTRAINT `fk_user_has_user_user1`
    FOREIGN KEY (`follower_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_user_user2`
    FOREIGN KEY (`followee_id`)
    REFERENCES `mealplanner`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_user_has_user_user2_idx` ON `mealplanner`.`follow` (`followee_id` ASC);

CREATE INDEX `fk_user_has_user_user1_idx` ON `mealplanner`.`follow` (`follower_id` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`nutrient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`nutrient` (
  `product_NDB_Number` INT NOT NULL,
  `nutrient_code` INT NOT NULL,
  `nutrient_name` VARCHAR(32) NOT NULL,
  `derivation_code` VARCHAR(10) NOT NULL,
  `output_value` DOUBLE NOT NULL,
  `output_uom` VARCHAR(10) NOT NULL,
  CONSTRAINT `fk_nutrient_product1`
    FOREIGN KEY (`product_NDB_Number`)
    REFERENCES `mealplanner`.`product` (`NDB_Number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nutrient_dcd`
	FOREIGN KEY (`derivation_code`)
    REFERENCES `mealplanner`.`derivation_code_description` (`derivation_code`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE INDEX `fk_nutrient_product1_idx` ON `mealplanner`.`nutrient` (`product_NDB_Number` ASC);


-- -----------------------------------------------------
-- Table `mealplanner`.`serving_size`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mealplanner`.`serving_size` (
  `product_NDB_Number` INT NOT NULL,
  `serving_size` DOUBLE NOT NULL,
  `serving_size_uom` VARCHAR(10) NOT NULL,
  `household_serving_size` DOUBLE NOT NULL DEFAULT 0,
  `household_serving_size_uom` VARCHAR(10) NOT NULL,
  `preparation_state` VARCHAR(32) NULL,
  PRIMARY KEY (`product_NDB_Number`),
  CONSTRAINT `fk_serving_size_product1`
    FOREIGN KEY (`product_NDB_Number`)
    REFERENCES `mealplanner`.`product` (`NDB_Number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX `fk_serving_size_product1_idx` ON `mealplanner`.`serving_size` (`product_NDB_Number` ASC);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
