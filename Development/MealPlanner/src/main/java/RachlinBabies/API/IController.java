package RachlinBabies.API;

import java.util.Map;

interface IController {

  Map<String, Object> get(int id);

  void create(Map<String, Object> attributes);

  void update(int id, Map<String, Object> attributes);

  void delete(int id);

}