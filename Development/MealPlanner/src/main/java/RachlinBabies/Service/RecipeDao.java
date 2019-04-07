package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Recipe;

/**
 * Queries offered on Recipe
 */
public interface RecipeDao {
    /**
     * Get recipe with the given Id
     * @param  recipeId of the recipe
     * @return desired recipe
     */
    Recipe get(int recipeId);

    /**
     * Get all the recipes visible to the session user.
     * @return List of intakes
     */
    List<Recipe> getAll();

    /**
     * Create a new Recipe
     * @param toInsert Model of the desired recipe to insert
     * @return whether or not the insert was successful.
     */
    boolean create(Recipe toInsert);

    /**
     * Updates the recipe time of the recipe specified with the id.  Requires intake_id and new time.
     * @param intakeWithTimestamp the intake with the desired Timestamp.  Only the timestamp will
     *                            be updated.
     * @return whether or not the update was successful.
     */
    boolean updateRecipe(Recipe intakeWithTimestamp);

    //delete

    // Gets all the recipes that can be created based on the ingredients we already have
    List<Recipe> getByStock(int userId);

    // Gets a the list of recipes created by the given user
    List<Recipe> myRecipes(int userId);

    // Search By Name
    List<Recipe> SearchByName(String keywords);

}