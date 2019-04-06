package RachlinBabies.Service;

import RachlinBabies.Model.User;

import RachlinBabies.Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class UserService extends Service<User> implements UserDao{

  /**
   * To be used when the user logs in.  Sets the session user Id so that all queries
   * only return data that the session user is allowed to see.
   * Don't wanna deal with sessions.
   *
   * @param id the id of the logged in user.
   */
  private static void setUser(int id) {
    userId = id;
  }

  @Override
  public boolean create(User toInsert) {
    int result = 0;
    String query = String.format(
            "INSERT INTO user (email, username, password) VALUES (?,?,?)");
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, toInsert.getEmail());
      stmt.setString(2, toInsert.getUsername());
      stmt.setString(3, toInsert.getPassword());
      result = stmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return result > 0;
  }
  // User(Integer id, String username, String password, String email)
  User convert(ResultSet rs) throws SQLException {
    Integer id = rs.getInt("user_id");
    String username = rs.getString("username");
    String email = rs.getString("email");
    String password = rs.getString("password");

    return new User(id, username, email, password);
  }

  @Override
  public User get(int id) {
    User rv = null;
    String query = "select * from user where user_id = ?";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          rv = convert(rs);
        }
      }
    } catch(SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return rv;
  }

  @Override
  public List<User> getAll() {
    List<User> all = null;
    String query = "select * from user";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          all = convertList(rs);
        }
      }
    } catch(SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return all;
  }

  public boolean login(String username, String password) {
    User usr= null;
    String query = "select * from user where username = ? and password = ?";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          usr = convert(rs);
        }
      }
    } catch(SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    if(usr != null) {
      setUser(usr.getId());
      return true;
    }
    return false;
  }
}
