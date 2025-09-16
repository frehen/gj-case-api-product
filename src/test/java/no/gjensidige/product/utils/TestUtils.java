package no.gjensidige.product.utils;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;

import java.math.BigInteger;

public class TestUtils {
    @SuppressWarnings("SameParameterValue")
    public static ProductDTO createProductDTO(
            String productName,
            Double unitCost,
            Double unitPrice,
            BigInteger numberSold) {
        ProductDTO p = new ProductDTO();
        p.setProductName(productName);
        p.setCategory("Category 1");
        p.setImageLink("static.gjensidige.com/");
        p.setUnitCost(unitCost);
        p.setUnitPrice(unitPrice);
        p.setNumberSold(numberSold);
        return p;
    }

    @SuppressWarnings("SameParameterValue")
    public static Product createProduct(
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
