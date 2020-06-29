package com.fcs.billingapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ResultMasterBO implements Serializable, Parcelable {

    private String key;
    private String value;


    public ResultMasterBO(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<ResultMasterBO> CREATOR = new Creator<ResultMasterBO>() {
        @Override
        public ResultMasterBO createFromParcel(Parcel in) {
            return new ResultMasterBO(in);
        }

        @Override
        public ResultMasterBO[] newArray(int size) {
            return new ResultMasterBO[size];
        }
    };

    public ResultMasterBO() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
