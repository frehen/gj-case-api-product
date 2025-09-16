package no.gjensidige.product.dto;

import java.math.BigInteger;

/**
 * ProductDTO is a data transfer object we use
 * for communication in the rest controllers.
 *
 */
public class ProductDTO {

    String productName;
    String category;
    String imageLink;
    Double unitCost;
    Double unitPrice;
    BigInteger numberSold;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigInteger getNumberSold() {
        return numberSold;
    }

    public void setNumberSold(BigInteger numberSold) {
        this.numberSold = numberSold;
    }
}
