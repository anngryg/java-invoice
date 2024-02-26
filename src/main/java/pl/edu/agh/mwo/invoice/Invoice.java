package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products;
  //  private Collection<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        // TODO: implement
        this.products.add(product);
    }

    public void addProduct(Product product, Integer quantity) {
        // TODO: implement
        this.products.put(product, quantity);
    }

    public BigDecimal getNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;
        for (Product product: this.products) {
            netPrice = netPrice.add(product.getPrice());
        }
        return netPrice;
    }

    public BigDecimal getTax() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getGrossPrice() {
        return BigDecimal.ZERO;
    }
}
