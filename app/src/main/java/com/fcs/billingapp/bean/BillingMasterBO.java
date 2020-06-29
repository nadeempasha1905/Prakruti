package com.fcs.billingapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nadeem on 8/16/2017.
 */

public class BillingMasterBO implements Serializable, Parcelable {

    private int    KEY_INDEX                  ;
    private String KEY_LOCATION               ;
    private String KEY_RR_NO                  ;
    private String KEY_LEDGER_NO              ;
    private String KEY_ACTUAL_FOLIO_NO        ;
    private String KEY_SPOT_FOLIO_NO          ;
    private int    KEY_TARIFF_CODE            ;
    private String KEY_BILL_NO                ;
    private String KEY_BILL_DATE              ;
    private String KEY_CONSMR_NAME            ;
    private String KEY_ADDRESS1               ;
    private String KEY_ADDRESS2               ;
    private String KEY_ADDRESS3               ;
    private String KEY_BILLING_MONTH          ;
    private String KEY_READING_DATE           ;
    private String KEY_READER_CODE            ;
    private int    KEY_INSTALLATION_STATUS    ;
    private double KEY_LINE_MINIMUM           ;
    private double KEY_SANCT_HP               ;
    private double KEY_SANCT_KW               ;
    private double KEY_CT_PT                  ;
    private int    KEY_PREV_MTR_RDG           ;
    private double KEY_AVG_CONSUMPTION        ;
    private double KEY_POWER_FACTOR           ;
    private String KEY_MTR_CHNG_DT1           ;
    private double KEY_MTR_CHNG_RDG1          ;
    private String KEY_MTR_CHNG_DT2           ;
    private double KEY_MTR_CHNG_RGD2          ;
    private double KEY_FAC_RATE               ;
    private double KEY_DMD_ARREARS            ;
    private double KEY_INT_ARREARS            ;
    private double KEY_TAX_ARREARS            ;
    private double KEY_DELAY_INTEREST         ;
    private double KEY_AMT_PAID               ;
    private String KEY_PAID_DATE1             ;
    private double KEY_AMT_PAID2              ;
    private String KEY_PAID_DATE2             ;
    private double KEY_OTHERS                 ;
    private String BILLPRINTED                ;
    private double CAPRBTAMT                  ;
    private double PREVIOUS_DEMAND1           ;
    private double PREVIOUS_DEMAND2           ;
    private double PREVIOUS_DEMAND3           ;
    private String BILLING_MODE               ;
    private double METER_CONST                ;
    private double APPEAL_AMOUNT              ;
    private double INT_APPEAL_AMOUNT          ;
    private double KVAH                       ;
    private String INSTAL_TYP                 ;
    private double HP_ROUND                   ;
    private double KW_ROUND                   ;
    private String PREVIOUS_BILL_DATE         ;
    private double CREDITBF                   ;
    private double DEBITBF                    ;
    private String DUE_DATE                   ;
    private String IVRSID                     ;
    private double PARTFRACTION               ;
    private int    NUMDL                      ;
    private String CGEXEMPT_FLG               ;
    private String DC_FLG                     ;
    private double INT_ARREARS2               ;
    private double INT_TAX                    ;
    private String FIRST_RDG_FLG              ;
    private int    CREAPING_PERC              ;
    private String MNR_FLG                    ;
    private long    OLD_MTR_RDG                ;
    private double INT_ON_TAX                 ;
    private String SUBDIV                     ;
    private double NO_TAX_COMP                ;
    private String PREV_RDG_FLG               ;
    private String PLFLAG                     ;
    private double PREVCKWH                   ;
    private double REG_PENALTY                ;
    private String PF_FLAG                    ;
    private int    LASTMONTHFRACTION          ;
    private int    CUR_QRTR                   ;
    private int    FREQUENCY                  ;
    private double ANNUAL_MIN_FIX             ;
    private double CAPACITOR                  ;
    private String CHEQDIS                    ;
    private double ORPHAN_RBT                 ;
    private double MMD_CREDIT                 ;
    private String MTR_CHNG_FLG               ;
    private double ADDNL3MMD                  ;
    private int    TEMP_TARIFF_CODE           ;
    private double LESSCLAIMED                ;
    private double MORECLAIMED                ;
    private int    PRESENT_RDG                ;
    private long    N_UNITSCONSUMED            ;
    private double N_TAX                      ;
    private double TOTALBILL                  ;
    private double N_REBATE                   ;
    private double TOTAL_FIXED_TARIFF         ;
    private double P_NTOTAL_ENAERGY_TARIFF    ;
    private double CREDIT_CF                  ;
    private String METER_STS                  ;
    private double LESS_MORE_CLAIMED          ;
    private double TEMP_TAX                   ;
    private double DIFF_AMOUNT                ;
    private double PF_PENALTY                 ;
    private double PH_REBATE                  ;
    private double PL_REBATE                  ;
    private String FIRST_RDG_FLAG             ;
    private String FULL_MONTH_FLAG            ;
    private double ORPHAN_AMOUNT              ;
    private double RR_REBATE                  ;
    private double LT4_DEBIT                  ;
    private int    PL_TEMP_TARIFF_CODE        ;
    private double ENERGYAMOUNTPLUSTAX        ;
    private String UPLOADEDTOSERVER           ;
    private byte   FIXED_OBJ[]                ;
    private byte   ENERGY_OBJ[]               ;
    private double PRSTCKWH	                  ;
    private String NFC_CODE	                  ;   //This field Added to Store NFC Code
    private String NFC_TAG	                  ;   //This field Added to Store NFC TAG Exists Or Not ( Y/N )
    private byte   METER_IMAGE[]	          ; // to store image
    private double LATITUDE	                  ;   //to store position in latitude
    private double LONGITUDE	              ;   //to store position in longitude

    private char FLG_Solar_rebate               ;
    private char FLG_FL_rebate	                ;
    private char FLG_PH_rebate	                ;
    private char FLG_telep_rebate               ;
    private char FLG_METER		                ;
    private char FLG_REBATE_CAP	                ;
    private char FLG_DEMAND_BASED               ;
    private char FLG_RURAL_REBATE               ;
    private char FLG_NORMAL		                ;
    private char FLG_METER_TVM	                ;
    private char FLG_ECS_USER	                ;

    private String CREATED_BY                 ;
    private String CREATED_ON                 ;
    private String UPDATED_BY                 ;
    private String UPDATED_ON                 ;
    private ArrayList<FixedChargeListBO>  fixedChargeListBOs;
    private ArrayList<EnergyChargeListBO> energyChargeListBOs;

    //Extras..............

    private double HP_Min_Fix;
    private int tempKW_Round;
    private double temp_total;


