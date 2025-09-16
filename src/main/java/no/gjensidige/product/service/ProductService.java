package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * ProductService
 *
 * <p>Class responsible for data manipulation between dto and entity
 *
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

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
        Product p = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        modelMapper.map(inputProduct, p);
        productRepository.save(p);
        return p;
    }

    public ProductDTO convertToDTO(Product product) {

        return modelMapper.map(product, ProductDTO.class);
    }

    public Product convertToEntity(ProductDTO productDTO) {

        return modelMapper.map(productDTO, Product.class);

    }


}
