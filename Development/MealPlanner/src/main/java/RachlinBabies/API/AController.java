package RachlinBabies.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import RachlinBabies.DBUtils.DatabaseConnection;

public class AController implements IController {

  static int userId;
  private final String table;
  private final String pk;

  AController(String table, String pk) {
    this.table = table;
    this.pk = pk;
  }

  public static void setUserId(int id) {
    userId = id;
  }

  public Map<String, Object> get(int id) {
    Connection con = DatabaseConnection.getConnection();
    try {
      ResultSet rs;
      try (PreparedStatement stmt = con
              .prepareStatement(String.format("SELECT * FROM %s WHERE user_id = ? AND %s = ?",
                      table, pk))) {
        stmt.setInt(1, userId);
        stmt.setInt(2, id);
        rs = stmt.executeQuery();
      }
      List<Map<String, Object>> output = DatabaseConnection.resultsList(rs);
      if (!output.isEmpty()) {
        return output.get(0);
      }
      rs.close();
    } catch (SQLException e) {
      Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(con);
    }
    return null;
  }

  public void delete(int id) {
    Connection con = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = con
            .prepareStatement(String.format("DELETE FROM %s WHERE user_id = ? AND %s = ?",
                    table, pk))) {
      stmt.setInt(1, userId);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(con);
    }
  }

  public void update(int id, Map<String, Object> values) {
    StringBuilder query = new StringBuilder(String.format("UPDATE %s ", table));
    int i = 0;
    int size = values.size();
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      query.append(String.format("SET %s = %s", entry.getKey(), String.valueOf(entry.getValue())));
      if (i != size - 1) {
        query.append(", ");
      }
      i++;
    }
    query.append(String.format(" WHERE %s = %d", pk, id));
    Connection con = DatabaseConnection.getConnection();
    try {
      try (Statement stmt = con.createStatement()) {
        stmt.executeUpdate(query.toString());
      }
    } catch (SQLException e) {
      Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(con);
    }
  }

  public void create(Map<String, Object> values) {
    StringBuilder fields = new StringBuilder("(");
    StringBuilder vals = new StringBuilder("(");
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      fields.append(entry.getKey());
      fields.append(",");
      vals.append(String.valueOf(entry.getValue()));
      vals.append(",");
    }
    fields.append("userID)");
    vals.append(String.format("%d)", userId));
    String query = String.format("INSERT INTO %s %s VALUES %s",
            table, fields.toString(), vals.toString());
    Connection con = DatabaseConnection.getConnection();
    try {
      try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
        stmt.executeUpdate(query);
      }
    } catch (SQLException e) {
      Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(con);
    }
  }
}
