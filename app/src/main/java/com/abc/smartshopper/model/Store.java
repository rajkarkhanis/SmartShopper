package com.abc.smartshopper.model;
public  class Store   {
    public String storeName;
    public String storeUrl;
    public String storePrice;

    public Store() {
    }

    public Store(String storeName, String storeUrl, String storePrice) {
        this.storeName = storeName;
        this.storeUrl = storeUrl;
        this.storePrice = storePrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(String storePrice) {
        this.storePrice = storePrice;
    }
}
