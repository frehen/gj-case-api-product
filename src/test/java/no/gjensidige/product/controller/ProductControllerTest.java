package no.gjensidige.product.controller;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;


    @Mock
    private ProductService productService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
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
        p.setId(1l);

        when(productService.getProduct(1l)).thenReturn(p);

        Product product = productController.getProduct(1l);

        verify(productService).getProduct(1l);
        assertEquals(1l, product.getId().longValue());
    }

    @Test
    public void createProduct() {
    }

    @Test
    public void updateProduct() {
    }

    @Test
    public void deleteProduct() {

        Product p = new Product();
        p.setId(1l);

        when(productService.deleteProduct(1l)).thenReturn(p);

        Product product = productController.deleteProduct(1l);

        verify(productService).deleteProduct(1l);

        assertEquals(1l, product.getId().longValue());

    }
}