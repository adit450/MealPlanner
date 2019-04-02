package RachlinBabies.Service;

import java.util.List;

/**
 * Operations offered by a DataAccessObject, an object used to query the database.
 * @param <M> The Model being operated on.
 */
public interface DataAccessObject<M> {
  M get(int id);
  List<M> getAll();
  boolean update(M toUpdate);
  boolean delete(int id);
  boolean create(M toCreate);
}
