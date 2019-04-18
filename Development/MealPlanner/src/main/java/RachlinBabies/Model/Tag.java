package RachlinBabies.Model;


import java.util.List;

/**
 * Class representing the tags of a recipe.
 */
public class Tag {
  private Integer id;
  private String name;

  public Tag(){}

  public Tag(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return this.name;
  }
}