package com.prakruthi.billingapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prakruthi.billingapp.bean.BillingMasterBO;
import com.prakruthi.billingapp.bean.EnergyChargeListBO;
import com.prakruthi.billingapp.bean.FixedChargeListBO;
import com.prakruthi.billingapp.bean.TariffFixBO;
import com.prakruthi.billingapp.bean.TariffMainBO;
import com.prakruthi.billingapp.bean.TariffRebateBO;
import com.prakruthi.billingapp.bean.TariffSlabBO;
import com.prakruthi.billingapp.constants.Constants;
import com.prakruthi.billingapp.constants.DatabaseConstants;
import com.prakruthi.billingapp.constants.TableScripts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Nadeem on 8/6/2017.
 */

public class DatabaseImplementation extends SQLiteOpenHelper{

    Context context ;
    private static DatabaseImplementation sInstance;
    private static SQLiteDatabase  db;



    public static synchronized DatabaseImplementation getInstance (Context context){

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseImplementation(context.getApplicationContext());

        }
        Log.d("-->","Instance Created");
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */

    private DatabaseImplementation(Context context)
    {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABSE_VERSION);
        // TODO Auto-generated constructor stub
        Log.d("["+this.getClass().getSimpleName()+"]-->","Constructor 1...");


    }


    public DatabaseImplementation(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseImplementation(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @SuppressLint("LongLogTag")
    public boolean CreateDatabaseTables(){

        try{

            db = this.getWritableDatabase();

            db.execSQL(TableScripts.CREATE_ITEM_MASTER_TABLE);
            Log.d("["+DatabaseConstants.TABLE_ITEM_MASTER_DETAILS+"]-->","Created...");

         /*   db.execSQL(TableScripts.CREATE_TARIFF_MAIN_TABLE);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_MAIN+"]-->","Created...");

            db.execSQL(TableScripts.CREATE_TARIFF_FIX_TABLE);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_FIX+"]-->","Created...");

            db.execSQL(TableScripts.CREATE_TARIFF_REBATE_TABLE);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_REBATE+"]-->","Created...");

            db.execSQL(TableScripts.CREATE_TARIFF_SLAB_TABLE);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_SLAB+"]-->","Created...");

            db.execSQL(TableScripts.CREATE_BILLING_MASTER_TABLE);
            Log.d("["+DatabaseConstants.TABLE_NAME_BILLING_MASTER+"]-->","Created...");

            db.execSQL(TableScripts.CREATE_DTC_TABLE);
            Log.d("["+DatabaseConstants.TABLE_NAME_DTC_MASTER+"]-->","Created...");
*/


        }catch (Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->"," Exception : "+e);
        }

        return true;
    }

    @SuppressLint("LongLogTag")
    public void DropDatabaseTables(){

        db = this.getWritableDatabase();

        try {

            db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_ITEM_MASTER_DETAILS);
            Log.d("["+DatabaseConstants.TABLE_ITEM_MASTER_DETAILS+"]-->"," Dropped...");

            /*db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_NAME_TARIFF_MAIN);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_MAIN+"]-->"," Dropped...");
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_NAME_TARIFF_FIX);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_FIX+"]-->"," Dropped...");
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_NAME_TARIFF_REBATE);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_REBATE+"]-->"," Dropped...");
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_NAME_TARIFF_SLAB);
            Log.d("["+DatabaseConstants.TABLE_NAME_TARIFF_SLAB+"]-->"," Dropped...");
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_NAME_BILLING_MASTER);
            Log.d("["+DatabaseConstants.TABLE_NAME_BILLING_MASTER+"]-->"," Dropped...");
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseConstants.TABLE_NAME_DTC_MASTER);
            Log.d("["+DatabaseConstants.TABLE_NAME_DTC_MASTER+"]-->"," Dropped...");*/

        }catch (Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->DropDatabaseTables : "," Exception : "+e);
        }
    }

    public int InsertRecord(String record){
        db = this.getWritableDatabase();
        int Count = 0;

        try{
            db = getWritableDatabase();

            db.execSQL(record);
            Count++ ;
        }catch(Exception e){
            Count = 0;
            Log.d("["+this.getClass().getSimpleName()+"]-->","ERROR : "+e.toString());
        }

        return Count;
    }

    public BillingMasterBO GetConsumerRecordByIndex(int INDEX, int NAVIGATION, String AUTO_RRNO){

        BillingMasterBO billingMasterBO = null;
        String Query = "";

        try{



        db = this.getWritableDatabase();



        if(NAVIGATION == Constants.ALL
                ||
                NAVIGATION == Constants.NEXT
                    ||
                    NAVIGATION == Constants.FIRST
                        ||
                        NAVIGATION == Constants.PREVIOUS
                            ||
                            NAVIGATION == Constants.LAST
                ){
            Query = "SELECT * FROM "+DatabaseConstants.TABLE_NAME_BILLING_MASTER+ " LIMIT  "+INDEX+" , 1 ;  ";

        }else if(NAVIGATION == Constants.RRNO){

            Query = "SELECT * FROM "+DatabaseConstants.TABLE_NAME_BILLING_MASTER+ " WHERE  KEY_RR_NO = '"+AUTO_RRNO+"' ;  ";
        }

        Cursor recordset = db.rawQuery(Query,null);

        if(recordset.moveToNext()){

            billingMasterBO = new BillingMasterBO();

            billingMasterBO.setKEY_INDEX(recordset.getInt(recordset.getColumnIndex("KEY_INDEX")));
            billingMasterBO.setKEY_LOCATION(recordset.getString(recordset.getColumnIndex("KEY_LOCATION")));
            billingMasterBO.setKEY_RR_NO(recordset.getString(recordset.getColumnIndex("KEY_RR_NO")));
            billingMasterBO.setKEY_LEDGER_NO(recordset.getString(recordset.getColumnIndex("KEY_LEDGER_NO")));
            billingMasterBO.setKEY_ACTUAL_FOLIO_NO(recordset.getString(recordset.getColumnIndex("KEY_ACTUAL_FOLIO_NO")));
            billingMasterBO.setKEY_SPOT_FOLIO_NO(recordset.getString(recordset.getColumnIndex("KEY_SPOT_FOLIO_NO")));
            billingMasterBO.setKEY_TARIFF_CODE(recordset.getInt(recordset.getColumnIndex("KEY_TARIFF_CODE")));
            billingMasterBO.setKEY_BILL_NO(recordset.getString(recordset.getColumnIndex("KEY_BILL_NO")));
            billingMasterBO.setKEY_BILL_DATE(recordset.getString(recordset.getColumnIndex("KEY_BILL_DATE")));
            billingMasterBO.setKEY_CONSMR_NAME(recordset.getString(recordset.getColumnIndex("KEY_CONSMR_NAME")));
            billingMasterBO.setKEY_ADDRESS1(recordset.getString(recordset.getColumnIndex("KEY_ADDRESS1")));
            billingMasterBO.setKEY_ADDRESS2(recordset.getString(recordset.getColumnIndex("KEY_ADDRESS2")));
            billingMasterBO.setKEY_ADDRESS3(recordset.getString(recordset.getColumnIndex("KEY_ADDRESS3")));
            billingMasterBO.setKEY_BILLING_MONTH(recordset.getString(recordset.getColumnIndex("KEY_BILLING_MONTH")));
            billingMasterBO.setKEY_READING_DATE(recordset.getString(recordset.getColumnIndex("KEY_READING_DATE")));
            billingMasterBO.setKEY_READER_CODE(recordset.getString(recordset.getColumnIndex("KEY_READER_CODE")));
            billingMasterBO.setKEY_INSTALLATION_STATUS(recordset.getInt(recordset.getColumnIndex("KEY_INSTALLATION_STATUS")));
            billingMasterBO.setKEY_LINE_MINIMUM(recordset.getDouble(recordset.getColumnIndex("KEY_LINE_MINIMUM")));
            billingMasterBO.setKEY_SANCT_HP(recordset.getDouble(recordset.getColumnIndex("KEY_SANCT_HP")));
            billingMasterBO.setKEY_SANCT_KW(recordset.getDouble(recordset.getColumnIndex("KEY_SANCT_KW")));
            billingMasterBO.setKEY_CT_PT(recordset.getDouble(recordset.getColumnIndex("KEY_CT_PT")));
            billingMasterBO.setKEY_PREV_MTR_RDG(recordset.getInt(recordset.getColumnIndex("KEY_PREV_MTR_RDG")));
            billingMasterBO.setKEY_AVG_CONSUMPTION(recordset.getDouble(recordset.getColumnIndex("KEY_AVG_CONSUMPTION")));
            billingMasterBO.setKEY_POWER_FACTOR(recordset.getDouble(recordset.getColumnIndex("KEY_POWER_FACTOR")));
            billingMasterBO.setKEY_MTR_CHNG_DT1(recordset.getString(recordset.getColumnIndex("KEY_MTR_CHNG_DT1")));
            billingMasterBO.setKEY_MTR_CHNG_RDG1(recordset.getDouble(recordset.getColumnIndex("KEY_MTR_CHNG_RDG1")));
            billingMasterBO.setKEY_MTR_CHNG_DT2(recordset.getString(recordset.getColumnIndex("KEY_MTR_CHNG_DT2")));
            billingMasterBO.setKEY_MTR_CHNG_RGD2(recordset.getDouble(recordset.getColumnIndex("KEY_MTR_CHNG_RGD2")));
            billingMasterBO.setKEY_FAC_RATE(recordset.getDouble(recordset.getColumnIndex("KEY_FAC_RATE")));
            billingMasterBO.setKEY_DMD_ARREARS(recordset.getDouble(recordset.getColumnIndex("KEY_DMD_ARREARS")));
            billingMasterBO.setKEY_INT_ARREARS(recordset.getDouble(recordset.getColumnIndex("KEY_INT_ARREARS")));
            billingMasterBO.setKEY_TAX_ARREARS(recordset.getDouble(recordset.getColumnIndex("KEY_TAX_ARREARS")));
            billingMasterBO.setKEY_DELAY_INTEREST(recordset.getDouble(recordset.getColumnIndex("KEY_DELAY_INTEREST")));
            billingMasterBO.setKEY_AMT_PAID(recordset.getDouble(recordset.getColumnIndex("KEY_AMT_PAID")));
            billingMasterBO.setKEY_PAID_DATE1(recordset.getString(recordset.getColumnIndex("KEY_PAID_DATE1")));
            billingMasterBO.setKEY_AMT_PAID2(recordset.getDouble(recordset.getColumnIndex("KEY_AMT_PAID2")));
            billingMasterBO.setKEY_PAID_DATE2(recordset.getString(recordset.getColumnIndex("KEY_PAID_DATE2")));
            billingMasterBO.setKEY_OTHERS(recordset.getDouble(recordset.getColumnIndex("KEY_OTHERS")));
            billingMasterBO.setBILLPRINTED(recordset.getString(recordset.getColumnIndex("BILLPRINTED")));
            billingMasterBO.setCAPRBTAMT(recordset.getDouble(recordset.getColumnIndex("CAPRBTAMT")));
            billingMasterBO.setPREVIOUS_DEMAND1(recordset.getDouble(recordset.getColumnIndex("PREVIOUS_DEMAND1")));
            billingMasterBO.setPREVIOUS_DEMAND2(recordset.getDouble(recordset.getColumnIndex("PREVIOUS_DEMAND2")));
            billingMasterBO.setPREVIOUS_DEMAND3(recordset.getDouble(recordset.getColumnIndex("PREVIOUS_DEMAND3")));
            billingMasterBO.setBILLING_MODE(recordset.getString(recordset.getColumnIndex("BILLING_MODE")));
            billingMasterBO.setMETER_CONST(recordset.getDouble(recordset.getColumnIndex("METER_CONST")));
            billingMasterBO.setAPPEAL_AMOUNT(recordset.getDouble(recordset.getColumnIndex("APPEAL_AMOUNT")));
            billingMasterBO.setINT_APPEAL_AMOUNT(recordset.getDouble(recordset.getColumnIndex("INT_APPEAL_AMOUNT")));
            billingMasterBO.setKVAH(recordset.getDouble(recordset.getColumnIndex("KVAH")));
            billingMasterBO.setINSTAL_TYP(recordset.getString(recordset.getColumnIndex("INSTAL_TYP")));
            billingMasterBO.setHP_ROUND(recordset.getDouble(recordset.getColumnIndex("HP_ROUND")));
            billingMasterBO.setKW_ROUND(recordset.getDouble(recordset.getColumnIndex("KW_ROUND")));
            billingMasterBO.setPREVIOUS_BILL_DATE(recordset.getString(recordset.getColumnIndex("PREVIOUS_BILL_DATE")));
            billingMasterBO.setCREDITBF(recordset.getDouble(recordset.getColumnIndex("CREDITBF")));
            billingMasterBO.setDEBITBF(recordset.getDouble(recordset.getColumnIndex("DEBITBF")));
            billingMasterBO.setDUE_DATE(recordset.getString(recordset.getColumnIndex("DUE_DATE")));
            billingMasterBO.setIVRSID(recordset.getString(recordset.getColumnIndex("IVRSID")));
            billingMasterBO.setPARTFRACTION(recordset.getDouble(recordset.getColumnIndex("PARTFRACTION")));
            billingMasterBO.setNUMDL(recordset.getInt(recordset.getColumnIndex("NUMDL")));
            billingMasterBO.setCGEXEMPT_FLG(recordset.getString(recordset.getColumnIndex("CGEXEMPT_FLG")));
            billingMasterBO.setDC_FLG(recordset.getString(recordset.getColumnIndex("DC_FLG")));
            billingMasterBO.setINT_TAX(recordset.getDouble(recordset.getColumnIndex("INT_TAX")));
            billingMasterBO.setFIRST_RDG_FLG(recordset.getString(recordset.getColumnIndex("FIRST_RDG_FLG")));
            billingMasterBO.setCREAPING_PERC(recordset.getInt(recordset.getColumnIndex("CREAPING_PERC")));
            billingMasterBO.setMNR_FLG(recordset.getString(recordset.getColumnIndex("MNR_FLG")));
            billingMasterBO.setOLD_MTR_RDG(recordset.getInt(recordset.getColumnIndex("OLD_MTR_RDG")));
            billingMasterBO.setINT_ON_TAX(recordset.getDouble(recordset.getColumnIndex("INT_ON_TAX")));
            billingMasterBO.setSUBDIV(recordset.getString(recordset.getColumnIndex("SUBDIV")));
            billingMasterBO.setNO_TAX_COMP(recordset.getDouble(recordset.getColumnIndex("NO_TAX_COMP")));
            billingMasterBO.setPREV_RDG_FLG(recordset.getString(recordset.getColumnIndex("PREV_RDG_FLG")));
            billingMasterBO.setPLFLAG(recordset.getString(recordset.getColumnIndex("PLFLAG")));
            billingMasterBO.setPREVCKWH(recordset.getDouble(recordset.getColumnIndex("PREVCKWH")));
            billingMasterBO.setREG_PENALTY(recordset.getDouble(recordset.getColumnIndex("REG_PENALTY")));
            billingMasterBO.setPF_FLAG(recordset.getString(recordset.getColumnIndex("PF_FLAG")));
            billingMasterBO.setLASTMONTHFRACTION(recordset.getInt(recordset.getColumnIndex("LASTMONTHFRACTION")));
            billingMasterBO.setCUR_QRTR(recordset.getInt(recordset.getColumnIndex("CUR_QRTR")));
            billingMasterBO.setFREQUENCY(recordset.getInt(recordset.getColumnIndex("FREQUENCY")));
            billingMasterBO.setANNUAL_MIN_FIX(recordset.getDouble(recordset.getColumnIndex("ANNUAL_MIN_FIX")));
            billingMasterBO.setCAPACITOR(recordset.getDouble(recordset.getColumnIndex("CAPACITOR")));
            billingMasterBO.setCHEQDIS(recordset.getString(recordset.getColumnIndex("CHEQDIS")));
            billingMasterBO.setORPHAN_RBT(recordset.getDouble(recordset.getColumnIndex("ORPHAN_RBT")));
            billingMasterBO.setMMD_CREDIT(recordset.getDouble(recordset.getColumnIndex("MMD_CREDIT")));
            billingMasterBO.setMTR_CHNG_FLG(recordset.getString(recordset.getColumnIndex("MTR_CHNG_FLG")));
            billingMasterBO.setADDNL3MMD(recordset.getDouble(recordset.getColumnIndex("ADDNL3MMD")));
            billingMasterBO.setTEMP_TARIFF_CODE(recordset.getInt(recordset.getColumnIndex("TEMP_TARIFF_CODE")));
            billingMasterBO.setLESSCLAIMED(recordset.getDouble(recordset.getColumnIndex("LESSCLAIMED")));
            billingMasterBO.setMORECLAIMED(recordset.getDouble(recordset.getColumnIndex("MORECLAIMED")));
            billingMasterBO.setPRESENT_RDG(recordset.getInt(recordset.getColumnIndex("PRESENT_RDG")));
            billingMasterBO.setN_UNITSCONSUMED(recordset.getInt(recordset.getColumnIndex("N_UNITSCONSUMED")));
            billingMasterBO.setN_TAX(recordset.getDouble(recordset.getColumnIndex("N_TAX")));
            billingMasterBO.setTOTALBILL(recordset.getDouble(recordset.getColumnIndex("TOTALBILL")));
            billingMasterBO.setN_REBATE(recordset.getDouble(recordset.getColumnIndex("N_REBATE")));
            billingMasterBO.setTOTAL_FIXED_TARIFF(recordset.getDouble(recordset.getColumnIndex("TOTAL_FIXED_TARIFF")));
            billingMasterBO.setP_NTOTAL_ENAERGY_TARIFF(recordset.getDouble(recordset.getColumnIndex("P_NTOTAL_ENAERGY_TARIFF")));
            billingMasterBO.setCREDIT_CF(recordset.getDouble(recordset.getColumnIndex("CREDIT_CF")));
            billingMasterBO.setMETER_STS(recordset.getString(recordset.getColumnIndex("METER_STS")));
            billingMasterBO.setLESS_MORE_CLAIMED(recordset.getDouble(recordset.getColumnIndex("LESS_MORE_CLAIMED")));
            billingMasterBO.setTEMP_TAX(recordset.getDouble(recordset.getColumnIndex("TEMP_TAX")));
            billingMasterBO.setDIFF_AMOUNT(recordset.getDouble(recordset.getColumnIndex("DIFF_AMOUNT")));
            billingMasterBO.setPF_PENALTY(recordset.getDouble(recordset.getColumnIndex("PF_PENALTY")));
            billingMasterBO.setPH_REBATE(recordset.getDouble(recordset.getColumnIndex("PH_REBATE")));
            billingMasterBO.setPL_REBATE(recordset.getDouble(recordset.getColumnIndex("PL_REBATE")));
            billingMasterBO.setFIRST_RDG_FLAG(recordset.getString(recordset.getColumnIndex("FIRST_RDG_FLAG")));
            billingMasterBO.setFULL_MONTH_FLAG(recordset.getString(recordset.getColumnIndex("FULL_MONTH_FLAG")));
            billingMasterBO.setORPHAN_AMOUNT(recordset.getDouble(recordset.getColumnIndex("ORPHAN_AMOUNT")));
            billingMasterBO.setRR_REBATE(recordset.getDouble(recordset.getColumnIndex("RR_REBATE")));
            billingMasterBO.setLT4_DEBIT(recordset.getDouble(recordset.getColumnIndex("LT4_DEBIT")));
            billingMasterBO.setPL_TEMP_TARIFF_CODE(recordset.getInt(recordset.getColumnIndex("PL_TEMP_TARIFF_CODE")));
            billingMasterBO.setENERGYAMOUNTPLUSTAX(recordset.getDouble(recordset.getColumnIndex("ENERGYAMOUNTPLUSTAX")));
            billingMasterBO.setUPLOADEDTOSERVER(recordset.getString(recordset.getColumnIndex("UPLOADEDTOSERVER")));
            billingMasterBO.setPRSTCKWH(recordset.getDouble(recordset.getColumnIndex("PRSTCKWH")));
            billingMasterBO.setLATITUDE(recordset.getDouble(recordset.getColumnIndex("LATITUDE")));
            billingMasterBO.setLONGITUDE(recordset.getDouble(recordset.getColumnIndex("LONGITUDE")));
            billingMasterBO.setNFC_CODE(recordset.getString(recordset.getColumnIndex("NFC_CODE")));
            billingMasterBO.setNFC_TAG(recordset.getString(recordset.getColumnIndex("NFC_TAG")));
            /*billingMasterBO.setCREATED_BY(recordset.getString(recordset.getColumnIndex("CREATED_BY")));
            billingMasterBO.setCREATED_ON(recordset.getString(recordset.getColumnIndex("CREATED_ON")));
            billingMasterBO.setUPDATED_BY(recordset.getString(recordset.getColumnIndex("UPDATED_BY")));
            billingMasterBO.setUPDATED_ON(recordset.getString(recordset.getColumnIndex("UPDATED_ON")));*/

            //FLAGS....................
            billingMasterBO.setFLG_Solar_rebate(recordset.getString(recordset.getColumnIndex("FLG_Solar_rebate")).charAt(0));
            billingMasterBO.setFLG_FL_rebate(recordset.getString(recordset.getColumnIndex("FLG_FL_rebate")).charAt(0));
            billingMasterBO.setFLG_PH_rebate(recordset.getString(recordset.getColumnIndex("FLG_PH_rebate")).charAt(0));
            billingMasterBO.setFLG_telep_rebate(recordset.getString(recordset.getColumnIndex("FLG_telep_rebate")).charAt(0));
            billingMasterBO.setFLG_METER(recordset.getString(recordset.getColumnIndex("FLG_METER")).charAt(0));
            billingMasterBO.setFLG_REBATE_CAP(recordset.getString(recordset.getColumnIndex("FLG_REBATE_CAP")).charAt(0));
            billingMasterBO.setFLG_DEMAND_BASED(recordset.getString(recordset.getColumnIndex("FLG_DEMAND_BASED")).charAt(0));
            billingMasterBO.setFLG_RURAL_REBATE(recordset.getString(recordset.getColumnIndex("FLG_RURAL_REBATE")).charAt(0));
            billingMasterBO.setFLG_METER_TVM(recordset.getString(recordset.getColumnIndex("FLG_METER_TVM")).charAt(0));
            billingMasterBO.setFLG_ECS_USER(recordset.getString(recordset.getColumnIndex("FLG_ECS_USER")).charAt(0));
            billingMasterBO.setFLG_NORMAL(recordset.getString(recordset.getColumnIndex("FLG_NORMAL")).charAt(0));


            try{
                ArrayList<FixedChargeListBO> fixed = new ArrayList<FixedChargeListBO>(); Log.d("check","1");
                byte[] b = recordset.getBlob(recordset.getColumnIndex("FIXED_OBJ")); Log.d("check","2");
                if(b != null){
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(b)); Log.d("check","3");
                    fixed = (ArrayList<FixedChargeListBO>) ois.readObject(); Log.d("check","4");
                }
                billingMasterBO.setFixedChargeListBOs(fixed); Log.d("check","5");

                ArrayList<EnergyChargeListBO> energy = new ArrayList<EnergyChargeListBO>(); Log.d("check","6");
                byte[] b1 = recordset.getBlob(recordset.getColumnIndex("ENERGY_OBJ")); Log.d("check","7");
                if(b1 != null){
                    ObjectInputStream ois1 = new ObjectInputStream(new ByteArrayInputStream(b1)); Log.d("check","8");
                    energy = (ArrayList<EnergyChargeListBO>) ois1.readObject(); Log.d("check","9");
                }
                billingMasterBO.setEnergyChargeListBOs(energy); Log.d("check","10");

            }catch(Exception e){
                Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured : "+e.toString());
            }

            billingMasterBO.setMETER_IMAGE(null);
        }

        if(recordset != null){
            recordset.close();
        }



        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Ocured in  getUploadRecordCount: "+e.toString());
        }
        return  billingMasterBO;
    }



    public String[] getRRNOforAutoComnplete() {
        // TODO Auto-generated method stub

        db = this.getWritableDatabase();

        ArrayList<String> al = new ArrayList<String>();
        String[] rrno_value = null;

        try {
            String Query = "SELECT KEY_RR_NO FROM " + DatabaseConstants.TABLE_NAME_BILLING_MASTER + ";  ";

            Cursor recordset = db.rawQuery(Query, null);
            while (recordset.moveToNext()) {
                al.add(recordset.getString(0));
            }

            rrno_value = new String[al.size()];

            for (int j = 0; j < al.size(); j++) {
                rrno_value[j] = al.get(j);
            }

            if (recordset != null) {
                recordset.close();
            }
            db.close();
        } catch (Exception e) {
            Log.d("[" + this.getClass().getSimpleName() + "]-->", "Error Occured in getRRNOforAutoComnplete : " + e.toString());
        }
        return rrno_value;
    }

    public ArrayList<TariffSlabBO> getEnergySlabs() {

        db = this.getWritableDatabase();

        ArrayList<TariffSlabBO> al = null;

        try{
            al =  new ArrayList<TariffSlabBO>();

            String Query = " SELECT * FROM " +DatabaseConstants.TABLE_NAME_TARIFF_SLAB+ " ;";

            Cursor energy = db.rawQuery(Query, null);


            while(energy.moveToNext()){

                TariffSlabBO slb = new TariffSlabBO();
                //Log.d("["+this.getClass().getSimpleName()+"]-->","Query = "+Query);
                slb.setTariffCode(energy.getInt(energy.getColumnIndex("TARIFFCODE")));
                slb.setItem(energy.getString(energy.getColumnIndex("ITEM")).charAt(0));
                slb.setFromUnits(energy.getInt(energy.getColumnIndex("FROMUNITS")));
                slb.setToUnits(energy.getInt(energy.getColumnIndex("TOUNITS")));
                slb.setTariff(energy.getInt(energy.getColumnIndex("SLAB_AMOUNT")));

                al.add(slb);
                //Log.d("["+this.getClass().getSimpleName()+"]-->","TariffCode : "+energy.getInt(energy.getColumnIndex("TariffCode")));
            }
            energy.close();

            db.close();
        }
        catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in getEnergySlabs: "+e.toString());
        }finally{
            //Log.d("["+this.getClass().getSimpleName()+"]-->","In finally TariffSlabBO size = "+al.size());
        }
        //Log.d("["+this.getClass().getSimpleName()+"]-->","TariffSlabBO size = "+al.size());
        return al;
    }

    public ArrayList<TariffMainBO> getTariffMain() {

        db = this.getWritableDatabase();

        ArrayList<TariffMainBO> al =  new ArrayList<TariffMainBO>();

        try{

            String Query = " SELECT * FROM " +DatabaseConstants.TABLE_NAME_TARIFF_MAIN+ " ;";

            Cursor energy = db.rawQuery(Query, null);

            while(energy.moveToNext()){

                TariffMainBO slb = new TariffMainBO();

                slb.setTariffCode(energy.getInt(energy.getColumnIndex("TARIFFCODE")));
                slb.setTariffType(energy.getString(energy.getColumnIndex("TARIFFTYPE")));
                slb.setTariffPowerUnit(energy.getString(energy.getColumnIndex("TARIFFPOWERUNIT")));
                slb.setMinFixed(energy.getInt(energy.getColumnIndex("MINFIXED")));
                slb.setDCUnits(energy.getLong(energy.getColumnIndex("DCUNITS")));
                slb.setTariffTax(energy.getInt(energy.getColumnIndex("TARIFFTAX")));
                slb.setConstCharge(energy.getInt(energy.getColumnIndex("CONSTCHARGE")));

                al.add(slb);
            }

            if(energy != null){
                energy.close();
            }

            db.close();
        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in getTariffMain : "+e.toString());
        }

        return al;

    }

    public ArrayList<TariffFixBO> getTariffFix() {

        db = this.getWritableDatabase();

        ArrayList<TariffFixBO> al =  new ArrayList<TariffFixBO>();

        try{

            String Query = " SELECT * FROM " +DatabaseConstants.TABLE_NAME_TARIFF_FIX+ " ;";

            Cursor energy = db.rawQuery(Query, null);

            while(energy.moveToNext()){

                TariffFixBO slb = new TariffFixBO();

                slb.setTariffCode(energy.getInt(energy.getColumnIndex("TARIFFCODE")));
                slb.setItem(energy.getString(energy.getColumnIndex("ITEM")).charAt(0));
                slb.setChargeType(energy.getString(energy.getColumnIndex("CHARGETYPE")).charAt(0));
                slb.setFrom(energy.getInt(energy.getColumnIndex("FIX_FROM")));
                slb.setTo(energy.getInt(energy.getColumnIndex("FIX_TO")));
                slb.setTariff(energy.getInt(energy.getColumnIndex("FIX_AMOUNT")));
                al.add(slb);
            }

            if(energy != null){
                energy.close();
            }

            db.close();

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in getTariffFix : "+e.toString());
        }


        return al;
    }

    public TariffRebateBO getTariffRebatebyRow(int row) {

        db = this.getWritableDatabase();

        String Query = " SELECT * FROM " +DatabaseConstants.TABLE_NAME_TARIFF_REBATE+ "   LIMIT  "+row+",1 ;" ;

        TariffRebateBO slb = new TariffRebateBO();

        try{
            Cursor energy = db.rawQuery(Query, null);

            while(energy.moveToNext()){

                slb.setRebateCode(energy.getString(energy.getColumnIndex("REBATECODE")));
                slb.setChType(energy.getString(energy.getColumnIndex("CHTYPE")).charAt(0));
                slb.setRebate(energy.getInt(energy.getColumnIndex("REBATE")));
                slb.setMaxRebate(energy.getInt(energy.getColumnIndex("MAXREBATE")));
                slb.setRebateType(energy.getString(energy.getColumnIndex("REBATETYPE")).charAt(0));
            }

            if(energy != null){
                energy.close();
            }

            db.close();

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in getTariffRebatebyRow : "+e.toString());
        }
        return slb;
    }

    public String getTariffNameForCode(int key_tariff_code) {
        Log.d("["+this.getClass().getSimpleName()+"]-->","tarif code = "+key_tariff_code);

        db = this.getWritableDatabase();

        String trf = null;
        try{
            Cursor Upload = db.rawQuery("SELECT  TARIFFTYPE  FROM   " +
                    ""+DatabaseConstants.TABLE_NAME_TARIFF_MAIN +
                    " WHERE  TARIFFCODE = "+key_tariff_code+" ;  ", null);


            while (Upload.moveToNext()) {
                trf = Upload.getString(Upload.getColumnIndex("TARIFFTYPE"));
            }

            if(Upload != null){
                Upload.close();
            }

            db.close();

        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Ocured in  getTariffNameForCode: "+e.toString());
        }

        return trf;


    }

    public void UpdateBilledRecord(BillingMasterBO bill_rec_ob) {

        // TODO Auto-generated method stub

        db = this.getWritableDatabase();

        try{
            Log.d("["+this.getClass().getSimpleName()+"]-->","chk 1");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//Log.d("["+this.getClass().getSimpleName()+"]-->","chk 2");
            ObjectOutputStream oos = new ObjectOutputStream(baos);//Log.d("["+this.getClass().getSimpleName()+"]-->","chk 3");
            ArrayList<FixedChargeListBO> fixed = bill_rec_ob.getFixedChargeListBOs();
            oos.writeObject(fixed);Log.d("["+this.getClass().getSimpleName()+"]-->","chk 4");
            oos.close();
            byte[] bArrayFixedChrg = baos.toByteArray(); //Log.d("["+this.getClass().getSimpleName()+"]-->","chk 5");


            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();//Log.d("["+this.getClass().getSimpleName()+"]-->","chk 6");
            ObjectOutputStream oos1 = new ObjectOutputStream(baos1);//Log.d("["+this.getClass().getSimpleName()+"]-->","chk 7");
            ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
            oos1.writeObject(energy);//Log.d("["+this.getClass().getSimpleName()+"]-->","chk 8");
            oos1.close();
            byte[] bArrayEnergyChrg = baos1.toByteArray();// Log.d("["+this.getClass().getSimpleName()+"]-->","chk 9");

            Log.d("["+this.getClass().getSimpleName()+"]-->","UpdateBNCRecordWithGenerartedValues ....");

            Log.d("["+this.getClass().getSimpleName()+"]-->","KEY_INDEX =  "+bill_rec_ob.getKEY_INDEX());

            ContentValues values = new ContentValues();

            values.put("KEY_INDEX",bill_rec_ob.getKEY_INDEX());
            values.put("KEY_LOCATION ",bill_rec_ob.getKEY_LOCATION());
            values.put("KEY_RR_NO",bill_rec_ob.getKEY_RR_NO());
            values.put("KEY_LEDGER_NO ",bill_rec_ob.getKEY_LEDGER_NO().trim());
            values.put("KEY_ACTUAL_FOLIO_NO ", bill_rec_ob.getKEY_ACTUAL_FOLIO_NO().trim());
            values.put("KEY_SPOT_FOLIO_NO",bill_rec_ob.getKEY_SPOT_FOLIO_NO().trim());
            values.put("KEY_TARIFF_CODE", bill_rec_ob.getKEY_TARIFF_CODE());
            values.put("KEY_BILL_NO",bill_rec_ob.getKEY_BILL_NO().trim());
            values.put("KEY_BILL_DATE",bill_rec_ob.getKEY_BILL_DATE().trim());
            values.put("KEY_CONSMR_NAME",bill_rec_ob.getKEY_CONSMR_NAME().trim());
            values.put("KEY_ADDRESS1",bill_rec_ob.getKEY_ADDRESS1().trim());
            values.put("KEY_ADDRESS2",bill_rec_ob.getKEY_ADDRESS2().trim());
            values.put("KEY_ADDRESS3",bill_rec_ob.getKEY_ADDRESS3().trim());
            values.put("KEY_BILLING_MONTH",bill_rec_ob.getKEY_BILLING_MONTH().trim());
            values.put("KEY_READING_DATE", bill_rec_ob.getKEY_READING_DATE().trim());
            values.put("KEY_READER_CODE",bill_rec_ob.getKEY_READER_CODE().trim());
            values.put("KEY_INSTALLATION_STATUS",bill_rec_ob.getKEY_INSTALLATION_STATUS());
            values.put("KEY_LINE_MINIMUM",bill_rec_ob.getKEY_LINE_MINIMUM());
            values.put("KEY_SANCT_HP",   bill_rec_ob.getKEY_SANCT_HP());
            values.put("KEY_SANCT_KW",   bill_rec_ob.getKEY_SANCT_KW());
            values.put("KEY_CT_PT",   bill_rec_ob.getKEY_CT_PT());
            values.put("KEY_PREV_MTR_RDG",bill_rec_ob.getKEY_PREV_MTR_RDG());
            values.put("KEY_AVG_CONSUMPTION",   bill_rec_ob.getKEY_AVG_CONSUMPTION());
            values.put("KEY_POWER_FACTOR",  bill_rec_ob.getKEY_POWER_FACTOR());
            values.put("KEY_MTR_CHNG_DT1",bill_rec_ob.getKEY_MTR_CHNG_DT1().trim());
            values.put("KEY_MTR_CHNG_RDG1", bill_rec_ob.getKEY_MTR_CHNG_RDG1());
            values.put("KEY_MTR_CHNG_DT2",  bill_rec_ob.getKEY_MTR_CHNG_DT2().trim());
            values.put("KEY_MTR_CHNG_RGD2", bill_rec_ob.getKEY_MTR_CHNG_RGD2());
            values.put("KEY_FAC_RATE",bill_rec_ob.getKEY_FAC_RATE());
            values.put("KEY_DMD_ARREARS",   bill_rec_ob.getKEY_DMD_ARREARS());
            values.put("KEY_INT_ARREARS",   bill_rec_ob.getKEY_INT_ARREARS());
            values.put("KEY_TAX_ARREARS",   bill_rec_ob.getKEY_TAX_ARREARS());
            values.put("KEY_DELAY_INTEREST",bill_rec_ob.getKEY_DELAY_INTEREST());
            values.put("KEY_AMT_PAID",   bill_rec_ob.getKEY_AMT_PAID());
            values.put("KEY_PAID_DATE1",  bill_rec_ob.getKEY_PAID_DATE1().trim());
            values.put("KEY_AMT_PAID2",bill_rec_ob.getKEY_AMT_PAID2());
            values.put("KEY_PAID_DATE2",  bill_rec_ob.getKEY_PAID_DATE2().trim());
            values.put("KEY_OTHERS", bill_rec_ob.getKEY_OTHERS());
            values.put("BILLPRINTED",  bill_rec_ob.getBILLPRINTED().trim());
            values.put("CAPRBTAMT",bill_rec_ob.getCAPRBTAMT());
            values.put("PREVIOUS_DEMAND1",   bill_rec_ob.getPREVIOUS_DEMAND1());
            values.put("PREVIOUS_DEMAND2", bill_rec_ob.getPREVIOUS_DEMAND2());
            values.put("PREVIOUS_DEMAND3",  bill_rec_ob.getPREVIOUS_DEMAND3());
            values.put("BILLING_MODE",bill_rec_ob.getBILLING_MODE());
            values.put("METER_CONST",bill_rec_ob.getMETER_CONST());
            values.put("APPEAL_AMOUNT",   bill_rec_ob.getAPPEAL_AMOUNT());
            values.put("INT_APPEAL_AMOUNT", bill_rec_ob.getINT_APPEAL_AMOUNT());
            values.put("KVAH",  bill_rec_ob.getKVAH());
            values.put("INSTAL_TYP", bill_rec_ob.getINSTAL_TYP());
            values.put("HP_ROUND",   bill_rec_ob.getHP_ROUND());
            values.put("KW_ROUND",   bill_rec_ob.getKW_ROUND());
            values.put("PREVIOUS_BILL_DATE",   bill_rec_ob.getPREVIOUS_BILL_DATE());
            values.put("CREDITBF",   bill_rec_ob.getCREDITBF());
            values.put("DEBITBF", bill_rec_ob.getDEBITBF());
            values.put("DUE_DATE",  bill_rec_ob.getDUE_DATE());
            values.put("IVRSID",bill_rec_ob.getIVRSID());
            values.put("PARTFRACTION",bill_rec_ob.getPARTFRACTION());
            values.put("NUMDL",  bill_rec_ob.getNUMDL());
            values.put("CGEXEMPT_FLG",bill_rec_ob.getCGEXEMPT_FLG());
            values.put("DC_FLG", bill_rec_ob.getDC_FLG());
            values.put("INT_ARREARS2", bill_rec_ob.getINT_ARREARS2());
            values.put("INT_TAX",   bill_rec_ob.getINT_TAX());
            values.put("FIRST_RDG_FLG",  bill_rec_ob.getFIRST_RDG_FLG());
            values.put("CREAPING_PERC",bill_rec_ob.getCREAPING_PERC());
            values.put("MNR_FLG",  bill_rec_ob.getMNR_FLG());
            values.put("OLD_MTR_RDG",bill_rec_ob.getOLD_MTR_RDG());
            values.put("INT_ON_TAX",   bill_rec_ob.getINT_ON_TAX());
            values.put("SUBDIV",  bill_rec_ob.getSUBDIV());
            values.put("NO_TAX_COMP", bill_rec_ob.getNO_TAX_COMP());
            values.put("PREV_RDG_FLG",  bill_rec_ob.getPREV_RDG_FLG());
            values.put("PLFLAG",bill_rec_ob.getPLFLAG());
            values.put("PREVCKWH",bill_rec_ob.getPREVCKWH());
            values.put("REG_PENALTY",bill_rec_ob.getREG_PENALTY());
            values.put("PF_FLAG", bill_rec_ob.getPF_FLAG());
            values.put("LASTMONTHFRACTION",bill_rec_ob.getLASTMONTHFRACTION());
            values.put("CUR_QRTR",bill_rec_ob.getCUR_QRTR());
            values.put("FREQUENCY",bill_rec_ob.getFREQUENCY());
            values.put("ANNUAL_MIN_FIX",   bill_rec_ob.getANNUAL_MIN_FIX());
            values.put("CAPACITOR", bill_rec_ob.getCAPACITOR());
            values.put("CHEQDIS",  bill_rec_ob.getCHEQDIS());
            values.put("ORPHAN_RBT",bill_rec_ob.getORPHAN_RBT());
            values.put("MMD_CREDIT",   bill_rec_ob.getMMD_CREDIT());
            values.put("MTR_CHNG_FLG",  bill_rec_ob.getMTR_CHNG_FLG());
            values.put("ADDNL3MMD",bill_rec_ob.getADDNL3MMD() );
            values.put("TEMP_TARIFF_CODE",bill_rec_ob.getTEMP_TARIFF_CODE() );
            values.put("LESSCLAIMED",bill_rec_ob.getLESSCLAIMED());
            values.put("MORECLAIMED", bill_rec_ob.getMORECLAIMED());
            values.put("PRESENT_RDG ",   bill_rec_ob.getPRESENT_RDG());
            values.put("N_UNITSCONSUMED",  bill_rec_ob.getN_UNITSCONSUMED());
            values.put("N_TAX",  bill_rec_ob.getN_TAX());
            values.put("TOTALBILL",bill_rec_ob.getTOTALBILL());
            values.put("N_REBATE", bill_rec_ob.getN_REBATE());
            values.put("TOTAL_FIXED_TARIFF",   bill_rec_ob.getTOTAL_FIXED_TARIFF());
            values.put("P_NTOTAL_ENAERGY_TARIFF",   bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF());
            values.put("CREDIT_CF",   bill_rec_ob.getCREDIT_CF());
            values.put("METER_STS",  bill_rec_ob.getMETER_STS());
            values.put("LESS_MORE_CLAIMED", bill_rec_ob.getLESS_MORE_CLAIMED());
            values.put("TEMP_TAX",   bill_rec_ob.getTEMP_TAX());
            values.put("DIFF_AMOUNT",  bill_rec_ob.getDIFF_AMOUNT());
            values.put("PF_PENALTY",bill_rec_ob.getPF_PENALTY());
            values.put("PH_REBATE", bill_rec_ob.getPH_REBATE());
            values.put("PL_REBATE",   bill_rec_ob.getPL_REBATE());
            values.put("FIRST_RDG_FLAG",  bill_rec_ob.getFIRST_RDG_FLAG());
            values.put("FULL_MONTH_FLAG",bill_rec_ob.getFULL_MONTH_FLAG());
            values.put("ORPHAN_AMOUNT",bill_rec_ob.getORPHAN_AMOUNT());
            values.put("RR_REBATE",   bill_rec_ob.getRR_REBATE());
            values.put("LT4_DEBIT",   bill_rec_ob.getLT4_DEBIT());
            values.put("PL_TEMP_TARIFF_CODE",   bill_rec_ob.getPL_TEMP_TARIFF_CODE());
            values.put("ENERGYAMOUNTPLUSTAX",bill_rec_ob.getENERGYAMOUNTPLUSTAX());
            values.put("UPLOADEDTOSERVER",bill_rec_ob.getUPLOADEDTOSERVER());
            values.put("FIXED_OBJ",bArrayFixedChrg);
            values.put("ENERGY_OBJ",bArrayEnergyChrg);
            values.put("PRSTCKWH",bill_rec_ob.getPRSTCKWH());
            //values.put("METER_IMAGE",bill_rec_ob.getMeterImage());
            values.put("LATITUDE", bill_rec_ob.getLATITUDE());
            values.put("LONGITUDE", bill_rec_ob.getLONGITUDE());

            // Log.d("["+this.getClass().getSimpleName()+"]-->","METER_IMAGE = "+bill_rec_ob.getMeterImage());
            // Log.d("["+this.getClass().getSimpleName()+"]-->","LATITUDE = "+bill_rec_ob.getLatitude());
            // Log.d("["+this.getClass().getSimpleName()+"]-->","LONGITUDE = "+bill_rec_ob.getLongitude());


           // String[] argValue = {bill_rec_ob.getRr_No(),bill_rec_ob.getLocation()+bill_rec_ob.getBill_Date(),bill_rec_ob.getReader_Code()};
            Log.d("["+this.getClass().getSimpleName()+"]-->","chk 10");
		 /*int i = sd.update(TableName.BNC_Table_Name, values,
				 " KEY_RR_NO = ? AND KEY_LOCATION =?  AND KEY_BILL_DATE = ?  AND KEY_READER_CODE = ?",argValue);*/

            int i = db.update(DatabaseConstants.TABLE_NAME_BILLING_MASTER, values,
                    " KEY_INDEX  = "+bill_rec_ob.getKEY_INDEX(),null);

            // Log.d("["+this.getClass().getSimpleName()+"]-->","chk 11");

            if(i > 0){
                //	 Log.d("["+this.getClass().getSimpleName()+"]-->","chk 15");
                Log.d("["+this.getClass().getSimpleName()+"]-->","Record Updated ");
            }
            // Log.d("["+this.getClass().getSimpleName()+"]-->","chk 12");
        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error : "+e.toString());
            //	Log.d("["+this.getClass().getSimpleName()+"]-->","chk 13");
        }

        db.close();
    }

    public JSONObject SetBilledRecordToUpload(String rrNumber) {

        db = getWritableDatabase();

        Log.d("[" + this.getClass().getSimpleName() + "]-->", "SetBilledRecordToUpload Called ........");

        double nRebate = 0.0;

        JSONObject jsonObject = null;

        Cursor Upload = db.rawQuery("SELECT * FROM   " +
                "" + DatabaseConstants.TABLE_NAME_BILLING_MASTER +
                " WHERE KEY_RR_NO = '" + rrNumber + "' AND  BILLPRINTED = 'Y' AND UPLOADEDTOSERVER = 'N' ; ", null);


        while (Upload.moveToNext()) {

            jsonObject = new JSONObject();

            try {
                jsonObject.put("KEY_RR_NO", Upload.getString(Upload.getColumnIndex("KEY_RR_NO")));
                jsonObject.put("KEY_READER_CODE", Upload.getString(Upload.getColumnIndex("KEY_READER_CODE")));
                jsonObject.put("KEY_LOCATION", Upload.getString(Upload.getColumnIndex("KEY_LOCATION")));
                jsonObject.put("KEY_BILL_NO", Upload.getString(Upload.getColumnIndex("KEY_BILL_NO")));
                jsonObject.put("KEY_BILL_DATE", Upload.getString(Upload.getColumnIndex("KEY_BILL_DATE")));
                jsonObject.put("PRESENT_RDG", Upload.getString(Upload.getColumnIndex("PRESENT_RDG")));
                jsonObject.put("METER_STS", Upload.getString(Upload.getColumnIndex("METER_STS")));
                jsonObject.put("N_UNITSCONSUMED", Upload.getString(Upload.getColumnIndex("N_UNITSCONSUMED")));
                jsonObject.put("TOTAL_FIXED_TARIFF", Upload.getString(Upload.getColumnIndex("TOTAL_FIXED_TARIFF")));
                jsonObject.put("P_NTOTAL_ENAERGY_TARIFF", Upload.getString(Upload.getColumnIndex("P_NTOTAL_ENAERGY_TARIFF")));
                jsonObject.put("N_TAX", Upload.getString(Upload.getColumnIndex("N_TAX")));

                nRebate = Upload.getDouble(Upload.getColumnIndex("N_REBATE")) - Upload.getDouble(Upload.getColumnIndex("PL_REBATE"));
                nRebate = nRebate - Upload.getDouble(Upload.getColumnIndex("PH_REBATE"));
                nRebate = nRebate - Upload.getDouble(Upload.getColumnIndex("ORPHAN_RBT"));

                jsonObject.put("N_REBATE", nRebate);
                jsonObject.put("CREDIT_CF", Upload.getString(Upload.getColumnIndex("CREDIT_CF")));
                jsonObject.put("BILLPRINTED", Upload.getString(Upload.getColumnIndex("BILLPRINTED")));

                ArrayList<FixedChargeListBO> fixed = new ArrayList<FixedChargeListBO>();
                byte[] b = Upload.getBlob(Upload.getColumnIndex("FIXED_OBJ"));
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new ByteArrayInputStream(b));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fixed = (ArrayList<FixedChargeListBO>) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Log.d("[" + this.getClass().getSimpleName() + "]-->", "fixed.size() = " + fixed.size() + "   ---   " + fixed);
                if (fixed.size() > 0) {
                    Log.d("[" + this.getClass().getSimpleName() + "]-->", "check 4");
                    Iterator<FixedChargeListBO> it = fixed.iterator();
                    for (int i = 0; i < 5; i++) {
                        if (it.hasNext()) {
                            FixedChargeListBO fixedList = it.next();
                            jsonObject.put("FIXED_UNIT_" + (i + 1), fixedList.getUnits());
                            jsonObject.put("FIXED_RATE_" + (i + 1), fixedList.getRate());
                        } else {
                            //Temp = Temp + "~0.0~0.0";
                            jsonObject.put("FIXED_UNIT_" + (i + 1), "0.0");
                            jsonObject.put("FIXED_RATE_" + (i + 1), "0.0");
                        }
                    }

                } else {
                    for (int j = 0; j < 5; j++) {
                        jsonObject.put("FIXED_UNIT_" + (j + 1), "0.0");
                        jsonObject.put("FIXED_RATE_" + (j + 1), "0.0");
                    }
                }

                ArrayList<EnergyChargeListBO> energy = new ArrayList<EnergyChargeListBO>();
                byte[] b1 = Upload.getBlob(Upload.getColumnIndex("ENERGY_OBJ"));
                ObjectInputStream ois1 = null;
                try {
                    ois1 = new ObjectInputStream(new ByteArrayInputStream(b1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    energy = (ArrayList<EnergyChargeListBO>) ois1.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Log.d("[" + this.getClass().getSimpleName() + "]-->", "energy.size() = " + energy.size() + "   ---   " + energy);
                if (energy.size() > 0) {
                    Iterator<EnergyChargeListBO> it = energy.iterator();
                    for (int i = 0; i < 6; i++) {
                        if (it.hasNext()) {
                            EnergyChargeListBO energyList = it.next();
					/*Temp = Temp + "~" +energyList.getUnits() + "~"+energyList.getRate()/100;*/
                            // Temp = Temp + "~" + energyList.getUnits() + "~" + energyList.getRate();
                            jsonObject.put("ENERGY_UNIT_" + (i + 1), energyList.getUnits());
                            jsonObject.put("ENERGY_RATE_" + (i + 1), energyList.getRate());
                        } else {
                            // Temp = Temp + "~0.0~0.0";
                            jsonObject.put("ENERGY_UNIT_" + (i + 1), "0.0");
                            jsonObject.put("ENERGY_RATE_" + (i + 1), "0.0");
                        }
                    }

                } else {
                    for (int i = 0; i < 6; i++) {
                        //Temp = Temp + "~0.0~0.0";
                        jsonObject.put("ENERGY_UNIT_" + (i + 1), "0.0");
                        jsonObject.put("ENERGY_RATE_" + (i + 1), "0.0");
                    }
                }

                jsonObject.put("DIFF_AMOUNT", Upload.getString(Upload.getColumnIndex("DIFF_AMOUNT")));
                jsonObject.put("PF_PENALTY", Upload.getString(Upload.getColumnIndex("PF_PENALTY")));
                jsonObject.put("PL_REBATE", Upload.getString(Upload.getColumnIndex("PL_REBATE")));
                jsonObject.put("PRSTCKWH", Upload.getString(Upload.getColumnIndex("PRSTCKWH")));
                jsonObject.put("PH_REBATE", Upload.getString(Upload.getColumnIndex("PH_REBATE")));
                jsonObject.put("MORECLAIMED", Upload.getString(Upload.getColumnIndex("MORECLAIMED")));
                jsonObject.put("RR_REBATE", "0.0");
                jsonObject.put("CAPRBTAMT", Upload.getString(Upload.getColumnIndex("CAPRBTAMT")));
                jsonObject.put("ORPHAN_AMOUNT", Upload.getString(Upload.getColumnIndex("ORPHAN_AMOUNT")));
                jsonObject.put("USER_ID", "NADEEM");
                jsonObject.put("LATITUDE", Upload.getString(Upload.getColumnIndex("LATITUDE")));
                jsonObject.put("LONGITUDE", Upload.getString(Upload.getColumnIndex("LONGITUDE")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Upload.close();

        return jsonObject;
    }
    public int getDownloadedRecordCount(){

        int ret = 0;

        db = this.getWritableDatabase();

        try{
            Cursor recordset = db.rawQuery("SELECT count(*) FROM   " +
                    ""+DatabaseConstants.TABLE_NAME_BILLING_MASTER+ "  ;  ", null);

            while (recordset.moveToNext()) {
                ret = recordset.getInt(0);
                Log.d("["+this.getClass().getSimpleName()+"]-->","Dowloaded Count : "+ret);
            }

            if(recordset != null){
                recordset.close();
            }

            db.close();

        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Ocured in getDownloadedRecordCount: "+e.toString());
        }

        return ret;
    }

    public int getUploadedToServerRecordCount(){

        int ret = 0;

        db = getWritableDatabase();

        try{
            Cursor Upload = db.rawQuery("SELECT count(*) FROM   " +
                    ""+DatabaseConstants.TABLE_NAME_BILLING_MASTER+
                    /*" WHERE BILLPRINTED = 'Y' AND UPLOADEDTOSERVER = 'N' ;  ", null);*/
                    " WHERE BILLPRINTED = 'Y' AND UPLOADEDTOSERVER = 'Y' ;  ", null);

            while (Upload.moveToNext()) {
                ret = Upload.getInt(0);
                Log.d("["+this.getClass().getSimpleName()+"]-->","Uploaded Count : "+ret);
            }

            if(Upload != null){
                Upload.close();
            }

            db.close();

        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Ocured in  getUploadRecordCount: "+e.toString());
        }

        return ret;
    }

    public int getBilledRecordCount(){

        int ret = 0;

        db = getWritableDatabase();

        try{
            Cursor Upload = db.rawQuery("SELECT count(*) FROM   " +
                    ""+DatabaseConstants.TABLE_NAME_BILLING_MASTER+
                    " WHERE BILLPRINTED = 'Y' ;  ", null);
            while (Upload.moveToNext()) {
                ret = Upload.getInt(0);
                Log.d("["+this.getClass().getSimpleName()+"]-->","Billed Count : "+ret);
            }

            if(Upload != null){
                Upload.close();
            }

            db.close();

        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Ocured in  getUploadRecordCount: "+e.toString());
        }

        return ret;
    }

    public void UpdateUploadStatus(String RrNumber) {

        // TODO Auto-generated method stub

        db = this.getWritableDatabase();

        try{

            Log.d("["+this.getClass().getSimpleName()+"]-->","UpdateUploadStatus  , KEY_RR_NO =  "+RrNumber);

            ContentValues values = new ContentValues();

            values.put("UPLOADEDTOSERVER","Y");


            int i = db.update(DatabaseConstants.TABLE_NAME_BILLING_MASTER, values,
                    " KEY_RR_NO  = '"+RrNumber+"'",null);

            if(i > 0){
                //	 Log.d("["+this.getClass().getSimpleName()+"]-->","chk 15");
                Log.d("["+this.getClass().getSimpleName()+"]-->","UPLOADEDTOSERVER = Y , Record Updated ");
            }
            // Log.d("["+this.getClass().getSimpleName()+"]-->","chk 12");
        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error : "+e.toString());
            //	Log.d("["+this.getClass().getSimpleName()+"]-->","chk 13");
        }

        db.close();
    }

    public ArrayList<HashMap<String, String>> getItemMasterDetails() {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        db = this.getWritableDatabase();

        String trf = null;
        try{
            Cursor items = db.rawQuery("SELECT  *  FROM   " +
                    ""+DatabaseConstants.TABLE_ITEM_MASTER_DETAILS, null);

            Log.d("items",""+items);

            while (items.moveToNext()) {
               // trf = items.getString(items.getColumnIndex("TARIFFTYPE"));
                HashMap<String,String> user = new HashMap<>();
                user.put("KEY_INDEX",items.getString(items.getColumnIndex("KEY_INDEX")));
                user.put("DESCRIPTION_ENGLISH",items.getString(items.getColumnIndex("DESCRIPTION_ENGLISH")));
                user.put("DESCRIPTION_OTHER",items.getString(items.getColumnIndex("DESCRIPTION_OTHER")));
                list.add(user);

                Log.d("Index",""+items.getInt(items.getColumnIndex("KEY_INDEX")));
                Log.d("Desc-Full",""+items.getString(items.getColumnIndex("DESCRIPTION_ENGLISH")));
                Log.d("Desc-Short",""+items.getString(items.getColumnIndex("DESCRIPTION_OTHER")));
            }


            if(items != null){
                items.close();
            }

            db.close();

        }catch (Exception e) {
            // TODO: handle exception
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Ocured in  getItemMasterDetails: "+e.toString());
        }

        return list;


    }
}
