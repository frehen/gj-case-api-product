package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * ProductService
 *
 * Class responsible of data manipulation between dto and entity
 *
 *
 */

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProduct(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    }

    public Product deleteProduct(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(p);
        return p;
    }


    public Product createProduct(ProductDTO inputProduct) {

        Product product = convertToEntity(inputProduct);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDTO inputProduct) {
        if (!Objects.equals(id, inputProduct.getId())) {
            throw new IllegalArgumentException(String.format(
                    "Product id in request path: %s does not match id in the request body: %s",
                    id, inputProduct.getId()));
        }
        Product p = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        modelMapper.map(inputProduct, p);
        productRepository.save(p);
        return p;
    }

    public ProductDTO convertToDTO(Product product) {

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        return product;

    }


}
