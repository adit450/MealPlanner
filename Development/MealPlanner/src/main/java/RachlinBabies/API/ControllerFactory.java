package RachlinBabies.API;

import RachlinBabies.Service.IntakeService;
import RachlinBabies.Service.ProductService;

public abstract class ControllerFactory {
  private static IntakeController intake;
  private static ProductController product;

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
}
