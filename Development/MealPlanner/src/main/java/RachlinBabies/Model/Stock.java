package RachlinBabies.Model;

public class Stock {
  private Integer stockId;
  private Integer userId;
  private Integer ndb;
  private Integer quantity;
  private Product product;
  
  public Stock(){}

  private Stock(Integer stockId, Integer userId, Integer ndb, Integer quantity, Product product) {
    this.stockId = stockId;
    this.userId = userId;
    this.ndb = ndb;
    this.quantity = quantity;
    this.product = product;
  }
  
  public static class StockBuilder {
    private Integer stockId;
    private Integer userId;
    private Integer ndb;
    private Integer quantity;
    private Product product;

    public StockBuilder stockId(int id) {
      this.stockId = id;
      return this;
    }

    public StockBuilder userId(int userId) {
      this.userId = userId;
      return this;
    }

    public StockBuilder ndb(int ndb) {
      this.ndb = ndb;
      return this;
    }

    public StockBuilder quantity(int quantity) {
      this.quantity = quantity;
      return this;
    }

    public StockBuilder product(Product product) {
      this.product = product;
      return this;
    }

    public Stock build() {
      return new Stock(this.stockId, this.userId, this.ndb, this.quantity, this.product);
    }
  }
  
  public Integer getStockId() {
    return stockId;
  }
  
  public Integer getUserId() {
    return userId;
  }

  public Integer getNdb() {
    return ndb;
  }
}
