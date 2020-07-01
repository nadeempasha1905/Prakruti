package com.prakruthi.billingapp.bean;

import java.io.Serializable;

/**
 * Created by Nadeem on 8/22/2017.
 */

public class TariffSlabBO implements Serializable {

    private int TariffCode;
    private char Item;
    private int  FromUnits;
    private int  ToUnits;
    private int Tariff; // in paise per unit

    public int getTariffCode() {
        return TariffCode;
    }

    public void setTariffCode(int tariffCode) {
        TariffCode = tariffCode;
    }

    public char getItem() {
        return Item;
    }

    public void setItem(char item) {
        Item = item;
    }

    public int getFromUnits() {
        return FromUnits;
    }

    public void setFromUnits(int fromUnits) {
        FromUnits = fromUnits;
    }

    public int getToUnits() {
        return ToUnits;
    }

    public void setToUnits(int toUnits) {
        ToUnits = toUnits;
    }

    public int getTariff() {
        return Tariff;
    }

    public void setTariff(int tariff) {
        Tariff = tariff;
    }
}
