package RachlinBabies.Model;

/**
 * Model representing nutrients.
 */
public class Nutrient {
  private String derivationDescription;
  private int nutrientCode;
  private String nutrientName;
  private double outputValue;
  private String outputUom;

  public Nutrient(String derivationDescription, int nutrientCode, String nutrientName,
                  double outputValue, String outputUom) {
    this.derivationDescription = derivationDescription;
    this.nutrientCode = nutrientCode;
    this.nutrientName = nutrientName;
    this.outputValue = outputValue;
    this.outputUom = outputUom;
  }
}
