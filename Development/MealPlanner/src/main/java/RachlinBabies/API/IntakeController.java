package RachlinBabies.API;

import RachlinBabies.Service.IntakeDao;

import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.*;

public class IntakeController {

  IntakeController(final IntakeDao intakeService) {

    get("/intakes", (req, res) -> intakeService.getAll(), json());
  }
}
