package RachlinBabies.Service;

public abstract class UserService extends Service {

  /**
   * To be used when the user logs in.  Sets the session user Id so that all queries
   * only return data that the session user is allowed to see.
   * Don't wanna deal with sessions.
   *
   * @param id the id of the logged in user.
   */
  private static void setUser(int id) {
    userId = id;
  }
}
