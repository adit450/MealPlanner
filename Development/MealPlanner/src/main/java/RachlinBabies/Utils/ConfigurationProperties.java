package RachlinBabies.Utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class ConfigurationProperties.
 */
public class ConfigurationProperties {

  private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

  private static ConfigurationProperties singleton;

  private static final String PROPERTIES_FILE_NM = "config.properties";

  private Properties prop;

  /**
   * Instantiates a new configuration properties.
   */
  private ConfigurationProperties() {
    prop = new Properties();
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NM);

      if (inputStream != null) {
        prop.load(inputStream);
        inputStream.close();
      } else {
        LOGGER.log(Level.SEVERE, "Unable to load properties file");
      }
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }

  }

  /**
   * Gets the single instance of ConfigurationProperties.
   *
   * @return single instance of ConfigurationProperties
   */
  static ConfigurationProperties getInstance(){
    if (null == singleton) {
      singleton = new ConfigurationProperties();
    }
    return singleton;
  }

  /**
   * Gets the property by name.
   *
   * @param propertyName the property name
   * @return the property
   */
  String getProperty(String propertyName) {
    if(null == prop) {
      return null;
    }
    return prop.getProperty(propertyName);
  }
}
