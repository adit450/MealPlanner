package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Stock;

public interface StockDao {
  /**
   * Get the user's stocks.
   * @return user's stocks.
   */
  List<Stock> getStocks();

  /**
   * Get stock from stock_id
   * @param id stock_id
   * @return stock with given id
   */
  Stock getStockById(int id);
}
