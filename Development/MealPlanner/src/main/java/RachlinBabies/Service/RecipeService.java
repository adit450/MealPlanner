package RachlinBabies.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java
        .sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import RachlinBabies.Model.Product;
import RachlinBabies.Utils.DatabaseConnection;
import RachlinBabies.Model.Recipe;

/**
 * Service class that holds all the queries to the database that relate to Intakes.
 */
public class RecipeService extends Service<Recipe> implements RecipeDao {



    /**
     * Inserts a new Recipe into the database.
     *
     * @param toInsert The recipe to insert
     * @return whether or not the insert was successful.
     */

    public boolean create(Recipe toInsert) {

        /**
        int result = 0;
        String query = String.format(
                "INSERT INTO (recipe_id, creator_id, instructions, name, description,yield, created_at, ingredients) VALUES (?,?,?,?,?,?,?,?)",
                TABLES.get(toInsert.getType()), FK.get(toInsert.getType()));
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, recipeId);
            stmt.setInt(2, toInsert.getUserId());
            stmt.setString(3, toInsert.getInstructions());
            stmt.setString(4, toInsert.getName());
            stmt.setString(5, toInsert.getDescription);
            stmt.setString(6, toInsert.getYield);
            stmt.setTimestamp(7, toInsert.getCreatedOnDate());
            // stmt.setList(8, toInsert.getCreatedOnDate());



            result = stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return result > 0;

     */

    return true;

    }


    /**
     * Returns the Recipe with the given recipeId.
     * @param recipeId the id of the recipe to return.
     * @return the specified Recipe
     */
    public Recipe get(int recipeId) {
        Recipe ans = null;
        String query = "SELECT * FROM recipe WHERE recipe_id = ? and !deleted";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, recipeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.first()) {
                    ans = convert(rs);
                }
            }
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return ans;
    }

    /**
     * Gets all the Recipes in the database
     *
     * @return List of all recipes
     */
    public List<Recipe> getAll() {
        List<Recipe> recipes = null;

        String query = "SELECT recipe_id, creator_id, instructions, name, description,yield, created_at FROM recipe WHERE !deleted";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try(ResultSet rs = stmt.executeQuery()){
                recipes = convertList(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return recipes;
    }


    /**
     * Gets a the list of recipes created by the given user
     *
     * @return List of all recipes created by the given user
     */
    public List<Recipe> myRecipes(int userId) {
        List<Recipe> recipes = null;

        String query = "SELECT recipe_id, creator_id, instructions, name, description,yield, created_at FROM recipe WHERE creator_id = ? and and !deleted";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try(ResultSet rs = stmt.executeQuery()){
                recipes = convertList(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return recipes;
    }


    /**
     * Searches for recipes by name
     *
     * @return List of recipes that match keywords entered by user
     */
    public List<Recipe> SearchByName(String keywords) {
        List<Recipe> recipes = null;

        String query = "SELECT recipe_id, creator_id, instructions, name, description,yield, created_at FROM recipe WHERE name like ? and !deleted";

        String param = String.format("%%%s%%", keywords);
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, param);
            try(ResultSet rs = stmt.executeQuery()){
                recipes = convertList(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return recipes;
    }

    /**
     * Returns a list of all recipes that can be made based on the users ingredients
     *
     * @return list of all recipes that can be made based on the users ingredients
     */
    public List<Recipe> getByStock(int userId) {
        List<Recipe> recipes = null;

        String query = "SELECT recipe_id, creator_id, instructions, name, description,yield, created_at FROM recipe WHERE recipeCanBeMade(?,recipe_id) and !deleted";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try(ResultSet rs = stmt.executeQuery()){
                recipes = convertList(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return recipes;
    }



    /**
     * Updates the Intake time of the intake specified with the id.  Requires intake_id, new time,
     * and the type.
     * @param intakeWithTimestamp the intake with the desired Timestamp.  Only the timestamp will
     *                            be updated.
     * @return whether or not the update was successful.
     */
    public boolean updateRecipe(Recipe intakeWithTimestamp) {

        /**

        int result = 0;
        String query = String.format("UPDATE %s SET intake_time = ? WHERE intake_id = ? AND user_id = ?",
                TABLES.get(intakeWithTimestamp.getType()));
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, intakeWithTimestamp.getIntakeDate());
            stmt.setInt(2, intakeWithTimestamp.getId());
            stmt.setInt(3, userId);
            result = stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return result > 0;
    }
     */
    /**
     * Deletes the specified intake.
     * @param recipeId the intakeId fo the desired intake to delete
     * @return if the delete was successful
     */

    return true;
    }
    public boolean delete(int intakeId) {
        int rowsChanged = 0;
        String query = "CALL delete_intake(?,?,?)";
        Connection connection = DatabaseConnection.getConnection();
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, intakeId);
            stmt.setInt(2, userId);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.executeUpdate();
            rowsChanged = stmt.getInt(3);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return rowsChanged > 0;
    }

    /**
     * Extracts an Intake Model from the ResultSet
     * @param rs the ResultSet
     * @return the Intake made from the ResultSet
     * @throws SQLException when JDBC dies
     */
    Recipe convert(ResultSet rs) throws SQLException {
        return new Recipe.RecipeBuilder()
                .recipeId(rs.getInt("recipe_id"))
                .userId(rs.getInt("creator_id"))
                .instructions(rs.getString("instructions"))
                .name(rs.getString("name"))
                .descriptions(rs.getString("description"))
                .yield(rs.getInt("yield"))
                .createdOnDate(rs.getTimestamp("created_at"))
                .build();
    }
}