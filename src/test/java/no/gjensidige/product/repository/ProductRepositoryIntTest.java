package no.gjensidige.product.repository;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryIntTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void resetDatabase() {
        productRepository.deleteAll();
    }

    @Test
    void findAllProducts() {
        Product p1 = TestUtils.createProduct(null, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));
        Product p2 = TestUtils.createProduct(null, "Product 2", 20.0, 220.0, BigInteger.valueOf(2000));
        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getProductName)
                .containsExactlyInAnyOrder("Product 1", "Product 2");
    }
}