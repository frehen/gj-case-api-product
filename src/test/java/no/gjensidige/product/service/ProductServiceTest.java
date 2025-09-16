package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        Product product = productService.getProduct(1L);

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

    @Test
    public void updateProductWithProductNotFoundException() {
        Long productId = 1L;
        ProductDTO inputProduct = createProductDTO(productId);
        Product p = mm.map(inputProduct,Product.class);

        when(modelMapper.map(inputProduct, Product.class)).thenReturn(p);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productId, inputProduct);
        });
    }

    @Test
    public void updateProductWithIllegalArgumentException() {
        Long productId = 1L;
        Long invalidProductId = 2L;
        ProductDTO inputProduct = createProductDTO(invalidProductId);

        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, inputProduct);
        });
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
        inputProduct.setNumberSold(BigInteger.valueOf(20));
        return inputProduct;
    }

    @Test
    public void deleteProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);
        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.deleteProduct(1L);
        verify(productRepository).delete(p);

        assertEquals(p,product);
    }


    @Test
    public void deleteProductWithException() {
        Optional<Product> op = Optional.empty();

        when(productRepository.findById(anyLong())).thenReturn(op);

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(10L);
        });
        verify(productRepository).findById(10L);
    }

    @Test
    public void convertToDTO() {
        Product product = new Product();
        product.setId(1L);
        product.setCategory("Hardware");
        product.setProductName("Seagate Baracuda 500GB");
        product.setNumberSold(new BigInteger("1000000000000"));
        product.setUnitPrice(55.50);
        product.setImageLink("static.gjensidige.com/");
        product.setUnitCost(10.00);

        when(modelMapper.map(product, ProductDTO.class)).thenReturn(mm.map(product,ProductDTO.class));

        ProductDTO productDTO = productService.convertToDTO(product);

        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getCategory(), productDTO.getCategory());
        assertEquals(product.getProductName(),productDTO.getProductName());
        assertEquals(product.getNumberSold(), productDTO.getNumberSold());
        assertEquals(product.getUnitPrice(), productDTO.getUnitPrice());
        assertEquals(product.getImageLink(), productDTO.getImageLink());
        assertEquals(product.getUnitCost(), productDTO.getUnitCost());
    }

    @Test
    public void convertToEntity() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setCategory("Hardware");
        productDTO.setProductName("Seagate Baracuda 500GB");
        productDTO.setNumberSold(new BigInteger("1000000000000"));
        productDTO.setUnitPrice(55.50);
        productDTO.setImageLink("static.gjensidige.com/");
        productDTO.setUnitCost(10.00);

        when(modelMapper.map(productDTO,Product.class)).thenReturn(mm.map(productDTO,Product.class));

        Product product = productService.convertToEntity(productDTO);

        assertEquals(productDTO.getCategory(),product.getCategory());
        assertEquals(productDTO.getProductName(),product.getProductName());
        assertEquals(productDTO.getNumberSold(),product.getNumberSold());
        assertEquals(productDTO.getUnitPrice(),product.getUnitPrice());

    }
}