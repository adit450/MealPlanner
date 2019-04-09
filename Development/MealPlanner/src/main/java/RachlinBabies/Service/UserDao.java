package RachlinBabies.Service;

import RachlinBabies.Model.User;

import java.util.List;

public interface UserDao {

    /**
     * Get user with the given Id
     * @param id id of the user
     * @return desired user
     */
    User get(int id);

    /**
     * Get all the users
     * @return List of users
     */
    List<User> getAll();

    /**
     * Create a new user
     * @param toInsert desired user to insert
     * @return whether or not the insert was successful.
     */
    boolean create(User toInsert);

    boolean login(String username, String password);

}
