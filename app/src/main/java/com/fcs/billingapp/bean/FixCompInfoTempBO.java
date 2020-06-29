package com.fcs.billingapp.bean;

import java.io.Serializable;

/**
 * Created by Nadeem on 8/22/2017.
 */

public class FixCompInfoTempBO implements Serializable {

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
