package RachlinBabies.API;

import RachlinBabies.Model.Product;
import RachlinBabies.Service.ProductDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.get;

class ProductController {

    ProductController(final ProductDao productService) {
        get("/products/search/:name", (req, res) -> {
            String name = req.params(":name");
            return productService.searchProduct(name);
        }, json());

        get("/products/:ndb", (req, res) -> {
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
