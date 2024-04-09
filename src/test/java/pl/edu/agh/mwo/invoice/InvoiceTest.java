package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasNumber(){
        Invoice invoice = new Invoice();
        int number = invoice.getNumber();
    }

    @Test
    public void testInvoiceNumberIsGreaterThan0(){
        Assert.assertTrue(new Invoice().getNumber()>0);
    }

    @Test
    public void testNumberOfSecondInvoiceIsGreaterThanPreviousOne(){
        int n1 = new Invoice().getNumber();
        int n2 = new Invoice().getNumber();
        Assert.assertThat(n2, Matchers.greaterThan(n1));
    }

    @Test
    public void testPrintInvoice(){
        Invoice invoice = new Invoice();
        Product product1 = new TaxFreeProduct("Pasta", new BigDecimal("15"));
        Product product2 = new DairyProduct("Mleko", new BigDecimal("10"));
        Product product3 = new OtherProduct("Skarpety", new BigDecimal("20"));

        invoice.addProduct(product1);
        invoice.addProduct(product2, 10);
        invoice.addProduct(product3, 20);
        String output = "Numer faktury: " + invoice.getNumber() + "\n" +
                "Nazwa produktu: Mleko ...... liczba sztuk: 10 ...... Cena netto: 10PLN ...... VAT: 8.00% ...... Wartość brutto: 108.00PLN\n" +
                "Nazwa produktu: Pasta ...... liczba sztuk: 1 ...... Cena netto: 15PLN ...... VAT: 0% ...... Wartość brutto: 15PLN\n" +
                "Nazwa produktu: Skarpety ...... liczba sztuk: 20 ...... Cena netto: 20PLN ...... VAT: 23.00% ...... Wartość brutto: 492.00PLN\n" +
                "Liczba pozycji na fakturze: 3"+
                "\nRazem do zapłaty: 615.00 PLN";

        Assert.assertEquals(output, invoice.printInvoice());
    }

    @Test
    public void testAddingDuplicate(){
        Invoice invoice = new Invoice();
        Product product1 = new TaxFreeProduct("Kapusta", new BigDecimal("5"));

        invoice.addProduct(product1, 10);
        invoice.addProduct(product1, 10);

        String output = "Numer faktury: " + invoice.getNumber() + "\n" +
                "Nazwa produktu: Kapusta ...... liczba sztuk: 20 ...... Cena netto: 5PLN ...... VAT: 0% ...... Wartość brutto: 100PLN\n" +
                "Liczba pozycji na fakturze: 1"+
                "\nRazem do zapłaty: 100 PLN";

        Assert.assertEquals(output, invoice.printInvoice());
    }

    @Test
    public void testBottleOfWineAndFuel() {
        invoice.addProduct(new BottleOfWine("Vihno Verde", new BigDecimal("14")), 1);
        invoice.addProduct(new FuelCanister("pb 95", new BigDecimal("3")), 10);

        Assert.assertThat(new BigDecimal("115.28"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testFuelPriceMotherInLawDay() {
        FuelCanister product1 = new FuelCanister("pb 95", new BigDecimal("10"));
        LocalDate motherInLawDay = LocalDate.of(LocalDate.now().getYear(), 3, 5);
        FuelCanister.setTestMotherInLawDay(motherInLawDay);
        BigDecimal expectedPriceWithTax = new BigDecimal("15.56");
        Assert.assertEquals(expectedPriceWithTax, product1.getPriceWithTax());
    }
    @Test
    public void testFuelPriceOnRegularDay() {
        FuelCanister product1 = new FuelCanister("pb 98", new BigDecimal("10"));
        BigDecimal expectedPriceWithTax = new BigDecimal("17.86");
        Assert.assertEquals(expectedPriceWithTax, product1.getPriceWithTax());
    }

}
