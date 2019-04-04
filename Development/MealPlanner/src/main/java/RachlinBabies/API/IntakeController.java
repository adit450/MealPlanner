package RachlinBabies.API;

import java.sql.Timestamp;

import RachlinBabies.Model.Intake;
import RachlinBabies.Service.IntakeDao;
import RachlinBabies.Utils.ResponseError;

import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.*;

class IntakeController {

  IntakeController(final IntakeDao intakeService) {

    get("/intakes", (req, res) -> intakeService.getAll(), json());

    get("/intakes/:id", (req, res) -> {
      int id = Integer.parseInt(req.params(":id"));
      Intake intake = intakeService.get(id);
      if (intake != null) {
        res.type("application/json");
        return intake;
      }
      res.status(404);
      return new ResponseError(String.format("No intake with id %d found", id));
    }, json());

    post("/intakes", (req, res) -> {
      Intake intake = new Intake.IntakeBuilder()
              .sourceId(Integer.parseInt(req.params(":source_id")))
              .servings(Integer.parseInt(req.params(":servings")))
              .intakeType(req.params(":type"))
              .intakeDate(Timestamp.valueOf(req.params(":intakeDate")))
              .build();
      if (intakeService.create(intake)) {
        return "Intake successfully inserted";
      } else {
        res.status(400);
        return new ResponseError("Failed to insert intake");
      }
    }, json());

    patch("/intakes/:id/time", (req, res) -> {
      Intake intake = new Intake.IntakeBuilder()
              .id(Integer.parseInt(req.params(":id")))
              .intakeDate(Timestamp.valueOf(req.params(":intakeDate")))
              .intakeType(req.params(":type"))
              .build();
      if (intakeService.updateIntakeTime(intake)) {
        return "Intake successfully updated";
      } else {
        res.status(400);
        return new ResponseError("Failed to update intake");
      }
    }, json());

    /*
    delete("/intakes/:id", (req, res) -> {
      int id = Integer.parseInt(req.params(":id"));
      Intake intake = intakeService.get(id);
      if (intake == null) {
        res.status(404);
        return new ResponseError(String.format("No intake with id %d found", id));
      } else if (!intakeService.delete(id)) {
        res.status(400);
        return new ResponseError(String.format("Failed to delete intake with id %d", id));
      } else {
        return String.format("Intake %d successfully deleted", id);
      }
    }, json());
    */
  }
}
