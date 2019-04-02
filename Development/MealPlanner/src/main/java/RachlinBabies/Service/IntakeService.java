package RachlinBabies.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import RachlinBabies.Utils.DatabaseConnection;
import RachlinBabies.Model.Intake;

/**
 * Service class that holds all the queries to the database that relate to Intakes.
 */
public class IntakeService extends Service<Intake> implements IntakeDao {

  private static final Map<Intake.IntakeType, String> TABLES;
  private static final Map<Intake.IntakeType, String> FK;

  static {
    TABLES = new Hashtable<>();
    FK = new Hashtable<>();
    TABLES.put(Intake.IntakeType.STK, "intake_stock");
    TABLES.put(Intake.IntakeType.RCP, "intake_recipe");
    FK.put(Intake.IntakeType.STK, "stock_id");
    FK.put(Intake.IntakeType.RCP, "recipe_id");
  }

  /**
   * Inserts a new Intake into the database.
   * 
   * @param toInsert The intake to insert
   * @return whether or not the insert was successful.
   */
  public boolean create(Intake toInsert) {
    int result = 0;
    String query = String.format(
            "INSERT INTO %s (user_id, %s, servings, intake_time) VALUES (?,?,?,?)",
            FK.get(toInsert.getType()), TABLES.get(toInsert.getType()));
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, toInsert.getSourceId());
      stmt.setInt(3, toInsert.getServings());
      stmt.setTimestamp(4, toInsert.getIntakeDate());
      result = stmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return result > 0;
  }

  /**
   * Returns the intake with the given intakeId.
   * @param intakeId the id of the intake to return.
   * @return the specified Intake
   */
  public Intake get(int intakeId) {
    return null;
  }

  /**
   * Updates the Intake with the same Id with the given attributes.
   * @param toUpdate the Intake Object holding the wanted attributes.
   * @return if the update was successful
   */
  public boolean update(Intake toUpdate) {
    return false;
  }

  /**
   * Deletes the specified intake.
   * @param intakeId the intakeId fo the desired intake to delete
   * @return if the delete was successful
   */
  public boolean delete(int intakeId) {
    return false;
  }

  /**
   * Gets all the intakes visible to the user.
   *
   * @return List of both recipe and stock intakes ordered by date.
   */
  public List<Intake> getAll() {
    List<Intake> intakes = null;
    String query = "SELECT * FROM " +
            "(SELECT intake_id, user_id, recipe_id as 'source_id', servings, intake_time, " +
            "'RCP' as type FROM intake_recipe WHERE user_id = ? " +
            "UNION " +
            "SELECT intake_id, user_id, stock_id as 'source_id', servings, intake_time, " +
            "'STK' as type FROM intake_stock WHERE user_id = ?) intakes " +
            "ORDER BY intake_time DESC";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, userId);
      try(ResultSet rs = stmt.executeQuery()){
        intakes = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return intakes;
  }

  /**
   * Extracts an Intake Model from the ResultSet
   * @param rs the ResultSet
   * @return the Intake made from the ResultSet
   * @throws SQLException when JDBC dies
   */
  Intake convert(ResultSet rs) throws SQLException {
    return new Intake.IntakeBuilder()
            .id(rs.getInt("intake_id"))
            .userId(rs.getInt("user_id"))
            .sourceId(rs.getInt("source_id"))
            .servings(rs.getInt("servings"))
            .intakeDate(rs.getTimestamp("intake_time"))
            .intakeType(rs.getString("type"))
            .build();
  }
}
