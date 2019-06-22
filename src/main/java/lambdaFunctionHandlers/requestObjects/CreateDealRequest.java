package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;

public class CreateDealRequest extends CreateObjectRequest {
    // Required
    public String sponsor;
    public String productName;
    public String productCreditPrice;
    public String quantity;

    // Optional
    public String productImagePath;
    public String[] productImagePaths;
    public String validTime;
    public String productStoreLink;

    public CreateDealRequest(String sponsor, String productName, String productCreditPrice, String
            quantity, String productImagePath, String[] productImagePaths, String validTime,
                             String productStoreLink) {
        this.sponsor = sponsor;
        this.productName = productName;
        this.productCreditPrice = productCreditPrice;
        this.quantity = quantity;
        this.productImagePath = productImagePath;
        this.productImagePaths = productImagePaths;
        this.validTime = validTime;
        this.productStoreLink = productStoreLink;
    }

    public CreateDealRequest() {}

    @Override
    public boolean ifHasEmptyString() throws ExceedsDatabaseLimitException {
        return CreateObjectRequest.hasEmptyString(sponsor, productName, productCreditPrice, quantity,
                productImagePath, validTime, productStoreLink) && CreateObjectRequest.arrayHasEmptyString(
                        productImagePaths);
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCreditPrice() {
        return productCreditPrice;
    }

    public void setProductCreditPrice(String productCreditPrice) {
        this.productCreditPrice = productCreditPrice;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public String[] getProductImagePaths() {
        return productImagePaths;
    }

    public void setProductImagePaths(String[] productImagePaths) {
        this.productImagePaths = productImagePaths;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getProductStoreLink() {
        return productStoreLink;
    }

    public void setProductStoreLink(String productStoreLink) {
        this.productStoreLink = productStoreLink;
    }
}
