package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    private static int lastNumber = 0;
    private final int number;

    public Invoice(){
        this.number = ++lastNumber;
    }

    public String printInvoice() {
        StringBuilder invoiceString = new StringBuilder();
        invoiceString.append("Numer faktury: ").append(number).append("\n");
        int count = 0;

        List<Map.Entry<Product, Integer>> sortedProducts = new ArrayList<>(products.entrySet());
        sortedProducts.sort(Comparator.comparing(p -> p.getKey().getName()));

        for (Map.Entry<Product, Integer> entry : sortedProducts) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            invoiceString.append("Nazwa produktu: ")
                    .append(product.getName())
                    .append(" ...... ")
                    .append("liczba sztuk: ")
                    .append(quantity)
                    .append(" ...... Cena netto: ")
                    .append(product.getPrice())
                    .append("PLN ...... VAT: ")
                    .append(product.getTaxPercent().multiply(new BigDecimal(100)))
                    .append("% ...... Wartość brutto: ")
                    .append(product.getPriceWithTax().multiply(new BigDecimal(quantity)))
                    .append("PLN\n");
            count++;
        }

        invoiceString.append("Liczba pozycji na fakturze: ").append(count);
        invoiceString.append("\nRazem do zapłaty: ").append(getGrossTotal()).append(" PLN");
        return invoiceString.toString();
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }

        Integer productsQuantity = products.get(product);
        if (productsQuantity != null) {
            Integer newQuantity = productsQuantity + quantity;
            products.put(product, newQuantity);
        } else {

            products.put(product, quantity);
        }
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return number;
    }

}