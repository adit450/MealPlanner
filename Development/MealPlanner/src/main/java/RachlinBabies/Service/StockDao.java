package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Stock;

public interface StockDao {
  /**
   * Get the user's stocks.
   * @return user's stocks.
   */
  List<Stock> getStocks();

  Stock getStockById(int id);
}
