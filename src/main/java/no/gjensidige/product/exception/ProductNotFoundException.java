package no.gjensidige.product.exception;

/**
 *
 * Exception thrown when product is not in the database.
 *
 */
public class ProductNotFoundException  extends RuntimeException {

    public ProductNotFoundException(Long id) {
            super("Could not find product with id : " + id);
    }
}

