package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuelCanister extends Product {
    private static final BigDecimal excise = new BigDecimal("5.56");
    private static LocalDate motherInLawDay = LocalDate.of((LocalDate.now().getYear()), 3, 5);
    private static LocalDate testDate;

    public FuelCanister(String name, BigDecimal price) {
        super(name, price , new BigDecimal("0.23"));
    }

    public static void setTestMotherInLawDay(LocalDate date) {
        testDate = date;
    }
    @Override
    public BigDecimal getPriceWithTax() {
        LocalDate today = LocalDate.now();
        if (testDate != null) {
            today = testDate;
        } else {
            today = LocalDate.now();
        }

        if (today.getMonthValue() == motherInLawDay.getMonthValue() &&
                today.getDayOfMonth() == motherInLawDay.getDayOfMonth()) {
            return getPrice().add(excise);
        } else {
            BigDecimal tax = getPrice().multiply(getTaxPercent());
            return getPrice().add(tax).add(excise);
        }
    }

}
