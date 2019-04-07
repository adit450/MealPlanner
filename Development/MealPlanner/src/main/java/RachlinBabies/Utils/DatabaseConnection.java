package RachlinBabies.Utils;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * The Class DatabaseConnection.
 */
public abstract class DatabaseConnection {


  private static final String HOST = ConfigurationProperties.getInstance().getProperty("dbhost");

  private static final String DB_NAME = ConfigurationProperties.getInstance().getProperty("dbname");

  private static final String USERNAME =
          ConfigurationProperties.getInstance().getProperty("dbusername");

  private static final String PASSWORD =
          ConfigurationProperties.getInstance().getProperty("dbpassword");

  private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

  /**
   * To prevent instantiation of this class.
   */
  private DatabaseConnection() {}

  /**
   * Gets the database dataSource.
   *
   * @return the dataSource
   */
  private static DataSource getDataSource() {
    MysqlDataSource ds = new MysqlDataSource();
    ds.setUrl(String.format("%s%s?useSSL=false", HOST, DB_NAME));
    ds.setUser(USERNAME);
    ds.setPassword(PASSWORD);
    return ds;
  }

  /**
   * Gets a database connection.
   * @return the connection.
   */
  public static Connection getConnection() {
    DataSource ds = getDataSource();
    Connection con = null;
    try {
      con = ds.getConnection();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return con;
  }

  /**
   * Close the given connection
   * @param connection the connection to close.
   */
  public static void closeConnection(Connection connection) {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public static void setAutoCommit(Connection connection, boolean bool) {
    try {
      connection.setAutoCommit(bool);
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public static void rollback(Connection connection) {
    try {
      connection.rollback();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }
}
