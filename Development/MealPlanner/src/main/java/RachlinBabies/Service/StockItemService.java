package RachlinBabies.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import RachlinBabies.Model.StockItem;
import RachlinBabies.Utils.DatabaseConnection;

public class StockItemService extends Service<StockItem> implements StockItemDao {

  public List<StockItem> getStock(int stockId) {
    List<StockItem> stockItems = null;
    String query = "SELECT stock_id, stock_item_id, quantity, expiration_date, NDB_Number " +
            "FROM stock_item JOIN product_stock USING (stock_id)" +
            "WHERE user_id = ? AND stock_id = ? " +
            "GROUP BY stock_item_id " +
            "ORDER BY expiration_date ASC";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, stockId);
      try (ResultSet rs = stmt.executeQuery()) {
        stockItems = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return stockItems;
  }

  public List<StockItem> getCriticalStockItems() {
    String query = "SELECT stock_id, stock_item_id, quantity, expiration_date, NDB_Number " +
            "FROM stock_item JOIN product_stock USING (stock_id)" +
            "WHERE user_id = ? " +
            "AND DATEDIFF(CURDATE(), expiration_date) <= 3 " +
            "AND CURDATE() <= expiration_date " +
            "GROUP BY stock_item_id " +
            "ORDER BY expiration_date ASC";
    return getManyStockItem(query);
  }

  public List<StockItem> getExpiredStockItems() {
    String query = "SELECT stock_id, stock_item_id, quantity, expiration_date, NDB_Number " +
            "FROM stock_item JOIN product_stock USING (stock_id)" +
            "WHERE user_id = ? " +
            "AND CURDATE() > expiration_date " +
            "GROUP BY stock_item_id " +
            "ORDER BY expiration_date ASC";
    return getManyStockItem(query);
  }

  private List<StockItem> getManyStockItem(String query) {
    List<StockItem> stockItems = null;
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        stockItems = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return stockItems;
  }

  public boolean updateStockItem(StockItem stockItem) {
    int result = 0;
    String query = "UPDATE stock_item " +
            "SET quantity = ? AND expiration_date = ? " +
            "WHERE stock_item_id = ?";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, stockItem.getQuantity());
      stmt.setTimestamp(2, stockItem.getExpirationDate());
      stmt.setInt(3, stockItem.getStockItemId());
      result = stmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return result > 0;
  }

  public boolean createStockItem(StockItem stockItem) {
    int result = 0;
    String query = "CALL add_stock(?,?,?,?)";
    Connection connection = DatabaseConnection.getConnection();
    try (CallableStatement stmt = connection.prepareCall(query)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, stockItem.getNdb());
      stmt.setInt(3, stockItem.getQuantity());
      stmt.registerOutParameter(4, Types.INTEGER);
      stmt.executeUpdate();
      result = stmt.getInt(4);
    } catch(SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return result > 0;
  }

  public boolean deleteStockItem(int stockItemId) {
    int result = 0;
    String query = "DELETE FROM stock_item WHERE stock_item_id = ?";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, stockItemId);
      result = stmt.executeUpdate();
    } catch(SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return result > 0;
  }

  StockItem convert(ResultSet rs) throws SQLException {
    return new StockItem(rs.getInt("stock_id"),
            rs.getInt("stock_item_id"),
            rs.getInt("quantity"),
            rs.getTimestamp("expiration_date"),
            rs.getInt("NDB_Number"));
  }
}
