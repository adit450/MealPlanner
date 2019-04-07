package RachlinBabies.Service;

import java.util.List;

import RachlinBabies.Model.Product;

public interface ProductDao {

  /**
   * Filters all products by name and returns them in a list in alphabetical order.
   * @param name The name of the product that is being searched.
   * @param limit the limit for fake pagination.
   * @return Ordered list of products that fall under the filter.
   */
  List<Product> searchProduct(String name, Integer limit);

  /**
   * Returns a more detailed overview of the Product, including nutrients and ingredients.
   * @param ndb the ndb number (primary key) of the product.
   * @return detailed Product
   */
  Product get(int ndb);
}
