package com.prakruthi.billingapp.fragments;

public class ItemDataObject {

    private String mProductNameEnglish;
    private String mProductNameKannada;
    private String mProductMeasuringUnit;
    private String mProductDiscountType;
    private String mProductPrice;
    private String mProductDiscountPrice;

    public ItemDataObject() {
    }

    public ItemDataObject(String mProductNameEnglish, String mProductNameKannada, String mProductMeasuringUnit, String mProductDiscountType, String mProductPrice, String mProductDiscountPrice) {
        this.mProductNameEnglish = mProductNameEnglish;
        this.mProductNameKannada = mProductNameKannada;
        this.mProductMeasuringUnit = mProductMeasuringUnit;
        this.mProductDiscountType = mProductDiscountType;
        this.mProductPrice = mProductPrice;
        this.mProductDiscountPrice = mProductDiscountPrice;
    }

    public String getmProductNameEnglish() {
        return mProductNameEnglish;
    }

    public void setmProductNameEnglish(String mProductNameEnglish) {
        this.mProductNameEnglish = mProductNameEnglish;
    }

    public String getmProductNameKannada() {
        return mProductNameKannada;
    }

    public void setmProductNameKannada(String mProductNameKannada) {
        this.mProductNameKannada = mProductNameKannada;
    }

    public String getmProductMeasuringUnit() {
        return mProductMeasuringUnit;
    }

    public void setmProductMeasuringUnit(String mProductMeasuringUnit) {
        this.mProductMeasuringUnit = mProductMeasuringUnit;
    }

    public String getmProductDiscountType() {
        return mProductDiscountType;
    }

    public void setmProductDiscountType(String mProductDiscountType) {
        this.mProductDiscountType = mProductDiscountType;
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
