package RachlinBabies.API;

import RachlinBabies.Model.User;
import RachlinBabies.Service.UserDao;
import RachlinBabies.Service.UserService;
import RachlinBabies.Utils.ResponseMessage;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static RachlinBabies.Utils.JsonUtil.*;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;

class UserController {

    UserController(final UserDao userService) {
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
