package no.gjensidige.product.service;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ReportServiceTest {

    public ModelMapper mm = new ModelMapper();

    @InjectMocks
    ReportService reportService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFinancialReportSingleProduct() {
        Product p = createProduct(1L, "Product 1",10.0, 110.0, BigInteger.valueOf(1000));

        when(productRepository.findAll()).thenReturn(Collections.singletonList(p));
        FinancialReport report = reportService.getFinancialReport();
        assertEquals(p, report.getHighestMarginProduct());
        assertEquals(p, report.getLowestMarginProduct());
        assertEquals(p, report.getMostSoldProduct());
        assertEquals(p, report.getLeastSoldProduct());
        assertEquals((110.0 - 10.0) * 1000 , report.getTotalMargin(), 0.1);
        assertEquals(110.0 * 1000, report.getTotalTurnover(), 0.1);
        assertEquals(10.0 * 1000, report.getTotalCost(), 0.1);
        assertNotNull(report.getCreated());
    }

    @Test
    public void getFinancialReportManyProducts() {
        Product p1 = createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));
        Product p2 = createProduct(2L, "Product 2", 10.0, 110.0, BigInteger.valueOf(999));
        Product p3 = createProduct(3L, "Product 3", 10.0, 110.0, BigInteger.valueOf(1500));
        Product p4 = createProduct(4L, "Product 4", 10.0, 210.0, BigInteger.valueOf(1000));
        Product p5 = createProduct(5L, "Product 5", 10.0, 60.0, BigInteger.valueOf(1000));
        Product p6 = createProduct(6L, "Product 6", 10.0, 110.0, BigInteger.valueOf(1000));
        double totalMargin = 100 * 1000 + 100 * 999 + 100 * 1500 + 200 * 1000 + 50 * 1000 + 100 * 1000;
        double totalTurnover = 110 * 1000 + 110 * 999 + 110 * 1500 + 210 * 1000 + 60 * 1000 + 110 * 1000;
        double totalCost = 10 * 1000 + 10 * 999 + 10 * 1500 + 10 * 1000 + 10 * 1000 + 10 * 1000;

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1,p2,p3,p4,p5,p6));

        FinancialReport report = reportService.getFinancialReport();

        assertEquals(p4, report.getHighestMarginProduct());
        assertEquals(p5, report.getLowestMarginProduct());
        assertEquals(p3, report.getMostSoldProduct());
        assertEquals(p2, report.getLeastSoldProduct());
        assertEquals(totalMargin , report.getTotalMargin(), 0.1);
        assertEquals(totalTurnover, report.getTotalTurnover(), 0.1);
        assertEquals(totalCost, report.getTotalCost(), 0.1);
        assertNotNull(report.getCreated());
    }

    @Test
    public void getFinancialReportLargeNumberOfUnitsSold() {
        Product p1 = createProduct(1L, "Product 1",1.0, 2.0, new BigInteger(  "100000000"));
        Product p2 = createProduct(2L, "Product 2",1.0, 2.0, new BigInteger( "1000000000"));
        double totalMargin = new BigInteger("100000000").add(new BigInteger( "1000000000")).doubleValue();
        double totalTurnover = BigInteger.valueOf(2).multiply(new BigInteger("100000000"))
                .add(BigInteger.valueOf(2).multiply(new BigInteger( "1000000000")))
                .doubleValue();
        double totalCost = new BigInteger("100000000").add(new BigInteger( "1000000000")).doubleValue();

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1,p2));

        FinancialReport report = reportService.getFinancialReport();

        assertEquals(p2, report.getHighestMarginProduct());
        assertEquals(p1, report.getLowestMarginProduct());
        assertEquals(p2, report.getMostSoldProduct());
        assertEquals(p1, report.getLeastSoldProduct());
        assertEquals(totalMargin , report.getTotalMargin(), 0.1);
        assertEquals(totalTurnover, report.getTotalTurnover(), 0.1);
        assertEquals(totalCost, report.getTotalCost(), 0.1);
        assertNotNull(report.getCreated());
    }

    private Product createProduct(
            Long id,
            String productName,
            Double unitCost,
            Double unitPrice,
            BigInteger numberSold) {
        Product p = new Product();
        p.setId(id);
        p.setProductName(productName);
        p.setCategory("Category 1");
        p.setImageLink("static.gjensidige.com/");
        p.setUnitCost(unitCost);
        p.setUnitPrice(unitPrice);
        p.setNumberSold(numberSold);
        return p;
    }

}