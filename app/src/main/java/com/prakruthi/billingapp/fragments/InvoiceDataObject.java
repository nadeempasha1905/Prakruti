package com.prakruthi.billingapp.fragments;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvoiceDataObject implements Parcelable {

    private Integer invoiceNumber;
    private String invoiceDate;
    private Integer itemId;
    private Integer totalItems;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal grossAmount;
    private BigDecimal discountAmount;
    private BigDecimal netAmount;
    private String mProductNameEnglish;
    private String mProductNameKannada;
    private String mProductMeasuringUnit;
    private String mProductDiscountType;
    private String mProductPrice;
    private String mProductDiscountPrice;
    private String createdBy;
    private String createdOn;
    private String updatedBy;
    private String updatedOn;

    public InvoiceDataObject() {
    }

    protected InvoiceDataObject(Parcel in) {
        if (in.readByte() == 0) {
            invoiceNumber = null;
        } else {
            invoiceNumber = in.readInt();
        }
        invoiceDate = in.readString();
        if (in.readByte() == 0) {
            itemId = null;
        } else {
            itemId = in.readInt();
        }
        if (in.readByte() == 0) {
            totalItems = null;
        } else {
            totalItems = in.readInt();
        }
        mProductNameEnglish = in.readString();
        mProductNameKannada = in.readString();
        mProductMeasuringUnit = in.readString();
        mProductDiscountType = in.readString();
        mProductPrice = in.readString();
        mProductDiscountPrice = in.readString();
        createdBy = in.readString();
        createdOn = in.readString();
        updatedBy = in.readString();
        updatedOn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (invoiceNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(invoiceNumber);
        }
        dest.writeString(invoiceDate);
        if (itemId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(itemId);
        }
        if (totalItems == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalItems);
        }
        dest.writeString(mProductNameEnglish);
        dest.writeString(mProductNameKannada);
        dest.writeString(mProductMeasuringUnit);
        dest.writeString(mProductDiscountType);
        dest.writeString(mProductPrice);
        dest.writeString(mProductDiscountPrice);
        dest.writeString(createdBy);
        dest.writeString(createdOn);
        dest.writeString(updatedBy);
        dest.writeString(updatedOn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InvoiceDataObject> CREATOR = new Creator<InvoiceDataObject>() {
        @Override
        public InvoiceDataObject createFromParcel(Parcel in) {
            return new InvoiceDataObject(in);
        }

        @Override
        public InvoiceDataObject[] newArray(int size) {
            return new InvoiceDataObject[size];
        }
    };

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
