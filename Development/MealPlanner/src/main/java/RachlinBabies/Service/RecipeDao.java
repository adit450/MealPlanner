package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Recipe;
import RachlinBabies.Model.Tag;

/**
 * Queries offered on Recipe
 */
public interface RecipeDao {
    /**
     * Get recipe with the given Id
     * @param  recipeId of the recipe
     * @return desired recipe
     */
    Recipe getRecipe(int recipeId);

    /**
     * Create a new Recipe
     * @param toInsert Model of the desired recipe to insert
     * @return whether or not the insert was successful.
     */
    boolean createRecipe(Recipe toInsert);

    /**
     * Updates the recipe time of the recipe specified with the id.  Requires intake_id and new time.
     * @param intakeWithTimestamp the intake with the desired Timestamp.  Only the timestamp will
     *                            be updated.
     * @return whether or not the update was successful.
     */
    boolean updateRecipe(Recipe intakeWithTimestamp);

  /**
   * Deletes the recipe with the given id.
   * @param recipeId the id of the recipe to delete
   * @return whether or not hte delete was successful.
   */
    boolean deleteRecipe(int recipeId);

    /**
     * Get recipes that can be made with your stock.
     * @return the recipes that you can make
     */
    List<Recipe> getByStock();

  /**
   * Get recipes authored by the session user.
   * @return the user's recipes
   */
    List<Recipe> myRecipes();

  /**
   * Search recipes by name
   * @param name name to filter by.
   * @return recipes matching search parameter.
   */
    List<Recipe> searchRecipes(String name);

  /**
   * Filter recipes by tag
   * @param tags the ids of the tags to filter by
   * @return the recipes that have the given tags.
   */
  List<Recipe> filterByTag(int[] tags);
}