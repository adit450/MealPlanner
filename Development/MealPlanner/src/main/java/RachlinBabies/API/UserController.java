package RachlinBabies.API;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import RachlinBabies.Model.User;
import RachlinBabies.Service.UserDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.before;
import static spark.Spark.options;
import static spark.Spark.post;

class UserController {

    UserController(final UserDao userService) {
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        
        post("/register", (req, res) -> {
            User usr = dateGson().fromJson(req.body(), User.class);
            if (userService.create(usr)) {
                return new ResponseMessage("User successfully inserted");
            } else {
                res.status(400);
                return new ResponseMessage("Failed to insert intake");
            }
        }, json());

        post("/login", (req, res) -> {

            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = req.body();

                Map<String, String> map = new HashMap<String, String>();

                // convert JSON string to Map
                map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
                });
                if(userService.login(map.get("username"), map.get("password"))) {
                    return new ResponseMessage("Login Succeeded!");
                }
                return new ResponseMessage("Login Failed");
            } catch (JsonGenerationException e) {
                e.printStackTrace();
                return new ResponseMessage("Login Failed");
            } catch (JsonMappingException e) {
                e.printStackTrace();
                return new ResponseMessage("Login Failed");
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseMessage("Login Failed");
            }
        }, json());
    }
}
