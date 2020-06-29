package com.fcs.billingapp.constants;

/**
 * Created by Nadeem on 8/5/2017.
 */

public class TableScripts {

    public static String CREATE_TARIFF_MAIN_TABLE =
            "CREATE TABLE  "+DatabaseConstants.TABLE_NAME_TARIFF_MAIN+" ("+
                    "TARIFFCODE         INTEGER," +
                    "TARIFFTYPE         TEXT," +
                    "TARIFFPOWERUNIT    TEXT," +
                    "MINFIXED           INTEGER," +
                    "DCUNITS            INTEGER," +
                    "TARIFFTAX          INTEGER," +
                    "CONSTCHARGE        INTEGER" + ")";

    public static String CREATE_TARIFF_FIX_TABLE =
            "CREATE TABLE  "+DatabaseConstants.TABLE_NAME_TARIFF_FIX+" (" +
                    "TARIFFCODE         INTEGER," +
                    "ITEM               TEXT,"+
                    "CHARGETYPE         TEXT," +
                    "FIX_FROM           INTEGER," +
                    "FIX_TO             INTEGER," +
                    "FIX_AMOUNT      INTEGER" + ")";

    public static String CREATE_TARIFF_SLAB_TABLE =
            "CREATE TABLE  "+DatabaseConstants.TABLE_NAME_TARIFF_SLAB+" (" +
                    "TARIFFCODE     INTEGER," +
                    "ITEM           TEXT,"+
                    "FROMUNITS      INTEGER," +
                    "TOUNITS        INTEGER," +
                    "SLAB_AMOUNT    INTEGER" + ")";

    public static String CREATE_TARIFF_REBATE_TABLE =
            "CREATE TABLE  "+DatabaseConstants.TABLE_NAME_TARIFF_REBATE+" (" +
                    "REBATECODE     INTEGER," +
                    "CHTYPE         TEXT,"+
                    "REBATE         INTEGER," +
                    "MAXREBATE      INTEGER," +
                    "REBATETYPE     TEXT" + ")";

    public static String CREATE_FLAG_MASTER_TABLE =
            "CREATE TABLE  "+DatabaseConstants.TABLE_NAME_FLAGS_MASTER+" (" +
                    "FLG_LOCATION       TEXT," +
                    "FLG_RR_NO 		    TEXT,"+
                    "FLG_BILL_DATE	    TEXT," +
                    "FLG_Mr_Code		TEXT," +
                    "FLG_Solar_rebate   TEXT," +
                    "FLG_FL_rebate	    TEXT," +
                    "FLG_PH_rebate	    TEXT," +
                    "FLG_telep_rebate   TEXT," +
                    "FLG_BILL_GEN_FLAG  TEXT," +
                    "FLG_METER		    TEXT," +
                    "FLG_REBATE_CAP	    TEXT," +
                    "FLG_DEMAND_BASED   TEXT," +
                    "FLG_RURAL_REBATE   TEXT," +
                    "FLG_NORMAL		    TEXT," +
                    "FLG_METER_TVM	    TEXT," +
                    "FLG_ECS_USER	    TEXT)";


    public static String CREATE_DTC_TABLE = "CREATE TABLE "+DatabaseConstants.TABLE_NAME_DTC_MASTER + "  "
            +"(  TDR_LOC_CD      TEXT," +
            "    TDR_FDR_NO      TEXT," +
            "    TDR_TC_NO       TEXT," +
            "    TDR_TC_NAME     TEXT," +
            "    TDR_RDG         REAL," +
            "    TDR_BKWH_UNITS  REAL," +
            "    TDR_BILL_DT     TEXT," +
            "    TDR_MTR_CD      TEXT," +
            "    TDR_USERID      TEXT," +
            "    TDR_TMPSTP      TEXT," +
            "    TDR_RDG_STS     TEXT) ";



