package no.gjensidige.product.service;

import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertNotNull;

public class ReportServiceTest {

    public ModelMapper mm = new ModelMapper();

    @InjectMocks
    ReportService reportService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ModelMapper modelMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFinancialReport() {
        FinancialReport report = reportService.getFinancialReport();
        assertNotNull(report);
    }

}