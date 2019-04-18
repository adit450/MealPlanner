package RachlinBabies.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import RachlinBabies.Model.Tag;
import RachlinBabies.Utils.DatabaseConnection;

public class TagService extends Service<Tag> implements TagDao {

  public Tag getTag(int id) {
    Tag tag = null;
    String query = "SELECT * FROM tag WHERE tag_id = ?";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          tag = convert(rs);
        }
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return tag;
  }


  public List<Tag> searchTag(String name) {
    List<Tag> tags = null;
    String query = "SELECT * FROM tag WHERE tag_name LIKE ? ORDER BY tag_name";
    String filter = String.format("%%%s%%", name);
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, filter);
      try (ResultSet rs = stmt.executeQuery()) {
        tags = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return tags;
  }


  public List<Tag> getTags() {
    List<Tag> tags = null;
    String query = "SELECT * FROM tag ORDER BY tag_name";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      try (ResultSet rs = stmt.executeQuery()) {
        tags = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return tags;
  }

  public boolean createTag(Tag tag) {
    int inserts = 0;
    String query = "INSERT INTO tag (tag_name) VALUES (?)";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      inserts = stmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return inserts > 0;
  }

  Tag convert(ResultSet rs) throws SQLException {
    return new Tag(rs.getInt("tag_id"), rs.getString("tag_name"));
  }
}
