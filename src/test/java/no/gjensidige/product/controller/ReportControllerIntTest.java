package no.gjensidige.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.service.ReportService;
import no.gjensidige.product.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerIntTest {
    @MockitoBean
    ReportService reportService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInitialization() {
        assertNotNull(mockMvc);
        assertNotNull(reportService);
        assertNotNull(objectMapper);
    }

    @Test
    public void getFinancialReport() throws Exception {
        FinancialReport r = createFinancialReport();

        when(reportService.getFinancialReport()).thenReturn(r);

        MockHttpServletResponse response = mockMvc.perform(get("/reports/financial")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        FinancialReport returnedReport = objectMapper.readValue(
                response.getContentAsString(),
                FinancialReport.class
        );

        assertEquals(returnedReport.getCreated(), r.getCreated());
        assertEquals(returnedReport.getHighestMarginProduct().getId(), r.getHighestMarginProduct().getId());
        assertEquals(returnedReport.getLowestMarginProduct().getId(), r.getLowestMarginProduct().getId());
        assertEquals(returnedReport.getMostSoldProduct().getId(), r.getMostSoldProduct().getId());
        assertEquals(returnedReport.getTotalMargin(), r.getTotalMargin(), 0.1);
        assertEquals(returnedReport.getTotalTurnover(), r.getTotalTurnover(), 0.1);
        assertEquals(returnedReport.getTotalCost(), r.getTotalCost(), 0.1);
    }

    private FinancialReport createFinancialReport() {
        Product p1 = TestUtils.createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));
        Product p2 = TestUtils.createProduct(1L, "Product 2", 10.0, 60.0, BigInteger.valueOf(500));

        FinancialReport r = new FinancialReport();
        r.setCreated(new Timestamp(System.currentTimeMillis()));
        r.setHighestMarginProduct(p1);
        r.setLowestMarginProduct(p2);
        r.setMostSoldProduct(p1);
        r.setTotalMargin(10.0);
        r.setTotalTurnover(20.0);
        r.setTotalCost(30.0);
        return r;
    }
}
