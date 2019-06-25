package main.java.lambdaFunctionHandlers.requestObjects;

import main.java.databaseObjects.Product;
import main.java.databaseOperations.exceptions.ExceedsDatabaseLimitException;

/**
 * The POJO for the request if the Lambda caller wants to create a {@link Product} in the database.
 */
public class CreateProductRequest extends CreateObjectRequest {
    // Required
    public String owner;
    public String deal;

    // Optional
    public String expirationDate;
    public String[] codes;
    public String[] links;

    public CreateProductRequest(String owner, String deal, String expirationDate, String[] codes, String[] links) {
        this.owner = owner;
        this.deal = deal;
        this.expirationDate = expirationDate;
        this.codes = codes;
        this.links = links;
    }

    public CreateProductRequest() {}

    @Override
    public boolean ifHasEmptyString() throws ExceedsDatabaseLimitException {
        return hasEmptyString(owner, deal, expirationDate) || arrayHasEmptyString(codes, links);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String[] getCodes() {
        return codes;
    }

    public void setCodes(String[] codes) {
        this.codes = codes;
    }

    public String[] getLinks() {
        return links;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }
}
