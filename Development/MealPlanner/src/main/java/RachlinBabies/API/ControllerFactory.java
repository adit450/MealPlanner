package RachlinBabies.API;

import RachlinBabies.Service.IntakeService;

public abstract class ControllerFactory {
  private static IntakeController intake;

  private ControllerFactory() {}

  public static IntakeController getIntakeController() {
    if (intake == null) {
      intake = new IntakeController(new IntakeService());
    }
    return intake;
  }
}
