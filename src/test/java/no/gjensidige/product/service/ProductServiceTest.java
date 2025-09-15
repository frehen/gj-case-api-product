package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    public ModelMapper mm = new ModelMapper();

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ModelMapper modelMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllProducts() {

        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name ->
        {
            Product p = new Product();
            p.setProductName(name);
            productList.add(p);
        });


        when(productRepository.findAll()).thenReturn(productList);

        List<Product> productList1 = productService.getAllProducts();

        verify(productRepository).findAll();

        assertEquals(3, productList1.size());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);


        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.getProduct(1l);

        assertEquals(p,product);
    }

    @Test
    public void createProduct() {
        ProductDTO inputProduct = createProductDTO(null);
        Product p = mm.map(inputProduct,Product.class);
        p.setId(99L);

        when(modelMapper.map(inputProduct, Product.class)).thenReturn(p);
        when(productRepository.save(p)).thenReturn(p);

        Product product = productService.createProduct(inputProduct);
        verify(productRepository).save(p);

        assertEquals(p,product);
    }

    @Test
    public void updateProduct() {
        Long productId = 1L;
        ProductDTO inputProduct = createProductDTO(productId);
        Product p = mm.map(inputProduct,Product.class);

        when(modelMapper.map(inputProduct, Product.class)).thenReturn(p);
        when(productRepository.findById(productId)).thenReturn(Optional.of(p));

        Product product = productService.updateProduct(productId, inputProduct);
        verify(productRepository).findById(productId);

        assertEquals(p,product);
    }

    @Test( expected = ProductNotFoundException.class)
    public void updateProductWithProductNotFoundException() {
        Long productId = 1L;
        ProductDTO inputProduct = createProductDTO(productId);
        Product p = mm.map(inputProduct,Product.class);

        when(modelMapper.map(inputProduct, Product.class)).thenReturn(p);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        productService.updateProduct(productId, inputProduct);
    }

    @Test( expected = IllegalArgumentException.class)
    public void updateProductWithIllegalArgumentException() {
        Long productId = 1L;
        Long invalidProductId = 2L;
        ProductDTO inputProduct = createProductDTO(invalidProductId);

        productService.updateProduct(productId, inputProduct);
    }

    private static ProductDTO createProductDTO(Long id) {
        ProductDTO inputProduct = new ProductDTO();
        inputProduct.setId(id);
        inputProduct.setCategory("Helseforsikring");
        inputProduct.setProductName("Helse1");
        inputProduct.setPrice(100.00);
        inputProduct.setNumberSold(BigInteger.valueOf(10));
        inputProduct.setImageLink("static.gjensidige.com/");
        inputProduct.setUnitCost(100.00);
        inputProduct.setNumberSold(BigInteger.valueOf(20));
        return inputProduct;
    }

    @Test
    public void deleteProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);
        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.deleteProduct(1l);
        verify(productRepository).delete(p);

        assertEquals(p,product);
    }


    @Test(expected = ProductNotFoundException.class)
    public void deleteProductWithException() {
        Optional<Product> op = Optional.empty();

        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.deleteProduct(10l);

        verify(productRepository).findById(10l);
        fail("Didn't throw not found exception");
    }

    @Test
    public void convertToDTO() {
        Product product = new Product();
        product.setCategory("Hardware");
        product.setProductName("Seagate Baracuda 500GB");
        product.setNumberSold(BigInteger.valueOf(200));
        product.setUnitPrice(55.50);

        when(modelMapper.map(product, ProductDTO.class)).thenReturn(mm.map(product,ProductDTO.class));
        ProductDTO productDTO = productService.convertToDTO(product);
    }

    @Test
    public void convertToEntity() {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("Hardware");
        productDTO.setProductName("Seagate Baracuda 500GB");
        productDTO.setNumberSold(BigInteger.valueOf(200));
        productDTO.setPrice(55.50);

        when(modelMapper.map(productDTO,Product.class)).thenReturn(mm.map(productDTO,Product.class));
        Product product = productService.convertToEntity(productDTO);

        assertEquals(product.getProductName(),productDTO.getProductName());
        assertEquals(product.getNumberSold(),productDTO.getNumberSold());
        assertEquals(product.getCategory(),productDTO.getCategory());

    }
}