package no.gjensidige.product.controller;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.service.ReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class ReportControllerTest {
    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    private AutoCloseable closeable;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void getFinancialReport() {
        FinancialReport r = new FinancialReport();
        Product highestMarginProduct = createProduct(1L);
        Product lowestMarginProduct = createProduct(2L);
        Product mostSoldProduct = createProduct(3L);
        r.setHighestMarginProduct(highestMarginProduct);
        r.setLowestMarginProduct(lowestMarginProduct);
        r.setMostSoldProduct(mostSoldProduct);
        r.setTotalMargin(10.0);
        r.setTotalTurnover(20.0);
        r.setTotalCost(30.0);

        when(reportService.getFinancialReport()).thenReturn(r);

        FinancialReport report = reportController.getFinancialReport();
        assertEquals(r, report);
    }

    private Product createProduct(Long id) {
        Product p = new Product();
        p.setId(id);
        return p;
    }

}