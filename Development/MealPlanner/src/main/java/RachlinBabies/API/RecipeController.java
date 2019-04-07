package RachlinBabies.API;

import RachlinBabies.Model.Recipe;
import RachlinBabies.Service.RecipeDao;
import RachlinBabies.Utils.ResponseMessage;

import java.util.List;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static RachlinBabies.Utils.JsonUtil.toJson;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class RecipeController {

  private static final String BAD_GET = "Could not get recipes";

  RecipeController(final RecipeDao recipeService) {

    get("/recipes/detailed/:id", (req, res) -> {
      int recipeId = Integer.parseInt(req.params(":id"));
      Recipe recipe = recipeService.getRecipe(recipeId);
      if (recipe != null) {
        return recipe;
      }
      res.status(404);
      return new ResponseMessage(String.format("No recipe with id %d found", recipeId));
    }, json());

    get("/recipes/search/:name", (req, res) -> {
      String name = req.params(":name");
      List<Recipe> recipes = recipeService.searchRecipes(name);
      if (recipes != null) {
        return recipes;
      }
      res.status(404);
      return new ResponseMessage(BAD_GET);
    }, json());

    get("/recipes/available", (req, res) -> {
      List<Recipe> recipes = recipeService.getByStock();
      if (recipes != null) {
        return recipes;
      }
      res.status(404);
      return new ResponseMessage(BAD_GET);
    }, json());

    get("/recipes/myRecipes", (req, res) -> {
      List<Recipe> recipes = recipeService.myRecipes();
      if (recipes != null) {
        return recipes;
      }
      res.status(404);
      return new ResponseMessage(BAD_GET);
    }, json());

    post("/recipes", (req, res) -> {
      Recipe payload = dateGson().fromJson(req.body(), Recipe.class);
      if (recipeService.createRecipe(payload)) {
        return new ResponseMessage("Recipe created");
      }
      res.status(400);
      return new ResponseMessage("Could not create recipe");
    }, json());

    put("/recipes", (req, res) -> {
      Recipe payload = dateGson().fromJson(req.body(), Recipe.class);
      if (recipeService.updateRecipe(payload)) {
        return new ResponseMessage("Recipe updated");
      }
      res.status(400);
      return new ResponseMessage("Could not update recipe");
    }, json());

    delete("/recipes/:id", (req, res) -> {
      int recipeId = Integer.parseInt(req.params(":id"));
      if (recipeService.deleteRecipe(recipeId)) {
        return new ResponseMessage(String.format("Recipe %d deleted", recipeId));
      }
      res.status(400);
      return new ResponseMessage(String.format("Could not dlete recipe %d", recipeId));
    }, json());

    exception(Exception.class, (e, req, res) -> {
      res.status(400);
      res.body(toJson(new ResponseMessage(e)));
    });
  }
}



