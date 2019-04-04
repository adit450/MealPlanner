package RachlinBabies;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;


import RachlinBabies.Utils.DatabaseConnection;

class DBTestUtils {

  static void initData() throws SQLException {
    String query = "CALL init_db()";
    Connection connection = DatabaseConnection.getConnection();
    try (CallableStatement stmt = connection.prepareCall(query)) {
      stmt.executeUpdate();
    }
  }
}