    public BillingMasterBO(Parcel in) {
        KEY_INDEX = in.readInt();
        KEY_LOCATION = in.readString();
        KEY_RR_NO = in.readString();
        KEY_LEDGER_NO = in.readString();
        KEY_ACTUAL_FOLIO_NO = in.readString();
        KEY_SPOT_FOLIO_NO = in.readString();
        KEY_TARIFF_CODE = in.readInt();
        KEY_BILL_NO = in.readString();
        KEY_BILL_DATE = in.readString();
        KEY_CONSMR_NAME = in.readString();
        KEY_ADDRESS1 = in.readString();
        KEY_ADDRESS2 = in.readString();
        KEY_ADDRESS3 = in.readString();
        KEY_BILLING_MONTH = in.readString();
        KEY_READING_DATE = in.readString();
        KEY_READER_CODE = in.readString();
        KEY_INSTALLATION_STATUS = in.readInt();
        KEY_LINE_MINIMUM = in.readDouble();
        KEY_SANCT_HP = in.readDouble();
        KEY_SANCT_KW = in.readDouble();
        KEY_CT_PT = in.readDouble();
        KEY_PREV_MTR_RDG = in.readInt();
        KEY_AVG_CONSUMPTION = in.readDouble();
        KEY_POWER_FACTOR = in.readDouble();
        KEY_MTR_CHNG_DT1 = in.readString();
        KEY_MTR_CHNG_RDG1 = in.readDouble();
        KEY_MTR_CHNG_DT2 = in.readString();
        KEY_MTR_CHNG_RGD2 = in.readDouble();
        KEY_FAC_RATE = in.readDouble();
        KEY_DMD_ARREARS = in.readDouble();
        KEY_INT_ARREARS = in.readDouble();
        KEY_TAX_ARREARS = in.readDouble();
        KEY_DELAY_INTEREST = in.readDouble();
        KEY_AMT_PAID = in.readDouble();
        KEY_PAID_DATE1 = in.readString();
        KEY_AMT_PAID2 = in.readDouble();
        KEY_PAID_DATE2 = in.readString();
        KEY_OTHERS = in.readDouble();
        BILLPRINTED = in.readString();
        CAPRBTAMT = in.readDouble();
        PREVIOUS_DEMAND1 = in.readDouble();
        PREVIOUS_DEMAND2 = in.readDouble();
        PREVIOUS_DEMAND3 = in.readDouble();
        BILLING_MODE = in.readString();
        METER_CONST = in.readDouble();
        APPEAL_AMOUNT = in.readDouble();
        INT_APPEAL_AMOUNT = in.readDouble();
        KVAH = in.readDouble();
        INSTAL_TYP = in.readString();
        HP_ROUND = in.readDouble();
        KW_ROUND = in.readDouble();
        PREVIOUS_BILL_DATE = in.readString();
        CREDITBF = in.readDouble();
        DEBITBF = in.readDouble();
        DUE_DATE = in.readString();
        IVRSID = in.readString();
        PARTFRACTION = in.readDouble();
        NUMDL = in.readInt();
        CGEXEMPT_FLG = in.readString();
        DC_FLG = in.readString();
        INT_ARREARS2 = in.readDouble();
        INT_TAX = in.readDouble();
        FIRST_RDG_FLG = in.readString();
        CREAPING_PERC = in.readInt();
        MNR_FLG = in.readString();
        OLD_MTR_RDG = in.readLong();
        INT_ON_TAX = in.readDouble();
        SUBDIV = in.readString();
        NO_TAX_COMP = in.readDouble();
        PREV_RDG_FLG = in.readString();
        PLFLAG = in.readString();
        PREVCKWH = in.readDouble();
        REG_PENALTY = in.readDouble();
        PF_FLAG = in.readString();
        LASTMONTHFRACTION = in.readInt();
        CUR_QRTR = in.readInt();
        FREQUENCY = in.readInt();
        ANNUAL_MIN_FIX = in.readDouble();
        CAPACITOR = in.readDouble();
        CHEQDIS = in.readString();
        ORPHAN_RBT = in.readDouble();
        MMD_CREDIT = in.readDouble();
        MTR_CHNG_FLG = in.readString();
        ADDNL3MMD = in.readDouble();
        TEMP_TARIFF_CODE = in.readInt();
        LESSCLAIMED = in.readDouble();
        MORECLAIMED = in.readDouble();
        PRESENT_RDG = in.readInt();
        N_UNITSCONSUMED = in.readLong();
        N_TAX = in.readDouble();
        TOTALBILL = in.readDouble();
        N_REBATE = in.readDouble();
        TOTAL_FIXED_TARIFF = in.readDouble();
        P_NTOTAL_ENAERGY_TARIFF = in.readDouble();
        CREDIT_CF = in.readDouble();
        METER_STS = in.readString();
        LESS_MORE_CLAIMED = in.readDouble();
        TEMP_TAX = in.readDouble();
        DIFF_AMOUNT = in.readDouble();
        PF_PENALTY = in.readDouble();
        PH_REBATE = in.readDouble();
        PL_REBATE = in.readDouble();
        FIRST_RDG_FLAG = in.readString();
        FULL_MONTH_FLAG = in.readString();
        ORPHAN_AMOUNT = in.readDouble();
        RR_REBATE = in.readDouble();
        LT4_DEBIT = in.readDouble();
        PL_TEMP_TARIFF_CODE = in.readInt();
        ENERGYAMOUNTPLUSTAX = in.readDouble();
        UPLOADEDTOSERVER = in.readString();
        FIXED_OBJ = in.createByteArray();
        ENERGY_OBJ = in.createByteArray();
        PRSTCKWH = in.readDouble();
        NFC_CODE = in.readString();
        NFC_TAG = in.readString();
        METER_IMAGE = in.createByteArray();
        LATITUDE = in.readDouble();
        LONGITUDE = in.readDouble();
        FLG_Solar_rebate = (char) in.readInt();
        FLG_FL_rebate = (char) in.readInt();
        FLG_PH_rebate = (char) in.readInt();
        FLG_telep_rebate = (char) in.readInt();
        FLG_METER = (char) in.readInt();
        FLG_REBATE_CAP = (char) in.readInt();
        FLG_DEMAND_BASED = (char) in.readInt();
        FLG_RURAL_REBATE = (char) in.readInt();
        FLG_NORMAL = (char) in.readInt();
        FLG_METER_TVM = (char) in.readInt();
        FLG_ECS_USER = (char) in.readInt();
        CREATED_BY = in.readString();
        CREATED_ON = in.readString();
        UPDATED_BY = in.readString();
        UPDATED_ON = in.readString();
        HP_Min_Fix = in.readDouble();
        tempKW_Round = in.readInt();
        temp_total = in.readDouble();
    }

