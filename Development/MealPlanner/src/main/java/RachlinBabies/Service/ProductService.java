package RachlinBabies.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import RachlinBabies.Model.Nutrient;
import RachlinBabies.Model.Product;
import RachlinBabies.Utils.DatabaseConnection;

/**
 * Service class that holds all the queries on Products.
 */
public class ProductService extends Service<Product> implements ProductDao {

  public List<Product> searchProduct(String name) {
    List<Product> products = null;
    String query = "SELECT NDB_Number, long_name, expr_rate, manufacturer, serving_size, " +
            "serving_size_uom, household_serving_size, household_serving_size_uom\n" +
            "FROM product JOIN serving_size ON (product.NDB_Number = serving_size.product_NDB_Number)\n" +
            "where long_name like ?\n" +
            "ORDER BY long_name";
    String param = String.format("%%%s%%", name);
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, param);
      try (ResultSet rs = stmt.executeQuery()) {
        products = convertList(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return products;
  }

  public Product get(int ndb) {
    Product product = null;
    String productQuery = "SELECT NDB_Number, long_name, expr_rate, manufacturer, serving_size, serving_size_uom, household_serving_size, household_serving_size_uom, ingredients_english \n" +
            "FROM product JOIN serving_size ON (product.NDB_Number = serving_size.product_NDB_Number)\n" +
            "where NDB_Number = ?";
    String nutrientQuery = "SELECT Derivation_Descript, nutrient_code, nutrient_name, output_value, output_uom\n" +
            "FROM nutrient\n" +
            "JOIN derivation_code_description as derivation_description \n" +
            "ON (nutrient.derivation_code = derivation_description.derivation_code)\n" +
            "where product_ndb_number = ?";
    Set<Nutrient> nutrients = new HashSet<>();
    Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement selectProduct = connection.prepareStatement(productQuery)) {
      selectProduct.setInt(1, ndb);
      try (ResultSet rs = selectProduct.executeQuery()) {
        if (rs.first()) {
          product = convert(rs);
          product.setIngredients(rs.getString("ingredients_english"));
          try (PreparedStatement selectNutrient = connection.prepareStatement(nutrientQuery)) {
            selectNutrient.setInt(1, ndb);
            try (ResultSet rs2 = selectNutrient.executeQuery()) {
              while (rs2.next()) {
                nutrients.add(convertNutrient(rs2));
              }
              product.setNutrients(nutrients);
            }
          }
        }
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DatabaseConnection.closeConnection(connection);
    }
    return product;
  }

  Product convert(ResultSet rs) throws SQLException {
    return new Product(rs.getInt("NDB_Number"),
            rs.getString("long_name"),
            rs.getInt("expr_rate"),
            rs.getString("manufacturer"),
            rs.getDouble("serving_size"),
            rs.getDouble("household_serving_size"),
            rs.getString("serving_size_uom"),
            rs.getString("household_serving_size_uom"));
  }

  private Nutrient convertNutrient(ResultSet rs) throws SQLException {
    return new Nutrient(rs.getString("Derivation_Descript"),
            rs.getInt("nutrient_code"),
            rs.getString("nutrient_name"),
            rs.getDouble("output_value"),
            rs.getString("output_uom"));
  }
}
