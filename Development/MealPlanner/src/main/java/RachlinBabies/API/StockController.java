package RachlinBabies.API;

import java.util.List;

import RachlinBabies.Model.Stock;
import RachlinBabies.Service.StockDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.get;

public class StockController {
  StockController(final StockDao stockService) {
    get("/stocks", (req, res) -> {
      List<Stock> stocks = stockService.getStocks();
      if (stocks != null) {
        return stocks;
      }
      res.status(404);
      return new ResponseMessage("Could not get stocks");
    }, json());

    get("/stocks/one/:id", (req, res) -> {
      int id = Integer.parseInt(req.params(":id"));
      Stock stock = stockService.getStockById(id);
      if (stock != null) {
        return stock;
      }
      res.status(404);
      return new ResponseMessage("Could not get stocks");
    }, json());
  }
}