    public static final Creator<BillingMasterBO> CREATOR = new Creator<BillingMasterBO>() {
        @Override
        public BillingMasterBO createFromParcel(Parcel in) {
            return new BillingMasterBO(in);
        }

        @Override
        public BillingMasterBO[] newArray(int size) {
            return new BillingMasterBO[size];
        }
    };

    public BillingMasterBO() {

    }

    public double getTemp_total() {
        return temp_total;
    }

    public void setTemp_total(double temp_total) {
        this.temp_total = temp_total;
    }

    public int getTempKW_Round() {
        return tempKW_Round;
    }

    public void setTempKW_Round(int tempKW_Round) {
        this.tempKW_Round = tempKW_Round;
    }

    public double getHP_Min_Fix() {
        return HP_Min_Fix;
    }

    public void setHP_Min_Fix(double HP_Min_Fix) {
        this.HP_Min_Fix = HP_Min_Fix;
    }

    public char getFLG_Solar_rebate() {
        return FLG_Solar_rebate;
    }

    public void setFLG_Solar_rebate(char FLG_Solar_rebate) {
        this.FLG_Solar_rebate = FLG_Solar_rebate;
    }

    public char getFLG_FL_rebate() {
        return FLG_FL_rebate;
    }

    public void setFLG_FL_rebate(char FLG_FL_rebate) {
        this.FLG_FL_rebate = FLG_FL_rebate;
    }

    public char getFLG_PH_rebate() {
        return FLG_PH_rebate;
    }

    public void setFLG_PH_rebate(char FLG_PH_rebate) {
        this.FLG_PH_rebate = FLG_PH_rebate;
    }

    public char getFLG_telep_rebate() {
        return FLG_telep_rebate;
    }

    public void setFLG_telep_rebate(char FLG_telep_rebate) {
        this.FLG_telep_rebate = FLG_telep_rebate;
    }

    public char getFLG_METER() {
        return FLG_METER;
    }

    public void setFLG_METER(char FLG_METER) {
        this.FLG_METER = FLG_METER;
    }

    public char getFLG_REBATE_CAP() {
        return FLG_REBATE_CAP;
    }

    public void setFLG_REBATE_CAP(char FLG_REBATE_CAP) {
        this.FLG_REBATE_CAP = FLG_REBATE_CAP;
    }

    public char getFLG_DEMAND_BASED() {
        return FLG_DEMAND_BASED;
    }

    public void setFLG_DEMAND_BASED(char FLG_DEMAND_BASED) {
        this.FLG_DEMAND_BASED = FLG_DEMAND_BASED;
    }

    public char getFLG_RURAL_REBATE() {
        return FLG_RURAL_REBATE;
    }

    public void setFLG_RURAL_REBATE(char FLG_RURAL_REBATE) {
        this.FLG_RURAL_REBATE = FLG_RURAL_REBATE;
    }

    public char getFLG_NORMAL() {
        return FLG_NORMAL;
    }

    public void setFLG_NORMAL(char FLG_NORMAL) {
        this.FLG_NORMAL = FLG_NORMAL;
    }

    public char getFLG_METER_TVM() {
        return FLG_METER_TVM;
    }

    public void setFLG_METER_TVM(char FLG_METER_TVM) {
        this.FLG_METER_TVM = FLG_METER_TVM;
    }

    public char getFLG_ECS_USER() {
        return FLG_ECS_USER;
    }

    public void setFLG_ECS_USER(char FLG_ECS_USER) {
        this.FLG_ECS_USER = FLG_ECS_USER;
    }

    public ArrayList<FixedChargeListBO> getFixedChargeListBOs() {
        return fixedChargeListBOs;
    }

    public void setFixedChargeListBOs(ArrayList<FixedChargeListBO> fixedChargeListBOs) {
        this.fixedChargeListBOs = fixedChargeListBOs;
    }

    public ArrayList<EnergyChargeListBO> getEnergyChargeListBOs() {
        return energyChargeListBOs;
    }

    public void setEnergyChargeListBOs(ArrayList<EnergyChargeListBO> energyChargeListBOs) {
        this.energyChargeListBOs = energyChargeListBOs;
    }

    public int getKEY_INDEX() {
        return KEY_INDEX;
    }

    public void setKEY_INDEX(int KEY_INDEX) {
        this.KEY_INDEX = KEY_INDEX;
    }

    public String getKEY_LOCATION() {
        return KEY_LOCATION;
    }

    public void setKEY_LOCATION(String KEY_LOCATION) {
        this.KEY_LOCATION = KEY_LOCATION;
    }

    public String getKEY_RR_NO() {
        return KEY_RR_NO;
    }

    public void setKEY_RR_NO(String KEY_RR_NO) {
        this.KEY_RR_NO = KEY_RR_NO;
    }

    public String getKEY_LEDGER_NO() {
        return KEY_LEDGER_NO;
    }

    public void setKEY_LEDGER_NO(String KEY_LEDGER_NO) {
        this.KEY_LEDGER_NO = KEY_LEDGER_NO;
    }

    public String getKEY_ACTUAL_FOLIO_NO() {
        return KEY_ACTUAL_FOLIO_NO;
    }

    public void setKEY_ACTUAL_FOLIO_NO(String KEY_ACTUAL_FOLIO_NO) {
        this.KEY_ACTUAL_FOLIO_NO = KEY_ACTUAL_FOLIO_NO;
    }

    public String getKEY_SPOT_FOLIO_NO() {
        return KEY_SPOT_FOLIO_NO;
    }

    public void setKEY_SPOT_FOLIO_NO(String KEY_SPOT_FOLIO_NO) {
        this.KEY_SPOT_FOLIO_NO = KEY_SPOT_FOLIO_NO;
    }

    public int getKEY_TARIFF_CODE() {
        return KEY_TARIFF_CODE;
    }

    public void setKEY_TARIFF_CODE(int KEY_TARIFF_CODE) {
        this.KEY_TARIFF_CODE = KEY_TARIFF_CODE;
    }

    public String getKEY_BILL_NO() {
        return KEY_BILL_NO;
    }

    public void setKEY_BILL_NO(String KEY_BILL_NO) {
        this.KEY_BILL_NO = KEY_BILL_NO;
    }

    public String getKEY_BILL_DATE() {
        return KEY_BILL_DATE;
    }

    public void setKEY_BILL_DATE(String KEY_BILL_DATE) {
        this.KEY_BILL_DATE = KEY_BILL_DATE;
    }

    public String getKEY_CONSMR_NAME() {
        return KEY_CONSMR_NAME;
    }

