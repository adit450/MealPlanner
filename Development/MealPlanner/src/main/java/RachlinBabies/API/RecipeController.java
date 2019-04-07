package RachlinBabies.API;

import RachlinBabies.Model.Recipe;
import RachlinBabies.Service.RecipeDao;
import RachlinBabies.Utils.ResponseMessage;

import java.util.List;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static RachlinBabies.Utils.JsonUtil.toJson;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class RecipeController {

    RecipeController(final RecipeDao RecipeService) {

        get("/recipes", (req, res) -> RecipeService.getAll(), json());

        get("/recipes/:recipeId", (req, res) -> {
            int recipeId = Integer.parseInt(req.params(":recipeId"));
            Recipe recipe = RecipeService.get(recipeId);
            if (recipe != null) {
                return recipe;
            }
            res.status(404);

            return new ResponseMessage(String.format("No recipe with id %d found", recipeId));
        }, json());

        get("/recipes/search/:keywords", (req, res) -> {
            System.out.println(req.params());
            String keywords = req.params(":keywords");
            System.out.println(keywords);
            List<Recipe> recipes = RecipeService.SearchByName(keywords);
            if (keywords != null) {
                return recipes;
            }
            res.status(404);

            return new ResponseMessage(String.format("No recipes found with name %d ", keywords));
        }, json());

        get("/recipes/getByStock/:userId", (req, res) -> {
            int userId = Integer.parseInt(req.params(":userId"));
            List<Recipe> recipes = RecipeService.getByStock(userId);
            if (recipes != null) {
                return recipes;
            }
            res.status(404);

            return new ResponseMessage(String.format("No recipes can be made by this user", userId));
        }, json());

        get("/recipes/myRecipes/:userId", (req, res) -> {
            int userId = Integer.parseInt(req.params(":userId"));
            List<Recipe> recipes = RecipeService.myRecipes(userId);
            if (recipes != null) {
                return recipes;
            }
            res.status(404);

            return new ResponseMessage(String.format("No recipes have been made by this user", userId));
        }, json());
    }}



