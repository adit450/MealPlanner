package RachlinBabies.API;

import RachlinBabies.Service.ProductDao;

import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.get;

class ProductController {

  ProductController(final ProductDao productService) {
    get("/products/:name", (req, res) -> {
      String name = req.params(":name");
      return productService.searchProduct(name);
    }, json());
  }
}
