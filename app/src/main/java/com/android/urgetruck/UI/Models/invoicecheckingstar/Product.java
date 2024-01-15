package com.android.urgetruck.UI.Models.invoicecheckingstar;

public class Product {
    private boolean isLoaded;
    private String productCategory;
    private String productName;
    private String productTransactionCode;
    private int quantity;

    public Product(boolean isLoaded, String productCategory, String productName, String productTransactionCode, int quantity) {
        this.isLoaded = isLoaded;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productTransactionCode = productTransactionCode;
        this.quantity = quantity;
    }
    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTransactionCode() {
        return productTransactionCode;
    }

    public void setProductTransactionCode(String productTransactionCode) {
        this.productTransactionCode = productTransactionCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
