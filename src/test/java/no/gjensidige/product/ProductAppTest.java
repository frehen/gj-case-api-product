package no.gjensidige.product;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import no.gjensidige.product.service.ProductService;
import no.gjensidige.product.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;
import java.util.Arrays;

import static no.gjensidige.product.utils.TestUtils.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductAppTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportService reportService;

    @BeforeEach
    void resetDatabase() {
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(productRepository);
        assertNotNull(productService);
        assertNotNull(reportService);
    }

    @Test
    void getFinancialReport() {
        Product p1 = createProduct(null, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));
        Product p2 = createProduct(null, "Product 2", 10.0, 110.0, BigInteger.valueOf(999));
        Product p3 = createProduct(null, "Product 3", 10.0, 110.0, BigInteger.valueOf(1500));
        Product p4 = createProduct(null, "Product 4", 10.0, 210.0, BigInteger.valueOf(1000));
        Product p5 = createProduct(null, "Product 5", 10.0, 60.0, BigInteger.valueOf(1000));
        Product p6 = createProduct(null, "Product 6", 10.0, 110.0, BigInteger.valueOf(1000));
        double totalMargin = 100 * 1000 + 100 * 999 + 100 * 1500 + 200 * 1000 + 50 * 1000 + 100 * 1000;
        double totalTurnover = 110 * 1000 + 110 * 999 + 110 * 1500 + 210 * 1000 + 60 * 1000 + 110 * 1000;
        double totalCost = 10 * 1000 + 10 * 999 + 10 * 1500 + 10 * 1000 + 10 * 1000 + 10 * 1000;
        productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6));

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        ResponseEntity<FinancialReport> response = restClient.get()
                .uri("/reports/financial")
                .retrieve()
                .toEntity(FinancialReport.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        FinancialReport report = response.getBody();
        assertEquals(p4.getProductName(), report.getHighestMarginProduct().getProductName());
        assertEquals(p5.getProductName(), report.getLowestMarginProduct().getProductName());
        assertEquals(p3.getProductName(), report.getMostSoldProduct().getProductName());
        assertEquals(p2.getProductName(), report.getLeastSoldProduct().getProductName());
        assertEquals(totalMargin, report.getTotalMargin(), 0.1);
        assertEquals(totalTurnover, report.getTotalTurnover(), 0.1);
        assertEquals(totalCost, report.getTotalCost(), 0.1);
        assertNotNull(report.getCreated());
    }

    @Test
    void getOpenApiMainPage() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
        ResponseEntity<String> response = restClient.get()
                .uri("/swagger-ui/index.html")
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("Swagger UI");
    }


    @Test
    void getOpenApiDocs() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
        ResponseEntity<String> response = restClient.get()
                .uri("/v3/api-docs")
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("API documentation for Product and Report controllers");
        assertThat(response.getBody()).contains("product-controller");
        assertThat(response.getBody()).contains("report-controller");
    }
}