    public void setKEY_CONSMR_NAME(String KEY_CONSMR_NAME) {
        this.KEY_CONSMR_NAME = KEY_CONSMR_NAME;
    }

    public String getKEY_ADDRESS1() {
        return KEY_ADDRESS1;
    }

    public void setKEY_ADDRESS1(String KEY_ADDRESS1) {
        this.KEY_ADDRESS1 = KEY_ADDRESS1;
    }

    public String getKEY_ADDRESS2() {
        return KEY_ADDRESS2;
    }

    public void setKEY_ADDRESS2(String KEY_ADDRESS2) {
        this.KEY_ADDRESS2 = KEY_ADDRESS2;
    }

    public String getKEY_ADDRESS3() {
        return KEY_ADDRESS3;
    }

    public void setKEY_ADDRESS3(String KEY_ADDRESS3) {
        this.KEY_ADDRESS3 = KEY_ADDRESS3;
    }

    public String getKEY_BILLING_MONTH() {
        return KEY_BILLING_MONTH;
    }

    public void setKEY_BILLING_MONTH(String KEY_BILLING_MONTH) {
        this.KEY_BILLING_MONTH = KEY_BILLING_MONTH;
    }

    public String getKEY_READING_DATE() {
        return KEY_READING_DATE;
    }

    public void setKEY_READING_DATE(String KEY_READING_DATE) {
        this.KEY_READING_DATE = KEY_READING_DATE;
    }

    public String getKEY_READER_CODE() {
        return KEY_READER_CODE;
    }

    public void setKEY_READER_CODE(String KEY_READER_CODE) {
        this.KEY_READER_CODE = KEY_READER_CODE;
    }

    public int getKEY_INSTALLATION_STATUS() {
        return KEY_INSTALLATION_STATUS;
    }

    public void setKEY_INSTALLATION_STATUS(int KEY_INSTALLATION_STATUS) {
        this.KEY_INSTALLATION_STATUS = KEY_INSTALLATION_STATUS;
    }

    public double getKEY_LINE_MINIMUM() {
        return KEY_LINE_MINIMUM;
    }

    public void setKEY_LINE_MINIMUM(double KEY_LINE_MINIMUM) {
        this.KEY_LINE_MINIMUM = KEY_LINE_MINIMUM;
    }

    public double getKEY_SANCT_HP() {
        return KEY_SANCT_HP;
    }

    public void setKEY_SANCT_HP(double KEY_SANCT_HP) {
        this.KEY_SANCT_HP = KEY_SANCT_HP;
    }

    public double getKEY_SANCT_KW() {
        return KEY_SANCT_KW;
    }

    public void setKEY_SANCT_KW(double KEY_SANCT_KW) {
        this.KEY_SANCT_KW = KEY_SANCT_KW;
    }

    public double getKEY_CT_PT() {
        return KEY_CT_PT;
    }

    public void setKEY_CT_PT(double KEY_CT_PT) {
        this.KEY_CT_PT = KEY_CT_PT;
    }

    public int getKEY_PREV_MTR_RDG() {
        return KEY_PREV_MTR_RDG;
    }

    public void setKEY_PREV_MTR_RDG(int KEY_PREV_MTR_RDG) {
        this.KEY_PREV_MTR_RDG = KEY_PREV_MTR_RDG;
    }

    public double getKEY_AVG_CONSUMPTION() {
        return KEY_AVG_CONSUMPTION;
    }

    public void setKEY_AVG_CONSUMPTION(double KEY_AVG_CONSUMPTION) {
        this.KEY_AVG_CONSUMPTION = KEY_AVG_CONSUMPTION;
    }

    public double getKEY_POWER_FACTOR() {
        return KEY_POWER_FACTOR;
    }

    public void setKEY_POWER_FACTOR(double KEY_POWER_FACTOR) {
        this.KEY_POWER_FACTOR = KEY_POWER_FACTOR;
    }

    public String getKEY_MTR_CHNG_DT1() {
        return KEY_MTR_CHNG_DT1;
    }

    public void setKEY_MTR_CHNG_DT1(String KEY_MTR_CHNG_DT1) {
        this.KEY_MTR_CHNG_DT1 = KEY_MTR_CHNG_DT1;
    }

    public double getKEY_MTR_CHNG_RDG1() {
        return KEY_MTR_CHNG_RDG1;
    }

    public void setKEY_MTR_CHNG_RDG1(double KEY_MTR_CHNG_RDG1) {
        this.KEY_MTR_CHNG_RDG1 = KEY_MTR_CHNG_RDG1;
    }

    public String getKEY_MTR_CHNG_DT2() {
        return KEY_MTR_CHNG_DT2;
    }

    public void setKEY_MTR_CHNG_DT2(String KEY_MTR_CHNG_DT2) {
        this.KEY_MTR_CHNG_DT2 = KEY_MTR_CHNG_DT2;
    }

    public double getKEY_MTR_CHNG_RGD2() {
        return KEY_MTR_CHNG_RGD2;
    }

    public void setKEY_MTR_CHNG_RGD2(double KEY_MTR_CHNG_RGD2) {
        this.KEY_MTR_CHNG_RGD2 = KEY_MTR_CHNG_RGD2;
    }

    public double getKEY_FAC_RATE() {
        return KEY_FAC_RATE;
    }

    public void setKEY_FAC_RATE(double KEY_FAC_RATE) {
        this.KEY_FAC_RATE = KEY_FAC_RATE;
    }

    public double getKEY_DMD_ARREARS() {
        return KEY_DMD_ARREARS;
    }

    public void setKEY_DMD_ARREARS(double KEY_DMD_ARREARS) {
        this.KEY_DMD_ARREARS = KEY_DMD_ARREARS;
    }

    public double getKEY_INT_ARREARS() {
        return KEY_INT_ARREARS;
    }

    public void setKEY_INT_ARREARS(double KEY_INT_ARREARS) {
        this.KEY_INT_ARREARS = KEY_INT_ARREARS;
    }

    public double getKEY_TAX_ARREARS() {
        return KEY_TAX_ARREARS;
    }

    public void setKEY_TAX_ARREARS(double KEY_TAX_ARREARS) {
        this.KEY_TAX_ARREARS = KEY_TAX_ARREARS;
    }

    public double getKEY_DELAY_INTEREST() {
        return KEY_DELAY_INTEREST;
    }

    public void setKEY_DELAY_INTEREST(double KEY_DELAY_INTEREST) {
        this.KEY_DELAY_INTEREST = KEY_DELAY_INTEREST;
    }

    public double getKEY_AMT_PAID() {
        return KEY_AMT_PAID;
    }

