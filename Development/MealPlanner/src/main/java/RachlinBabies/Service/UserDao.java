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

    /** Allows this user to follow the user with the given id
     * @param id of the user to be followed
     * @return whether or not the follow was successful
     */

    boolean followUser(int id);

    /** Allows this user to unfollow the user with the given id
     * @param id of the user to be followed
     * @return whether or not the follow was successful
     */

   boolean unfollowUser(int id);

   /** Gets a list of all users that follow the given user
    * @return list of all users that follow the given user
    */

   List<User> getFollowers();

   /** Gets a list of all user that are followed by the given user
     * @return list of all user that are followed by the given user
     */

   List<User> getFollowing();
}
