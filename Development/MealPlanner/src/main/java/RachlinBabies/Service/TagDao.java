package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Tag;

public interface TagDao {
  /**
   * Gets the tag with the given id.
   * @param id the id of the tag to find.
   * @return the desired Tag. Returns null if no such tag exists.
   */
  Tag getTag(int id);

  /**
   * Gets all the tags with the given name.
   * @param name the name to filter by.
   * @return the tags that match the given name.
   */
  List<Tag> searchTag(String name);

  /**
   * Gets all tags.
   * @return list of all tags.
   */
  List<Tag> getTags();

  /**
   * Create a new tag.
   * @param tag The tag to insert. Only the Name is needed.
   * @return whether or not the insert was successful.
   */
  boolean createTag(Tag tag);
}
