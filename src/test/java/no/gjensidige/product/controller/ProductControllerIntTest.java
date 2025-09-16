package no.gjensidige.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.service.ProductService;
import no.gjensidige.product.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerIntTest {
    @MockitoBean
    ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInitialization() {
        assertNotNull(mockMvc);
        assertNotNull(productService);
        assertNotNull(objectMapper);
    }

    @Test
    public void getProducts() throws Exception {
        Product p1 = TestUtils.createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));
        Product p2 = TestUtils.createProduct(2L, "Product 2", 10.0, 110.0, BigInteger.valueOf(999));
        Product p3 = TestUtils.createProduct(3L, "Product 3", 10.0, 110.0, BigInteger.valueOf(1500));
        Product p4 = TestUtils.createProduct(4L, "Product 4", 10.0, 210.0, BigInteger.valueOf(1000));
        Product p5 = TestUtils.createProduct(5L, "Product 5", 10.0, 60.0, BigInteger.valueOf(1000));
        Product p6 = TestUtils.createProduct(6L, "Product 6", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.getAllProducts()).thenReturn(Arrays.asList(p1, p2, p3, p4, p5, p6));

        MockHttpServletResponse response = mockMvc.perform(get("/products/")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Product> products = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertThat(products).hasSize(6);
        assertThat(products).allSatisfy(product -> assertThat(product.getProductName()).startsWith("Product "));
    }

    @Test
    public void getProduct() throws Exception {
        Product p = TestUtils.createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.getProduct(1L)).thenReturn(p);

        MockHttpServletResponse response = mockMvc.perform(get("/products/1")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Product product = objectMapper.readValue(
                response.getContentAsString(),
                Product.class
        );

        assertEquals(product);
    }

    @Test
    public void getProductWithNotFound() throws Exception {
        when(productService.getProduct(1L)).thenThrow(
                new ProductNotFoundException(1L)
        );

        MockHttpServletResponse response = mockMvc.perform(get("/products/1")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("Could not find product with id : 1");
    }

    @Test
    public void createProduct() throws Exception {
        ProductDTO inputProduct = TestUtils.createProductDTO(null, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));
        Product product = TestUtils.createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.createProduct(any())).thenReturn(product);

        MockHttpServletResponse response = mockMvc.perform(post("/products/")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inputProduct))
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()) // Should be isCreated()?
                .andReturn().getResponse();

        Product returnedProduct = objectMapper.readValue(
                response.getContentAsString(),
                Product.class
        );

        assertEquals(returnedProduct);
    }

    @Test
    public void updateProduct() throws Exception {
        ProductDTO inputProduct = TestUtils.createProductDTO(null, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        Product product = TestUtils.createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.updateProduct(eq(1L), any())).thenReturn(product);

        MockHttpServletResponse response = mockMvc.perform(put("/products/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inputProduct))
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Product returnedProduct = objectMapper.readValue(
                response.getContentAsString(),
                Product.class
        );

        assertEquals(returnedProduct);
    }

    @Test
    public void updateProductWithNotFound() throws Exception {
        ProductDTO inputProduct = TestUtils.createProductDTO(null, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.updateProduct(eq(1L), any())).thenThrow(
                new ProductNotFoundException(1L)
        );

        MockHttpServletResponse response = mockMvc.perform(put("/products/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inputProduct))
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("Could not find product with id : 1");
    }

    @Test
    public void updateProductWithIllegalArgument() throws Exception {
        ProductDTO inputProduct = TestUtils.createProductDTO(2L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.updateProduct(eq(1L), any())).thenThrow(
                new IllegalArgumentException("Product id in path and body do not match")
        );

        MockHttpServletResponse response = mockMvc.perform(put("/products/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inputProduct))
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("Product id in path and body do not match");
    }

    @Test
    public void deleteProduct() throws Exception {
        Product p = TestUtils.createProduct(1L, "Product 1", 10.0, 110.0, BigInteger.valueOf(1000));

        when(productService.deleteProduct(1L)).thenReturn(p);

        MockHttpServletResponse response = mockMvc.perform(delete("/products/1")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Product returnedProduct = objectMapper.readValue(
                response.getContentAsString(),
                Product.class
        );

        assertEquals(returnedProduct);
    }

    @Test
    public void deleteProductWithNotFound() throws Exception {
        when(productService.deleteProduct(1L)).thenThrow(
                new ProductNotFoundException(1L)
        );

        MockHttpServletResponse response = mockMvc.perform(delete("/products/1")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("Could not find product with id : 1");
    }

    private static void assertEquals(Product returnedProduct) {
        assertThat(returnedProduct.getId()).isEqualTo(1L);
        assertThat(returnedProduct.getProductName()).isEqualTo("Product 1");
        assertThat(returnedProduct.getCategory()).isEqualTo("Category 1");
        assertThat(returnedProduct.getUnitCost()).isEqualTo(10.0);
        assertThat(returnedProduct.getUnitPrice()).isEqualTo(110.0);
        assertThat(returnedProduct.getNumberSold()).isEqualTo(BigInteger.valueOf(1000));
        assertThat(returnedProduct.getImageLink()).isEqualTo("static.gjensidige.com/");
    }
}
