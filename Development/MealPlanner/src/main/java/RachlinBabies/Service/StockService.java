package RachlinBabies.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import RachlinBabies.Model.Stock;
import RachlinBabies.Utils.DatabaseConnection;

public class StockService extends Service<Stock> implements StockDao {

  public List<Stock> getStocks() {
    List<Stock> stocks = null;
    String query = "SELECT stock.*, sum(quantity) as 'quantity'\n" +
            "long_name, expr_rate, manufacturer, serving_size,\n" +
            "serving_size_uom, household_serving_size, household_serving_size_uom\n" +
            "FROM product_stock stock\n" +
            "JOIN stock_item USING (stock_id)\n" +
            "JOIN product p USING (NDB_Number)\n" +
            "JOIN serving_size ss ON (p.NDB_Number = ss.product_NDB_Number)\n" +
            "WHERE user_id = ?\n" +
            "GROUP BY stock_id";
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        stocks = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return stocks;
  }

  Stock convert(ResultSet rs) throws SQLException {
    return new Stock.StockBuilder()
            .ndb(rs.getInt("NDB_Number"))
            .quantity(rs.getInt("quantity"))
            .stockId(rs.getInt("stock_id"))
            .userId(rs.getInt("user_id"))
            .product(new ProductService().convert(rs))
            .build();
  }
}
