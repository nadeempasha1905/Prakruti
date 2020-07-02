package com.prakruthi.billingapp.fragments;

public class DataObject {

    private String mProductName;
    private String mProductPrice;
    private String mProductDiscountPrice;

    public DataObject(String mProductName, String mProductPrice, String mProductDiscountPrice) {
        this.mProductName = mProductName;
        this.mProductPrice = mProductPrice;
        this.mProductDiscountPrice = mProductDiscountPrice;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getmProductPrice() {
        return mProductPrice;
    }

    public void setmProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public String getmProductDiscountPrice() {
        return mProductDiscountPrice;
    }

    public void setmProductDiscountPrice(String mProductDiscountPrice) {
        this.mProductDiscountPrice = mProductDiscountPrice;
    }
}
