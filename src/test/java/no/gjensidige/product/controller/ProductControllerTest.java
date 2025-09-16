package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.service.ProductService;
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

    @BeforeEach
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
        ProductDTO inputProduct = createProductDTO(null);
        Product p = mm.map(inputProduct,Product.class);
        p.setId(1L); // Id is set by the repository, not from the ProductDTO

        when(productService.createProduct(inputProduct)).thenReturn(p);

        Product product = productController.createProduct(inputProduct);
        verify(productService).createProduct(inputProduct);
        assertEquals(p, product);
    }

    @Test
    public void updateProduct() {
        Long productId = 1L;
        ProductDTO inputProduct = createProductDTO(productId);
        Product p = mm.map(inputProduct,Product.class);
        when(productService.updateProduct(productId,inputProduct )).thenReturn(p);

        Product product = productController.updateProduct(productId,inputProduct);
        verify(productService).updateProduct(productId,inputProduct);
        assertEquals(p, product);
    }

    private static ProductDTO createProductDTO(Long id) {
        ProductDTO inputProduct = new ProductDTO();
        inputProduct.setId(id);
        inputProduct.setCategory("Helseforsikring");
        inputProduct.setProductName("Helse1");
        inputProduct.setUnitPrice(100.00);
        inputProduct.setNumberSold(BigInteger.valueOf(10));
        inputProduct.setImageLink("static.gjensidige.com/");
        inputProduct.setUnitCost(100.00);
        return inputProduct;
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