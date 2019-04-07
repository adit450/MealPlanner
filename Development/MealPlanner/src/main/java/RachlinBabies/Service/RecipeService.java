package RachlinBabies.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import RachlinBabies.Model.Product;
import RachlinBabies.Model.Recipe;
import RachlinBabies.Utils.DatabaseConnection;

import static RachlinBabies.Utils.DatabaseConnection.rollback;
import static RachlinBabies.Utils.DatabaseConnection.setAutoCommit;
import static RachlinBabies.Utils.JsonUtil.toJson;

/**
 * Service class that holds all the queries to the database that relate to Intakes.
 */
public class RecipeService extends Service<Recipe> implements RecipeDao {

  private static final String SELECT_RECIPES = "SELECT recipe_id, creator_id, " +
          "instructions, name, description, yield, created_at " +
          "FROM recipe WHERE !deleted";

  public Recipe getRecipe(int recipeId) {
    Recipe recipe = null;
    Set<Recipe.RecipeProduct> ingredients = new HashSet<>();
    String query = "SELECT * FROM recipe WHERE recipe_id = ? AND !deleted";
    String getIngredients = "SELECT rp.*, long_name, expr_rate, manufacturer, serving_size, " +
            "serving_size_uom, household_serving_size, household_serving_size_uom " +
            "FROM recipe_has_product rp JOIN product p USING (NDB_Number) " +
            "JOIN serving_size ss ON (p.NDB_Number = ss.product_NDB_Number) " +
            "WHERE recipe_id = ?";

    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, recipeId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          recipe = convert(rs);
        }
      }
      if (recipe != null) {
        try (PreparedStatement stmt2 = connection.prepareStatement(getIngredients)) {
          stmt2.setInt(1, recipeId);
          try (ResultSet rs = stmt2.executeQuery()) {
            Product product;
            while (rs.next()) {
              product = new ProductService().convert(rs);
              ingredients.add(new Recipe.RecipeProduct(product, rs.getInt("servings")));
            }
            recipe.setIngredients(ingredients);
          }
        }
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipe;
  }

  public List<Recipe> myRecipes() {
    List<Recipe> recipes = null;

    String query = SELECT_RECIPES + " AND creator_id = ?";

    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipes;
  }


  public List<Recipe> searchRecipes(String name) {
    List<Recipe> recipes = null;
    String query = SELECT_RECIPES + " AND name LIKE ?";
    String param = String.format("%%%s%%", name);
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, param);
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipes;
  }

  public List<Recipe> getByStock() {
    List<Recipe> recipes = null;

    String query = SELECT_RECIPES + " AND recipeCanBeMade(?,recipe_id)";

    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipes;
  }

  public boolean createRecipe(Recipe toInsert) {
    boolean success = false;
    int recipeId = 0;
    String query = "INSERT INTO recipe(creator_id, instructions, name, description, yield) " +
            "VALUES (?,?,?,?,?)";
    Connection connection = DatabaseConnection.getConnection();
    try {
      connection.setAutoCommit(false);
      try (PreparedStatement stmt = connection.prepareStatement(query,
              Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, userId);
        stmt.setString(2, toInsert.getInstructions());
        stmt.setString(3, toInsert.getName());
        stmt.setString(4, toInsert.getDescriptions());
        stmt.setInt(5, toInsert.getYield());
        stmt.executeUpdate();
        try (ResultSet rs = stmt.getGeneratedKeys()) {
          if (rs.first()) {
            recipeId = rs.getInt(1);
          }
        }
      }
      if (recipeId != 0) {
        success = replaceIngredients(recipeId, toInsert.getIngredients(), connection);
      }
      if (success) {
        connection.commit();
      } else {
        connection.rollback();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      rollback(connection);
    } finally {
      setAutoCommit(connection, true);
      DatabaseConnection.closeConnection(connection);
    }
    return success;
  }

  public boolean updateRecipe(Recipe toUpdate) {
    boolean updated = false;
    int recipeId = toUpdate.getRecipeId();
    String query = "UPDATE recipe SET name = ?, description = ?, yield = ?, instructions =? " +
            "WHERE creator_id = ? AND recipe_id = ?";
    Connection connection = DatabaseConnection.getConnection();
    try {
      connection.setAutoCommit(false);
      try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, toUpdate.getName());
        stmt.setString(2, toUpdate.getDescriptions());
        stmt.setInt(3, toUpdate.getYield());
        stmt.setString(4, toUpdate.getInstructions());
        if (stmt.executeUpdate() > 0) {
          updated = replaceIngredients(recipeId, toUpdate.getIngredients(), connection);
        }
      }
      if (updated) {
        connection.commit();
      } else {
        connection.rollback();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      rollback(connection);
    } finally {
      setAutoCommit(connection, true);
      DatabaseConnection.closeConnection(connection);
    }
    return updated;
  }

  /**
   * Helper method that clears ingredients related to a recipe and then inserts new values.
   * @param recipeId the id of the related recipe
   * @param ingredients the ingredients to insert
   * @param connection the JDBC connection
   * @return if the insert was successful.
   */
  private boolean replaceIngredients(int recipeId, Set<Recipe.RecipeProduct> ingredients,
                                    Connection connection) {
    boolean success = false;
    int ingredientInserts = 0;
    String insertIngredients = "INSERT INTO recipe_has_product VALUES (?,?,?)";
    String clearIngredients = "DELETE FROM recipe_has_product WHERE recipe_id = ?";
    try {
      try (PreparedStatement stmt = connection.prepareStatement(clearIngredients)) {
        stmt.setInt(1, recipeId);
        stmt.executeUpdate();
      }
      try (PreparedStatement stmt = connection.prepareStatement(insertIngredients)) {
        for (Recipe.RecipeProduct rp : ingredients) {
          stmt.setInt(1, recipeId);
          stmt.setInt(2, rp.getProduct().getNdb());
          stmt.setInt(3, rp.getServings());
          ingredientInserts += stmt.executeUpdate();
        }
        success = ingredientInserts == ingredients.size();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return success;
  }

  public boolean deleteRecipe(int recipeId) {
    int result = 0;
    String query = "UPDATE recipe SET deleted = TRUE WHERE recipe_id = ? AND creator_id = ?";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, recipeId);
      stmt.setInt(2, userId);
      result = stmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.getConnection();
    }
    return result > 0;
  }

  /**
   * Extracts an Intake Model from the ResultSet
   *
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
            .dateCreated(rs.getTimestamp("created_at"))
            .build();
  }
}