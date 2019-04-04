package RachlinBabies;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import spark.utils.IOUtils;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static junit.framework.Assert.fail;

class TestResponse {
  final String body;
  final int status;
  static final Set<String> FAKE_METHODS;

  static {
    FAKE_METHODS = new HashSet<>();
    FAKE_METHODS.add("PATCH");
  }

  TestResponse(int status, String body) {
    this.status = status;
    this.body = body;
  }

  Map<String,String> json() {
    return dateGson().fromJson(body, HashMap.class);
  }

  List<String> jsonArray() { return dateGson().fromJson(body, ArrayList.class); }

  static TestResponse request(String method, String path) {
    return request(method, path, null);
  }

  static TestResponse request(String method, String path, String payload) {
    try {
      URL url = new URL("http://localhost:4567" + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      if (FAKE_METHODS.contains(method)) {
        connection.setRequestProperty("X-HTTP-Method-Override", method);
        connection.setRequestMethod("PUT");
      } else {
        connection.setRequestMethod(method);
      }
      connection.setDoOutput(true);
      if (payload != null) {
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write(payload);
        writer.close();
      }
      connection.connect();
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        String body = IOUtils.toString(connection.getInputStream());
        return new TestResponse(connection.getResponseCode(), body);
      } else {
        String body = IOUtils.toString(connection.getErrorStream());
        return new TestResponse(connection.getResponseCode(), body);
      }
    } catch (IOException e) {
      e.printStackTrace();
      fail("Sending request failed: " + e.getMessage());
      return null;
    }
  }
}
