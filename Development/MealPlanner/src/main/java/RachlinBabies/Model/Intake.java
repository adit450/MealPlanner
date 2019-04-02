package RachlinBabies.Model;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Map;

/**
 * Model representing the intake tables.
 */
public class Intake {

  private int id;
  private int userId;
  private int sourceId;
  private int servings;
  private Timestamp intakeDate;
  private IntakeType type;

  public enum IntakeType {
    STK("STK"),

    RCP("RCP");

    private String abbreviation;

    IntakeType(String abbrev) {
      abbreviation = abbrev;
    }

    @Override
    public String toString() {
      return abbreviation;
    }
  }

  private static Map<String, IntakeType> types;

  static {
    types = new Hashtable<>();
    for (IntakeType type : IntakeType.values()) {
      types.put(type.toString(), type);
    }
  }

  public Intake(int id, int userId, int sourceId, int servings,
                 Timestamp intakeDate, IntakeType type) {
    this.id = id;
    this.userId = userId;
    this.sourceId = sourceId;
    this.servings = servings;
    this.intakeDate = intakeDate;
    this.type = type;
  }

  /**
   * Builder class for Intake.
   */
  public static class IntakeBuilder {
    private int id;
    private int userId;
    private int sourceId;
    private int servings;
    private Timestamp intakeDate;
    private IntakeType type;

    public IntakeBuilder id(int id) {
      this.id = id;
      return this;
    }
    public IntakeBuilder userId(int userId) {
      this.userId = userId;
      return this;
    }
    public IntakeBuilder sourceId(int sourceId) {
      this.sourceId = sourceId;
      return this;
    }
    public IntakeBuilder servings(int servings) {
      this.servings = servings;
      return this;
    }
    public IntakeBuilder intakeDate(Timestamp intakeDate) {
      this.intakeDate = intakeDate;
      return this;
    }
    public IntakeBuilder intakeType(String type) {
      this.type = types.get(type);
      return this;
    }

    public Intake build() {
      return new Intake(this.id, this.userId, this.sourceId, this.servings,
              this.intakeDate, this.type);
    }
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public int getSourceId() {
    return sourceId;
  }

  public int getServings() {
    return servings;
  }

  public Timestamp getIntakeDate() {
    return intakeDate;
  }

  public IntakeType getType() {
    return type;
  }
}
