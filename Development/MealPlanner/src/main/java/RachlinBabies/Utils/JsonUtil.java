package RachlinBabies.Utils;

import com.google.gson.Gson;

import spark.ResponseTransformer;

/**
 * Utility class for converting objects to JSON
 */
public class JsonUtil {

  public static String toJson(Object object) {
    return new Gson().toJson(object);
  }

  public static ResponseTransformer json() {
    return JsonUtil::toJson;
  }
}
