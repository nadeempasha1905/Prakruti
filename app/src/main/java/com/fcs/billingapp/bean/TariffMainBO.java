package com.fcs.billingapp.bean;

import java.io.Serializable;

/**
 * Created by Nadeem on 8/22/2017.
 */

public class TariffMainBO implements Serializable {

    private int TariffCode;
    private String TariffType;
    private String TariffPowerUnit; // K - KW / H - HP
    private int MinFixed;   // Minum fixed charges if any
    private long DCUnits;   // paise per unit
    private int TariffTax;     // paise per unit
    private int ConstCharge; //The constant charge applicable with no units calculations

    public int getTariffCode() {
        return TariffCode;
    }

    public void setTariffCode(int tariffCode) {
        TariffCode = tariffCode;
    }

    public String getTariffType() {
        return TariffType;
    }

    public void setTariffType(String tariffType) {
        TariffType = tariffType;
    }

    public String getTariffPowerUnit() {
        return TariffPowerUnit;
    }

    public void setTariffPowerUnit(String tariffPowerUnit) {
        TariffPowerUnit = tariffPowerUnit;
    }

    public int getMinFixed() {
        return MinFixed;
    }

    public void setMinFixed(int minFixed) {
        MinFixed = minFixed;
    }

    public long getDCUnits() {
        return DCUnits;
    }

    public void setDCUnits(long DCUnits) {
        this.DCUnits = DCUnits;
    }

    public int getTariffTax() {
        return TariffTax;
    }

    public void setTariffTax(int tariffTax) {
        TariffTax = tariffTax;
    }

    public int getConstCharge() {
        return ConstCharge;
    }

    public void setConstCharge(int constCharge) {
        ConstCharge = constCharge;
    }
}
