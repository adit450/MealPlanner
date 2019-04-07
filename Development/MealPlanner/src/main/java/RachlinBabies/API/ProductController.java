package RachlinBabies.API;

import java.util.Map;

import RachlinBabies.Model.Product;
import RachlinBabies.Service.ProductDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.get;

class ProductController {

  ProductController(final ProductDao productService) {
    get("/products/:name", (req, res) -> {
      String name = req.params(":name");
      return productService.searchProduct(name, null);
    }, json());

    get("/products/:name/:limit", (req, res) -> {
      String name = req.params(":name");
      Integer limit = Integer.parseInt(req.params("limit"));
      return productService.searchProduct(name, limit);
    }, json());


    get("/product/:ndb", (req, res) -> {
      int ndb = Integer.parseInt(req.params(":ndb"));
      Product product = productService.get(ndb);
      if (product != null) {
        return product;
      }
      res.status(404);
      return new ResponseMessage(String.format("No product with ndb %d found", ndb));
    }, json());
  }
}
