package RachlinBabies.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.ResponseTransformer;

/**
 * Utility class for converting objects to JSON
 */
public class JsonUtil {

  public static Gson dateGson() {
    return new GsonBuilder().setDateFormat("d MMM yyyy HH:mm:ss").create();
  }

  public static String toJson(Object object) {
    return dateGson().toJson(object);
  }

  public static ResponseTransformer json() {
    return JsonUtil::toJson;
  }
}
