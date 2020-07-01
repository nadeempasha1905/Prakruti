package com.prakruthi.billingapp.constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nadeem on 8/8/2017.
 */

public class TariffConstants {

    public static String     VER             = "BV3.32";
    public static double     LTPFPENAMT      = 0.02 ;       //Power factor penalty amount
    public static double     POWERFACTLMT    = 0.85;        //Power factor limit
    public static int        FL_Units_Limit  = 200;         //Free lighting consumer limit
    public static int        PHREBATEPERCENT = 20;          //Physically handicaped rebate percentage

    public static int        LT1A            = 111;         // old tariff code 01
    public static int     	 LT2AI           = 121;
    public static int     	 LT2AII          = 122;
    public static int     	 LT2AIII         = 123;         // old tariff code 05
    public static int        LTPL            = 159;         // same as old one 120
    public static int        LT2B            = 124;         // old tariff code 06
    public static int        LT2BI           = 124;         // old tariff code 86
    public static int        LT2BII          = 125;         // old tariff code 87
    public static int        LT3             = 131;         // old tariff code 10
    public static int        LT3I            = 131;         // old tariff code 88
    public static int        LT3II           = 132;         // old tariff code 89
    public static int        LTFL1           = 1;
    public static int        LTFL2           = 2;
    public static int        LTFL3           = 3;
    public static int        LT4A1           = 141;
    public static int        LT4A2           = 143;
    public static int        LT4B            = 144;
    public static int        LT4CI           = 145;
    public static int        LT4CII          = 146;

    JSONArray TARIFF_CONSTANTS = new JSONArray();

    public TariffConstants(){
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject();jsonObject.put("tariffcode","111");jsonObject.put("tariffvalue","LT1A");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","121");jsonObject.put("tariffvalue","LT2AI");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","122");jsonObject.put("tariffvalue","LT2AII");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","123");jsonObject.put("tariffvalue","LT2AIII");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","124");jsonObject.put("tariffvalue","LT2BI");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","125");jsonObject.put("tariffvalue","LT2BII");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","131");jsonObject.put("tariffvalue","LT3I");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","132");jsonObject.put("tariffvalue","LT3II");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","1");jsonObject.put("tariffvalue","LTFL1");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","2");jsonObject.put("tariffvalue","LTFL2");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","3");jsonObject.put("tariffvalue","LTFL3");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","141");jsonObject.put("tariffvalue","LT4AI");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","143");jsonObject.put("tariffvalue","LT4A2");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","144");jsonObject.put("tariffvalue","LT4B");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","145");jsonObject.put("tariffvalue","LT4CI");TARIFF_CONSTANTS.put(jsonObject);
            jsonObject = new JSONObject();jsonObject.put("tariffcode","146");jsonObject.put("tariffvalue","LT4CII");TARIFF_CONSTANTS.put(jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTariffDescription(String tariffcode){

        String tariff_descr = "";
        try {
            for(int i = 0; i < TARIFF_CONSTANTS.length();i++){
                    JSONObject jsonObject = (JSONObject) TARIFF_CONSTANTS.get(i);

                    if(jsonObject.get("tariffcode").equals(tariffcode)){
                        tariff_descr = (String) jsonObject.get("tariffvalue");
                    }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tariff_descr;
    }
}
