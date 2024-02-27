package pl.edu.agh.mwo.invoice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<>();
  //  private Collection<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        // TODO: implement
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        // TODO: implement
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity<=0){
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }else products.put(product, products.getOrDefault(product, 0) + quantity);
    }

    public BigDecimal getNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()){
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            netPrice = netPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
        return netPrice;
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            tax = tax.add(product.getPrice().multiply(product.getTaxPercent()).multiply(BigDecimal.valueOf(quantity)));
        }
        return tax;
    }

    public BigDecimal getGrossPrice() {
        BigDecimal grossPrice = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer qty = entry.getValue();
            grossPrice = grossPrice.add(product.getPriceWithTax().multiply(BigDecimal.valueOf(qty)));
        }
        return grossPrice;
    }
}
