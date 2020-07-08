package com.prakruthi.billingapp.fragments;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ItemDataObject implements Parcelable {

    private Integer mId;
    private String mProductNameEnglish;
    private String mProductNameKannada;
    private String mProductMeasuringUnit;
    private String mProductDiscountType;
    private String mProductPrice;
    private String mProductDiscountPrice;
    private String mProductDeletedStatus;
    private String mCreatedBy;
    private String mCreatedOn;
    private String mUpdatedBy;
    private String mUpdatedOn;
    private String Quantity;

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

    protected ItemDataObject(Parcel in) {
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readInt();
        }
        mProductNameEnglish = in.readString();
        mProductNameKannada = in.readString();
        mProductMeasuringUnit = in.readString();
        mProductDiscountType = in.readString();
        mProductPrice = in.readString();
        mProductDiscountPrice = in.readString();
        mProductDeletedStatus = in.readString();
        mCreatedBy = in.readString();
        mCreatedOn = in.readString();
        mUpdatedBy = in.readString();
        mUpdatedOn = in.readString();
        Quantity = in.readString();
    }

    public static final Creator<ItemDataObject> CREATOR = new Creator<ItemDataObject>() {
        @Override
        public ItemDataObject createFromParcel(Parcel in) {
            return new ItemDataObject(in);
        }

        @Override
        public ItemDataObject[] newArray(int size) {
            return new ItemDataObject[size];
        }
    };

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

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmProductDeletedStatus() {
        return mProductDeletedStatus;
    }

    public void setmProductDeletedStatus(String mProductDeletedStatus) {
        this.mProductDeletedStatus = mProductDeletedStatus;
    }

    public String getmCreatedBy() {
        return mCreatedBy;
    }

    public void setmCreatedBy(String mCreatedBy) {
        this.mCreatedBy = mCreatedBy;
    }

    public String getmCreatedOn() {
        return mCreatedOn;
    }

    public void setmCreatedOn(String mCreatedOn) {
        this.mCreatedOn = mCreatedOn;
    }

    public String getmUpdatedBy() {
        return mUpdatedBy;
    }

    public void setmUpdatedBy(String mUpdatedBy) {
        this.mUpdatedBy = mUpdatedBy;
    }

    public String getmUpdatedOn() {
        return mUpdatedOn;
    }

    public void setmUpdatedOn(String mUpdatedOn) {
        this.mUpdatedOn = mUpdatedOn;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (mId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(mId);
        }
        parcel.writeString(mProductNameEnglish);
        parcel.writeString(mProductNameKannada);
        parcel.writeString(mProductMeasuringUnit);
        parcel.writeString(mProductDiscountType);
        parcel.writeString(mProductPrice);
        parcel.writeString(mProductDiscountPrice);
        parcel.writeString(mProductDeletedStatus);
        parcel.writeString(mCreatedBy);
        parcel.writeString(mCreatedOn);
        parcel.writeString(mUpdatedBy);
        parcel.writeString(mUpdatedOn);
        parcel.writeString(Quantity);
    }
}
