package no.gjensidige.product.service;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.model.FinancialReport;
import no.gjensidige.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * ReportService
 *
 * <p>Class responsible for producing reports based on product data
 *
 */
@Service
public class ReportService {
    private final ProductRepository productRepository;

    public ReportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public FinancialReport getFinancialReport() {
        List<Product> products = productRepository.findAll();
        BigDecimal totalRevenue = products.stream()
                .map(p -> BigDecimal.valueOf(p.getUnitPrice()).multiply(new BigDecimal(p.getNumberSold())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCost = products.stream()
                .map(p -> BigDecimal.valueOf(p.getUnitCost()).multiply(new BigDecimal(p.getNumberSold())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMargin = totalRevenue.subtract(totalCost);

        Product mostSold = products.stream()
                .max(Comparator.comparing(Product::getNumberSold))
                .orElse(null);

        Product leastSold = products.stream()
                .min(Comparator.comparing(Product::getNumberSold))
                .orElse(null);

        Product highestMarginProduct = products.stream()
                .max(Comparator.comparing(p ->
                        BigDecimal.valueOf(p.getUnitPrice() - p.getUnitCost())
                                .multiply(new BigDecimal(p.getNumberSold()))
                ))
                .orElse(null);

        Product lowestMarginProduct = products.stream()
                .min(Comparator.comparing(p ->
                        BigDecimal.valueOf(p.getUnitPrice() - p.getUnitCost())
                                .multiply(new BigDecimal(p.getNumberSold()))
                ))
                .orElse(null);

        FinancialReport r = new FinancialReport();
        r.setCreated(new Timestamp(Instant.now().toEpochMilli()));
        r.setTotalTurnover(totalRevenue.doubleValue());
        r.setTotalCost(totalCost.doubleValue());
        r.setTotalMargin(totalMargin.doubleValue());
        r.setMostSoldProduct(mostSold);
        r.setLeastSoldProduct(leastSold);
        r.setHighestMarginProduct(highestMarginProduct);
        r.setLowestMarginProduct(lowestMarginProduct);
        return r;
    }
}
