package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.service.ProductService;
import no.gjensidige.product.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductControllerTest {
    public ModelMapper mm = new ModelMapper();

    @InjectMocks
    private ProductController productController;


    @Mock
    private ProductService productService;

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
    public void getProducts() {
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name ->
        {
            Product p = new Product();
            p.setProductName(name);
            productList.add(p);
        });


        when(productService.getAllProducts()).thenReturn(productList);

        List<Product> productList1 = productController.getProducts();

        verify(productService).getAllProducts();

        assertEquals(3, productList1.size());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1L);

        when(productService.getProduct(1L)).thenReturn(p);

        Product product = productController.getProduct(1L);

        verify(productService).getProduct(1L);
        assertEquals(1L, product.getId().longValue());
    }

    @Test
    public void createProduct() {
        ProductDTO inputProduct = TestUtils.createProductDTO(
                "Helse1", 100.00, 100.00, BigInteger.valueOf(10)
        );
        Product p = mm.map(inputProduct, Product.class);
        p.setId(1L);

        when(productService.createProduct(inputProduct)).thenReturn(p);

        Product product = productController.createProduct(inputProduct);
        verify(productService).createProduct(inputProduct);
        assertEquals(p, product);
    }

    @Test
    public void updateProduct() {
        ProductDTO inputProduct = TestUtils.createProductDTO(
                "Helse1", 100.00, 100.00, BigInteger.valueOf(10)
        );
        Long productId = 1L;
        Product p = mm.map(inputProduct, Product.class);
        p.setId(productId);

        when(productService.updateProduct(productId, inputProduct)).thenReturn(p);

        Product product = productController.updateProduct(productId, inputProduct);
        verify(productService).updateProduct(productId, inputProduct);
        assertEquals(p, product);
    }

    @Test
    public void deleteProduct() {
        Product p = new Product();
        p.setId(1L);

        when(productService.deleteProduct(1L)).thenReturn(p);

        Product product = productController.deleteProduct(1L);

        verify(productService).deleteProduct(1L);

        assertEquals(1L, product.getId().longValue());
    }
}