    public void setKEY_AMT_PAID(double KEY_AMT_PAID) {
        this.KEY_AMT_PAID = KEY_AMT_PAID;
    }

    public String getKEY_PAID_DATE1() {
        return KEY_PAID_DATE1;
    }

    public void setKEY_PAID_DATE1(String KEY_PAID_DATE1) {
        this.KEY_PAID_DATE1 = KEY_PAID_DATE1;
    }

    public double getKEY_AMT_PAID2() {
        return KEY_AMT_PAID2;
    }

    public void setKEY_AMT_PAID2(double KEY_AMT_PAID2) {
        this.KEY_AMT_PAID2 = KEY_AMT_PAID2;
    }

    public String getKEY_PAID_DATE2() {
        return KEY_PAID_DATE2;
    }

    public void setKEY_PAID_DATE2(String KEY_PAID_DATE2) {
        this.KEY_PAID_DATE2 = KEY_PAID_DATE2;
    }

    public double getKEY_OTHERS() {
        return KEY_OTHERS;
    }

    public void setKEY_OTHERS(double KEY_OTHERS) {
        this.KEY_OTHERS = KEY_OTHERS;
    }

    public String getBILLPRINTED() {
        return BILLPRINTED;
    }

    public void setBILLPRINTED(String BILLPRINTED) {
        this.BILLPRINTED = BILLPRINTED;
    }

    public double getCAPRBTAMT() {
        return CAPRBTAMT;
    }

    public void setCAPRBTAMT(double CAPRBTAMT) {
        this.CAPRBTAMT = CAPRBTAMT;
    }

    public double getPREVIOUS_DEMAND1() {
        return PREVIOUS_DEMAND1;
    }

    public void setPREVIOUS_DEMAND1(double PREVIOUS_DEMAND1) {
        this.PREVIOUS_DEMAND1 = PREVIOUS_DEMAND1;
    }

    public double getPREVIOUS_DEMAND2() {
        return PREVIOUS_DEMAND2;
    }

    public void setPREVIOUS_DEMAND2(double PREVIOUS_DEMAND2) {
        this.PREVIOUS_DEMAND2 = PREVIOUS_DEMAND2;
    }

    public double getPREVIOUS_DEMAND3() {
        return PREVIOUS_DEMAND3;
    }

    public void setPREVIOUS_DEMAND3(double PREVIOUS_DEMAND3) {
        this.PREVIOUS_DEMAND3 = PREVIOUS_DEMAND3;
    }

    public String getBILLING_MODE() {
        return BILLING_MODE;
    }

    public void setBILLING_MODE(String BILLING_MODE) {
        this.BILLING_MODE = BILLING_MODE;
    }

    public double getMETER_CONST() {
        return METER_CONST;
    }

    public void setMETER_CONST(double METER_CONST) {
        this.METER_CONST = METER_CONST;
    }

    public double getAPPEAL_AMOUNT() {
        return APPEAL_AMOUNT;
    }

    public void setAPPEAL_AMOUNT(double APPEAL_AMOUNT) {
        this.APPEAL_AMOUNT = APPEAL_AMOUNT;
    }

    public double getINT_APPEAL_AMOUNT() {
        return INT_APPEAL_AMOUNT;
    }

    public void setINT_APPEAL_AMOUNT(double INT_APPEAL_AMOUNT) {
        this.INT_APPEAL_AMOUNT = INT_APPEAL_AMOUNT;
    }

    public double getKVAH() {
        return KVAH;
    }

    public void setKVAH(double KVAH) {
        this.KVAH = KVAH;
    }

    public String getINSTAL_TYP() {
        return INSTAL_TYP;
    }

    public void setINSTAL_TYP(String INSTAL_TYP) {
        this.INSTAL_TYP = INSTAL_TYP;
    }

    public double getHP_ROUND() {
        return HP_ROUND;
    }

    public void setHP_ROUND(double HP_ROUND) {
        this.HP_ROUND = HP_ROUND;
    }

    public double getKW_ROUND() {
        return KW_ROUND;
    }

    public void setKW_ROUND(double KW_ROUND) {
        this.KW_ROUND = KW_ROUND;
    }

    public String getPREVIOUS_BILL_DATE() {
        return PREVIOUS_BILL_DATE;
    }

    public void setPREVIOUS_BILL_DATE(String PREVIOUS_BILL_DATE) {
        this.PREVIOUS_BILL_DATE = PREVIOUS_BILL_DATE;
    }

    public double getCREDITBF() {
        return CREDITBF;
    }

    public void setCREDITBF(double CREDITBF) {
        this.CREDITBF = CREDITBF;
    }

    public double getDEBITBF() {
        return DEBITBF;
    }

    public void setDEBITBF(double DEBITBF) {
        this.DEBITBF = DEBITBF;
    }

    public String getDUE_DATE() {
        return DUE_DATE;
    }

    public void setDUE_DATE(String DUE_DATE) {
        this.DUE_DATE = DUE_DATE;
    }

    public String getIVRSID() {
        return IVRSID;
    }

    public void setIVRSID(String IVRSID) {
        this.IVRSID = IVRSID;
    }

    public double getPARTFRACTION() {
        return PARTFRACTION;
    }

    public void setPARTFRACTION(double PARTFRACTION) {
        this.PARTFRACTION = PARTFRACTION;
    }

    public int getNUMDL() {
        return NUMDL;
    }

    public void setNUMDL(int NUMDL) {
        this.NUMDL = NUMDL;
    }

    public String getCGEXEMPT_FLG() {
        return CGEXEMPT_FLG;
    }

    public void setCGEXEMPT_FLG(String CGEXEMPT_FLG) {
        this.CGEXEMPT_FLG = CGEXEMPT_FLG;
    }

    public String getDC_FLG() {
        return DC_FLG;
    }

    public void setDC_FLG(String DC_FLG) {
        this.DC_FLG = DC_FLG;
    }

    public double getINT_ARREARS2() {
        return INT_ARREARS2;
    }

    public void setINT_ARREARS2(double INT_ARREARS2) {
        this.INT_ARREARS2 = INT_ARREARS2;
    }

    public double getINT_TAX() {
        return INT_TAX;
    }

    public void setINT_TAX(double INT_TAX) {
        this.INT_TAX = INT_TAX;
    }

    public String getFIRST_RDG_FLG() {
        return FIRST_RDG_FLG;
    }

    public void setFIRST_RDG_FLG(String FIRST_RDG_FLG) {
        this.FIRST_RDG_FLG = FIRST_RDG_FLG;
    }

    public int getCREAPING_PERC() {
        return CREAPING_PERC;
    }

    public void setCREAPING_PERC(int CREAPING_PERC) {
        this.CREAPING_PERC = CREAPING_PERC;
    }

