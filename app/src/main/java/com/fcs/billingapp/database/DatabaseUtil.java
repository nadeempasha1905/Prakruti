package com.fcs.billingapp.database;

import android.annotation.SuppressLint;
import android.util.Log;

import com.fcs.billingapp.constants.DatabaseConstants;
import com.fcs.billingapp.constants.TariffConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nadeem on 8/7/2017.
 */

public class DatabaseUtil {

    @SuppressLint("LongLogTag")
    public ArrayList<String> getTariffMainStatementList(JSONArray TARIFF_MAIN){

        ArrayList<String> al = new ArrayList<>();

        for (int j = 0; j < TARIFF_MAIN.length(); j++) {


            String insert_query = "";
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) TARIFF_MAIN.get(j);
                //Log.d("value",(String) jsonObject.get("value"));
                //Log.d("description",(String) jsonObject.get("description"));
                //Log.d("type",(String) jsonObject.get("type"));

                insert_query = " INSERT INTO " + DatabaseConstants.TABLE_NAME_TARIFF_MAIN +
                        " (TARIFFCODE ,TARIFFTYPE ,TARIFFPOWERUNIT,MINFIXED,DCUNITS,TARIFFTAX,CONSTCHARGE ) " +
                        "  VALUES" +
                        " ('" + (String) jsonObject.get("TM_TRF_CODE") + "'," +
                        " '"  + (String) jsonObject.get("TM_TRF_TYPE") + "'," +
                        " '"  + (String) jsonObject.get("TM_TRF_PWR_UNIT") + "'," +
                        " '"  + (String) jsonObject.get("TM_MIN_FXD") + "'," +
                        " '"  + (String) jsonObject.get("TM_DC_UNTS") + "'," +
                        " '"  + (String) jsonObject.get("TM_TRF_TAX") + "'," +
                        " '"  + (String) jsonObject.get("TM_CONST_CHRGS") + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            al.add(insert_query);
        }
        Log.d("getTariffMainStatementList : ", "" + al.size());
        return al;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<String> getTariffFixStatementList(JSONArray TARIFF_FIX) {
        ArrayList<String> al = new ArrayList<>();

        for (int j = 0; j < TARIFF_FIX.length(); j++) {


            String insert_query = "";
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) TARIFF_FIX.get(j);

                insert_query = " INSERT INTO " + DatabaseConstants.TABLE_NAME_TARIFF_FIX +
                        " (TARIFFCODE ,ITEM ,CHARGETYPE,FIX_FROM,FIX_TO,FIX_AMOUNT ) " +
                        "  VALUES" +
                        " ('" + (String) jsonObject.get("TF_TRF_CODE") + "'," +
                        " '"  + (String) jsonObject.get("TF_CHARGE_TYP") + "'," +
                        " '"  + (String) jsonObject.get("TF_ITEM") + "'," +
                        " '"  + (String) jsonObject.get("TF_FROM_UNITS") + "'," +
                        " '"  + (String) jsonObject.get("TF_TO_UNITS") + "'," +
                        " '"  + (String) jsonObject.get("TF_TRF_AMT") + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            al.add(insert_query);
        }
        Log.d("getTariffFixStatementList : ", "" + al.size());
        return al;
    }

    public ArrayList<String> getTariffRebateStatementList(JSONArray TARIFF_REBATE) {

        ArrayList<String> al = new ArrayList<>();

        for (int j = 0; j < TARIFF_REBATE.length(); j++) {


            String insert_query = "";
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) TARIFF_REBATE.get(j);

                insert_query = " INSERT INTO " + DatabaseConstants.TABLE_NAME_TARIFF_REBATE +
                        " (REBATECODE ,CHTYPE ,REBATE,MAXREBATE,REBATETYPE ) " +
                        "  VALUES" +
                        " ('" + (String) jsonObject.get("TR_RBT_CODE") + "'," +
                        " '"  + (String) jsonObject.get("TR_CH_TYPE") + "'," +
                        " '"  + (String) jsonObject.get("TR_RBT") + "'," +
                        " '"  + (String) jsonObject.get("TR_MAX_RBT") + "'," +
                        " '"  + (String) jsonObject.get("TR_RBT_TYP") + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            al.add(insert_query);
        }
        Log.d("getTariffRebateStatementList : ", "" + al.size());
        return al;
    }

    public ArrayList<String> getTariffSlabStatementList(JSONArray TARIFF_SLAB) {

        ArrayList<String> al = new ArrayList<>();

        for (int j = 0; j < TARIFF_SLAB.length(); j++) {


            String insert_query = "";
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) TARIFF_SLAB.get(j);

                insert_query = " INSERT INTO " + DatabaseConstants.TABLE_NAME_TARIFF_SLAB +
                        " (TARIFFCODE ,ITEM ,FROMUNITS,TOUNITS,SLAB_AMOUNT ) " +
                        "  VALUES" +
                        " ('" + (String) jsonObject.get("TS_TRF_CODE") + "'," +
                        " '"  + (String) jsonObject.get("TS_ITEM") + "'," +
                        " '"  + (String) jsonObject.get("TS_FROM_UNITS") + "'," +
                        " '"  + (String) jsonObject.get("TS_TO_UNITS") + "'," +
                        " '"  + (String) jsonObject.get("TS_TRF_AMT") + "')";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            al.add(insert_query);
        }
        Log.d("getTariffSlabStatementList : ", "" + al.size());
        return al;
    }

    public ArrayList<String> getConsumerDataStatementList(JSONArray CONSUMER_DATA) {

        ArrayList<String> al = new ArrayList<>();
        int INDEX = 0;

        for (int j = 0; j < CONSUMER_DATA.length(); j++) {


            String insert_query = "";
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) CONSUMER_DATA.get(j);

                String FL_Rebate = "";
                int Tariff_Code = 0;
                int Set_Tariff_Code = 0;
                int Temp_Tariff_Code = 0;
                int Temp_PLTariff_Code = 0 ;

                Set_Tariff_Code  =  Integer.parseInt((String) jsonObject.get("HDD_TRF_CODE"));
                Tariff_Code =  Integer.parseInt((String) jsonObject.get("HDD_TRF_CODE"));

                if(((String) jsonObject.get("HDD_FL_REBATE")).equals("Y")){
                    FL_Rebate = "Y";
                }else{
                    FL_Rebate = "N";
                }

                if(FL_Rebate.equals("Y")){

                    Temp_Tariff_Code = Tariff_Code ;

                    if(Tariff_Code == TariffConstants.LT2AI){
                        Set_Tariff_Code = TariffConstants.LTFL1;
                    }else if(Tariff_Code == TariffConstants.LT2AII){
                        Set_Tariff_Code = TariffConstants.LTFL2;
                    }else if(Tariff_Code == TariffConstants.LT2AIII){
                        Set_Tariff_Code = TariffConstants.LTFL3;
                    }
                }else{
                    Temp_Tariff_Code = Tariff_Code ;
                }

                if(((String) jsonObject.get("HDD_PL_FLG")).equals("Y")){
                    Temp_PLTariff_Code = Tariff_Code;
                    Set_Tariff_Code    = TariffConstants.LTPL;
                }else{
                    Temp_PLTariff_Code = Tariff_Code;
                }


                insert_query = " INSERT INTO " + DatabaseConstants.TABLE_NAME_BILLING_MASTER +
                        " (" +
                        //  1           2               3               4           5               6
                        "  KEY_INDEX ,KEY_LOCATION ,KEY_RR_NO,KEY_LEDGER_NO,KEY_ACTUAL_FOLIO_NO,KEY_SPOT_FOLIO_NO," +
                        //          7           8               9           10              11          12          13
                        "  KEY_TARIFF_CODE, KEY_BILL_NO,KEY_BILL_DATE,KEY_CONSMR_NAME,KEY_ADDRESS1,KEY_ADDRESS2,KEY_ADDRESS3," +
                        //          14          15                  16              17                      18
                        "  KEY_BILLING_MONTH,KEY_READING_DATE,KEY_READER_CODE,KEY_INSTALLATION_STATUS,KEY_LINE_MINIMUM," +
                        //  19              20          21          22                  23              24
                        "  KEY_SANCT_HP,KEY_SANCT_KW,KEY_CT_PT,KEY_PREV_MTR_RDG,KEY_AVG_CONSUMPTION,KEY_POWER_FACTOR," +
                        //      25                  26                  27              28              29
                        "  KEY_MTR_CHNG_DT1,KEY_MTR_CHNG_RDG1,KEY_MTR_CHNG_DT2,KEY_MTR_CHNG_RGD2,KEY_FAC_RATE," +
                        //          30          31          32                      33              34          35
                        "  KEY_DMD_ARREARS,KEY_INT_ARREARS,KEY_TAX_ARREARS,KEY_DELAY_INTEREST,KEY_AMT_PAID,KEY_PAID_DATE1," +
                        //      36             37         38          39          40         41              42
                        "  KEY_AMT_PAID2,KEY_PAID_DATE2,KEY_OTHERS,BILLPRINTED,CAPRBTAMT,PREVIOUS_DEMAND1,PREVIOUS_DEMAND2, " +
                        //          43           44          45             46            47         48         49
                        "  PREVIOUS_DEMAND3,BILLING_MODE,METER_CONST,APPEAL_AMOUNT,INT_APPEAL_AMOUNT,KVAH,INSTAL_TYP," +
                        //      50          51              52      53      54      55          56      57      58
                        "  HP_ROUND,KW_ROUND,PREVIOUS_BILL_DATE,CREDITBF,DEBITBF,DUE_DATE,IVRSID,PARTFRACTION,NUMDL," +
                        //     59        60          61       62             63         64       65         66
                        "  CGEXEMPT_FLG,DC_FLG,INT_ARREARS2,INT_TAX,FIRST_RDG_FLG,CREAPING_PERC,MNR_FLG,OLD_MTR_RDG," +
                        //     67         68       69       70          71      72         73      74          75
                        "  INT_ON_TAX,SUBDIV,NO_TAX_COMP,PREV_RDG_FLG,PLFLAG,PREVCKWH,REG_PENALTY,PF_FLAG,LASTMONTHFRACTION," +
                        //     76       77           78         79       80         81       82         83          84
                        "  CUR_QRTR,FREQUENCY,ANNUAL_MIN_FIX,CAPACITOR,CHEQDIS,ORPHAN_RBT,MMD_CREDIT,MTR_CHNG_FLG,ADDNL3MMD," +
                        //              85       86          87          88            89        90        91      92
                        "  TEMP_TARIFF_CODE,LESSCLAIMED,MORECLAIMED,PRESENT_RDG,N_UNITSCONSUMED,N_TAX,TOTALBILL,N_REBATE," +
                        //      93                      94                 95      96                97         98
                        "  TOTAL_FIXED_TARIFF,P_NTOTAL_ENAERGY_TARIFF,CREDIT_CF,METER_STS,LESS_MORE_CLAIMED,TEMP_TAX," +
                        //     99           100        101      102       103                 104         105
                        "  DIFF_AMOUNT,PF_PENALTY,PH_REBATE,PL_REBATE,FIRST_RDG_FLAG,FULL_MONTH_FLAG,ORPHAN_AMOUNT," +
                        //     106      107        108                      109               110
                        "  RR_REBATE,LT4_DEBIT,PL_TEMP_TARIFF_CODE,ENERGYAMOUNTPLUSTAX,UPLOADEDTOSERVER," +
                        //      111     112        113     114      115      116        117      118
                        "  FIXED_OBJ,ENERGY_OBJ,PRSTCKWH,NFC_CODE,NFC_TAG,METER_IMAGE,LATITUDE,LONGITUDE ," +
                        //      119             120             121         122             123             124
                        "FLG_Solar_rebate,FLG_FL_rebate,FLG_PH_rebate,FLG_telep_rebate,FLG_METER,FLG_REBATE_CAP," +
                        //      125             126             126         128         129
                        "FLG_DEMAND_BASED,FLG_RURAL_REBATE,FLG_NORMAL,FLG_METER_TVM,FLG_ECS_USER" +
                        " ) " +
                        "  VALUES" +
                        " ("  + (INDEX++)+"," +                                                 //1
                        " '"  + (String) jsonObject.get("HDD_LOC_CD") + "'," +                  //2
                        " '"  + (String) jsonObject.get("HDD_RR_NO") + "'," +                   //3
                        " '"  + (String) jsonObject.get("HDD_LDGR_NO") + "'," +                 //4
                        " '"  + (String) jsonObject.get("HDD_ACTL_FOLIO_NO") + "'," +           //5
                        " '"  + (String) jsonObject.get("HDD_SPT_FOLIO_NO") + "'," +            //6
                        "  "  + Set_Tariff_Code + "," +                                         //7
                        " '"  + (String) jsonObject.get("HDD_BILL_NUM") + "'," +                //8
                        " '"  + (String) jsonObject.get("HDD_BILL_DT") + "'," +                 //9
                        " '"  + (String) jsonObject.get("HDD_CONSMR_NAME") + "'," +             //10
                        " '"  + (String) jsonObject.get("HDD_ADDR1") + "'," +                   //11
                        " '"  + (String) jsonObject.get("HDD_ADDR2") + "'," +                   //12
                        " '"  + (String) jsonObject.get("HDD_ADDR3") + "'," +                   //13
                        " '"  + (String) jsonObject.get("HDD_BILLING_MONTH") + "'," +           //14
                        " '"  + (String) jsonObject.get("HDD_RDG_DT") + "'," +                  //15
                        " '"  + (String) jsonObject.get("HDD_MTR_RDR_CD") + "'," +              //16
                        "  "  + Integer.parseInt((String) jsonObject.get("HDD_INSTAL_STS")) + " ," +              //17
                        "  "  + Double.parseDouble((String) jsonObject.get("HDD_LINE_MIN")) + "," +               //18
                        "  "  + Double.parseDouble((String) jsonObject.get("HDD_SANCT_HP")) + "," +               //19
                        "  "  + Double.parseDouble((String) jsonObject.get("HDD_SANCT_KW")) + "," +               //20
                        "  "  + Double.parseDouble((String) jsonObject.get("HDD_CT_RATIO")) + "," +               //21
                        "  "  + Long.parseLong((String) jsonObject.get("HDD_PREV_RDG")) + "," +                   //22
                        "  "  + Math.round(Double.parseDouble((String) jsonObject.get("HDD_AVG_CONSMP"))) + "," + //23
                        "  "  + Double.parseDouble((String) jsonObject.get("HDD_PWR_FACTOR")) + "," +             //24
                        " '"  + (String) jsonObject.get("HDD_MTRCHNG_DT1") + "'," +        //25
                        "  "  + Double.parseDouble((String) jsonObject.get("HDD_MTRCHNG_RDG1")) + "," +           //26
                        " '"  + (String) jsonObject.get("HDD_MTRCHN_GDT2") + "', " +         //27
                        " '"  + Double.parseDouble((String) jsonObject.get("HDD_MTRCHN_GRDG2")) + "'," +          //28
                        " ''," +                                                                                  //29
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_DEMAND_ARREARS")) + "," +          //30
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_INT_ARRS_3MMD_DEP_INT")) + "," +   //31
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_TAX_ARREARS")) + "," +             //32
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_DELAY_INT")) + "," +               //33
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_AMT_PAID1")) + "," +               //34
                        " '" + (String) jsonObject.get("HDD_PAID_DATE1") + "', " +          //35
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_AMT_PAID2")) + "," +               //36
                        " '" + (String) jsonObject.get("HDD_PAID_DATE2") + "', " +          //37
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_OTHERS")) + "," +                  //38
                        " '" + (String) jsonObject.get("HDD_BILL_GEN_FLAG") + "'," +                             //39
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_REBATE_CAP")) + "," +              //40
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_PREVIOUS_DEMAND1")) + "," +        //41
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_PREVIOUS_DEMAND2")) + "," +        //42
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_PREVIOUS_DEMAND3")) + "," +        //43
                        " '" + (String) jsonObject.get("HDD_BILLING_MODE") + "'," +                              //44
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_MTR_CONST")) + "," +               //45
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_APPEAL_AMOUNT")) + "," +           //46
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_INT_ON_APPEAL_AMT")) + "," +       //47
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_KVAH")) + "," +                    //48
                        " '" + (String) jsonObject.get("HDD_INST_TYP") + "'," +                                  //49
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_SANCT_HP")) + "," +                //50
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_SANCT_KW")) + "," +                //51
                        " '" + (String) jsonObject.get("HDD_BILL_DT") + "', " +             //52
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_P_CREDIT")) + "," +                //53
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_P_DEBIT")) + "," +                 //54
                        " '" + (String) jsonObject.get("HDD_DUE_DATE") + "'," +                //55
                        " '" + (String) jsonObject.get("HDD_IVRS_ID") + "'," +                 //56
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_PART_PERIOD")) + "," +             //57
                        " "  + Integer.parseInt((String) jsonObject.get("HDD_DLMNR")) + "," +                   //58
                        " '" + (String) jsonObject.get("HDD_TAX_FLG") + "'," +                 //59
                        " '" + (String) jsonObject.get("HDD_DC_FLG") + "'," +                  //60
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_INTRST_ARRS_HDD_DELAY_ARRS")) + "," +//61
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_INTRST_TAX")) + "," +              //62
                        " '" + (String) jsonObject.get("HDD_FIRST_RDG_FLG") + "'," +           //63
                        " "  + Integer.parseInt((String) jsonObject.get("HDD_ERR_PRCNT")) + "," +               //64
                        " '" + (String) jsonObject.get("HDD_MNR_FLG") + "'," +                 //65
                        " "  + Integer.parseInt((String) jsonObject.get("HDD_OLD_MTR_CONSMP")) + "," +          //66
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_INT_ON_TAX")) + "," +              //67
                        " '" + (String) jsonObject.get("HDD_SUBDIV_NAME") + "'," +             //68
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_NO_TAX_COMP")) + "," +             //69
                        " '" + (String) jsonObject.get("HDD_PREV_RDG_FLG") + "'," +            //70
                        " '" + (String) jsonObject.get("HDD_PL_FLG") + "'," +                  //71
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_PREV_CKWH")) + "," +               //72
                        " "  + Double.parseDouble( (String) jsonObject.get("HDD_REG_PENALTY")) + "," +             //73
                        " '" + (String) jsonObject.get("HDD_CALC_PF_FLG") + "'," +             //74
                        " '" + (String) jsonObject.get("HDD_LAST_DL_DAYS") + "'," +            //75
                        " '" + (String) jsonObject.get("HDD_CUR_QRTR") + "'," +                //76
                        " '" + (String) jsonObject.get("HDD_FREQUENCY") + "'," +               //77
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_ANNUAL_MIN_FIX")) + "," +          //78
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_CAPACITY_RBT")) + "," +            //79
                        " '" + (String) jsonObject.get("HDD_CHQ_DIS_FLG") + "'," +             //80
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_ORPHANAGE_REBATE")) + "," +        //81
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_DEP_INT")) + "," +                  //82
                        " '" + (String) jsonObject.get("HDD_MC_FLG") + "'," +                  //83
                        " "  + Double.parseDouble((String) jsonObject.get("HDD_3MMD_DEP")) + "," +                //84
                        "  " + Temp_Tariff_Code+ ","                          +                //85
                        "  " + ((double)0.0)+ ","                          +                   //86
                        "  " + ((double)0.0)+ ","                          +                   //87
                        "  " + ((long)0.0)+ ","                            +                   //88
                        "  " + ((long)0.0)+ ","                            +                   //89
                        "  " + ((double)0.0)+ ","                          +                   //90
                        "  " + ((double)0.0)+ ","                          +                   //91
                        "  " + ((double)0.0)+ ","                          +                   //92
                        "  " + ((double)0.0)+ ","                          +                   //93
                        "  " + ((double)0.0)+ ","                          +                   //94
                        "  " + ((double)0.0)+ ","                          +                   //95
                        " '" + (((String) jsonObject.get("HDD_MNR_FLG")).equals("Y") ? "Y" : "N" ) + "'," +//96
                        "  " + ((double)0.0)+ ","                          +                   //97
                        "  " + ((double)0.0)+ ","                          +                   //98
                        "  " + ((double)0.0)+ ","                          +                   //99
                        "  " + ((double)0.0)+ ","                          +                   //100
                        "  " + ((double)0.0)+ ","                          +                   //101
                        "  " + ((double)0.0)+ ","                          +                   //102
                        " 'N',"                                            +                   //103
                        " 'N',"                                            +                   //104
                        "  " + ((double)0.0)+ ","                          +                   //105
                        "  " + ((double)0.0)+ ","                          +                   //106
                        "  " + ((double)0.0)+ ","                          +                   //107
                        "  " + Temp_PLTariff_Code+ ","                     +              //108
                        "  " + ((double)0.0)+ ","                          +                   //109
                        " 'N',"                                            +                   //110
                        " NULL,"                                           +                  //111
                        " NULL,"                                           +                  //112
                        "  " + ((double)0.0)+ ","                          +                   //113
                        " '" + (String) jsonObject.get("HDD_NFC_CODE") + "'," +                //114
                        " '" + (String) jsonObject.get("HDD_NFC_TAG") + "'," +                 //115
                        " NULL,"                                           +                  //116
                        "  " + ((double)0.0)+ ","                          +                   //117
                        "  " + ((double)0.0) + "," +                                           //118

                        //FLAGS
                        " '" + (((String) jsonObject.get("HDD_SOLAR_REBATE")).equals("Y") ? "1" : "0" )+ "'," + //119
                        " '" + ((FL_Rebate).equals("Y") ? "1" : "0" ) + "'," +                                  //120
                        " '" + (((String) jsonObject.get("HDD_PH_REBATE")).equals("Y") ? "1" : "0" ) + "'," +   //121
                        " '" + (((String) jsonObject.get("HDD_TELEP_REBATE")).equals("Y") ? "1" : "0" ) + "'," +//122
                        " '0'," +//123
                        " '0'," +//124
                        " '0'," +//125
                        " '" + (((String) jsonObject.get("HDD_RURAL_REBATE")).equals("Y") ? "1" : "0" ) + "'," +//126
                        " '0'," +//127
                        " '0'," +//128
                        " '" + (((String) jsonObject.get("HDD_ECS_FLG")).equals("Y") ? "1" : "0" ) + "'" +//129
                        ")";


            } catch (JSONException e) {
                e.printStackTrace();
            }
            al.add(insert_query);
        }
        Log.d("getConsumerDataStatementList : ", "" + al.size());
        return al;
    }
}
