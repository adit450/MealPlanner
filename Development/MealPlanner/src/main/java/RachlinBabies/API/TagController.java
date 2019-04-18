package RachlinBabies.API;

import RachlinBabies.Model.Tag;
import RachlinBabies.Service.TagDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class TagController {

  TagController(final TagDao tagService) {
    get("/tags/:id", (req, res) -> {
      int tagId = Integer.parseInt(req.params(":id"));
      Tag tag = tagService.getTag(tagId);
      if (tag != null) {
        return tag;
      }
      res.status(404);
      return new ResponseMessage(String.format("No recipe with id %d found", tagId));
    }, json());

    get("/tags", (req, res) -> tagService.getTags(), json());

    get("/tags/search/:name", (req, res) -> {
      String name = req.params(":name");
      return tagService.searchTag(name);
    }, json());

    post("/tags", (req, res) -> {
      Tag payload = dateGson().fromJson(req.body(), Tag.class);
      if (tagService.createTag(payload)) {
        return new ResponseMessage("Tag successfully inserted");
      }
      res.status(400);
      return new ResponseMessage("Failed to insert tag");
    }, json());
  }
}
