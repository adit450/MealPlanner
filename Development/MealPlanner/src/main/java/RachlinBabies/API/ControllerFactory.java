package RachlinBabies.API;

import RachlinBabies.Service.IntakeService;
import RachlinBabies.Service.ProductService;
import RachlinBabies.Service.RecipeService;
import RachlinBabies.Service.StockItemService;
import RachlinBabies.Service.StockService;
import RachlinBabies.Service.UserService;


public abstract class ControllerFactory {
  private static IntakeController intake;
  private static ProductController product;
  private static RecipeController recipe;
  private static StockController stock;
  private static StockItemController stockItem;
  private static UserController user;

  private ControllerFactory() {}

  public static IntakeController getIntakeController() {
    if (intake == null) {
      intake = new IntakeController(new IntakeService());
    }
    return intake;
  }

  public static ProductController getProductController() {
    if (product == null) {
      product = new ProductController(new ProductService());
    }
    return product;
  }

  public static RecipeController getRecipeController() {
    if (recipe == null) {
      recipe = new RecipeController(new RecipeService());
    }
    return recipe;
  }
    
  public static StockController getStockController() {
    if (stock == null) {
      stock = new StockController(new StockService());
    }
    return stock;
  }

  public static StockItemController getStockItemController() {
    if (stockItem == null) {
      stockItem = new StockItemController(new StockItemService());
    }
    return stockItem;
  }

  public static UserController getUserController() {
    if (user == null) {
      user = new UserController(new UserService());
    }
    return user;
  }
}
