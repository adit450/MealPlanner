package RachlinBabies.Model;


import java.sql.Timestamp;
import java.util.Set;

public class Recipe {

  private Integer recipeId;
  private Integer userId;
  private String instructions;
  private String name;
  private String descriptions;
  private Integer yield;
  private Timestamp createdOnDate;
  private Set<RecipeProduct> ingredients;

  public class RecipeProduct {
    Product product;
    int servings;

    public RecipeProduct(){}
    public RecipeProduct(Product product, int servings){
      this.product = product;
      this.servings = servings;
    }
  }

  public Recipe() {}


  private Recipe(int recipeId, int userId, String instructions, String name, String descriptions,
                 int yield, Timestamp date) {
    this.recipeId = recipeId;
    this.userId = userId;
    this.instructions = instructions;
    this.name = name;
    this.descriptions = descriptions;
    this.yield = yield;
    this.createdOnDate = date;
  }

  public void setIngredients(Set<RecipeProduct> ingredients) {
    this.ingredients = ingredients;
  }

  /**
   * Builder class for Intake.
   */
  public static class RecipeBuilder {

    private int recipeId;
    private int userId;
    private String instructions;
    private String name;
    private String descriptions;
    private int yield;
    private Timestamp createdOnDate;


    public RecipeBuilder recipeId(int recipeId) {
      this.recipeId = recipeId;
      return this;
    }

    public RecipeBuilder userId(int userId) {
      this.userId = userId;
      return this;
    }

    public RecipeBuilder instructions(String instructions) {
      this.instructions = instructions;
      return this;
    }

    public RecipeBuilder name(String name) {
      this.name = name;
      return this;
    }

    public RecipeBuilder descriptions(String descriptions) {
      this.descriptions = descriptions;
      return this;
    }

    public RecipeBuilder createdOnDate(Timestamp createdOnDate) {
      this.createdOnDate = createdOnDate;
      return this;
    }

    public RecipeBuilder yield(int yield) {
      this.yield = yield;
      return this;
    }

    public Recipe build() {
      return new Recipe(this.recipeId, this.userId, this.instructions, this.name,
              this.descriptions, this.yield, this.createdOnDate);
    }


    public int getRecipeId() {
      return recipeId;
    }

    public int getUserId() {
      return userId;
    }

    public String getInstructions() {
      return instructions;
    }

    public String getName() {
      return name;
    }

    public String getDescriptions() {
      return descriptions;
    }

    public int getYield() {
      return yield;
    }

    public Timestamp getCreatedOnDate() {
      return createdOnDate;
    }

  }
}




