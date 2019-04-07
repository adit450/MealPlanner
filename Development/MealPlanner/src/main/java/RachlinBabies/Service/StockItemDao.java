package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.StockItem;

public interface StockItemDao {

  /**
   * Gets the stockItem with the given id.
   * @param stockItemId the stockItemId
   * @return the StockItem owned by the user.
   */
  StockItem getStockItem(int stockItemId);

  /**
   * Gets stock items of the same product, ordered by expiration date.
   * @param stockId stock id of the stock items
   * @return list of stock items
   */
  List<StockItem> getStock(int stockId);

  /**
   * Gets stock items that are going to expire soon.  For now, this means
   * stock items that expire within 3 days.
   * @return StockItems
   */
  List<StockItem> getCriticalStockItems();

  /**
   * Gets all stock items that have gone beyond their expiration date.
   * @return your wasted food.
   */
  List<StockItem> getExpiredStockItems();

  /**
   * Updates a stockItem's expiration date and quantity.
   * @param stockItem stockItem with expiration date and quantity
   * @return whether or not the update was a success.
   */
  boolean updateStockItem(StockItem stockItem);

  /**
   * Inserts a new StockItem.
   * @param stockItem the stockItem to insert. NDB and quantity are necessary.
   * @return whether or not the stockItem was successfully inserted.
   */
  boolean createStockItem(StockItem stockItem);

  /**
   * Deletes a StockItem.
   * @param stockItemId the id of the item to delete.
   * @return whether or not the delete was successful.
   */
  boolean deleteStockItem(int stockItemId);
}
