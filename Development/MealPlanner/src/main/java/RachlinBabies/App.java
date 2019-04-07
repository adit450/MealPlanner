package RachlinBabies;

import RachlinBabies.API.ControllerFactory;

public class App 
{
    public static void main( String[] args ) {
      ControllerFactory.getProductController();
      ControllerFactory.getIntakeController();
      ControllerFactory.getStockController();
      ControllerFactory.getStockItemController();
    }
}
