package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * RestController to handle CRUD operations for Products
 *
 */
@RestController
@RequestMapping(name = "Products", value = "products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/")
    List<Product> getProducts() {

        return productService.getAllProducts();
    }

    @GetMapping(value = "/{id}")
    Product getProduct(@PathVariable("id") Long id) {

        return productService.getProduct(id);
    }

    @PostMapping(value = "/")
    Product createProduct(@RequestBody ProductDTO inputProduct) {

        return productService.createProduct(inputProduct);

    }

    @PutMapping(value = "/{id}")
    Product updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO inputProduct) {

        return productService.updateProduct(id, inputProduct);

    }


    @DeleteMapping(value = "/{id}")
    Product deleteProduct(@PathVariable("id") Long id) {

        return productService.deleteProduct(id);
    }
}
