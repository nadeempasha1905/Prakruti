package com.fcs.billingapp.bean;

import java.io.Serializable;

/**
 * Created by Nadeem on 8/17/2017.
 */

public class EnergyChargeListBO implements Serializable {

    private long Units;
    private long Rate;
    private double Amount;

    public long getUnits() {
        return Units;
    }

    public void setUnits(long units) {
        Units = units;
    }

    public long getRate() {
        return Rate;
    }

    public void setRate(long rate) {
        Rate = rate;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}
