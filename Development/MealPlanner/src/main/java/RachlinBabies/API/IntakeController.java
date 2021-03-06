package RachlinBabies.API;

import RachlinBabies.Model.Intake;
import RachlinBabies.Service.IntakeDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static RachlinBabies.Utils.JsonUtil.toJson;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class IntakeController {

  IntakeController(final IntakeDao intakeService) {

    get("/intakes", (req, res) -> intakeService.getAll(), json());

    get("/intakes/:id", (req, res) -> {
      int id = Integer.parseInt(req.params(":id"));
      Intake intake = intakeService.get(id);
      if (intake != null) {
        return intake;
      }
      res.status(404);
      return new ResponseMessage(String.format("No intake with id %d found", id));
    }, json());

    post("/intakes", (req, res) -> {
      Intake intake = dateGson()
              .fromJson(req.body(), Intake.class);
      if (intakeService.create(intake)) {
        return new ResponseMessage("Intake successfully inserted");
      } else {
        res.status(400);
        return new ResponseMessage("Failed to insert intake");
      }
    }, json());

    put("/intakes/time", (req, res) -> {
      Intake intake = dateGson()
              .fromJson(req.body(), Intake.class);
      if (intakeService.updateIntakeTime(intake)) {
        return new ResponseMessage("Intake successfully updated");
      } else {
        res.status(400);
        return new ResponseMessage("Failed to update intake");
      }
    }, json());

    exception(Exception.class, (e, req, res) -> {
      res.status(400);
      res.body(toJson(new ResponseMessage(e)));
    });
  }
}
