package RachlinBabies.API;

import RachlinBabies.Model.Recipe;
import RachlinBabies.Model.Tag;
import RachlinBabies.Service.RecipeDao;
import RachlinBabies.Utils.ResponseMessage;

import java.util.List;
import java.util.Map;

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

    get("/recipes/filter", (req, res) -> {
      String[] tags = req.queryParams("tags").split(",");
      int[] payload = new int[tags.length];
      for (int i = 0; i < tags.length; i++) {
        payload[i] = Integer.parseInt(tags[i]);
      }
      return recipeService.filterByTag(payload);
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

    get("/recipes/myRecipes", (req, res) -> {
      List<Recipe> recipes = recipeService.myRecipes();
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



    post("/recipes", (req, res) -> {
      Recipe payload = dateGson().fromJson(req.body(), Recipe.class);
      if (recipeService.createRecipe(payload)) {
        return new ResponseMessage("Recipe created");
      }
      res.status(400);
      return new ResponseMessage("Could not create recipe");
    }, json());

    post("/recipes/rating", (req, res) -> {
      Map payload = dateGson().fromJson(req.body(), Map.class);
      int recipeId = ((Double)payload.get("recipeId")).intValue();
      int rating = ((Double)payload.get("rating")).intValue();
      if (rating < 0 || rating > 5) {
        res.status(400);
        return new ResponseMessage("Rating out of range (0-5)");
      }
      if (!recipeService.rate(recipeId, rating)) {
        res.status(400);
        return new ResponseMessage("Failed to add rating");
      }
      return new ResponseMessage("Rating added");
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
      return new ResponseMessage(String.format("Could not delete recipe %d", recipeId));
    }, json());

    exception(Exception.class, (e, req, res) -> {
      res.status(400);
      res.body(toJson(new ResponseMessage(e)));
    });
  }
}




