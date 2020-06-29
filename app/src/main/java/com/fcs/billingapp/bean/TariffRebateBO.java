package com.fcs.billingapp.bean;

import java.io.Serializable;

/**
 * Created by Nadeem on 8/22/2017.
 */

public class TariffRebateBO implements Serializable {

    private String RebateCode;
    private char chType;//d for direct , p for percentage
    private int Rebate;
    private int MaxRebate;
    private char RebateType; //0 for rebate on fixed charges //1 for rebate on both

    public String getRebateCode() {
        return RebateCode;
    }

    public void setRebateCode(String rebateCode) {
        RebateCode = rebateCode;
    }

    public char getChType() {
        return chType;
    }

    public void setChType(char chType) {
        this.chType = chType;
    }

    public int getRebate() {
        return Rebate;
    }

    public void setRebate(int rebate) {
        Rebate = rebate;
    }

    public int getMaxRebate() {
        return MaxRebate;
    }

    public void setMaxRebate(int maxRebate) {
        MaxRebate = maxRebate;
    }

    public char getRebateType() {
        return RebateType;
    }

    public void setRebateType(char rebateType) {
        RebateType = rebateType;
    }
}
