package RachlinBabies.Model;

import java.sql.Timestamp;

public class StockItem {
  private Integer stockId;
  private Integer stockItemId;
  private Integer ndb;
  private Integer quantity;
  private Timestamp expirationDate;

  public StockItem(){}

  public StockItem(Integer stockId, Integer stockItemId,
                   Integer quantity, Timestamp expirationDate,
                   Integer ndb) {
    this.stockId = stockId;
    this.stockItemId = stockItemId;
    this.quantity = quantity;
    this.expirationDate = expirationDate;
  }

  public Integer getQuantity() {
    return this.quantity;
  }

  public Integer getStockItemId() {
    return this.stockItemId;
  }

  public Integer getNdb() {
    return this.ndb;
  }

  public Timestamp getExpirationDate() {
    return this.expirationDate;
  }
}