    public String getMNR_FLG() {
        return MNR_FLG;
    }

    public void setMNR_FLG(String MNR_FLG) {
        this.MNR_FLG = MNR_FLG;
    }

    public long getOLD_MTR_RDG() {
        return OLD_MTR_RDG;
    }

    public void setOLD_MTR_RDG(long OLD_MTR_RDG) {
        this.OLD_MTR_RDG = OLD_MTR_RDG;
    }

    public double getINT_ON_TAX() {
        return INT_ON_TAX;
    }

    public void setINT_ON_TAX(double INT_ON_TAX) {
        this.INT_ON_TAX = INT_ON_TAX;
    }

    public String getSUBDIV() {
        return SUBDIV;
    }

    public void setSUBDIV(String SUBDIV) {
        this.SUBDIV = SUBDIV;
    }

    public double getNO_TAX_COMP() {
        return NO_TAX_COMP;
    }

    public void setNO_TAX_COMP(double NO_TAX_COMP) {
        this.NO_TAX_COMP = NO_TAX_COMP;
    }

    public String getPREV_RDG_FLG() {
        return PREV_RDG_FLG;
    }

    public void setPREV_RDG_FLG(String PREV_RDG_FLG) {
        this.PREV_RDG_FLG = PREV_RDG_FLG;
    }

    public String getPLFLAG() {
        return PLFLAG;
    }

    public void setPLFLAG(String PLFLAG) {
        this.PLFLAG = PLFLAG;
    }

    public double getPREVCKWH() {
        return PREVCKWH;
    }

    public void setPREVCKWH(double PREVCKWH) {
        this.PREVCKWH = PREVCKWH;
    }

    public double getREG_PENALTY() {
        return REG_PENALTY;
    }

    public void setREG_PENALTY(double REG_PENALTY) {
        this.REG_PENALTY = REG_PENALTY;
    }

    public String getPF_FLAG() {
        return PF_FLAG;
    }

    public void setPF_FLAG(String PF_FLAG) {
        this.PF_FLAG = PF_FLAG;
    }

    public int getLASTMONTHFRACTION() {
        return LASTMONTHFRACTION;
    }

    public void setLASTMONTHFRACTION(int LASTMONTHFRACTION) {
        this.LASTMONTHFRACTION = LASTMONTHFRACTION;
    }

    public int getCUR_QRTR() {
        return CUR_QRTR;
    }

    public void setCUR_QRTR(int CUR_QRTR) {
        this.CUR_QRTR = CUR_QRTR;
    }

    public int getFREQUENCY() {
        return FREQUENCY;
    }

    public void setFREQUENCY(int FREQUENCY) {
        this.FREQUENCY = FREQUENCY;
    }

    public double getANNUAL_MIN_FIX() {
        return ANNUAL_MIN_FIX;
    }

    public void setANNUAL_MIN_FIX(double ANNUAL_MIN_FIX) {
        this.ANNUAL_MIN_FIX = ANNUAL_MIN_FIX;
    }

    public double getCAPACITOR() {
        return CAPACITOR;
    }

    public void setCAPACITOR(double CAPACITOR) {
        this.CAPACITOR = CAPACITOR;
    }

    public String getCHEQDIS() {
        return CHEQDIS;
    }

    public void setCHEQDIS(String CHEQDIS) {
        this.CHEQDIS = CHEQDIS;
    }

    public double getORPHAN_RBT() {
        return ORPHAN_RBT;
    }

    public void setORPHAN_RBT(double ORPHAN_RBT) {
        this.ORPHAN_RBT = ORPHAN_RBT;
    }

    public double getMMD_CREDIT() {
        return MMD_CREDIT;
    }

    public void setMMD_CREDIT(double MMD_CREDIT) {
        this.MMD_CREDIT = MMD_CREDIT;
    }

    public String getMTR_CHNG_FLG() {
        return MTR_CHNG_FLG;
    }

    public void setMTR_CHNG_FLG(String MTR_CHNG_FLG) {
        this.MTR_CHNG_FLG = MTR_CHNG_FLG;
    }

    public double getADDNL3MMD() {
        return ADDNL3MMD;
    }

    public void setADDNL3MMD(double ADDNL3MMD) {
        this.ADDNL3MMD = ADDNL3MMD;
    }

    public int getTEMP_TARIFF_CODE() {
        return TEMP_TARIFF_CODE;
    }

    public void setTEMP_TARIFF_CODE(int TEMP_TARIFF_CODE) {
        this.TEMP_TARIFF_CODE = TEMP_TARIFF_CODE;
    }

    public double getLESSCLAIMED() {
        return LESSCLAIMED;
    }

    public void setLESSCLAIMED(double LESSCLAIMED) {
        this.LESSCLAIMED = LESSCLAIMED;
    }

    public double getMORECLAIMED() {
        return MORECLAIMED;
    }

    public void setMORECLAIMED(double MORECLAIMED) {
        this.MORECLAIMED = MORECLAIMED;
    }

    public int getPRESENT_RDG() {
        return PRESENT_RDG;
    }

    public void setPRESENT_RDG(int PRESENT_RDG) {
        this.PRESENT_RDG = PRESENT_RDG;
    }

    public long getN_UNITSCONSUMED() {
        return N_UNITSCONSUMED;
    }

    public void setN_UNITSCONSUMED(long n_UNITSCONSUMED) {
        N_UNITSCONSUMED = n_UNITSCONSUMED;
    }

    public double getN_TAX() {
        return N_TAX;
    }

    public void setN_TAX(double n_TAX) {
        N_TAX = n_TAX;
    }

    public double getTOTALBILL() {
        return TOTALBILL;
    }

    public void setTOTALBILL(double TOTALBILL) {
        this.TOTALBILL = TOTALBILL;
    }

    public double getN_REBATE() {
        return N_REBATE;
    }

    public void setN_REBATE(double n_REBATE) {
        N_REBATE = n_REBATE;
    }

    public double getTOTAL_FIXED_TARIFF() {
        return TOTAL_FIXED_TARIFF;
    }

    public void setTOTAL_FIXED_TARIFF(double TOTAL_FIXED_TARIFF) {
        this.TOTAL_FIXED_TARIFF = TOTAL_FIXED_TARIFF;
    }

    public double getP_NTOTAL_ENAERGY_TARIFF() {
        return P_NTOTAL_ENAERGY_TARIFF;
    }

    public void setP_NTOTAL_ENAERGY_TARIFF(double p_NTOTAL_ENAERGY_TARIFF) {
        P_NTOTAL_ENAERGY_TARIFF = p_NTOTAL_ENAERGY_TARIFF;
    }

