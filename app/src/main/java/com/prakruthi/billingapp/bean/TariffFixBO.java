package com.prakruthi.billingapp.bean;

import java.io.Serializable;

/**
 * Created by Nadeem on 8/22/2017.
 */

public class TariffFixBO implements Serializable {

    private int TariffCode;
    private char Item;
    private char ChargeType; // M - Monthly / Q - Quarterly, H - Half yearly, A - Yearly
    private int From;   // From sanctioned power
    private int To;       // To sanctioned power
    private long Tariff; // in paise per unit

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

    public char getChargeType() {
        return ChargeType;
    }

    public void setChargeType(char chargeType) {
        ChargeType = chargeType;
    }

    public int getFrom() {
        return From;
    }

    public void setFrom(int from) {
        From = from;
    }

    public int getTo() {
        return To;
    }

    public void setTo(int to) {
        To = to;
    }

    public long getTariff() {
        return Tariff;
    }

    public void setTariff(long tariff) {
        Tariff = tariff;
    }
}