    public static String CREATE_BILLING_MASTER_TABLE = "CREATE TABLE "+DatabaseConstants.TABLE_NAME_BILLING_MASTER +
                "  ( " +
                    " KEY_INDEX                  INTEGER  ," +
                    " KEY_LOCATION               TEXT,"+
                    " KEY_RR_NO                  TEXT,"+
                    " KEY_LEDGER_NO              TEXT,"+
                    " KEY_ACTUAL_FOLIO_NO        TEXT,"+
                    " KEY_SPOT_FOLIO_NO          TEXT,"+
                    " KEY_TARIFF_CODE            INTEGER,"+
                    " KEY_BILL_NO                TEXT,"+
                    " KEY_BILL_DATE              TEXT,"+
                    " KEY_CONSMR_NAME            TEXT,"+
                    " KEY_ADDRESS1               TEXT,"+
                    " KEY_ADDRESS2               TEXT,"+
                    " KEY_ADDRESS3               TEXT,"+
                    " KEY_BILLING_MONTH          TEXT,"+
                    " KEY_READING_DATE           TEXT,"+
                    " KEY_READER_CODE            TEXT,"+
                    " KEY_INSTALLATION_STATUS    INTEGER,"+
                    " KEY_LINE_MINIMUM           REAL,"+
                    " KEY_SANCT_HP               REAL,"+
                    " KEY_SANCT_KW               REAL,"+
                    " KEY_CT_PT                  REAL,"+
                    " KEY_PREV_MTR_RDG           INTEGER,"+
                    " KEY_AVG_CONSUMPTION        REAL,"+
                    " KEY_POWER_FACTOR           REAL,"+
                    " KEY_MTR_CHNG_DT1           TEXT,"+
                    " KEY_MTR_CHNG_RDG1          REAL,"+
                    " KEY_MTR_CHNG_DT2           TEXT,"+
                    " KEY_MTR_CHNG_RGD2          REAL,"+
                    " KEY_FAC_RATE               REAL,"+
                    " KEY_DMD_ARREARS            REAL,"+
                    " KEY_INT_ARREARS            REAL,"+
                    " KEY_TAX_ARREARS            REAL,"+
                    " KEY_DELAY_INTEREST         REAL,"+
                    " KEY_AMT_PAID               REAL,"+
                    " KEY_PAID_DATE1             TEXT,"+
                    " KEY_AMT_PAID2              REAL,"+
                    " KEY_PAID_DATE2             TEXT,"+
                    " KEY_OTHERS                 REAL,"+
                    " BILLPRINTED                TEXT,"+
                    " CAPRBTAMT                  REAL,"+
                    " PREVIOUS_DEMAND1           REAL,"+
                    " PREVIOUS_DEMAND2           REAL,"+
                    " PREVIOUS_DEMAND3           REAL,"+
                    " BILLING_MODE               TEXT,"+
                    " METER_CONST                REAL,"+
                    " APPEAL_AMOUNT              REAL,"+
                    " INT_APPEAL_AMOUNT          REAL,"+
                    " KVAH                       REAL,"+
                    " INSTAL_TYP                 TEXT,"+
                    " HP_ROUND                   REAL,"+
                    " KW_ROUND                   REAL,"+
                    " PREVIOUS_BILL_DATE         TEXT,"+
                    " CREDITBF                   REAL,"+
                    " DEBITBF                    REAL,"+
                    " DUE_DATE                   TEXT,"+
                    " IVRSID                     TEXT,"+
                    " PARTFRACTION               REAL,"+
                    " NUMDL                      INTEGER,"+
                    " CGEXEMPT_FLG               TEXT,"+
                    " DC_FLG                     TEXT,"+
                    " INT_ARREARS2               REAL,"+
                    " INT_TAX                    REAL,"+
                    " FIRST_RDG_FLG              TEXT,"+
                    " CREAPING_PERC              INTEGER,"+
                    " MNR_FLG                    TEXT,"+
                    " OLD_MTR_RDG                INTEGER,"+
                    " INT_ON_TAX                 REAL,"+
                    " SUBDIV                     TEXT,"+
                    " NO_TAX_COMP                REAL,"+
                    " PREV_RDG_FLG               TEXT,"+
                    " PLFLAG                     TEXT,"+
                    " PREVCKWH	                 REAL,"+
                    " REG_PENALTY                REAL,"+
                    " PF_FLAG                    TEXT,"+
                    " LASTMONTHFRACTION          INTEGER,"+
                    " CUR_QRTR                   INTEGER,"+
                    " FREQUENCY                  INTEGER,"+
                    " ANNUAL_MIN_FIX             REAL,"+
                    " CAPACITOR                  REAL,"+
                    " CHEQDIS                    TEXT,"+
                    " ORPHAN_RBT                 REAL,"+
                    " MMD_CREDIT                 REAL,"+
                    " MTR_CHNG_FLG               TEXT,"+
                    " ADDNL3MMD                  REAL,"+
                    " TEMP_TARIFF_CODE           INTEGER,"+
                    " LESSCLAIMED                REAL,"+
                    " MORECLAIMED                REAL,"+
                    " PRESENT_RDG                INTEGER,"+
                    " N_UNITSCONSUMED            INTEGER,"+
                    " N_TAX                      REAL,"+
                    " TOTALBILL                  REAL,"+
                    " N_REBATE                   REAL,"+
                    " TOTAL_FIXED_TARIFF         REAL,"+
                    " P_NTOTAL_ENAERGY_TARIFF    REAL,"+
                    " CREDIT_CF                  REAL,"+
                    " METER_STS                  TEXT,"+
                    " LESS_MORE_CLAIMED          REAL,"+
                    " TEMP_TAX                   REAL,"+
                    " DIFF_AMOUNT                REAL,"+
                    " PF_PENALTY                 REAL,"+
                    " PH_REBATE                  REAL,"+
                    " PL_REBATE                  REAL,"+
                    " FIRST_RDG_FLAG             TEXT,"+
                    " FULL_MONTH_FLAG            TEXT,"+
                    " ORPHAN_AMOUNT              REAL,"+
                    " RR_REBATE                  REAL,"+
                    " LT4_DEBIT                  REAL,"+
                    " PL_TEMP_TARIFF_CODE        INTEGER," +
                    " ENERGYAMOUNTPLUSTAX        REAL," +
                    " UPLOADEDTOSERVER           TEXT,   " +
                    " FIXED_OBJ                  blob,   " +
                    " ENERGY_OBJ                 blob,   " +
                    " PRSTCKWH	                 REAL , "+
                    " NFC_CODE	                 TEXT ,  "+   //This field Added to Store NFC Code
                    " NFC_TAG	                 TEXT , "+   //This field Added to Store NFC TAG Exists Or Not ( Y/N )
                    " METER_IMAGE	             blob , "+ // to store image
                    " LATITUDE	                 REAL ,  "+   //to store position in latitude
                    " LONGITUDE	                 REAL , "+   //to store position in longitude
                    "FLG_Solar_rebate            TEXT," +
                    "FLG_FL_rebate	             TEXT," +
                    "FLG_PH_rebate	             TEXT," +
                    "FLG_telep_rebate            TEXT," +
                    "FLG_METER		             TEXT," +
                    "FLG_REBATE_CAP	             TEXT," +
                    "FLG_DEMAND_BASED            TEXT," +
                    "FLG_RURAL_REBATE            TEXT," +
                    "FLG_NORMAL		             TEXT," +
                    "FLG_METER_TVM	             TEXT," +
                    "FLG_ECS_USER	             TEXT," +
                    " CREATED_BY                 TEXT, " +
                    " CREATED_ON                 TEXT, " +
                    " UPDATED_BY                 TEXT, " +
                    " UPDATED_ON                 TEXT, " +
                    " PRIMARY KEY(KEY_LOCATION,KEY_RR_NO)"+
                    ")" ;


    public static String CREATE_IMAGE_MASTER_TABLE = "CREATE TABLE "+DatabaseConstants.TABLE_NAME_IMAGE_MASTER + "  "
            +"(  IM_LOC_CD       TEXT, " +
            "    IM_RR_NO        TEXT, " +
            "    IM_IMAGE        TEXT, " +
            "    IM_CREATED_BY   TEXT, " +
            "    IM_CREATED_ON   TEXT, " +
            "    IM_UPDATED_BY   TEXT, " +
            "    IM_UPDATED_ON   TEXT, " +
            "    PRIMARY KEY(IM_LOC_CD,IM_RR_NO)" +
            " ) ";

    public static String CREATE_ITEM_MASTER_TABLE =
            "CREATE TABLE  "+DatabaseConstants.TABLE_ITEM_MASTER_DETAILS+" ("+
                    "KEY_INDEX         INTEGER," +
                    "DESCRIPTION_ENGLISH         TEXT," +
                    "DESCRIPTION_OTHER    TEXT"
                    + ")";

}