    public double getCREDIT_CF() {
        return CREDIT_CF;
    }

    public void setCREDIT_CF(double CREDIT_CF) {
        this.CREDIT_CF = CREDIT_CF;
    }

    public String getMETER_STS() {
        return METER_STS;
    }

    public void setMETER_STS(String METER_STS) {
        this.METER_STS = METER_STS;
    }

    public double getLESS_MORE_CLAIMED() {
        return LESS_MORE_CLAIMED;
    }

    public void setLESS_MORE_CLAIMED(double LESS_MORE_CLAIMED) {
        this.LESS_MORE_CLAIMED = LESS_MORE_CLAIMED;
    }

    public double getTEMP_TAX() {
        return TEMP_TAX;
    }

    public void setTEMP_TAX(double TEMP_TAX) {
        this.TEMP_TAX = TEMP_TAX;
    }

    public double getDIFF_AMOUNT() {
        return DIFF_AMOUNT;
    }

    public void setDIFF_AMOUNT(double DIFF_AMOUNT) {
        this.DIFF_AMOUNT = DIFF_AMOUNT;
    }

    public double getPF_PENALTY() {
        return PF_PENALTY;
    }

    public void setPF_PENALTY(double PF_PENALTY) {
        this.PF_PENALTY = PF_PENALTY;
    }

    public double getPH_REBATE() {
        return PH_REBATE;
    }

    public void setPH_REBATE(double PH_REBATE) {
        this.PH_REBATE = PH_REBATE;
    }

    public double getPL_REBATE() {
        return PL_REBATE;
    }

    public void setPL_REBATE(double PL_REBATE) {
        this.PL_REBATE = PL_REBATE;
    }

    public String getFIRST_RDG_FLAG() {
        return FIRST_RDG_FLAG;
    }

    public void setFIRST_RDG_FLAG(String FIRST_RDG_FLAG) {
        this.FIRST_RDG_FLAG = FIRST_RDG_FLAG;
    }

    public String getFULL_MONTH_FLAG() {
        return FULL_MONTH_FLAG;
    }

    public void setFULL_MONTH_FLAG(String FULL_MONTH_FLAG) {
        this.FULL_MONTH_FLAG = FULL_MONTH_FLAG;
    }

    public double getORPHAN_AMOUNT() {
        return ORPHAN_AMOUNT;
    }

    public void setORPHAN_AMOUNT(double ORPHAN_AMOUNT) {
        this.ORPHAN_AMOUNT = ORPHAN_AMOUNT;
    }

    public double getRR_REBATE() {
        return RR_REBATE;
    }

    public void setRR_REBATE(double RR_REBATE) {
        this.RR_REBATE = RR_REBATE;
    }

    public double getLT4_DEBIT() {
        return LT4_DEBIT;
    }

    public void setLT4_DEBIT(double LT4_DEBIT) {
        this.LT4_DEBIT = LT4_DEBIT;
    }

    public int getPL_TEMP_TARIFF_CODE() {
        return PL_TEMP_TARIFF_CODE;
    }

    public void setPL_TEMP_TARIFF_CODE(int PL_TEMP_TARIFF_CODE) {
        this.PL_TEMP_TARIFF_CODE = PL_TEMP_TARIFF_CODE;
    }

    public double getENERGYAMOUNTPLUSTAX() {
        return ENERGYAMOUNTPLUSTAX;
    }

    public void setENERGYAMOUNTPLUSTAX(double ENERGYAMOUNTPLUSTAX) {
        this.ENERGYAMOUNTPLUSTAX = ENERGYAMOUNTPLUSTAX;
    }

    public String getUPLOADEDTOSERVER() {
        return UPLOADEDTOSERVER;
    }

    public void setUPLOADEDTOSERVER(String UPLOADEDTOSERVER) {
        this.UPLOADEDTOSERVER = UPLOADEDTOSERVER;
    }

    public byte[] getFIXED_OBJ() {
        return FIXED_OBJ;
    }

    public void setFIXED_OBJ(byte[] FIXED_OBJ) {
        this.FIXED_OBJ = FIXED_OBJ;
    }

    public byte[] getENERGY_OBJ() {
        return ENERGY_OBJ;
    }

    public void setENERGY_OBJ(byte[] ENERGY_OBJ) {
        this.ENERGY_OBJ = ENERGY_OBJ;
    }

    public double getPRSTCKWH() {
        return PRSTCKWH;
    }

    public void setPRSTCKWH(double PRSTCKWH) {
        this.PRSTCKWH = PRSTCKWH;
    }

    public String getNFC_CODE() {
        return NFC_CODE;
    }

    public void setNFC_CODE(String NFC_CODE) {
        this.NFC_CODE = NFC_CODE;
    }

    public String getNFC_TAG() {
        return NFC_TAG;
    }

    public void setNFC_TAG(String NFC_TAG) {
        this.NFC_TAG = NFC_TAG;
    }

    public byte[] getMETER_IMAGE() {
        return METER_IMAGE;
    }

