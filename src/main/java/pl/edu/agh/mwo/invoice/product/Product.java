package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.text.ParseException;


public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if (name.equals(null))
            throw new IllegalArgumentException("");
        this.name = name;
        this.price = price;
        this.taxPercent = tax;

    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return price.add(price.multiply(taxPercent));
    }
}
