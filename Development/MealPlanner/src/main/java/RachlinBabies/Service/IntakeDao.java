package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Intake;

/**
 * Queries offered on Intake.
 */
public interface IntakeDao {
  /**
   * Get intake with the given Id
   * @param id id of the intake
   * @return desired intake
   */
  Intake get(int id);

  /**
   * Get all the intakes visible to the session user.
   * @return List of intakes
   */
  List<Intake> getAll();

  /**
   * Create a new Intake
   * @param toInsert Model of the desired intake to insert
   * @return whether or not the insert was successful.
   */
  boolean create(Intake toInsert);

  /**
   * Updates the Intake time of the intake specified with the id.  Requires intake_id and new time.
   * @param intakeWithTimestamp the intake with the desired Timestamp.  Only the timestamp will
   *                            be updated.
   * @return whether or not the update was successful.
   */
  boolean updateIntakeTime(Intake intakeWithTimestamp);

}
