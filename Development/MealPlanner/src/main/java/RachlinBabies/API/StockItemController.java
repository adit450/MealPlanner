package RachlinBabies.API;

import java.util.List;

import RachlinBabies.Model.StockItem;
import RachlinBabies.Service.StockItemDao;
import RachlinBabies.Utils.ResponseMessage;

import static RachlinBabies.Utils.JsonUtil.dateGson;
import static RachlinBabies.Utils.JsonUtil.json;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class StockItemController {
  StockItemController(final StockItemDao stockItemService) {
    get("/stock/all/:id", (req, res) -> {
      int stockId = Integer.parseInt(req.params(":id"));
      List<StockItem> stockItems = stockItemService.getStock(stockId);
      if (stockItems != null) {
        return stockItems;
      }
      res.status(404);
      return new ResponseMessage("Could not get stock");
    }, json());

    get("/stock/critical", (req, res) -> {
      List<StockItem> stockItems = stockItemService.getCriticalStockItems();
      if (stockItems != null) {
        return stockItems;
      }
      res.status(404);
      return new ResponseMessage("Could not get stock");
    }, json());

    get("/stock/expired", (req, res) -> {
      List<StockItem> stockItems = stockItemService.getExpiredStockItems();
      if (stockItems != null) {
        return stockItems;
      }
      res.status(404);
      return new ResponseMessage("Could not get stock");
    }, json());

    put("/stock", (req, res) -> {
      StockItem toUpdate = dateGson().fromJson(req.body(), StockItem.class);
      int id = toUpdate.getStockItemId();
      if (stockItemService.updateStockItem(toUpdate)) {
        return new ResponseMessage(String.format("StockItem %d successfully updated",
                id));
      }
      if (stockItemService.getStockItem(toUpdate.getStockItemId()) == null) {
        res.status(404);
        return new ResponseMessage(String.format("StockItem %d not found",
                id));
      }
      res.status(400);
      return new ResponseMessage("Could not update stockItem");
    }, json());

    post("/stock", (req, res) -> {
      StockItem toInsert = dateGson().fromJson(req.body(), StockItem.class);
      if (stockItemService.createStockItem(toInsert)) {
        return new ResponseMessage("StockItem added");
      }
      res.status(400);
      return new ResponseMessage("Could not insert stockItem");
    }, json());

    delete("/stock/:id", (req, res) -> {
      int id = Integer.parseInt(req.params(":id"));
      if (stockItemService.getStockItem(id) == null) {
        res.status(404);
        return new ResponseMessage(String.format("StockItem %d not found", id));
      } else if (stockItemService.deleteStockItem(id)) {
        return new ResponseMessage(String.format("StockItem %d deleted", id));
      } else {
        res.status(400);
        return new ResponseMessage(String.format("Could not delete StockItem %d", id));
      }
    }, json());

  }
}
