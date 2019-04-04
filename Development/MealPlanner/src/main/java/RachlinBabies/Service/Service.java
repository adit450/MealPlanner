package RachlinBabies.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract Service class that promises helper methods to convert ResultSets to the related model.
 * @param <M> The Model being operated on.
 */
public abstract class Service<M> {
  // set to 1 for testing purposes.
  static int userId = 1;
  static final Logger LOGGER = Logger.getLogger(Service.class.getName());

  /**
   * Converts a query result into a java model.
   * @param rs the ResultSet from the database
   * @return the Model object
   * @throws SQLException when JDBC dies
   */
  abstract M convert(ResultSet rs) throws SQLException;

  /**
   * Converts a ResultSet into a List of Models
   * @param rs the ResultSet from the database
   * @return a List of models represented by the ResultSet
   * @throws SQLException when JDBC dies
   */
  List<M> convertList(ResultSet rs) throws SQLException {
    List<M> conversion = new ArrayList<>();
    while (rs.next()) {
      conversion.add(convert(rs));
    }
    return conversion;
  }
}