    public void setMETER_IMAGE(byte[] METER_IMAGE) {
        this.METER_IMAGE = METER_IMAGE;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getCREATED_ON() {
        return CREATED_ON;
    }

    public void setCREATED_ON(String CREATED_ON) {
        this.CREATED_ON = CREATED_ON;
    }

    public String getUPDATED_BY() {
        return UPDATED_BY;
    }

    public void setUPDATED_BY(String UPDATED_BY) {
        this.UPDATED_BY = UPDATED_BY;
    }

    public String getUPDATED_ON() {
        return UPDATED_ON;
    }

    public void setUPDATED_ON(String UPDATED_ON) {
        this.UPDATED_ON = UPDATED_ON;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(KEY_INDEX);
        parcel.writeString(KEY_LOCATION);
        parcel.writeString(KEY_RR_NO);
        parcel.writeString(KEY_LEDGER_NO);
        parcel.writeString(KEY_ACTUAL_FOLIO_NO);
        parcel.writeString(KEY_SPOT_FOLIO_NO);
        parcel.writeInt(KEY_TARIFF_CODE);
        parcel.writeString(KEY_BILL_NO);
        parcel.writeString(KEY_BILL_DATE);
        parcel.writeString(KEY_CONSMR_NAME);
        parcel.writeString(KEY_ADDRESS1);
        parcel.writeString(KEY_ADDRESS2);
        parcel.writeString(KEY_ADDRESS3);
        parcel.writeString(KEY_BILLING_MONTH);
        parcel.writeString(KEY_READING_DATE);
        parcel.writeString(KEY_READER_CODE);
        parcel.writeInt(KEY_INSTALLATION_STATUS);
        parcel.writeDouble(KEY_LINE_MINIMUM);
        parcel.writeDouble(KEY_SANCT_HP);
        parcel.writeDouble(KEY_SANCT_KW);
        parcel.writeDouble(KEY_CT_PT);
        parcel.writeInt(KEY_PREV_MTR_RDG);
        parcel.writeDouble(KEY_AVG_CONSUMPTION);
        parcel.writeDouble(KEY_POWER_FACTOR);
        parcel.writeString(KEY_MTR_CHNG_DT1);
        parcel.writeDouble(KEY_MTR_CHNG_RDG1);
        parcel.writeString(KEY_MTR_CHNG_DT2);
        parcel.writeDouble(KEY_MTR_CHNG_RGD2);
        parcel.writeDouble(KEY_FAC_RATE);
        parcel.writeDouble(KEY_DMD_ARREARS);
        parcel.writeDouble(KEY_INT_ARREARS);
        parcel.writeDouble(KEY_TAX_ARREARS);
        parcel.writeDouble(KEY_DELAY_INTEREST);
        parcel.writeDouble(KEY_AMT_PAID);
        parcel.writeString(KEY_PAID_DATE1);
        parcel.writeDouble(KEY_AMT_PAID2);
        parcel.writeString(KEY_PAID_DATE2);
        parcel.writeDouble(KEY_OTHERS);
        parcel.writeString(BILLPRINTED);
        parcel.writeDouble(CAPRBTAMT);
        parcel.writeDouble(PREVIOUS_DEMAND1);
        parcel.writeDouble(PREVIOUS_DEMAND2);
        parcel.writeDouble(PREVIOUS_DEMAND3);
        parcel.writeString(BILLING_MODE);
        parcel.writeDouble(METER_CONST);
        parcel.writeDouble(APPEAL_AMOUNT);
        parcel.writeDouble(INT_APPEAL_AMOUNT);
        parcel.writeDouble(KVAH);
        parcel.writeString(INSTAL_TYP);
        parcel.writeDouble(HP_ROUND);
        parcel.writeDouble(KW_ROUND);
        parcel.writeString(PREVIOUS_BILL_DATE);
        parcel.writeDouble(CREDITBF);
        parcel.writeDouble(DEBITBF);
        parcel.writeString(DUE_DATE);
        parcel.writeString(IVRSID);
        parcel.writeDouble(PARTFRACTION);
        parcel.writeInt(NUMDL);
        parcel.writeString(CGEXEMPT_FLG);
        parcel.writeString(DC_FLG);
        parcel.writeDouble(INT_ARREARS2);
        parcel.writeDouble(INT_TAX);
        parcel.writeString(FIRST_RDG_FLG);
        parcel.writeInt(CREAPING_PERC);
        parcel.writeString(MNR_FLG);
        parcel.writeLong(OLD_MTR_RDG);
        parcel.writeDouble(INT_ON_TAX);
        parcel.writeString(SUBDIV);
        parcel.writeDouble(NO_TAX_COMP);
        parcel.writeString(PREV_RDG_FLG);
        parcel.writeString(PLFLAG);
        parcel.writeDouble(PREVCKWH);
        parcel.writeDouble(REG_PENALTY);
        parcel.writeString(PF_FLAG);
        parcel.writeInt(LASTMONTHFRACTION);
        parcel.writeInt(CUR_QRTR);
        parcel.writeInt(FREQUENCY);
        parcel.writeDouble(ANNUAL_MIN_FIX);
        parcel.writeDouble(CAPACITOR);
        parcel.writeString(CHEQDIS);
        parcel.writeDouble(ORPHAN_RBT);
        parcel.writeDouble(MMD_CREDIT);
        parcel.writeString(MTR_CHNG_FLG);
        parcel.writeDouble(ADDNL3MMD);
        parcel.writeInt(TEMP_TARIFF_CODE);
        parcel.writeDouble(LESSCLAIMED);
        parcel.writeDouble(MORECLAIMED);
        parcel.writeInt(PRESENT_RDG);
        parcel.writeLong(N_UNITSCONSUMED);
        parcel.writeDouble(N_TAX);
        parcel.writeDouble(TOTALBILL);
        parcel.writeDouble(N_REBATE);
        parcel.writeDouble(TOTAL_FIXED_TARIFF);
        parcel.writeDouble(P_NTOTAL_ENAERGY_TARIFF);
        parcel.writeDouble(CREDIT_CF);
        parcel.writeString(METER_STS);
        parcel.writeDouble(LESS_MORE_CLAIMED);
        parcel.writeDouble(TEMP_TAX);
        parcel.writeDouble(DIFF_AMOUNT);
        parcel.writeDouble(PF_PENALTY);
        parcel.writeDouble(PH_REBATE);
        parcel.writeDouble(PL_REBATE);
        parcel.writeString(FIRST_RDG_FLAG);
        parcel.writeString(FULL_MONTH_FLAG);
        parcel.writeDouble(ORPHAN_AMOUNT);
        parcel.writeDouble(RR_REBATE);
        parcel.writeDouble(LT4_DEBIT);
        parcel.writeInt(PL_TEMP_TARIFF_CODE);
        parcel.writeDouble(ENERGYAMOUNTPLUSTAX);
        parcel.writeString(UPLOADEDTOSERVER);
        parcel.writeByteArray(FIXED_OBJ);
        parcel.writeByteArray(ENERGY_OBJ);
        parcel.writeDouble(PRSTCKWH);
        parcel.writeString(NFC_CODE);
        parcel.writeString(NFC_TAG);
        parcel.writeByteArray(METER_IMAGE);
        parcel.writeDouble(LATITUDE);
        parcel.writeDouble(LONGITUDE);
        parcel.writeInt((int) FLG_Solar_rebate);
        parcel.writeInt((int) FLG_FL_rebate);
        parcel.writeInt((int) FLG_PH_rebate);
        parcel.writeInt((int) FLG_telep_rebate);
        parcel.writeInt((int) FLG_METER);
        parcel.writeInt((int) FLG_REBATE_CAP);
        parcel.writeInt((int) FLG_DEMAND_BASED);
        parcel.writeInt((int) FLG_RURAL_REBATE);
        parcel.writeInt((int) FLG_NORMAL);
        parcel.writeInt((int) FLG_METER_TVM);
        parcel.writeInt((int) FLG_ECS_USER);
        parcel.writeString(CREATED_BY);
        parcel.writeString(CREATED_ON);
        parcel.writeString(UPDATED_BY);
        parcel.writeString(UPDATED_ON);
        parcel.writeDouble(HP_Min_Fix);
        parcel.writeInt(tempKW_Round);
        parcel.writeDouble(temp_total);
    }
}
