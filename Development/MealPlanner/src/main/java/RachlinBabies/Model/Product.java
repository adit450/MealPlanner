package RachlinBabies.Model;

import java.util.Set;

/**
 * Model representing a Product.
 */
public class Product {
  private int ndb;
  private String longName;
  private int exprRate;
  private String manufacturer;
  private double servingSize;
  private double householdServingSize;
  private String servingSizeUom;
  private String householdServingSizeUom;
  private String ingredients;
  private Set<Nutrient> nutrients;

  public Product(int ndb, String longName, int exprRate, String manufacturer,
                 double servingSize, double householdServingSize,
                 String servingSizeUom, String householdServingSizeUom,
                 String ingredients) {
    this.ndb = ndb;
    this.longName = longName;
    this.exprRate = exprRate;
    this.manufacturer = manufacturer;
    this.servingSize = servingSize;
    this.householdServingSize = householdServingSize;
    this.servingSizeUom = servingSizeUom;
    this.householdServingSizeUom = householdServingSizeUom;
    this.ingredients = ingredients;
  }

  public Product(int ndb, String longName, int exprRate, String manufacturer,
                 double servingSize, double householdServingSize,
                 String servingSizeUom, String householdServingSizeUom) {
    this(ndb, longName, exprRate, manufacturer, servingSize,
            householdServingSize, servingSizeUom, householdServingSizeUom, null);
  }

  public void setNutrients(Set<Nutrient> nutrients) {
    this.nutrients = nutrients;
  }
}
