package RachlinBabies.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import RachlinBabies.Model.Product;
import RachlinBabies.Model.Recipe;
import RachlinBabies.Model.Tag;
import RachlinBabies.Utils.DatabaseConnection;

import static RachlinBabies.Utils.DatabaseConnection.rollback;
import static RachlinBabies.Utils.DatabaseConnection.setAutoCommit;

/**
 * Service class that holds all the queries to the database that relate to Intakes.
 */
public class RecipeService extends Service<Recipe> implements RecipeDao {

  private static final String SELECT_RECIPES = "SELECT recipe_id, creator_id, " +
          "instructions, name, description, yield, created_at, " +
          "avgRating(recipe_id) as 'overallRating', numRating(recipe_id) as 'ratings' " +
          "FROM recipe WHERE !deleted";

  private static final String ORDER_BY = " ORDER BY overallRating DESC, ratings DESC";

  public Recipe getRecipe(int recipeId) {
    Recipe recipe = null;
    Set<Recipe.RecipeProduct> ingredients = new HashSet<>();
    String query = "SELECT *, avgRating(recipe_id) as 'overallRating', " +
            "numRating(recipe_id) as 'ratings' FROM recipe WHERE recipe_id = ? AND !deleted";
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
        appendTags(recipe, connection);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipe;
  }

  public List<Recipe> filterByTag(int[] tags) {
    List<Recipe> recipes = new ArrayList<>();
    if (tags.length == 0) { return recipes; }
    String filter = "JOIN (SELECT recipe_id FROM recipe_has_tag WHERE tag_id = ?) filter%d USING (recipe_id)";
    StringBuilder filters = new StringBuilder();
    for (int i = 0; i < tags.length; i++) {
      filters.append(String.format(filter, i + 1));
    }
    String query = String.format("SELECT *, avgRating(recipe_id) as 'overallRating', " +
            "numRating(recipe_id) as 'ratings' " +
            "FROM recipe %s", filters) + ORDER_BY;
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      for (int i = 1; i <= tags.length; i++) {
        stmt.setInt(i, tags[i - 1]);
      }
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
      appendTagsToList(recipes, connection);
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipes;
  }

  public List<Recipe> myRecipes() {
    List<Recipe> recipes = null;

    String query = SELECT_RECIPES + " AND creator_id = ?" + ORDER_BY;

    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
      appendTagsToList(recipes, connection);
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipes;
  }

  public List<Recipe> searchRecipes(String name) {
    List<Recipe> recipes = null;
    String query = SELECT_RECIPES + " AND name LIKE ?" + ORDER_BY;
    String param = String.format("%%%s%%", name);
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, param);
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
      appendTagsToList(recipes, connection);
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return recipes;
  }

  public List<Recipe> getByStock() {
    List<Recipe> recipes = null;

    String query = SELECT_RECIPES + " AND recipeCanBeMade(?,recipe_id)" + ORDER_BY;

    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        recipes = convertList(rs);
      }
      appendTagsToList(recipes, connection);
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
        success = replaceIngredients(recipeId, toInsert.getIngredients(), connection)
                && replaceTags(recipeId, toInsert.getTags(), connection);
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
    String query = "UPDATE recipe SET name = ?, description = ?, yield = ?, instructions = ? " +
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
          updated = replaceIngredients(recipeId, toUpdate.getIngredients(), connection)
                  && replaceTags(recipeId, toUpdate.getTags(), connection);
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

  /**
   * Helper method that clears tags related to a recipe and then inserts new values.
   * @param recipeId the id of the related recipe
   * @param tags the tags to insert
   * @param connection the JDBC connection
   * @return if the insert was successful.
   */
  private boolean replaceTags(int recipeId, Set<Tag> tags, Connection connection) {
    boolean success = false;
    int tagInserts = 0;
    String insertTags = "INSERT INTO recipe_has_tag VALUES (?,?)";
    String clearTags = "DELETE FROM recipe_has_tag WHERE recipe_id = ?";
    try {
      try (PreparedStatement stmt = connection.prepareStatement(clearTags)) {
        stmt.setInt(1, recipeId);
        stmt.executeUpdate();
      }
      try (PreparedStatement stmt = connection.prepareStatement(insertTags)) {
        for (Tag tag : tags) {
          stmt.setInt(1, recipeId);
          stmt.setInt(2, tag.getId());
          tagInserts += stmt.executeUpdate();
        }
        success = tagInserts == tags.size();
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

  private void appendTagsToList(List<Recipe> recipes, Connection connection) throws SQLException {
    for (Recipe recipe : recipes) {
      appendTags(recipe, connection);
    }
  }

  /**
   * Append a recipe's tags to an existing Recipe
   * @param recipe the Recipe to add tags to.
   * @param connection the connection to reuse.
   */
  private void appendTags(Recipe recipe, Connection connection) throws SQLException {
    String query = "SELECT tag.* FROM tag JOIN recipe_has_tag USING (tag_id) WHERE recipe_id = ?";
    Service<Tag> tagService = new TagService();
    Set<Tag> tags = new HashSet<>();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, recipe.getRecipeId());
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          tags.add(tagService.convert(rs));
        }
      }
    }
    recipe.setTags(tags);
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
            .overallRating(rs.getDouble("overallRating"))
            .ratings(rs.getInt("ratings"))
            .build();
  }
}