package com.prakruthi.billingapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prakruthi.billingapp.bean.BillingMasterBO;
import com.prakruthi.billingapp.bean.CompInfoTempBO;
import com.prakruthi.billingapp.bean.EnergyChargeListBO;
import com.prakruthi.billingapp.bean.FixCompInfoTempBO;
import com.prakruthi.billingapp.bean.FixedChargeListBO;
import com.prakruthi.billingapp.bean.PrintRecordBO;
import com.prakruthi.billingapp.bean.ResultMasterBO;
import com.prakruthi.billingapp.bean.TariffFixBO;
import com.prakruthi.billingapp.bean.TariffMainBO;
import com.prakruthi.billingapp.bean.TariffRebateBO;
import com.prakruthi.billingapp.bean.TariffSlabBO;
import com.prakruthi.billingapp.constants.Print;
import com.prakruthi.billingapp.constants.TariffConstants;
import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.database.DatabaseUtil;
import com.prakruthi.billingapp.jobscheduler.AndroidUploadService;
import com.prakruthi.billingapp.listeners.LocationFinder;
import com.prakruthi.billingapp.spotbilling.MainActivity;
import com.prakruthi.billingapp.spotbilling.R;
import com.ngx.BluetoothPrinter;
import com.ngx.Enums.NGXBarcodeCommands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BillingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int    FIRST    = 1;
    public static final int    PREVIOUS = 2;
    public static final int    NEXT     = 3;
    public static final int    LAST     = 4;
    public static final int    ALL      = 5;
    public static final int    RRNO     = 6;

    private OnFragmentInteractionListener mListener;

    DatabaseUtil databaseUtil ;
    DatabaseImplementation databaseImplementation;

    static int INDEX = 0 ;

    BillingMasterBO bill_rec_ob = null;

    Button btn_generate_bill ;
    Button btn_next ;
    Button btn_last ;
    Button btn_prev ;
    Button btn_first ;


    EditText editText_PresentReading ;
    EditText editText_RRNO ;
    EditText editText_SpotFolio ;

    TextInputLayout textInputLayout_PresentReading ;

    AutoCompleteTextView autoCompleteTextView_RRNO ;

    RadioGroup radioGroup_ReadingType ;

    RadioButton radioButton_None;
    RadioButton radioButton_DL;
    RadioButton radioButton_MNR;

    TextView textView_bill_gen_sts;

    ArrayAdapter<String> adapter = null;

    boolean RADIO_CHECKED_IS_NONE ;
    String  METER_STATUS ;

    int PRESENT_READING ;

    String AlertMessage;

    int EnergySlabCount = 0;
    int FixedSlabCount = 0;
    double FAC_TAX = 0.0;
    int    FAC_unit = 0;

    TariffMainBO trfMain = null;
    TariffRebateBO tarifRebate = null;
    PrintRecordBO printBO = null;

    LocationFinder locationFinder = LocationFinder.getInstance();

    boolean BILLING_RECORD_EXISTS = false;

    public BillingFragment() {
        // Required empty public constructor
    }

    List<ResultMasterBO> resultMasterBOList = new ArrayList<ResultMasterBO>();

    private BluetoothPrinter mBtp = MainActivity.mBtp;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillingFragment newInstance(String param1, String param2) {
        BillingFragment fragment = new BillingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_billing, container, false);

        LoadViews(view);

        databaseImplementation = DatabaseImplementation.getInstance(getActivity());
        databaseUtil           = new DatabaseUtil();
        bill_rec_ob            = new BillingMasterBO();

        bill_rec_ob = databaseImplementation.GetConsumerRecordByIndex(INDEX,ALL, "");
        SetValuesToAutoComplete();

        if(BILLING_RECORD_EXISTS){
            SetValuesToUI();
        }

        SetPropertiesToUI(false);
        RefreshValues();

       /* Log.d("Print.mBluetoothSocket",""+Print.mBluetoothSocket);
        if(Print.mBluetoothSocket == null){
            ShowAlert("fail","Please connect to bluetooth printer !!!!","Error..!!");
        }else{
            if(!Print.mBluetoothSocket.isConnected()){
                ShowAlert("fail","Please connect to bluetooth printer !!!!","Error..!!");
            }
        }*/

        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshValues();
                INDEX = 0;
                bill_rec_ob = databaseImplementation.GetConsumerRecordByIndex(INDEX,FIRST, "");
                if(BILLING_RECORD_EXISTS){
                    SetValuesToUI();
                }
                SetPropertiesToUI(false);
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(INDEX == 0){
                    Toast.makeText(getActivity(),"No Record,Please Move Forward",Toast.LENGTH_LONG).show();
                    return;
                }

                RefreshValues();
                INDEX--;
                bill_rec_ob = databaseImplementation.GetConsumerRecordByIndex(INDEX,PREVIOUS, "");
                if(BILLING_RECORD_EXISTS){
                    SetValuesToUI();
                }
                SetPropertiesToUI(false);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(INDEX == databaseImplementation.getDownloadedRecordCount()-1){
                    Toast.makeText(getActivity(),"End Of Records,Please Move Backward",Toast.LENGTH_LONG).show();
                    return;
                }

                RefreshValues();
                INDEX++;
                bill_rec_ob = databaseImplementation.GetConsumerRecordByIndex(INDEX,NEXT, "");
                if(BILLING_RECORD_EXISTS){
                    SetValuesToUI();
                }
                SetPropertiesToUI(false);
            }
        });

        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshValues();
                INDEX = databaseImplementation.getDownloadedRecordCount() - 1;
                bill_rec_ob = databaseImplementation.GetConsumerRecordByIndex(INDEX,LAST, "");
                if(BILLING_RECORD_EXISTS){
                    SetValuesToUI();
                }
                SetPropertiesToUI(false);
            }
        });

        radioGroup_ReadingType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                onRadioButtonClicked(checkedId);

            }
        });

        btn_generate_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VerifyAndGenerateBill();
            }
        });

       /* Button buttontest = (Button) view.findViewById(R.id.buttontest);

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData();

                // Starting ResultActivity
                //Intent i = new Intent(getActivity(), ResultActivity.class);
               // startActivity(i);
            }
        });*/

        return view;
    }



    protected void sendData() {
        // TODO Auto-generated method stub

        try {

            //mOutputStream = mBluetoothSocket.getOutputStream();
            OutputStream os = Print.mBluetoothSocket.getOutputStream();


            // the text typed by the user
            String msg = "Test";
            msg += "\n";

            // byte[] temp = {0x10};
            // byte[] temp2 = {0x0D};



            /*mOutputStream.write(temp);*/

            byte[] send = null;

            // Check that there's actually something to send
            if (msg.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                send = msg.getBytes();
            }

            byte[] TEXT_MODE = {0x1B,0x21,1};

            byte[] CHARACTER_FONT = {0x1B,0x21,1};

            byte[] BOLD_START = {0x1B,0x45,1};
            byte[] BOLD_END = {0x1B,0x45,0};

            byte[] DOUBLE_HEIGHT_START = {0x1D,0x21,0x01,2};
            byte[] DOUBLE_HEIGHT_END = {0x1D,0x21,0x00,0};

            byte[] DOUBLE_WIDTH_START = {0x1D,0x21,6};
            byte[] DOUBLE_WIDTH_END = {0x1D,0x21,4};

            byte[] PRINT_BARCODE = {0x1D,0x6B,73,10};

            Print.mOutputStream.write(TEXT_MODE);
            Print.mOutputStream.write(BOLD_START);
            Print.mOutputStream.write(send);
            Print.mOutputStream.write(BOLD_END);
            Print.mOutputStream.write((byte)0x0D);
            Print.mOutputStream.write(Print.END);
            Print.mOutputStream.flush();

           /* mOutputStream.write(DOUBLE_HEIGHT_START);
            mOutputStream.write(send);
            mOutputStream.write(0x0D);*/


            Print.mOutputStream.write(TEXT_MODE);
            Print.mOutputStream.write(BOLD_START);
            Print.mOutputStream.write(DOUBLE_HEIGHT_START);
            Print.mOutputStream.write(send);
            Print.mOutputStream.write(DOUBLE_HEIGHT_END);
            Print.mOutputStream.write(BOLD_END);
            Print.mOutputStream.write((byte)0x0D);
            Print.mOutputStream.write(Print.END);
            Print.mOutputStream.flush();


            Print.mOutputStream.write(PRINT_BARCODE);
            Print.mOutputStream.write(send);
            Print.mOutputStream.write((byte)0x0D);
            Print.mOutputStream.write(Print.END);
            Print.mOutputStream.flush();



            // tell the user data were sent
           // myLabel.setText("Data Sent");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RefreshValues() {

        RADIO_CHECKED_IS_NONE = false;
        METER_STATUS = null;

        PRESENT_READING = 0;

        AlertMessage = null;

        EnergySlabCount = 0;
        FixedSlabCount = 0;
        FAC_TAX = 0.0;
        FAC_unit = 0;

        trfMain = null;
        tarifRebate = null;
        printBO = null;
    }

    private void VerifyAndGenerateBill() {

        if(ValidateBillStatus()){

            Toast.makeText(getActivity(),"Bill Already Generated",Toast.LENGTH_LONG).show();
            return;

        }

        if(RADIO_CHECKED_IS_NONE){

            String temp_present_reading = editText_PresentReading.getText().toString().trim();

            if(temp_present_reading.length() == 0 || temp_present_reading.equals("")){

                Toast.makeText(getActivity(),"Enter Present Reading !!!",Toast.LENGTH_LONG).show();
                return;

            }else{
                PRESENT_READING = Integer.parseInt(temp_present_reading);
            }
        }

        bill_rec_ob.setMETER_STS("");

        if(!bill_rec_ob.getDC_FLG().equals("Y")){

            if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 10 || bill_rec_ob.getKEY_INSTALLATION_STATUS() ==2){
                bill_rec_ob.setPRESENT_RDG(bill_rec_ob.getKEY_PREV_MTR_RDG());
            }else{
                if(bill_rec_ob.getINSTAL_TYP().equals("4")
                        || bill_rec_ob.getINSTAL_TYP().equals("2")
                                || bill_rec_ob.getINSTAL_TYP().equals("1")){
                    bill_rec_ob.setPRESENT_RDG(PRESENT_READING/100);

                }else{
                    bill_rec_ob.setPRESENT_RDG(PRESENT_READING);
                }
            }

            long prstcons,avgcons;

            prstcons = avgcons =  0;

            prstcons = bill_rec_ob.getPRESENT_RDG() - bill_rec_ob.getKEY_PREV_MTR_RDG();
            avgcons = (long) bill_rec_ob.getKEY_AVG_CONSUMPTION();
            avgcons = avgcons * 4;

            if(prstcons > avgcons ){
                Log.d("["+this.getClass().getSimpleName()+"]-->","ABNORMAL CONSUMPTION");
                AlertMessage =  "ABNORMAL CONSUMPTOIN.";
            }else{
                AlertMessage = "";
            }

            if( bill_rec_ob.getPRESENT_RDG() < bill_rec_ob.getKEY_PREV_MTR_RDG() ){
                Log.d("["+this.getClass().getSimpleName()+"]-->","RDG LESS THN PRV RDG");
                AlertMessage = "PRESENT READING IS LESS THAN PREVIOUS.";
            }else{
                AlertMessage = "";
            }

            if( bill_rec_ob.getKEY_INSTALLATION_STATUS() == 10 ||
                    bill_rec_ob.getKEY_INSTALLATION_STATUS() == 2 )
            {
                Log.d("["+this.getClass().getSimpleName()+"]-->","VACANT OR TEMP DISC");
                AlertMessage =  "VACANT OR TEMP DISC.";
            }else{
                AlertMessage = "";
            }

            Log.d("["+this.getClass().getSimpleName()+"]-->","IS MTR RDG CORRECT ?");
            AlertMessage = AlertMessage + "IS METER READING CORRECT ?";

            if( bill_rec_ob.getINSTAL_TYP().equals("4") || bill_rec_ob.getINSTAL_TYP().equals("2")
                    || bill_rec_ob.getINSTAL_TYP().equals("1") )
            {
                AlertMessage = AlertMessage + "\n PRST CKWH :"+bill_rec_ob.getPRSTCKWH() + " AND PREV CKWH : "+bill_rec_ob.getPREVCKWH();
            }
            else
            {
                AlertMessage = AlertMessage + "\n PRST RDG :"+bill_rec_ob.getPRESENT_RDG() + " AND PREV RDG : "+bill_rec_ob.getKEY_PREV_MTR_RDG();
            }
        }else{
            AlertMessage = AlertMessage + "\nDC BILLED ON LOAD BASIS ";
        }

        //ShowAlert("fail","Error !!!","RR Code Should Not Be Empty !!! ");
        PromptAlert("fail",AlertMessage,"Confirm","confirm_for_billing");

    }


    private void SetValuesToAutoComplete() {

        //Call The function to Populate auto complete
        String[] AUTO_RRNO_VAUES = databaseImplementation.getRRNOforAutoComnplete();

        if(AUTO_RRNO_VAUES != null){

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,AUTO_RRNO_VAUES);
            autoCompleteTextView_RRNO.setThreshold(1);
            //Set adapter to AutoCompleteTextView
            autoCompleteTextView_RRNO.setAdapter(adapter);
            autoCompleteTextView_RRNO.setOnItemSelectedListener(this);
            autoCompleteTextView_RRNO.setOnItemClickListener(this);

            BILLING_RECORD_EXISTS = true;

        }else{
            BILLING_RECORD_EXISTS = false;
        }

        //
    }

    private void SetPropertiesToUI(boolean NFC){

        if(NFC){

            radioGroup_ReadingType.setEnabled(false);
            editText_PresentReading.setText("");
            editText_PresentReading.setEnabled(false);

        }else{

            radioGroup_ReadingType.setEnabled(true);
            radioButton_None.setChecked(true);
            onRadioButtonClicked(R.id.radioButton_billing_none);
            radioButton_DL.setChecked(false);
            radioButton_MNR.setChecked(false);
            editText_PresentReading.clearComposingText();
            editText_PresentReading.clearFocus();
            editText_PresentReading.setEnabled(true);
            editText_PresentReading.setHint(R.string.billing_present_reading);
            textInputLayout_PresentReading.setErrorEnabled(false);
        }



    }

    private void onRadioButtonClicked(int checkedId) {

        //now check which radio button is selected
        //android switch statement
        switch(checkedId){

            case R.id.radioButton_billing_none:
                radioButton_None.setTypeface(null, Typeface.BOLD_ITALIC);
                radioButton_DL.setTypeface(null, Typeface.NORMAL);
                radioButton_MNR.setTypeface(null, Typeface.NORMAL);
                editText_PresentReading.setEnabled(true);
                editText_PresentReading.setText("");
                RADIO_CHECKED_IS_NONE = true;
                METER_STATUS = "NONE";
                break;

            case R.id.radioButton_billing_dl:

                radioButton_None.setTypeface(null, Typeface.NORMAL);
                radioButton_DL.setTypeface(null, Typeface.BOLD_ITALIC);
                radioButton_MNR.setTypeface(null, Typeface.NORMAL);
                editText_PresentReading.setEnabled(false);
                editText_PresentReading.clearComposingText();
                editText_PresentReading.clearFocus();
                //editText_PresentReading.setHint(R.string.billing_present_reading);
                textInputLayout_PresentReading.setErrorEnabled(false);
                RADIO_CHECKED_IS_NONE = false;
                METER_STATUS = "DL";
                break;

            case R.id.radioButton_billing_mnr:

                radioButton_None.setTypeface(null, Typeface.NORMAL);
                radioButton_DL.setTypeface(null, Typeface.NORMAL);
                radioButton_MNR.setTypeface(null, Typeface.BOLD_ITALIC);
                editText_PresentReading.setEnabled(false);
                editText_PresentReading.clearComposingText();
                editText_PresentReading.clearFocus();
                //editText_PresentReading.setHint(R.string.billing_present_reading);
                textInputLayout_PresentReading.setErrorEnabled(false);
                RADIO_CHECKED_IS_NONE = false;
                METER_STATUS = "MNR";
                break;
        }
    }

    private void SetValuesToUI() {

        editText_RRNO.setEnabled(false);
        editText_RRNO.setText(bill_rec_ob.getKEY_RR_NO());

        editText_SpotFolio.setEnabled(false);
        editText_SpotFolio.setText(bill_rec_ob.getKEY_SPOT_FOLIO_NO());

        if(bill_rec_ob.getBILLPRINTED().equals("Y")){

            Drawable img = getContext().getResources().getDrawable( R.drawable.if_tick_32 );
            img.setBounds( 0, 0, 60, 60 );
            textView_bill_gen_sts.setCompoundDrawables( img, null, null, null );

            textView_bill_gen_sts.setText("Bill Already Generated !!! ");
        }
        else if(bill_rec_ob.getBILLPRINTED().equals("N")){

            Drawable img = getContext().getResources().getDrawable( R.drawable.if_block_32 );
            img.setBounds( 0, 0, 60, 60 );
            textView_bill_gen_sts.setCompoundDrawables( img, null, null, null );

            textView_bill_gen_sts.setText("Bill Not Generated !!! ");
        }
    }

    private void LoadViews(View view) {

        btn_first = (Button) view.findViewById(R.id.billing_btn_first);
        btn_prev  = (Button) view.findViewById(R.id.billing_btn_previous);
        btn_next  = (Button) view.findViewById(R.id.billing_btn_next);
        btn_last  = (Button) view.findViewById(R.id.billing_btn_last);
        btn_generate_bill = (Button) view.findViewById(R.id.billing_btn_generate_bill);

        textInputLayout_PresentReading = (TextInputLayout) view.findViewById(R.id.il_billing_present_reading);

        editText_PresentReading = (EditText) view.findViewById(R.id.et_billing_present_reading);
        editText_RRNO           = (EditText) view.findViewById(R.id.et_billing_rrno);
        editText_SpotFolio      = (EditText) view.findViewById(R.id.et_billing_spotfolio);

        radioGroup_ReadingType  = (RadioGroup) view.findViewById(R.id.radiogroup_billing_readingtype);

        radioButton_None        = (RadioButton) view.findViewById(R.id.radioButton_billing_none);
        radioButton_MNR         = (RadioButton) view.findViewById(R.id.radioButton_billing_mnr);
        radioButton_DL          = (RadioButton) view.findViewById(R.id.radioButton_billing_dl);

        textView_bill_gen_sts = (TextView) view.findViewById(R.id.text_billing_generated_sts);

        autoCompleteTextView_RRNO = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView_rrno);

        editText_PresentReading.addTextChangedListener(new MyBillingTextWatcher(editText_PresentReading));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), "Position:"+position+" RR Number:"+parent.getItemAtPosition(position),
                Toast.LENGTH_LONG).show();

        String AUTO_RRNO = parent.getItemAtPosition(position).toString().trim();

        bill_rec_ob = databaseImplementation.GetConsumerRecordByIndex(0,RRNO,AUTO_RRNO);
        if(BILLING_RECORD_EXISTS){
            SetValuesToUI();
        }
        SetPropertiesToUI(false);

        INDEX = bill_rec_ob.getKEY_INDEX();
        autoCompleteTextView_RRNO.setText("");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public boolean ValidateBillStatus() {
        // TODO Auto-generated method stub
        boolean billSts = false;

        try
        {
            if(bill_rec_ob.getBILLPRINTED().equals("Y") && bill_rec_ob.getMETER_STS().equals("D")){
                bill_rec_ob.setBILLPRINTED("N");
                bill_rec_ob.setMETER_STS("");
                billSts = false;
            }
            else if(bill_rec_ob.getBILLPRINTED().equals("N")){
                billSts = false;
            }
            else{
                billSts = true;
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ValidateBillStatus : "+e.toString());
        }
        return  billSts;
    }

    private class MyBillingTextWatcher implements TextWatcher {

        private View view;

        public MyBillingTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {
                case R.id.et_billing_present_reading:
                    validate_present_reading();
                    break;
            }


        }
    }

    private boolean validate_present_reading() {

        if (editText_PresentReading.getText().toString().trim().isEmpty()) {
            textInputLayout_PresentReading.setError(getString(R.string.err_msg_presentreading));
            return false;
        } else {
            textInputLayout_PresentReading.setErrorEnabled(false);
        }
        return true;
    }

    private void ShowAlert(String status, final String message, String title) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());


        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status.equals("success")) {
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.succes);

            // Setting Dialog Title
            alertDialog.setTitle(title);

        } else {
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.fail);
            alertDialog.setTitle(Html.fromHtml("<font color='#FF0800'>"+title+"</font>"));
        }

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.bottom_navigation_content_frame, SettingsFragment.newInstance("",""), "yourFragTag").commit();

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void PromptAlert(String status, final String message, String title,final String functiontype) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());


        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status.equals("success")) {
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.succes);

            // Setting Dialog Title
            alertDialog.setTitle(title);

        } else {
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.fail);
            alertDialog.setTitle(Html.fromHtml("<font color='#FF0800'>"+title+"</font>"));
        }

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (functiontype.equals("confirm_for_billing")) {

                   // BillingGenImpl billingGen = new BillingGenImpl(bill_rec_ob);

                    FinalGenerateBill();

                }else if(functiontype.equals("confirm_pritn_bill")){

                   /*Intent i = new Intent(getActivity(), ResultActivity.class);
                   Bundle bundle = new Bundle();
                   bundle.putParcelable("billedrecord",bill_rec_ob);
                   i.putExtra("bundle",bundle);
                   startActivity(i);*/



                   // print_bill_to_printer();

                    print_kannada_bill();

                    bill_rec_ob.setBILLPRINTED("Y");
                    bill_rec_ob.setLATITUDE(locationFinder.GetLatitude());
                    bill_rec_ob.setLONGITUDE(locationFinder.GetLongitude());

                    Log.d("["+this.getClass().getSimpleName()+"]-->","Records To Be updated ....");
                    databaseImplementation.UpdateBilledRecord(bill_rec_ob);

                    AndroidUploadService.SendJobToQueue(bill_rec_ob.getKEY_RR_NO());


                   final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            ReloadCurrentFragment();
                        }
                    }, 1000);


                   // print_bill();

                  /*  print_bill();
                    print_barcode();

                    */
                }
            }

        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    private void ReloadCurrentFragment(){

        // Reload current fragment
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.bottom_navigation_content_frame, BillingFragment.newInstance("",""), "yourFragTag").commit();
    }

    private void FinalGenerateBill() {

        if(bill_rec_ob.getMNR_FLG().equals("N")){
            if(bill_rec_ob.getDC_FLG().equals("N")){
                bill_rec_ob.setMETER_STS("");
            }
        }
        Log.d("["+this.getClass().getSimpleName()+"]-->"," \n In GetMeterReadingAndComputeBill()");
        Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getMNR_FLG() "+bill_rec_ob.getMNR_FLG());
        Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getInstallation_Status() "+bill_rec_ob.getKEY_INSTALLATION_STATUS());
        if((bill_rec_ob.getMNR_FLG().equals("Y")
                || (METER_STATUS != null && (METER_STATUS.equals("DL") || (METER_STATUS.equals("MNR")))))
                    && bill_rec_ob.getKEY_INSTALLATION_STATUS() != 10){
            Log.d("["+this.getClass().getSimpleName()+"]-->"," \n bill_rec_ob.getMNR_Flg()");
            Log.d("["+this.getClass().getSimpleName()+"]-->"," if AcceptMeterStatusAndComputeDlMNR");

            AcceptMeterStatusAndComputeDlMNR();

        }else{
            GetMeterReading();
            Log.d("["+this.getClass().getSimpleName()+"]-->"," else GetMeterReading");
        }



        //Commented just to avoid printing.
        PromptAlert("success","Bill Generated Successfully. Print Bill ???","Confirm","confirm_pritn_bill");




        //print_bill();
        //print_barcode();

       /* bill_rec_ob.setBILLPRINTED("Y");
        bill_rec_ob.setLATITUDE(locationFinder.GetLatitude());
        bill_rec_ob.setLONGITUDE(locationFinder.GetLongitude());
        Log.d("["+this.getClass().getSimpleName()+"]-->","Records To Be updated ....");
        databaseImplementation.UpdateBilledRecord(bill_rec_ob);
        AndroidUploadService.SendJobToQueue(bill_rec_ob.getKEY_RR_NO());

        Intent i = new Intent(getActivity(), ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("billedrecord",bill_rec_ob);
        bundle.putSerializable("resultMasterBOList", (Serializable) resultMasterBOList);
        i.putExtra("bundle",bundle);
        startActivity(i);*/

    }

    private void print_barcode() {



    }


    private void print_command(int fonttype,String text_to_print ){

            //normal - 1
            //bold - 2
            //double height - 3
             //double height - 4
            //bold-doubleheight - 5

        try {
            Print.mOutputStream.write(Print.TEXT_MODE);

            if(fonttype == 2){
                Print.mOutputStream.write(Print.BOLD_START);
            }
            if(fonttype == 3){
                Print.mOutputStream.write(Print.DOUBLE_HEIGHT_START);
            }
            if(fonttype == 4){
                Print.mOutputStream.write(Print.DOUBLE_WIDTH_START);
            }
            if(fonttype == 5){
                Print.mOutputStream.write(Print.BOLD_DOUBLE_HEIGHT_FONT);
            }

            byte[] send = null;
            // Check that there's actually something to send
            if (text_to_print.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                send = text_to_print.getBytes();
            }
            Print.mOutputStream.write(send);


            if(fonttype == 2){
                Print.mOutputStream.write(Print.BOLD_END);
            }
            if(fonttype == 3){
                Print.mOutputStream.write(Print.DOUBLE_HEIGHT_END);
            }
            if(fonttype == 4){
                Print.mOutputStream.write(Print.DOUBLE_WIDTH_END);
            }
           /* if(fonttype == 5){
                Print.mOutputStream.write(Print.BOLD_DOUBLE_HEIGHT_FONT);
            }*/
            Print.mOutputStream.write((byte)0x0D); //carriage return
            Print.mOutputStream.write(Print.END);  //end of comm
            Print.mOutputStream.flush(); // clear buffer

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SetResultMasterValues(String Key , String Value){

        ResultMasterBO resultMasterBO = new ResultMasterBO();
        resultMasterBO.setKey(Key);
        resultMasterBO.setValue(Value);
        resultMasterBOList.add(resultMasterBO);

    }


    private void print_bill_to_printer() {



        String alignRight = "";
        printBO = new PrintRecordBO();

        try
        {
            double EC_TOTAL = 0;
            double FC_TOTAL = 0;

            Print.mOutputStream = Print.mBluetoothSocket.getOutputStream();

            print_command(3,"       Electricity Company Limited       ");
            print_command(2," ");
            print_command(3,"             Electricity Bill            ");

            print_command(2," ----------------------------------------");
            print_command(3,"  Account Details ");
            print_command(2," ");
            Log.d(" SUB DIV  : ",""+bill_rec_ob.getSUBDIV());
            print_command(2,"  Sub-Divisoin         "+bill_rec_ob.getSUBDIV());
            printBO.setSubDiv(bill_rec_ob.getSUBDIV());



            Log.d(" LOCATION :  ",""+bill_rec_ob.getKEY_LOCATION());
            print_command(2,"  Location             "+bill_rec_ob.getKEY_LOCATION());
            printBO.setLocation(bill_rec_ob.getKEY_LOCATION());

            Log.d(" RRNO     :  ",""+bill_rec_ob.getKEY_RR_NO());
            print_command(2,"  RR Number            "+bill_rec_ob.getKEY_RR_NO());
            printBO.setRrNo(bill_rec_ob.getKEY_RR_NO());


            Log.d(" IVRS ID  :  ",""+bill_rec_ob.getIVRSID());
            print_command(2,"  IVRS ID             "+bill_rec_ob.getIVRSID());
            printBO.setIvrsdId(bill_rec_ob.getIVRSID());


            Log.d(" CONSMR NAME:",""+bill_rec_ob.getKEY_CONSMR_NAME());

            print_command(2," ----------------------------------------");
            print_command(3,"  Person Details ");
            print_command(2," ");
            print_command(2,"  Consumer Name & Address ");
            print_command(2,"  "+bill_rec_ob.getKEY_CONSMR_NAME());
            printBO.setConsumerName(bill_rec_ob.getKEY_CONSMR_NAME());
            Log.d(" ADDRESS1 :  ",""+bill_rec_ob.getKEY_ADDRESS1());
            print_command(2,"  "+bill_rec_ob.getKEY_ADDRESS1());
            printBO.setAddress1(bill_rec_ob.getKEY_ADDRESS1());
            Log.d(" ADDRESS2 :  ",""+bill_rec_ob.getKEY_ADDRESS2());
            print_command(2,"  "+bill_rec_ob.getKEY_ADDRESS2());
            printBO.setAddress2(bill_rec_ob.getKEY_ADDRESS2());
            Log.d(" ADDRESS3 :  ",""+bill_rec_ob.getKEY_ADDRESS3());
            print_command(2,"  "+bill_rec_ob.getKEY_ADDRESS3());
            printBO.setAddress3(bill_rec_ob.getKEY_ADDRESS3());

            print_command(2,"  ----------------------------------------");
            print_command(3,"  Connection Details");
            print_command(2," ");
            Log.d(" MR CODE  :  ",""+bill_rec_ob.getKEY_READER_CODE());
            print_command(2,"  Meter Code           "+bill_rec_ob.getKEY_READER_CODE());
            printBO.setMrCode(bill_rec_ob.getKEY_READER_CODE());
            Log.d(" TARIFF   :  ",databaseImplementation.getTariffNameForCode(bill_rec_ob.getKEY_TARIFF_CODE()));
            print_command(2,"  Tariff Code          "+databaseImplementation.getTariffNameForCode(bill_rec_ob.getKEY_TARIFF_CODE()));
            printBO.setTariffCode(databaseImplementation.getTariffNameForCode(bill_rec_ob.getKEY_TARIFF_CODE()));
            Log.d(" BILL DATE:  ",bill_rec_ob.getKEY_BILL_DATE());
            print_command(2,"  Bill Date            "+bill_rec_ob.getKEY_BILL_DATE());
            printBO.setBillDate(bill_rec_ob.getKEY_BILL_DATE());
            Log.d(" BILL NO  :  ",bill_rec_ob.getKEY_BILL_NO());
            print_command(2,"  Bill No              "+bill_rec_ob.getKEY_BILL_NO());
            printBO.setBillNo(bill_rec_ob.getKEY_BILL_NO());
            if(trfMain.getTariffPowerUnit().equals("K")){
                Log.d(" LOAD     :  ",bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
                print_command(2,"  Load                 "+bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
                printBO.setLoad(bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
            }else{
                Log.d(" LOAD     :  ",bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
                print_command(2,"  Load                "+bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
                printBO.setLoad(bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
            }
            Log.d(" CT_RATIO :  ",""+bill_rec_ob.getKEY_CT_PT());
            print_command(2,"  CT Ratio             "+bill_rec_ob.getKEY_CT_PT());
            printBO.setCt_pt_ratio(""+bill_rec_ob.getKEY_CT_PT());
            String temp = "";
            if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 1){
                temp = "ACTIVE";
            }else if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 10){
                temp = "VACANT";
            }else if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 2){
                temp = "TEMP DISC";
            }
            Log.d(" INST STS :  ",temp);
            print_command(2,"  Installation Status  "+temp);
            printBO.setInstalltion_sts(temp);
            temp = "";
            if(bill_rec_ob.getFLG_FL_rebate() == 1 )
            {
                temp = "FL";
                if(bill_rec_ob.getOLD_MTR_RDG() > 0.001){
                    temp = temp+"/MC";
                }
            }

            if( bill_rec_ob.getOLD_MTR_RDG() > 0.001 && bill_rec_ob.getFLG_FL_rebate() != 1){
                temp = "MC";
            }

            if( bill_rec_ob.getPARTFRACTION() != 1.0 )
            {
                temp = "" + bill_rec_ob.getPARTFRACTION() * 30.0 + " Of Days/";
            }
            else
            {
                if(bill_rec_ob.getOLD_MTR_RDG() > 0.001 || bill_rec_ob.getFLG_FL_rebate() == 1)
                {
                    //	temp = temp;
                }
            }
            Log.d(" PART DAYS:  ",temp);
            print_command(2,"  Part Days            "+temp);
            printBO.setPartDays(temp);
            temp = "";
            if(bill_rec_ob.getMETER_STS().equals("M")){
                temp = "MNR";
            }else if(bill_rec_ob.getMETER_STS().equals("D")){
                temp = "DL";
            }else if(bill_rec_ob.getDC_FLG().equals("Y")){
                temp = "DC";
            }else{
                temp = ""+bill_rec_ob.getPRESENT_RDG();
            }
            Log.d(" PRST RDG :  ",temp);
            print_command(2,"  Present Reading      "+temp);
            printBO.setPresentReading(temp);

            if( bill_rec_ob.getINSTAL_TYP().equals("4") || bill_rec_ob.getINSTAL_TYP().equals("2")
                    || bill_rec_ob.getINSTAL_TYP().equals("1") ){
            }else{
                Log.d(" PREV RDG :  ",""+bill_rec_ob.getKEY_PREV_MTR_RDG());
                print_command(2,"  Previous Reading     "+bill_rec_ob.getKEY_PREV_MTR_RDG());
                printBO.setPreviousReading(""+bill_rec_ob.getKEY_PREV_MTR_RDG());
            }
            Log.d(" CONSMP   :  ",""+bill_rec_ob.getN_UNITSCONSUMED());
            print_command(2,"  Consumption          "+bill_rec_ob.getN_UNITSCONSUMED());
            printBO.setUnitsConsmp(""+bill_rec_ob.getN_UNITSCONSUMED());
            Log.d(" POWER FCT:  ",bill_rec_ob.getKEY_POWER_FACTOR()+"");
            print_command(2,"  Power Factor         "+bill_rec_ob.getKEY_POWER_FACTOR());
            printBO.setPowerFactor(""+bill_rec_ob.getKEY_POWER_FACTOR());
            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                if(FixedSlabCount > 3){
                    FixedSlabCount = 3;
                }

                ArrayList<FixedChargeListBO> fixed = new ArrayList<FixedChargeListBO>();
                fixed = bill_rec_ob.getFixedChargeListBOs();
                Log.d("["+this.getClass().getSimpleName()+"]-->","fixed.size() = "+fixed.size() + "   ---   "+fixed);

                print_command(2," ----------------------------------------");
                print_command(3,"  Fixed Charges(Unit,Rate,Amount)   ");

                if(fixed.size() > 0){
                    Iterator<FixedChargeListBO> it = fixed.iterator();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG:  ");
                    //print_command(2," FIXED CHRG  :  ");


                    print_command(2," ");


                    while(it.hasNext()){
                        Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG: Inside while : ");
                        /*if(FixedSlabCount != 3){*/
                        Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG: Inside if : ");
                        FixedChargeListBO fixedList = it.next();
                        /*Log.d(" "+fixedList.getRate()/100l+"_____"+fixedList.getUnits()/100l+"_____"+fixedList.getAmount();*/

                        Log.d(" "+fixedList.getUnits()/100l+"KW        "+ fixedList.getRate()/100l+"       "+fixedList.getAmount(),"");
                        print_command(2,"  "+fixedList.getUnits()/100l+"KW           "+ fixedList.getRate()/100l+"           "+fixedList.getAmount());
                        FC_TOTAL = (FC_TOTAL + fixedList.getAmount());
				/*}else{
					break;
				}*/
                    }
                }
                Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED TOTAL: "+FC_TOTAL);
                print_command(2," ");
                print_command(3,"  Fixed Total          "+FC_TOTAL);
                alignRight = String.format("%7s",RoundOffValue(FC_TOTAL));
                printBO.setFixedTotal(""+alignRight);
                //printBO.setFixedTotal(""+RoundOffValue(FC_TOTAL));
            }else{
                Log.d("["+this.getClass().getSimpleName()+"]-->","NO FIXED CHARGES: ");
                print_command(2," No Fixed Charges     ");
            }

            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                if(EnergySlabCount > 6){
                    EnergySlabCount = 6;
                }

                ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
                if(energy.size() > 0){
                    Iterator<EnergyChargeListBO> it = energy.iterator();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","ENERGY CHRG:  ");
                    print_command(2,"  ----------------------------------------");
                    print_command(3,"  Energy Charges(Unit,Rate,Amount)   ");
                    print_command(2," ");
                    while(it.hasNext()){
                        EnergyChargeListBO energyList = it.next();

                        if(energyList.getUnits() == 0l || energyList.getRate() == 0){
                            break;
                        }

                        /*Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100+"."+energyList.getRate()%100+ "_____"+energyList.getAmount();*/
                        Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100.0+ "_____"+energyList.getAmount(),"");
                        print_command(2,"  "+energyList.getUnits()+"              "+energyList.getRate()/100.0+ "               "+energyList.getAmount());
                        EC_TOTAL =  EC_TOTAL + energyList.getAmount();
                    }
                }
                Log.d("ENERGY TOTAL: ",""+EC_TOTAL);
                print_command(2," ");
                print_command(3,"  Energy Total          "+EC_TOTAL);
                alignRight = String.format("%7s",RoundOffValue(EC_TOTAL));
                printBO.setEnergyTotal(""+alignRight);
                //printBO.setEnergyTotal(""+RoundOffValue(EC_TOTAL));
            }else{
                if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                    ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
                    if(energy.size() > 0){
                        Iterator<EnergyChargeListBO> it = energy.iterator();
                        Log.d("["+this.getClass().getSimpleName()+"]-->","ENERGY CHRG:  ");
                        print_command(2,"  -----------------------------------------");
                        print_command(3,"  Energy Charges(Unit,Rate,Amount)   ");
                        print_command(2," ");
                        while(it.hasNext()){
                            EnergyChargeListBO energyList = it.next();

                            if(energyList.getUnits() == 0l || energyList.getRate() == 0){
                                break;
                            }

                            /*Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100+"."+energyList.getRate()%100+ "_____"+energyList.getAmount();*/
                            Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()+ "_____"+energyList.getAmount(),"");
                            print_command(2,"  "+energyList.getUnits()+"          "+energyList.getRate()+ "         "+energyList.getAmount());
                            EC_TOTAL =  EC_TOTAL + energyList.getAmount();
                        }
                    }
                    Log.d("ENERGY TOTAL: ",""+EC_TOTAL);
                    print_command(2," ");
                    print_command(3,"  Energy Total          "+EC_TOTAL);

                    alignRight = String.format("%7s",RoundOffValue(EC_TOTAL));
                    printBO.setEnergyTotal(""+alignRight);

                    //printBO.setEnergyTotal(""+RoundOffValue(EC_TOTAL));
                }
            }
            int len1;
            double totalamount,Total;
            double interestclub,arrearsclub,temp_float;
            int i;
            double temp_debit;
            double total_debt_reg_penalty;
            double ARRS_CONSMR_PAY;

            total_debt_reg_penalty = ARRS_CONSMR_PAY = 0.0;
            len1 = 0;
            totalamount = 0.0;
            interestclub = 0.0;
            arrearsclub = 0.0;
            temp_float = 0.0;
            Total = 0.0;
            ARRS_CONSMR_PAY = 0.0;
            i = 0;




	   /* Formatter fmt = new Formatter();
	    // Right justify by default
	    fmt.format("|%10.2f|", 123.123);*/

            print_command(2,"  ------------------------------------");
            print_command(3,"  Bill Breakup   ");
            print_command(2," ");
            /*Log.d(" TAX      :  ",Math.round((bill_rec_ob.getnTax()+FAC_TAX));*/
            Log.d(" TAX      :  ",(bill_rec_ob.getN_TAX()+FAC_TAX)+"");
            print_command(2,"  Tax                   "+RoundOffValue((bill_rec_ob.getN_TAX()+FAC_TAX)));
            alignRight = String.format("%7s",RoundOffValue((bill_rec_ob.getN_TAX()+FAC_TAX)));
            /*printBO.setTax(""+RoundOffValue((bill_rec_ob.getnTax()+FAC_TAX)));*/
            printBO.setTax(""+alignRight);


            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                Total = bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getN_TAX() ;
                Total = Total + bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() + bill_rec_ob.getPF_PENALTY();
                Total = Total + bill_rec_ob.getREG_PENALTY() - bill_rec_ob.getDIFF_AMOUNT();
                Total = Total + bill_rec_ob.getKEY_AMT_PAID() ;
            }else{
                if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                    Total = bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getDIFF_AMOUNT() + bill_rec_ob.getN_TAX();
                }
            }
            Log.d(" BILL AMT :  ",(Total+FAC_TAX+((double)FAC_unit)/100.0)+"");
            print_command(2,"  Bill Amount           "+RoundOffValue((Total+FAC_TAX+((double)FAC_unit)/100.0)));
            /*printBO.setBillAmount(""+RoundOffValue((Total+FAC_TAX+((double)FAC_unit)/100.0)));*/
            alignRight = String.format("%7s",RoundOffValue((Total+FAC_TAX+((double)FAC_unit)/100.0)) );
            printBO.setBillAmount(""+alignRight);

            interestclub = interestclub + bill_rec_ob.getKEY_INT_ARREARS();
            interestclub = interestclub + bill_rec_ob.getKEY_DELAY_INTEREST()+ bill_rec_ob.getINT_TAX() ;
            interestclub = interestclub + bill_rec_ob.getINT_ON_TAX();

            arrearsclub  = bill_rec_ob.getINT_ARREARS2()
                    +
                    bill_rec_ob.getKEY_DMD_ARREARS()
                    +
                    bill_rec_ob.getKEY_TAX_ARREARS();

            Log.d(" INTEREST :  ",interestclub+"");
            print_command(2,"  Interest              "+RoundOffValue(interestclub));
            /*printBO.setInterest(""+RoundOffValue(interestclub));*/
            alignRight = String.format("%7s",RoundOffValue(interestclub));
            printBO.setInterest(""+alignRight);



            Log.d(" ARREARS  :  ",arrearsclub+"");
            print_command(2,"  Arrears               "+RoundOffValue((arrearsclub)));
            /*printBO.setArrears(""+arrearsclub);*/
            alignRight = String.format("%7s",RoundOffValue(arrearsclub));
            printBO.setArrears(alignRight);

            interestclub = arrearsclub  + interestclub;
            Log.d(" ARR + INT:  ",(interestclub)+"");
            print_command(2,"  Arrears + Interest    "+RoundOffValue((interestclub)));
            alignRight = String.format("%7s",RoundOffValue(interestclub));
            /*printBO.setArrPlusInterest(""+RoundOffValue(interestclub));*/
            printBO.setArrPlusInterest(alignRight);

            if (bill_rec_ob.getLESSCLAIMED() > 0.0) {
                bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() + bill_rec_ob.getLESSCLAIMED());
            }
            bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() + bill_rec_ob.getPRSTCKWH() + bill_rec_ob.getREG_PENALTY());
            Log.d(" OTHERS   :  ",bill_rec_ob.getDEBITBF()+"");
            print_command(2,"  Others                "+RoundOffValue(bill_rec_ob.getDEBITBF()));
            alignRight = String.format("%7s",RoundOffValue(bill_rec_ob.getDEBITBF()));
            /*printBO.setOthers(""+RoundOffValue(bill_rec_ob.getDebitBF()));*/
            printBO.setOthers(alignRight);

            temp_float = bill_rec_ob.getDEBITBF() + EC_TOTAL + trfMain.getMinFixed();

            bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() - bill_rec_ob.getPRSTCKWH());

            if (bill_rec_ob.getMORECLAIMED() > 0.0) {
                bill_rec_ob.setCREDITBF(bill_rec_ob.getCREDITBF() + bill_rec_ob.getMORECLAIMED());
            }

            Log.d(" CREDIT   :  ",""+bill_rec_ob.getCREDITBF() * -1);
            print_command(2,"  Credit                "+RoundOffValue(bill_rec_ob.getCREDITBF() * -1));
            // printBO.setCredit(""+RoundOffValue((bill_rec_ob.getCreditBF() * -1)));
            alignRight = String.format("%7s",RoundOffValue((bill_rec_ob.getCREDITBF() * -1)));
            printBO.setCredit(alignRight);

            temp_debit = temp_float - bill_rec_ob.getMORECLAIMED();

            Log.d(" REBATE   :  ",""+bill_rec_ob.getN_REBATE() * -1);
            print_command(2,"  Reabte                "+RoundOffValue(bill_rec_ob.getN_REBATE() * -1));
            /*printBO.setRebate(""+RoundOffValue(bill_rec_ob.getnRebate() * -1));*/
            alignRight = String.format("%7s", RoundOffValue(bill_rec_ob.getN_REBATE() * -1));
            printBO.setRebate(alignRight);

            Log.d(" FAC      :  ",""+((double)FAC_unit)/100.0);
            print_command(2,"  FAC                   "+((double)FAC_unit)/100.0);
            /*printBO.setFAC(""+RoundOffValue(((double)FAC_unit)/100.0));*/
            alignRight = String.format("%7s",RoundOffValue(((double)FAC_unit)/100.0) );
            printBO.setFAC(alignRight);

            totalamount = bill_rec_ob.getN_REBATE();

            bill_rec_ob.setTemp_total(bill_rec_ob.getTOTALBILL() - totalamount);

            Log.d(" NET AMT  :  ",""+ Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0)));
            print_command(3,"  Net Amount            "+ Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0)));

            printBO.setNetAmount(""+Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0)));

            Log.d(" DUE DATE :  ", bill_rec_ob.getDUE_DATE());
            print_command(3,"  Due Date              "+bill_rec_ob.getDUE_DATE());
            printBO.setDueDate(bill_rec_ob.getDUE_DATE());

            ARRS_CONSMR_PAY = bill_rec_ob.getTemp_total() - temp_float ;

            printBO.setMessages("");

            print_command(2,"  ------------------------------------");
            print_command(2,"  ");

            byte[] send = null;
            String barcode_value = printBO.getLocation()+printBO.getRrNo()+"~"+printBO.getNetAmount();
            // Check that there's actually something to send
            if (barcode_value.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                send = barcode_value.getBytes();
            }
            Print.mOutputStream.write(Print.PRINT_BARCODE);
            Print.mOutputStream.write(send);
            Print.mOutputStream.write((byte)0x0D);
            Print.mOutputStream.write(Print.END);
            Print.mOutputStream.flush();

            if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A){
                if(bill_rec_ob.getN_UNITSCONSUMED() >= 0 && bill_rec_ob.getN_UNITSCONSUMED() <= 18 ){
                    Log.d(" GOK PAYABLE:", temp_debit+"");
                    print_command(2," GOK PAYABLE  :  "+temp_debit);
                    printBO.setMessages(printBO.getMessages()+"\n GOK PAYABLE:"+ temp_debit);
                    Log.d(" ARREARS CONSMR PAYABLE :  ",ARRS_CONSMR_PAY+"");
                    print_command(2,"  Arrears Consumer Payable "+ARRS_CONSMR_PAY);

                    printBO.setMessages(printBO.getMessages()+ "\n ARREARS CONSMR PAYABLE :  "+ARRS_CONSMR_PAY);
                }
            }else if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A1
                    && bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A2){
                Log.d(" GOK PAYABLE:", temp_debit+"");
                print_command(2," GOK PAYABLE  :  "+temp_debit);
                printBO.setMessages(printBO.getMessages()+ "\n GOK PAYABLE:"+ temp_debit);
                Log.d(" ARREARS CONSMR PAYABLE :  ",ARRS_CONSMR_PAY+"");
                print_command(2,"  Arrears Consumer Payable "+ARRS_CONSMR_PAY);
                printBO.setMessages(printBO.getMessages()+ "\n ARREARS CONSMR PAYABLE :  "+ARRS_CONSMR_PAY);
            }

            if (bill_rec_ob.getMMD_CREDIT() > 0.01)
            {
                Log.d(" INT ON MMD/MSD:", bill_rec_ob.getMMD_CREDIT()+"");
                print_command(2,"  Interest On MMD/MSD      "+bill_rec_ob.getMMD_CREDIT());
                printBO.setMessages(printBO.getMessages()+ "\n INT ON MMD/MSD:"+ bill_rec_ob.getMMD_CREDIT());
                Log.d(" CREDITED IS SUBJECT TO AUDI","");
                print_command(2,"  Credit Is Subject To Audi");
                printBO.setMessages(printBO.getMessages()+ "\n CREDITED IS SUBJECT TO AUDI");
            }

            if(bill_rec_ob.getADDNL3MMD() > 0.01)
            {
                Log.d(" ADDNL. 2MMD DUE:", bill_rec_ob.getADDNL3MMD()+"");
                print_command(2,"  Additional 2MMD Due      "+bill_rec_ob.getADDNL3MMD());
                printBO.setMessages(printBO.getMessages()+ "\n ADDNL. 2MMD DUE:"+ bill_rec_ob.getADDNL3MMD());
            }

            if( bill_rec_ob.getFLG_ECS_USER() == '1')
            {
                Log.d(" ECS USER NOT FOR PAYMENT","");
                print_command(2,"  ECS User Not For Payment ");
                printBO.setMessages(printBO.getMessages()+ "\n ECS USER NOT FOR PAYMENT");
            }

            if( bill_rec_ob.getCHEQDIS().equals("Y") )
            {
                Log.d(" NOT TO ACCEPT CHEQUES","");
                print_command(2,"  Not To Accept Cheques    ");
                printBO.setMessages(printBO.getMessages()+ "\n NOT TO ACCEPT CHEQUES");
            }
            Log.d("\n\n\n\n\n","");


            print_command(2," ");
            print_command(2," ");
            print_command(2," ");
            print_command(2," ");

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in prt_bill : "+e.toString());
        }

    }

    @SuppressLint("LongLogTag")
    private void print_bill() {

        String alignRight = "";
        printBO = new PrintRecordBO();

        try
        {
            double EC_TOTAL = 0;
            double FC_TOTAL = 0;

            Log.d(" SUB DIV  :  ",""+bill_rec_ob.getSUBDIV());
            printBO.setSubDiv(bill_rec_ob.getSUBDIV());
            Log.d(" LOCATION :  ",""+bill_rec_ob.getKEY_LOCATION());
            printBO.setLocation(bill_rec_ob.getKEY_LOCATION());
            Log.d(" RRNO     :  ",""+bill_rec_ob.getKEY_RR_NO());
            printBO.setRrNo(bill_rec_ob.getKEY_RR_NO());
            Log.d(" IVRD ID  :  ",""+bill_rec_ob.getIVRSID());
            printBO.setIvrsdId(bill_rec_ob.getIVRSID());
            Log.d(" CONSMR NAME:",""+bill_rec_ob.getKEY_CONSMR_NAME());
            printBO.setConsumerName(bill_rec_ob.getKEY_CONSMR_NAME());
            Log.d(" ADDRESS1 :  ",""+bill_rec_ob.getKEY_ADDRESS1());
            printBO.setAddress1(bill_rec_ob.getKEY_ADDRESS1());
            Log.d(" ADDRESS2 :  ",""+bill_rec_ob.getKEY_ADDRESS2());
            printBO.setAddress2(bill_rec_ob.getKEY_ADDRESS2());
            Log.d(" ADDRESS3 :  ",""+bill_rec_ob.getKEY_ADDRESS3());
            printBO.setAddress3(bill_rec_ob.getKEY_ADDRESS3());
            Log.d(" MR CODE  :  ",""+bill_rec_ob.getKEY_READER_CODE());
            printBO.setMrCode(bill_rec_ob.getKEY_READER_CODE());
            Log.d(" TARIFF   :  ",databaseImplementation.getTariffNameForCode(bill_rec_ob.getKEY_TARIFF_CODE()));
            printBO.setTariffCode(databaseImplementation.getTariffNameForCode(bill_rec_ob.getKEY_TARIFF_CODE()));
            Log.d(" BILL DATE:  ",bill_rec_ob.getKEY_BILL_DATE());
            printBO.setBillDate(bill_rec_ob.getKEY_BILL_DATE());
            Log.d(" BILL NO  :  ",bill_rec_ob.getKEY_BILL_NO());
            printBO.setBillNo(bill_rec_ob.getKEY_BILL_NO());
            if(trfMain.getTariffPowerUnit().equals("K")){
                Log.d(" LOAD     :  ",bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
                printBO.setLoad(bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
            }else{
                Log.d(" LOAD     :  ",bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
                printBO.setLoad(bill_rec_ob.getKEY_SANCT_KW()+" + "+bill_rec_ob.getKEY_SANCT_HP());
            }
            Log.d(" CT_RATIO :  ",""+bill_rec_ob.getKEY_CT_PT());
            printBO.setCt_pt_ratio(""+bill_rec_ob.getKEY_CT_PT());
            String temp = "";
            if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 1){
                temp = "ACTIVE";
            }else if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 10){
                temp = "VACANT";
            }else if(bill_rec_ob.getKEY_INSTALLATION_STATUS() == 2){
                temp = "TEMP DISC";
            }
            Log.d(" INST STS :  ",temp);
            printBO.setInstalltion_sts(temp);
            temp = "";
            if(bill_rec_ob.getFLG_FL_rebate() == 1 )
            {
                temp = "FL";
                if(bill_rec_ob.getOLD_MTR_RDG() > 0.001){
                    temp = temp+"/MC";
                }
            }

            if( bill_rec_ob.getOLD_MTR_RDG() > 0.001 && bill_rec_ob.getFLG_FL_rebate() != 1){
                temp = "MC";
            }

            if( bill_rec_ob.getPARTFRACTION() != 1.0 )
            {
                temp = "" + bill_rec_ob.getPARTFRACTION() * 30.0 + " Of Days/";
            }
            else
            {
                if(bill_rec_ob.getOLD_MTR_RDG() > 0.001 || bill_rec_ob.getFLG_FL_rebate() == 1)
                {
                    //	temp = temp;
                }
            }
            Log.d(" PART DAYS:  ",temp);
            printBO.setPartDays(temp);
            temp = "";
            if(bill_rec_ob.getMETER_STS().equals("M")){
                temp = "MNR";
            }else if(bill_rec_ob.getMETER_STS().equals("D")){
                temp = "DL";
            }else if(bill_rec_ob.getDC_FLG().equals("Y")){
                temp = "DC";
            }else{
                temp = ""+bill_rec_ob.getPRESENT_RDG();
            }
            Log.d(" PRST RDG :  ",temp);
            printBO.setPresentReading(temp);

            if( bill_rec_ob.getINSTAL_TYP().equals("4") || bill_rec_ob.getINSTAL_TYP().equals("2")
                    || bill_rec_ob.getINSTAL_TYP().equals("1") ){
            }else{
                Log.d(" PREV RDG :  ",""+bill_rec_ob.getKEY_PREV_MTR_RDG());
                printBO.setPreviousReading(""+bill_rec_ob.getKEY_PREV_MTR_RDG());
            }
            Log.d(" CONSMP   :  ",""+bill_rec_ob.getN_UNITSCONSUMED());
            printBO.setUnitsConsmp(""+bill_rec_ob.getN_UNITSCONSUMED());
            Log.d(" POWER FCT:  ",bill_rec_ob.getKEY_POWER_FACTOR()+"");
            printBO.setPowerFactor(""+bill_rec_ob.getKEY_POWER_FACTOR());
            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                if(FixedSlabCount > 3){
                    FixedSlabCount = 3;
                }

                ArrayList<FixedChargeListBO> fixed = new ArrayList<FixedChargeListBO>();
                fixed = bill_rec_ob.getFixedChargeListBOs();
                Log.d("["+this.getClass().getSimpleName()+"]-->","fixed.size() = "+fixed.size() + "   ---   "+fixed);
                if(fixed.size() > 0){
                    Iterator<FixedChargeListBO> it = fixed.iterator();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG:  ");
                    while(it.hasNext()){
                        Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG: Inside while : ");
				/*if(FixedSlabCount != 3){*/
                        Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG: Inside if : ");
                        FixedChargeListBO fixedList = it.next();
					/*Log.d(" "+fixedList.getRate()/100l+"_____"+fixedList.getUnits()/100l+"_____"+fixedList.getAmount();*/
                        Log.d(" "+fixedList.getUnits()/100l+"_____"+ fixedList.getRate()/100l+"_____"+fixedList.getAmount(),"");
                        FC_TOTAL = (FC_TOTAL + fixedList.getAmount());
				/*}else{
					break;
				}*/
                    }
                }
                Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED TOTAL: "+FC_TOTAL);
                alignRight = String.format("%7s",RoundOffValue(FC_TOTAL));
                printBO.setFixedTotal(""+alignRight);
                //printBO.setFixedTotal(""+RoundOffValue(FC_TOTAL));
            }else{
                Log.d("["+this.getClass().getSimpleName()+"]-->","NO FIXED CHARGES: ");
            }

            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                if(EnergySlabCount > 6){
                    EnergySlabCount = 6;
                }

                ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
                if(energy.size() > 0){
                    Iterator<EnergyChargeListBO> it = energy.iterator();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","ENERGY CHRG:  ");
                    while(it.hasNext()){
                        EnergyChargeListBO energyList = it.next();

                        if(energyList.getUnits() == 0l || energyList.getRate() == 0){
                            break;
                        }

				/*Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100+"."+energyList.getRate()%100+ "_____"+energyList.getAmount();*/
                        Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100.0+ "_____"+energyList.getAmount(),"");
                        EC_TOTAL =  EC_TOTAL + energyList.getAmount();
                    }
                Log.d("ENERGY TOTAL: ",""+EC_TOTAL);
                alignRight = String.format("%7s",RoundOffValue(EC_TOTAL));
                printBO.setEnergyTotal(""+alignRight);
                //printBO.setEnergyTotal(""+RoundOffValue(EC_TOTAL));
            }
            }else{
                if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                    ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
                    if(energy.size() > 0){
                        Iterator<EnergyChargeListBO> it = energy.iterator();
                        Log.d("["+this.getClass().getSimpleName()+"]-->","ENERGY CHRG:  ");
                        while(it.hasNext()){
                            EnergyChargeListBO energyList = it.next();

                            if(energyList.getUnits() == 0l || energyList.getRate() == 0){
                                break;
                            }

					/*Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100+"."+energyList.getRate()%100+ "_____"+energyList.getAmount();*/
                            Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()+ "_____"+energyList.getAmount(),"");
                            EC_TOTAL =  EC_TOTAL + energyList.getAmount();
                        }
                    }
                    Log.d("ENERGY TOTAL: ",""+EC_TOTAL);

                    alignRight = String.format("%7s",RoundOffValue(EC_TOTAL));
                    printBO.setEnergyTotal(""+alignRight);

                    //printBO.setEnergyTotal(""+RoundOffValue(EC_TOTAL));
                }
            }
            int len1;
            double totalamount,Total;
            double interestclub,arrearsclub,temp_float;
            int i;
            double temp_debit;
            double total_debt_reg_penalty;
            double ARRS_CONSMR_PAY;

            total_debt_reg_penalty = ARRS_CONSMR_PAY = 0.0;
            len1 = 0;
            totalamount = 0.0;
            interestclub = 0.0;
            arrearsclub = 0.0;
            temp_float = 0.0;
            Total = 0.0;
            ARRS_CONSMR_PAY = 0.0;
            i = 0;




	   /* Formatter fmt = new Formatter();
	    // Right justify by default
	    fmt.format("|%10.2f|", 123.123);*/



	    /*Log.d(" TAX      :  ",Math.round((bill_rec_ob.getnTax()+FAC_TAX));*/
            Log.d(" TAX      :  ",(bill_rec_ob.getN_TAX()+FAC_TAX)+"");
            alignRight = String.format("%7s",RoundOffValue((bill_rec_ob.getN_TAX()+FAC_TAX)));
	    /*printBO.setTax(""+RoundOffValue((bill_rec_ob.getnTax()+FAC_TAX)));*/
            printBO.setTax(""+alignRight);


            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                Total = bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getN_TAX() ;
                Total = Total + bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() + bill_rec_ob.getPF_PENALTY();
                Total = Total + bill_rec_ob.getREG_PENALTY() - bill_rec_ob.getDIFF_AMOUNT();
                Total = Total + bill_rec_ob.getKEY_AMT_PAID() ;
            }else{
                if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                    Total = bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getDIFF_AMOUNT() + bill_rec_ob.getN_TAX();
                }
            }
            Log.d(" BILL AMT :  ",(Total+FAC_TAX+((double)FAC_unit)/100.0)+"");
	    /*printBO.setBillAmount(""+RoundOffValue((Total+FAC_TAX+((double)FAC_unit)/100.0)));*/
            alignRight = String.format("%7s",RoundOffValue((Total+FAC_TAX+((double)FAC_unit)/100.0)) );
            printBO.setBillAmount(""+alignRight);

            interestclub = interestclub + bill_rec_ob.getKEY_INT_ARREARS();
            interestclub = interestclub + bill_rec_ob.getKEY_DELAY_INTEREST()+ bill_rec_ob.getINT_TAX() ;
            interestclub = interestclub + bill_rec_ob.getINT_ON_TAX();

            arrearsclub  = bill_rec_ob.getINT_ARREARS2()
                    +
                    bill_rec_ob.getKEY_DMD_ARREARS()
                    +
                    bill_rec_ob.getKEY_TAX_ARREARS();

            Log.d(" INTEREST :  ",interestclub+"");
	    /*printBO.setInterest(""+RoundOffValue(interestclub));*/
            alignRight = String.format("%7s",RoundOffValue(interestclub));
            printBO.setInterest(""+alignRight);



            Log.d(" ARREARS  :  ",arrearsclub+"");
	    /*printBO.setArrears(""+arrearsclub);*/
            alignRight = String.format("%7s",RoundOffValue(arrearsclub));
            printBO.setArrears(alignRight);

            interestclub = arrearsclub  + interestclub;
            Log.d(" ARR + INT:  ",(interestclub)+"");
            alignRight = String.format("%7s",RoundOffValue(interestclub));
	    /*printBO.setArrPlusInterest(""+RoundOffValue(interestclub));*/
            printBO.setArrPlusInterest(alignRight);

            if (bill_rec_ob.getLESSCLAIMED() > 0.0) {
                bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() + bill_rec_ob.getLESSCLAIMED());
            }
            bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() + bill_rec_ob.getPRSTCKWH() + bill_rec_ob.getREG_PENALTY());
            Log.d(" OTHERS   :  ",bill_rec_ob.getDEBITBF()+"");
            alignRight = String.format("%7s",RoundOffValue(bill_rec_ob.getDEBITBF()));
	    /*printBO.setOthers(""+RoundOffValue(bill_rec_ob.getDebitBF()));*/
            printBO.setOthers(alignRight);

            temp_float = bill_rec_ob.getDEBITBF() + EC_TOTAL + trfMain.getMinFixed();

            bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() - bill_rec_ob.getPRSTCKWH());

            if (bill_rec_ob.getMORECLAIMED() > 0.0) {
                bill_rec_ob.setCREDITBF(bill_rec_ob.getCREDITBF() + bill_rec_ob.getMORECLAIMED());
            }

            Log.d(" CREDIT   :  ",""+bill_rec_ob.getCREDITBF() * -1);
            // printBO.setCredit(""+RoundOffValue((bill_rec_ob.getCreditBF() * -1)));
            alignRight = String.format("%7s",RoundOffValue((bill_rec_ob.getCREDITBF() * -1)));
            printBO.setCredit(alignRight);

            temp_debit = temp_float - bill_rec_ob.getMORECLAIMED();

            Log.d(" REBATE   :  ",""+bill_rec_ob.getN_REBATE() * -1);
	    /*printBO.setRebate(""+RoundOffValue(bill_rec_ob.getnRebate() * -1));*/
            alignRight = String.format("%7s", RoundOffValue(bill_rec_ob.getN_REBATE() * -1));
            printBO.setRebate(alignRight);

            Log.d(" FAC      :  ",""+((double)FAC_unit)/100.0);
	    /*printBO.setFAC(""+RoundOffValue(((double)FAC_unit)/100.0));*/
            alignRight = String.format("%7s",RoundOffValue(((double)FAC_unit)/100.0) );
            printBO.setFAC(alignRight);

            totalamount = bill_rec_ob.getN_REBATE();

            bill_rec_ob.setTemp_total(bill_rec_ob.getTOTALBILL() - totalamount);

            Log.d(" NET AMT  :  ",""+ Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0)));

            printBO.setNetAmount(""+Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0)));

            Log.d(" DUE DATE :  ", bill_rec_ob.getDUE_DATE());
            printBO.setDueDate(bill_rec_ob.getDUE_DATE());

            ARRS_CONSMR_PAY = bill_rec_ob.getTemp_total() - temp_float ;

            printBO.setMessages("");

            if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A){
                if(bill_rec_ob.getN_UNITSCONSUMED() >= 0 && bill_rec_ob.getN_UNITSCONSUMED() <= 18 ){
                    Log.d(" GOK PAYABLE:", temp_debit+"");
                    printBO.setMessages(printBO.getMessages()+"\n GOK PAYABLE:"+ temp_debit);
                    Log.d(" ARREARS CONSMR PAYABLE :  ",ARRS_CONSMR_PAY+"");

                    printBO.setMessages(printBO.getMessages()+ "\n ARREARS CONSMR PAYABLE :  "+ARRS_CONSMR_PAY);
                }
            }else if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A1
                    && bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A2){
                Log.d(" GOK PAYABLE:", temp_debit+"");
                printBO.setMessages(printBO.getMessages()+ "\n GOK PAYABLE:"+ temp_debit);
                Log.d(" ARREARS CONSMR PAYABLE :  ",ARRS_CONSMR_PAY+"");
                printBO.setMessages(printBO.getMessages()+ "\n ARREARS CONSMR PAYABLE :  "+ARRS_CONSMR_PAY);
            }

            if (bill_rec_ob.getMMD_CREDIT() > 0.01)
            {
                Log.d(" INT ON MMD/MSD:", bill_rec_ob.getMMD_CREDIT()+"");
                printBO.setMessages(printBO.getMessages()+ "\n INT ON MMD/MSD:"+ bill_rec_ob.getMMD_CREDIT());
                Log.d(" CREDITED IS SUBJECT TO AUDI","");
                printBO.setMessages(printBO.getMessages()+ "\n CREDITED IS SUBJECT TO AUDI");
            }

            if(bill_rec_ob.getADDNL3MMD() > 0.01)
            {
                Log.d(" ADDNL. 2MMD DUE:", bill_rec_ob.getADDNL3MMD()+"");
                printBO.setMessages(printBO.getMessages()+ "\n ADDNL. 2MMD DUE:"+ bill_rec_ob.getADDNL3MMD());
            }

            if( bill_rec_ob.getFLG_ECS_USER() == '1')
            {
                Log.d(" ECS USER NOT FOR PAYMENT","");
                printBO.setMessages(printBO.getMessages()+ "\n ECS USER NOT FOR PAYMENT");
            }

            if( bill_rec_ob.getCHEQDIS().equals("Y") )
            {
                Log.d(" NOT TO ACCEPT CHEQUES","");
                printBO.setMessages(printBO.getMessages()+ "\n NOT TO ACCEPT CHEQUES");
            }
            Log.d("\n\n\n\n\n","");

            SetResultMasterValues("Date and Time",printBO.getDateAndTime());
            SetResultMasterValues("Sub-Division",printBO.getSubDiv());
            SetResultMasterValues("RRNO",printBO.getRrNo());
            SetResultMasterValues("IVRS ID",printBO.getIvrsdId());
            SetResultMasterValues("Consumer Name",printBO.getConsumerName());
            SetResultMasterValues("Address-1",printBO.getAddress1());
            SetResultMasterValues("Address-2",printBO.getAddress2());
            SetResultMasterValues("Address-3",printBO.getAddress3());
            SetResultMasterValues("Meter Code",printBO.getMrCode());
            SetResultMasterValues("Tariff Code",printBO.getTariffCode());
            SetResultMasterValues("Bill Date",printBO.getBillDate());
            SetResultMasterValues("Bill Number",printBO.getBillNo());
            SetResultMasterValues("Load",printBO.getLoad());
            SetResultMasterValues("CT-PT Ratio",printBO.getCt_pt_ratio());
            SetResultMasterValues("Installation Status",printBO.getInstalltion_sts());
            SetResultMasterValues("Part Days",printBO.getPartDays());
            SetResultMasterValues("Present Reading",printBO.getPresentReading());
            SetResultMasterValues("Previous Reading",printBO.getPreviousReading());
            SetResultMasterValues("Units Consumed",printBO.getUnitsConsmp());
            SetResultMasterValues("Power Factor",printBO.getPowerFactor());
            SetResultMasterValues("Fixed Total",printBO.getFixedTotal());
            SetResultMasterValues("Energy Total",printBO.getEnergyTotal());
            SetResultMasterValues("Tax",printBO.getTax());
            SetResultMasterValues("Bill Amount",printBO.getBillAmount());
            SetResultMasterValues("Interest",printBO.getInterest());
            SetResultMasterValues("Arrears",printBO.getArrears());
            SetResultMasterValues("Arrears + Interest",printBO.getArrPlusInterest());
            SetResultMasterValues("Others",printBO.getOthers());
            SetResultMasterValues("Credit",printBO.getCredit());
            SetResultMasterValues("Rebate",printBO.getRebate());
            SetResultMasterValues("FAC",printBO.getFAC());
            SetResultMasterValues("Due Date",printBO.getDueDate());
            SetResultMasterValues("Info",printBO.getMessages());

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in prt_bill : "+e.toString());
        }

    }

    private Object RoundOffValue(double fc_total) {
        BigDecimal bigDecimal = new BigDecimal(fc_total);
        BigDecimal roundedWithScale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        Log.d("["+this.getClass().getSimpleName()+"]-->","Rounded value with setting scale = "+roundedWithScale);
        return roundedWithScale;
    }

    private void GetMeterReading() {

        try{
            ComputeBill();
        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in GetMeterReading : "+e.toString());
        }
    }

    private void AcceptMeterStatusAndComputeDlMNR() {
        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->"," \n In AcceptMeterStatusAndComputeDlMNR()");
            if(bill_rec_ob.getMNR_FLG().equals("Y")){

                bill_rec_ob.setMETER_STS("M");
                Log.d("["+this.getClass().getSimpleName()+"]-->","Meter Not Reading \nGenerating Bill Using \n AVG Consumption");
                ShowAlert("success","INFO"," Meter Not Reading \nGenerating Bill Using \n AVG Consumption");

            }else{

                if(METER_STATUS.equals("MNR")){
                    bill_rec_ob.setMETER_STS("M");
                }
                else if(METER_STATUS.equals("DL")){
                    bill_rec_ob.setMETER_STS("D");
                }else if(METER_STATUS.equals("NONE")){
                    bill_rec_ob.setMETER_STS("");
                }
            }

            ComputeBill();
        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in AcceptMeterStatusAndComputeDlMNR : "+e.toString());
        }
    }

    private void ComputeBill() {

        try
        {
            double totalamount   = 0.0;
            double DLmoreclaimed = 0.0;
            double DLlessclaimed = 0.0;
            double Units = 0.0;

            bill_rec_ob.setN_REBATE(0.0);
            bill_rec_ob.setORPHAN_RBT(0.0);
            bill_rec_ob.setPL_REBATE(0.0);
            bill_rec_ob.setPL_REBATE(0.0);
            bill_rec_ob.setRR_REBATE(0.0);
            bill_rec_ob.setFIRST_RDG_FLAG("N");
            bill_rec_ob.setFULL_MONTH_FLAG("N");

            //tariffMainBO = billing.getTariffMainFromTable(bill_rec_ob.getTariff_Code());

            //flagsBO = bill_rec_ob.getFlagsBO();

            Log.d("["+this.getClass().getSimpleName()+"]-->"," \n In ComputeBill()");

            if(bill_rec_ob.getMTR_CHNG_FLG().equals("Y")){
                bill_rec_ob.setNUMDL(0);
            }

            //if(bill_rec_ob.getMtr_Sts().equals("") && bill_rec_ob.getNumDL() != 0){
            if(bill_rec_ob.getMETER_STS().equals("") && bill_rec_ob.getNUMDL() != 0){
                Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getMtr_Sts().equals() && bill_rec_ob.getNumDL() != 0");

                if(bill_rec_ob.getLASTMONTHFRACTION() != 30){
                    Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getLastmonthfraction() != 30");

                    AdjustDL();



                    DLmoreclaimed = bill_rec_ob.getMORECLAIMED();
                    DLlessclaimed = bill_rec_ob.getLESSCLAIMED();

                    Units = bill_rec_ob.getN_UNITSCONSUMED();

                    if(bill_rec_ob.getNUMDL() > 1)
                    {
                        AdjustDL();
                        DLmoreclaimed = DLmoreclaimed + bill_rec_ob.getMORECLAIMED();
                        DLlessclaimed = DLlessclaimed + bill_rec_ob.getLESSCLAIMED();
                        Units = Units + bill_rec_ob.getN_UNITSCONSUMED();

                    }
                    GetUnitsconsumed();

                    bill_rec_ob.setN_UNITSCONSUMED((long) (bill_rec_ob.getN_UNITSCONSUMED() - Units));
                    bill_rec_ob.setMORECLAIMED(DLmoreclaimed);
                    bill_rec_ob.setLESSCLAIMED(DLlessclaimed);

                    DLComputeBill();
                }
                else{
                    Log.d("["+this.getClass().getSimpleName()+"]-->","Inside else ");
                    bill_rec_ob.setFIRST_RDG_FLAG("Y");
                    AdjustDL();
                    Units = bill_rec_ob.getN_UNITSCONSUMED();
                    GetUnitsconsumed();

                    bill_rec_ob.setN_UNITSCONSUMED((long) (bill_rec_ob.getN_UNITSCONSUMED() - Units));

                    DLComputeBill();
                }

            }
            else{
                Log.d("["+this.getClass().getSimpleName()+"]-->","inside else DL");
                if(bill_rec_ob.getFLG_FL_rebate()  == '1'){
                    Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getFlagsBO().getFL_Rebate()  == 1");
                    ComputeFreeLightingBill();
                }
                else{

                    GetUnitsconsumed();
                    ComputePFPenalty();

                    if(bill_rec_ob.getPLFLAG().equals("P")){
                        Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getPf_flag() == p");
                        bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTPL);
                        ComputeEnergyTariff();
                        bill_rec_ob.setPL_REBATE(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF());
                        bill_rec_ob.setKEY_TARIFF_CODE(bill_rec_ob.getPL_TEMP_TARIFF_CODE());
                    }

                    ComputeEnergyTariff();

                    if(bill_rec_ob.getPLFLAG().equals("P")){
                        bill_rec_ob.setPL_REBATE(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getPL_REBATE());
                    }

                    ComputeFixedTariff();

                    if(bill_rec_ob.getFLG_RURAL_REBATE() == '1'){
                        CalculateRRRebate();
                    }

                    if(bill_rec_ob.getFLG_Solar_rebate() == '1'){
                        CalculateRebate();
                    }

                    if(bill_rec_ob.getCAPACITOR() > 0.001){
                        CalculateCapRebate();
                    }

                    if(bill_rec_ob.getORPHAN_RBT() > 0.00){
                        CalculateOrphnRbt();
                    }

                    if(bill_rec_ob.getPH_REBATE() == '1'){
                        ComputePHRebate();
                        bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE()+bill_rec_ob.getPH_REBATE());
                    }

                    ComputeTax();
                    ComputeTotalAmount();

                    bill_rec_ob.setCREDIT_CF(0);
                    bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE() + bill_rec_ob.getPL_REBATE());
                    bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE() + bill_rec_ob.getORPHAN_AMOUNT());

                    if((bill_rec_ob.getTOTALBILL() - bill_rec_ob.getCREDITBF()) < 0){

                        bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDITBF() - (bill_rec_ob.getTOTALBILL() - bill_rec_ob.getN_REBATE()));

                        bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDIT_CF() * -1);
                    }
                }


                bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDIT_CF()/100 + bill_rec_ob.getLT4_DEBIT());

                totalamount = bill_rec_ob.getCREDITBF();

                FAC_TAX = CalculateFuelAdjCharges();

                bill_rec_ob.setTOTALBILL(bill_rec_ob.getTOTALBILL() - totalamount);

                getRoundDiff(bill_rec_ob.getTOTALBILL()+FAC_TAX+((double)FAC_unit)/100.0);

                if(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() > 0.0){
                    bill_rec_ob.setP_NTOTAL_ENAERGY_TARIFF(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() + bill_rec_ob.getDIFF_AMOUNT());
                }
                else{
                    if(bill_rec_ob.getTOTAL_FIXED_TARIFF() > 0.0){
                        bill_rec_ob.setTOTAL_FIXED_TARIFF(bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getDIFF_AMOUNT());
                    }
                }
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeBill : "+e.toString());
        }

    }

    private void getRoundDiff(double d) {

        double f1;

        // memset(buff36,'\0',36);
        //sprintf(buff36,"%.0f", f);
        f1 = (double) d;
        f1 = (double) (f1 - d);
        bill_rec_ob.setDIFF_AMOUNT(f1);
    }

    private double CalculateFuelAdjCharges() {

        Log.d("["+this.getClass().getSimpleName()+"]-->","CalculateFuelAdjCharges");

        FAC_unit = 0;
        try
        {

            if(bill_rec_ob.getKEY_FAC_RATE() !=  0)
            {
                FAC_unit = 	 (int) (bill_rec_ob.getN_UNITSCONSUMED() * bill_rec_ob.getKEY_FAC_RATE())  ;

                FAC_TAX	 =  (((double)FAC_unit)/100.0 * trfMain.getTariffTax() )/100.0;
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in CalculateFuelAdjCharges : "+e.toString());
        }


        return FAC_TAX;
    }

    private void CalculateCapRebate() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","CalculateCapRebate");
            double caprbt;
            caprbt = 0.0;

            caprbt = bill_rec_ob.getN_UNITSCONSUMED() * bill_rec_ob.getCAPACITOR();
            bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE() + caprbt);

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in CalculateCapRebate : "+e.toString());
        }
    }

    private void ComputeFreeLightingBill() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeFreeLightingBill");
            GetUnitsconsumed();
            ComputeFreeLightingTempTax();

            GetUnitsconsumed();
            ComputePFPenalty();
            ComputeEnergyTariff();
            ComputeFixedTariff();

            if(bill_rec_ob.getFLG_Solar_rebate() == '1'){
                CalculateRebate();
            }

            ComputeTotalAmount();

            bill_rec_ob.setCREDIT_CF(0);
            bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE() + bill_rec_ob.getPL_REBATE());
            if((bill_rec_ob.getTOTALBILL() - bill_rec_ob.getCREDITBF()) < 0){

                bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDITBF()  - (bill_rec_ob.getTOTALBILL() - bill_rec_ob.getN_REBATE()));

                bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDIT_CF() * -1);

            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeFreeLightingBill : "+e.toString());
        }

    }

    private void DLComputeBill() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","DLComputeBill");

            if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                ComputeFreeLightingTempTax();
            }
            ComputeEnergyTariff();

            if(bill_rec_ob.getFLG_Solar_rebate() == '1'){
                CalculateRebate();
            }
            ComputePFPenalty();
            ComputeFixedTariff();

            if(bill_rec_ob.getFLG_PH_rebate() == '1'){
                ComputePHRebate();
                bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE()+bill_rec_ob.getPH_REBATE());
            }

            if(bill_rec_ob.getORPHAN_RBT() > 0.00){
                CalculateOrphnRbt();
                bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE()+bill_rec_ob.getORPHAN_AMOUNT());
            }
            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                ComputeTax();
            }
            ComputeTotalAmount();

            bill_rec_ob.setCREDIT_CF(0);
            bill_rec_ob.setN_REBATE(bill_rec_ob.getN_REBATE() + bill_rec_ob.getPL_REBATE());

            if(bill_rec_ob.getTOTALBILL() - bill_rec_ob.getCREDITBF() < 0){
                bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDITBF() - (bill_rec_ob.getTOTALBILL() - bill_rec_ob.getN_REBATE()));
                bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDIT_CF() * -1);
            }

            bill_rec_ob.setTOTALBILL(bill_rec_ob.getTOTALBILL() - bill_rec_ob.getCREDITBF());

            bill_rec_ob.setTOTALBILL(bill_rec_ob.getTOTALBILL() + bill_rec_ob.getLESSCLAIMED() - bill_rec_ob.getMORECLAIMED());

            bill_rec_ob.setCREDIT_CF(bill_rec_ob.getCREDIT_CF() - bill_rec_ob.getMORECLAIMED() + bill_rec_ob.getLESSCLAIMED());

            if(bill_rec_ob.getLESSCLAIMED() > 0.0){

                bill_rec_ob.setCREDIT_CF(bill_rec_ob.getLESSCLAIMED());
            }
        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in DLComputeBill : "+e.toString());
        }


    }

    private void ComputeTotalAmount() {

        Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeTotalAmount");
        double penaltyamt;
        double sum;
        double anamt;
        double anamt1;

        try
        {
            sum = 0.0;
            penaltyamt = 0.0;
            anamt = 0.0;
            anamt1 = 0.0;
            bill_rec_ob.setLT4_DEBIT(0.0);

            penaltyamt = bill_rec_ob.getPF_PENALTY() + bill_rec_ob.getREG_PENALTY();

            sum = bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF();
            sum = sum + bill_rec_ob.getN_TAX() + bill_rec_ob.getINT_TAX();
            sum = sum + bill_rec_ob.getINT_ON_TAX() + bill_rec_ob.getKEY_DMD_ARREARS();
            sum = sum + bill_rec_ob.getKEY_INT_ARREARS() + bill_rec_ob.getINT_ARREARS2() ;
            sum = sum + bill_rec_ob.getKEY_TAX_ARREARS() + bill_rec_ob.getKEY_DELAY_INTEREST();

            bill_rec_ob.setTOTALBILL(sum + bill_rec_ob.getDEBITBF() +
                    penaltyamt);

            if (bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A)
                bill_rec_ob.setTOTALBILL(bill_rec_ob.getTOTALBILL() + bill_rec_ob.getPRSTCKWH());


            if(bill_rec_ob.getANNUAL_MIN_FIX() > 0.0 &&  bill_rec_ob.getCUR_QRTR() == 4 )
            {
                bill_rec_ob.setPREVIOUS_DEMAND1(bill_rec_ob.getPREVIOUS_DEMAND1() + bill_rec_ob.getPREVIOUS_DEMAND2());
                bill_rec_ob.setPREVIOUS_DEMAND1(bill_rec_ob.getPREVIOUS_DEMAND1() + bill_rec_ob.getPREVIOUS_DEMAND3());

                anamt = bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF();
                anamt = anamt + penaltyamt;
                anamt1 = anamt;
                anamt = anamt + bill_rec_ob.getPREVIOUS_DEMAND1();

                if(bill_rec_ob.getHP_Min_Fix() > anamt )
                    bill_rec_ob.setCREDIT_CF(bill_rec_ob.getHP_Min_Fix() - anamt);
                if(bill_rec_ob.getCUR_QRTR() != 4 )
                    bill_rec_ob.setCREDIT_CF(0.0);

                bill_rec_ob.setLT4_DEBIT(bill_rec_ob.getCREDIT_CF() - anamt1);
                bill_rec_ob.setTOTALBILL(bill_rec_ob.getTOTALBILL() + bill_rec_ob.getLT4_DEBIT());
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeTotalAmount : "+e.toString());
        }
        
    }

    private void AdjustDL() {

        double energyamtandtax = 0.0 ;
        double rbtmoreclaimed= 0.0 ;
        double rbtlessclaimed= 0.0 ;
        double wrongrbt= 0.0 ;
        double correctrbt= 0.0 ;
        double penaltyamt= 0.0 ;
        double wrongsolarrbt = 0.0 ;
        double correctsolarrbt= 0.0 ;
        double wrongPHRebate= 0.0 ;
        double correctPHRebate= 0.0 ;
        double temppartfraction= 0.0 ; // PR keep this global
        double wrongohrbt= 0.0 ;
        double corctohrbt = 0.0;
        double tempAvgConsmp;

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","AdjustDL");

            tempAvgConsmp = bill_rec_ob.getKEY_AVG_CONSUMPTION();

            GetAverageUnits();


            bill_rec_ob.setN_UNITSCONSUMED((int) bill_rec_ob.getKEY_AVG_CONSUMPTION());

            temppartfraction = bill_rec_ob.getPARTFRACTION();


            if(!bill_rec_ob.getFIRST_RDG_FLG().equals("Y")){
                bill_rec_ob.setPARTFRACTION(bill_rec_ob.getLASTMONTHFRACTION() / 30.0);
            }else{
                if(bill_rec_ob.getFIRST_RDG_FLG().equals("Y")){
                    bill_rec_ob.setPARTFRACTION(30 / 30.0);
                }
            }

            if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                ComputeFreeLightingTempTax();
                Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeFreeLightingTempTax");
            }

            if(bill_rec_ob.getPLFLAG().equals("P")){
                bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTPL);
                ComputeEnergyTariff();
                bill_rec_ob.setPL_REBATE(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF());
                bill_rec_ob.setKEY_TARIFF_CODE(bill_rec_ob.getPL_TEMP_TARIFF_CODE());
            }
            ComputeEnergyTariff();


            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                ComputeFixedTariff();
            }

            if(bill_rec_ob.getFLG_PH_rebate() == '1'){
                ComputePHRebate();
                wrongPHRebate = wrongPHRebate + bill_rec_ob.getPH_REBATE();
            }

            if(bill_rec_ob.getPLFLAG().equals("P")){
                bill_rec_ob.setPL_REBATE(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getPL_REBATE());
            }

            wrongrbt = bill_rec_ob.getPL_REBATE();

            ComputePFPenalty();
            penaltyamt = bill_rec_ob.getPF_PENALTY();

            if(bill_rec_ob.getFLG_Solar_rebate() == '1'){
                CalculateRebate();
                wrongsolarrbt = bill_rec_ob.getN_REBATE();
            }
            if(bill_rec_ob.getORPHAN_RBT() > 0.00){
                CalculateOrphnRbt();
                wrongrbt = wrongrbt + bill_rec_ob.getORPHAN_AMOUNT();
            }
            if(bill_rec_ob.getFLG_RURAL_REBATE() == '1'){
                CalculateRRRebate();
            }

            if(bill_rec_ob.getFLG_Solar_rebate() == '1'){
                CalculateRebate();
                wrongsolarrbt = bill_rec_ob.getN_REBATE();
            }
            if(bill_rec_ob.getORPHAN_RBT() > 0.00){
                CalculateOrphnRbt();
                wrongrbt = wrongrbt + bill_rec_ob.getORPHAN_AMOUNT();
            }
            if(bill_rec_ob.getFLG_RURAL_REBATE() == '1'){
                CalculateRRRebate();
            }

            if( bill_rec_ob.getFLG_FL_rebate() != '1' ){
                bill_rec_ob.setN_TAX(( bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() * (double)trfMain.getTariffTax() / 100.0));
            }

            if(bill_rec_ob.getCGEXEMPT_FLG().equals("Y") || bill_rec_ob.getCGEXEMPT_FLG().equals("y")){
                bill_rec_ob.setN_TAX(0.0);
            }

            bill_rec_ob.setENERGYAMOUNTPLUSTAX( bill_rec_ob.getN_TAX()
                    +	bill_rec_ob.getN_REBATE()
                    +	bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF()
                    +	penaltyamt
                    +	wrongrbt
                    + 	wrongPHRebate);


            bill_rec_ob.setENERGYAMOUNTPLUSTAX(bill_rec_ob.getENERGYAMOUNTPLUSTAX() - wrongrbt);
            GetUnitsconsumed();
            GetpartunitsinAdj(temppartfraction);

            ComputePFPenalty();
            penaltyamt = bill_rec_ob.getPF_PENALTY();

            if(bill_rec_ob.getPF_FLAG().equals("P")){
                bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTPL);
                ComputeEnergyTariff();
                bill_rec_ob.setPL_REBATE(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF());
                bill_rec_ob.setKEY_TARIFF_CODE(bill_rec_ob.getPL_TEMP_TARIFF_CODE());
            }
            ComputeEnergyTariff();

            if(bill_rec_ob.getFLG_PH_rebate() != '1'){
                ComputeFixedTariff();
            }

            if(bill_rec_ob.getFLG_PH_rebate() == '1' ){
                ComputePHRebate();
                correctPHRebate = correctPHRebate + bill_rec_ob.getPH_REBATE();
            }

            if(bill_rec_ob.getPF_FLAG().equals("P"))
            {
                bill_rec_ob.setPL_REBATE(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getPL_REBATE());
            }
            correctrbt = bill_rec_ob.getPL_REBATE();

            if(bill_rec_ob.getFLG_Solar_rebate() == '1'){
                CalculateRebate();
                correctrbt = bill_rec_ob.getN_REBATE();
            }
            if(bill_rec_ob.getORPHAN_RBT() > 0.00){
                CalculateOrphnRbt();
                corctohrbt = corctohrbt + bill_rec_ob.getORPHAN_AMOUNT();
            }



            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                bill_rec_ob.setN_TAX(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() * (double)trfMain.getTariffTax() / 100.0);
            }

            if(bill_rec_ob.getCGEXEMPT_FLG().equals("Y")){
                bill_rec_ob.setN_TAX(0.0);
            }

            energyamtandtax = 	bill_rec_ob.getN_TAX()
                    +	penaltyamt
                    +	bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF()
                    +	bill_rec_ob.getN_REBATE()
                    -	correctrbt
                    -	correctPHRebate;

            energyamtandtax = energyamtandtax  - corctohrbt;

            if(energyamtandtax < bill_rec_ob.getENERGYAMOUNTPLUSTAX()){
                bill_rec_ob.setMORECLAIMED(bill_rec_ob.getENERGYAMOUNTPLUSTAX() - energyamtandtax);
            }else{
                bill_rec_ob.setLESSCLAIMED(energyamtandtax - bill_rec_ob.getENERGYAMOUNTPLUSTAX());
            }

            if(bill_rec_ob.getFULL_MONTH_FLAG().equals("Y")){

                if(bill_rec_ob.getLASTMONTHFRACTION() == 30){
                    bill_rec_ob.setMORECLAIMED(bill_rec_ob.getMORECLAIMED() * bill_rec_ob.getNUMDL());

                    bill_rec_ob.setLESSCLAIMED(bill_rec_ob.getLESSCLAIMED() * bill_rec_ob.getNUMDL());

                    bill_rec_ob.setN_UNITSCONSUMED(bill_rec_ob.getN_UNITSCONSUMED() * bill_rec_ob.getNUMDL());
                }
                else{
                    bill_rec_ob.setMORECLAIMED(bill_rec_ob.getMORECLAIMED() * (bill_rec_ob.getNUMDL() - 1));

                    bill_rec_ob.setLESSCLAIMED(bill_rec_ob.getLESSCLAIMED() * (bill_rec_ob.getNUMDL() - 1));

                    bill_rec_ob.setN_UNITSCONSUMED(bill_rec_ob.getN_UNITSCONSUMED() * (bill_rec_ob.getNUMDL() - 1));

                }
            }

            bill_rec_ob.setPARTFRACTION(temppartfraction);
            bill_rec_ob.setKEY_AVG_CONSUMPTION(tempAvgConsmp);

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in AdjustDL : "+e.toString());
        }
    }

    private void GetpartunitsinAdj(double temppartfraction) {

        double Units;
        double partround;
        int thismonthfraction;
        int days;

        try
        {
            Units = 0.0;
            partround = 0.0;
            thismonthfraction = 0;
            days = 0;

            partround = temppartfraction * 30;
            // partround = toFloat(temp);
            thismonthfraction = (int) partround;

            if(bill_rec_ob.getLASTMONTHFRACTION() != 30 && !bill_rec_ob.getFIRST_RDG_FLAG().equals("Y"))
            {
                if(thismonthfraction != 30)
                {
                    days = (int) (bill_rec_ob.getLASTMONTHFRACTION() + partround);
                    days = (int) (days + (30.0 * (bill_rec_ob.getNUMDL() - 1)));
                }
                else{
                    days = (int) (bill_rec_ob.getLASTMONTHFRACTION() + 30.0 * bill_rec_ob.getNUMDL());
                }

                Units = (bill_rec_ob.getN_UNITSCONSUMED()* bill_rec_ob.getLASTMONTHFRACTION()) / days;

                bill_rec_ob.setN_UNITSCONSUMED((long) Units);
                bill_rec_ob.setFIRST_RDG_FLAG("Y");

            }
            else
            if(bill_rec_ob.getFIRST_RDG_FLAG().equals("Y"))
            {
                if(thismonthfraction != 30)
                {
                    days = (int) (bill_rec_ob.getLASTMONTHFRACTION() + partround);
                    days = (int) (days + (30.0 * (bill_rec_ob.getNUMDL() - 1)));
                }
                else
                    days = (int) (bill_rec_ob.getLASTMONTHFRACTION() + (30.0 * bill_rec_ob.getNUMDL()));

                Units = (bill_rec_ob.getN_UNITSCONSUMED() * 30.0) / days;
                bill_rec_ob.setN_UNITSCONSUMED((long) Units);
                bill_rec_ob.setFIRST_RDG_FLAG("Y");

            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in GetpartunitsinAdj : "+e.toString());
        }
    }

    private void GetUnitsconsumed() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","GetUnitsconsumed");
            double actualunits  , Diff ;
            double tempUnits , partround ;
            int    currentperc ;
            long   templ ;

            currentperc  = 0;
            actualunits = 0;
            tempUnits = 0.0;
            partround = 0.0;
            Diff = 0.0;
            templ = 0L;
            int k = 0;

            if( bill_rec_ob.getKEY_INSTALLATION_STATUS() == 10 ||
                    bill_rec_ob.getKEY_INSTALLATION_STATUS() == 2 ){

                bill_rec_ob.setPRESENT_RDG(bill_rec_ob.getKEY_PREV_MTR_RDG());
            }


            if(bill_rec_ob.getMETER_STS().equals("M") || bill_rec_ob.getMETER_STS().equals("D")){

                if( bill_rec_ob.getNUMDL() > 0 )
                {
                    partround = (double)bill_rec_ob.getPARTFRACTION() * 30.0;

                    tempUnits = ((bill_rec_ob.getKEY_AVG_CONSUMPTION() * partround) / (double) bill_rec_ob.getLASTMONTHFRACTION());
                    bill_rec_ob.setDIFF_AMOUNT(Math.round(tempUnits));


                    Diff = Math.round(bill_rec_ob.getDIFF_AMOUNT());

                    if(Diff != 0.50){

                    }else{
                        tempUnits = tempUnits + Diff;
                    }
                    bill_rec_ob.setN_UNITSCONSUMED((long) tempUnits);

                }
                else{
                    Log.d("["+this.getClass().getSimpleName()+"]-->","CHECK NUMDL");
                    Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getPartFraction() : "+bill_rec_ob.getPARTFRACTION());
                    Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getAvg_Consumption() : "+bill_rec_ob.getKEY_AVG_CONSUMPTION());

                    partround = (double)bill_rec_ob.getPARTFRACTION() * 30.0;
                    Log.d("["+this.getClass().getSimpleName()+"]-->","(double)bill_rec_ob.getPartFraction() * 30.0 : "+partround);

                    partround = partround / 30.0;
                    Log.d("["+this.getClass().getSimpleName()+"]-->","partround / 30.0 : "+partround);

                    partround = partround * bill_rec_ob.getKEY_AVG_CONSUMPTION();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","partround * bill_rec_ob.getAvg_Consumption() : "+partround);

                    double temp = partround + 0.01;

                    bill_rec_ob.setN_UNITSCONSUMED((long) temp);
                    Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getnUnitsConsumed() : "+bill_rec_ob.getN_UNITSCONSUMED());
                }
                partround = (double)bill_rec_ob.getPARTFRACTION() * 30.0;
                Log.d("["+this.getClass().getSimpleName()+"]-->","(double)bill_rec_ob.getPartFraction() * 30.0 : "+partround);

                // partround = partround / 30.0;

                partround = partround * bill_rec_ob.getKEY_AVG_CONSUMPTION();
                Log.d("["+this.getClass().getSimpleName()+"]-->","partround * bill_rec_ob.getAvg_Consumption() : "+partround);

                partround = partround / 30.0;
                Log.d("["+this.getClass().getSimpleName()+"]-->","partround = partround / 30.0 : "+partround);

                //double temp = partround + 0.01;

                bill_rec_ob.setN_UNITSCONSUMED((long) partround);
                Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getnUnitsConsumed() : "+bill_rec_ob.getN_UNITSCONSUMED());

            /*bill_rec_ob.setnUnitsConsumed((long) temp);*/

                bill_rec_ob.setPRESENT_RDG(0);
                Log.d("["+this.getClass().getSimpleName()+"]-->","bill_rec_ob.getPrst_Rdg() : "+bill_rec_ob.getPRESENT_RDG());


            }else{


                Log.d("["+this.getClass().getSimpleName()+"]-->",
                        "bill_rec_ob.getPrst_Rdg() < bill_rec_ob.getPrev_Mtr_Rdg()  : "+bill_rec_ob.getPRESENT_RDG() + " " +bill_rec_ob.getKEY_PREV_MTR_RDG());
                if(bill_rec_ob.getPRESENT_RDG() < bill_rec_ob.getKEY_PREV_MTR_RDG()){

                    if( (bill_rec_ob.getKEY_PREV_MTR_RDG() <= 999) && (bill_rec_ob.getPRESENT_RDG() <= 999)){

                        bill_rec_ob.setN_UNITSCONSUMED(1000 - bill_rec_ob.getKEY_PREV_MTR_RDG() + bill_rec_ob.getPRESENT_RDG());
                    }
                    else{

                        if( (bill_rec_ob.getKEY_PREV_MTR_RDG() <= 9999) && (bill_rec_ob.getPRESENT_RDG() <= 9999)){

                            bill_rec_ob.setN_UNITSCONSUMED(10000 - bill_rec_ob.getKEY_PREV_MTR_RDG() + bill_rec_ob.getPRESENT_RDG());

                        }else{

                            if( (bill_rec_ob.getKEY_PREV_MTR_RDG() <= 99999l) && (bill_rec_ob.getPRESENT_RDG() <= 99999l)){

                                bill_rec_ob.setN_UNITSCONSUMED(100000l 	- (long)bill_rec_ob.getKEY_PREV_MTR_RDG() + (long)bill_rec_ob.getPRESENT_RDG());

                            }else{

                                bill_rec_ob.setN_UNITSCONSUMED(1000000l - (long)bill_rec_ob.getKEY_PREV_MTR_RDG() + (long) bill_rec_ob.getPRESENT_RDG());

                            }
                        }

                    }


                }else{

                    Log.d("["+this.getClass().getSimpleName()+"]-->",
                            "(long)bill_rec_ob.getPrst_Rdg() + (long)bill_rec_ob.getPrev_Mtr_Rdg()  : "+(long)bill_rec_ob.getPRESENT_RDG()
                            + " " +(long)bill_rec_ob.getKEY_PREV_MTR_RDG());
                    bill_rec_ob.setN_UNITSCONSUMED((long)bill_rec_ob.getPRESENT_RDG() - (long)bill_rec_ob.getKEY_PREV_MTR_RDG() );

                }

            }

            if(bill_rec_ob.getINSTAL_TYP().equals("4") || bill_rec_ob.getINSTAL_TYP().equals("2") || bill_rec_ob.getINSTAL_TYP().equals("1") ){

            }else{
                bill_rec_ob.setN_UNITSCONSUMED((long) (bill_rec_ob.getN_UNITSCONSUMED() * bill_rec_ob.getKEY_CT_PT()));

            }

            if(bill_rec_ob.getCREAPING_PERC() != 0){
                currentperc  = 100 + bill_rec_ob.getCREAPING_PERC();
                actualunits = (double) bill_rec_ob.getN_UNITSCONSUMED() * 100 / currentperc;

                bill_rec_ob.setN_UNITSCONSUMED((long) actualunits);


            }

            if(bill_rec_ob.getDC_FLG().equals("Y"))
            {

                GetDCUnits();
            }else{

                if(bill_rec_ob.getFIRST_RDG_FLG().equals("Y") ){

                    if(bill_rec_ob.getMETER_STS().equals("M") || bill_rec_ob.getMETER_STS().equals("D")){
                        GetDCUnits();
                    }

                }
            }

            bill_rec_ob.setOLD_MTR_RDG((long) (bill_rec_ob.getOLD_MTR_RDG() * bill_rec_ob.getKEY_CT_PT()));

            bill_rec_ob.setN_UNITSCONSUMED(bill_rec_ob.getN_UNITSCONSUMED() + bill_rec_ob.getOLD_MTR_RDG());

            partround = bill_rec_ob.getPARTFRACTION() * 30.0;
            partround = partround / 30.0;
            partround = (double)partround;

            partround = partround * 18.0;
            templ = (long)partround;

            if((bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A)
                    && (bill_rec_ob.getN_UNITSCONSUMED() > templ) ){

                currentperc = 0;
                double temp = bill_rec_ob.getKEY_OTHERS();
                currentperc = (int) temp;

                if(currentperc == 0){
                    bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LT2AI);
                }
                if(currentperc == 1){
                    bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LT2AI);
                }
                if(currentperc == 3){
                    bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LT2AII);
                }
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in GetUnitsconsumed : "+e.toString());
        }

    }

    private void GetDCUnits() {

        Log.d("["+this.getClass().getSimpleName()+"]-->","check GetDCUnits()");


        try
        {
            int i,j;
            double flkw , Units;
            double partround , tempHPRound;
            long KW;

            tempHPRound = 0.0;
            partround = 0.0;
            i = 0;
            j = 0;
            KW = 0L;
            flkw = 0.0;
            Units = 0.0;

            tempHPRound = bill_rec_ob.getHP_ROUND();
            bill_rec_ob.setTempKW_Round((int) bill_rec_ob.getKW_ROUND());
            partround = bill_rec_ob.getPARTFRACTION() * 30;
            partround = Math.round(partround);

            if( bill_rec_ob.getFLG_FL_rebate() == '1' ){
                bill_rec_ob.setKEY_TARIFF_CODE(bill_rec_ob.getTEMP_TARIFF_CODE());
            }


            ArrayList<TariffMainBO> tariflist = databaseImplementation.getTariffMain();

            Iterator<TariffMainBO> itmain = tariflist.iterator();

            boolean continue_code = false;
            while(itmain.hasNext()){
                trfMain = (TariffMainBO)itmain.next();
                if(bill_rec_ob.getKEY_TARIFF_CODE() == trfMain.getTariffCode()){
                    continue_code = true;
                    break;
                }
            }

            if(continue_code){

                if(trfMain.getTariffPowerUnit().equals("K")){
                    flkw = ((double)bill_rec_ob.getHP_ROUND()) / 100.0 ;
                    flkw = (flkw * 0.746) * 100.0;
                    bill_rec_ob.setKW_ROUND((long)bill_rec_ob.getKW_ROUND() + flkw);
                }else{
                    if(trfMain.getTariffPowerUnit().equals("H")){

                        flkw = ((double)bill_rec_ob.getKW_ROUND())/100.0;
                        flkw = (flkw / 0.746) / 100.0;
                        bill_rec_ob.setHP_ROUND((double)bill_rec_ob.getHP_ROUND() + flkw);
                        bill_rec_ob.setKW_ROUND(bill_rec_ob.getHP_ROUND());
                    }
                }

                KW = (long)bill_rec_ob.getKW_ROUND();

                i = 0;
                if( KW > 0L )
                {
                    if( (KW % 25L) > 0 )
                    {
                        j = (int) (KW % 25L);
                        if( j > 12 )
                            j = 25;
                        else
                            j = 0;
                        KW = (KW - (KW % 25L)) + (long) j;
                    }

	    		/*if( KW  < 100L ){
	    			KW = 100L;
	    		}*/
                }
            }


            flkw = (double) trfMain.getDCUnits() / 10000.0;

            Units = (double) KW * flkw;
            Units = Units * bill_rec_ob.getKEY_CT_PT();
            Units = Units * partround / 30.0 ;

            bill_rec_ob.setN_UNITSCONSUMED(Math.round(Units));
            bill_rec_ob.setKW_ROUND(bill_rec_ob.getTempKW_Round());
            bill_rec_ob.setHP_ROUND(tempHPRound);


            if( bill_rec_ob.getFLG_FL_rebate() == '1' )
            {
                if (bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT2AI){
                    bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTFL1);}
                else if (bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT2AII){
                    bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTFL2);}
                else if (bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT2AIII){
                    bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTFL3);}
            }

            if (bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A)  // == 1
            {
                partround = bill_rec_ob.getPARTFRACTION() * 30;
                partround = Math.round(partround);
                Units = (long) trfMain.getDCUnits() / 100.0 ;
                Units = Units * partround / 30.0 ;

                bill_rec_ob.setN_UNITSCONSUMED(Math.round(Units));
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in GetDCUnits : "+e.toString());
        }
        
    }

    private void CalculateRRRebate() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","CalculateRRRebate");
            double rrrebate;

            rrrebate = 0.0;

            tarifRebate =  databaseImplementation.getTariffRebatebyRow(4);

            if(tarifRebate !=  null){

                rrrebate = bill_rec_ob.getTOTAL_FIXED_TARIFF() * tarifRebate.getRebate() / 100.0;
            }
            bill_rec_ob.setRR_REBATE(rrrebate);

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in CalculateRRRebate : "+e.toString());
        }

    }

    private void CalculateOrphnRbt() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","CalculateOrphnRbt");
            double orphnrbt = 0.0;

            orphnrbt = bill_rec_ob.getORPHAN_RBT() * bill_rec_ob.getN_UNITSCONSUMED();
            bill_rec_ob.setORPHAN_AMOUNT(bill_rec_ob.getORPHAN_AMOUNT() + orphnrbt); ;

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in CalculateOrphnRbt : "+e.toString());
        }
    }

    private void CalculateRebate() {

        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","Inside Solra CalculateRebate");
            double nTemp, Solar_Rate,Solar_Units;
            double Solar_Max_Amt, Solar_Units_Limit;

            nTemp = 0.0;
            Solar_Rate = 50.0;
            Solar_Units = 100.0;
            Solar_Max_Amt = 0.0;
            Solar_Units_Limit = 0.0;

            Solar_Max_Amt = (Solar_Rate * bill_rec_ob.getPARTFRACTION());
            Solar_Units_Limit = (Solar_Units *  bill_rec_ob.getPARTFRACTION());
            if( bill_rec_ob.getN_UNITSCONSUMED() >= (long) Solar_Units_Limit )
                nTemp = Solar_Max_Amt;
            else
                nTemp =((double) (Solar_Rate * bill_rec_ob.getN_UNITSCONSUMED()) / 100.0);
            bill_rec_ob.setN_REBATE(nTemp);

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in CalculateRebate : "+e.toString());
        }

    }

    private void ComputePHRebate() {
        Log.d("["+this.getClass().getSimpleName()+"]-->","ComputePHRebate");
        double Erebate;
        double Frebate;

        try
        {
            Erebate = 0.0;
            Frebate = 0.0;

            if (bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT3I ||
                    bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT3II ||
                    bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT3)
            {
                Erebate = bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() *TariffConstants.PHREBATEPERCENT;
                Erebate = Erebate / 100;
                Frebate = bill_rec_ob.getTOTAL_FIXED_TARIFF() * TariffConstants.PHREBATEPERCENT;
                Frebate = Frebate / 100;
                Erebate = Erebate + Frebate;
                bill_rec_ob.setPH_REBATE(bill_rec_ob.getPH_REBATE()  + Erebate);
            }
            else
            {
                Frebate = (double) bill_rec_ob.getPARTFRACTION() * 30.0;
                Erebate = bill_rec_ob.getN_UNITSCONSUMED() *  Frebate / 30.0;
                Erebate = Erebate * 0.25;
                bill_rec_ob.setPH_REBATE(bill_rec_ob.getPH_REBATE()  + Erebate);
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputePHRebate : "+e.toString());
        }


    }

    private void ComputeFreeLightingTempTax() {

        Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeFreeLightingTempTax");
        bill_rec_ob.setKEY_TARIFF_CODE(bill_rec_ob.getTEMP_TARIFF_CODE());

        try
        {
            ComputePFPenalty();
            ComputeEnergyTariff();
            ComputeFixedTariff();
            ComputeTax();

            if(bill_rec_ob.getTEMP_TARIFF_CODE() == TariffConstants.LT2AI){
                bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTFL1);
            }else if(bill_rec_ob.getTEMP_TARIFF_CODE() == TariffConstants.LT2AII){
                bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTFL2);
            }else if(bill_rec_ob.getTEMP_TARIFF_CODE() == TariffConstants.LT2AIII){
                bill_rec_ob.setKEY_TARIFF_CODE(TariffConstants.LTFL3);
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeFreeLightingTempTax : "+e.toString());
        }

    }

    private void ComputeTax() {
        Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeTax");
        double sum;

        try
        {
            sum = 0.00;

            if(bill_rec_ob.getKEY_TARIFF_CODE() == trfMain.getTariffCode()){

                sum = bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF()
                        + bill_rec_ob.getPF_PENALTY();
                sum = sum + bill_rec_ob.getKEY_INT_ARREARS() + bill_rec_ob.getNO_TAX_COMP();
                sum = sum + bill_rec_ob.getDEBITBF() + bill_rec_ob.getREG_PENALTY();
                sum = sum + bill_rec_ob.getINT_ON_TAX() + bill_rec_ob.getKEY_DELAY_INTEREST();

                bill_rec_ob.setN_TAX((bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getN_REBATE() )  * ((double)trfMain.getTariffTax() / 100.0));

                if(bill_rec_ob.getCGEXEMPT_FLG().equals("Y")){
                    bill_rec_ob.setN_TAX(0.0);
                }

            }
        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeTax : "+e.toString());
        }


    }

    private void ComputeFixedTariff() {

        Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeFixedTariff");

        int i = 0,j = 0;
        long nUnits = 0L;
        long KW = 0L ;
        double flkw = 0.0 , tempKW  = 0.0,KWRound=0.0;
        double tempHPRound=0.0;
        double partround=0.0;
        double adj=0.0;
        double tempf = 0.0;


        try
        {
            bill_rec_ob.setTOTAL_FIXED_TARIFF(0.0);
            bill_rec_ob.setHP_Min_Fix(0.0);


            String TARIIF_CODE = "";
            //TariffMainBO trfMain = null;
            TariffFixBO tariffix = null;

            ArrayList<TariffMainBO> tariflist = databaseImplementation.getTariffMain();

            FixCompInfoTempBO fixCompInfoTemp = new FixCompInfoTempBO();

            Iterator<TariffMainBO> itmain = tariflist.iterator();

            boolean continue_code = false;
            while(itmain.hasNext()){
                trfMain = (TariffMainBO)itmain.next();
                if(bill_rec_ob.getKEY_TARIFF_CODE() == trfMain.getTariffCode()){
                    continue_code = true;
                    break;
                }
            }

            if(continue_code){
                Log.d("["+this.getClass().getSimpleName()+"]-->"," 1.bill_rec_ob.getHP_Round() : "+bill_rec_ob.getHP_ROUND());
                Log.d("["+this.getClass().getSimpleName()+"]-->"," 1.bill_rec_ob.getKW_Round() : "+bill_rec_ob.getKW_ROUND());
                if(trfMain.getTariffPowerUnit().equals("K")){
                    flkw = ((double)bill_rec_ob.getHP_ROUND()) / 100.0 ;
                    flkw = (flkw * 0.746) * 100.0;
                    bill_rec_ob.setKW_ROUND(bill_rec_ob.getKW_ROUND() + (long)flkw);
                }else{
                    if(trfMain.getTariffPowerUnit().equals("H")){

                        flkw = ((double)bill_rec_ob.getKW_ROUND())/100.0;
                        flkw = (flkw / 0.746) * 100.0;
                        bill_rec_ob.setHP_ROUND(bill_rec_ob.getHP_ROUND() + (long)flkw);
                        bill_rec_ob.setKW_ROUND(bill_rec_ob.getHP_ROUND());
                    }
                }
                Log.d("["+this.getClass().getSimpleName()+"]-->"," 2.bill_rec_ob.getKW_Round() : "+bill_rec_ob.getKW_ROUND());

                KW = (long)bill_rec_ob.getKW_ROUND();
                Log.d("["+this.getClass().getSimpleName()+"]-->"," KW : "+KW);
                i = 0;
                if( KW > 0L )
                {
                    if( (KW % 25L) > 0 )
                    {
                        j = (int) (KW % 25L);
                        if( j > 12 )
                            j = 25;
                        else
                            j = 0;
                        KW = (KW - (KW % 25L)) + (long) j;
                    }

                    if( KW  < 100L )
                        KW = 100L;
                }

                Log.d("["+this.getClass().getSimpleName()+"]-->"," after KW : "+KW);
                tempKW = (double)KW / 100.0 ;

                bill_rec_ob.setHP_Min_Fix(bill_rec_ob.getANNUAL_MIN_FIX() *  tempKW);

                for(j = 0; j < 5; j++)
                {
                    FixedChargeListBO fixed =  new FixedChargeListBO();
                    //FixCompInfoTemp fixCompInfoTemp = new FixCompInfoTemp();

                    fixed.setUnits(0);
                    fixed.setRate(0L);
                    fixCompInfoTemp.setUnits(0);
                    fixCompInfoTemp.setRate(0);
                    fixCompInfoTemp.setAmount(0.0);
                }

                partround = bill_rec_ob.getPARTFRACTION() * 30.0;
                Log.d("["+this.getClass().getSimpleName()+"]-->","partround"+partround);
                partround = (double)partround;
                Log.d("["+this.getClass().getSimpleName()+"]-->","partround"+partround);
                KWRound   =  KW * partround  / 30.0;
                KWRound   =  KWRound +  0.05 ;
                Log.d("["+this.getClass().getSimpleName()+"]-->","KWRound"+KWRound);
                KW = (long) KWRound;

                Log.d("["+this.getClass().getSimpleName()+"]-->"," after long KWRound"+KWRound);

                if(     bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A1  ||
                        bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A2  ||
                        bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4B   ||
                        bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4CI  ||
                        bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4CII   ){

                    if(bill_rec_ob.getCAPACITOR() > 0.0){

                    }else{
                        bill_rec_ob.setRR_REBATE(KW / 100.0 * ( 5.0 * partround * bill_rec_ob.getFREQUENCY()));
                    }

                }

                ArrayList<FixedChargeListBO> fixedList = new ArrayList<FixedChargeListBO>();

                ArrayList<TariffFixBO> tariffixList = databaseImplementation.getTariffFix();

                Log.d("["+this.getClass().getSimpleName()+"]-->","billing.getTariffFix() "+ tariffixList.size()+"\n KW = 0L :::: "+KW);

                Iterator<TariffFixBO> itfix = tariffixList.iterator();

                while(itfix.hasNext()){

                    tariffix  = (TariffFixBO)itfix.next();

                    if((tariffix.getTariffCode() == bill_rec_ob.getKEY_TARIFF_CODE()) && KW > 0L){

                        if(tariffix.getTo() != -1){

                            nUnits = (long) ((tariffix.getTo() - tariffix.getFrom()) * bill_rec_ob.getPARTFRACTION() * 100);

                        }else{

                            nUnits = KW;
                        }

                        KW = KW -nUnits;

                        FixedChargeListBO fixed =  new FixedChargeListBO();
                        //FixCompInfoTemp fixCompInfoTemp = new FixCompInfoTemp();

                        fixCompInfoTemp.setUnits(nUnits);
                        fixCompInfoTemp.setRate((long)tariffix.getTariff());

                        fixed.setUnits(fixCompInfoTemp.getUnits());
                        fixed.setRate((long)fixCompInfoTemp.getRate());

                        Log.d("["+this.getClass().getSimpleName()+"]-->","nUnits : "+nUnits);
                        Log.d("["+this.getClass().getSimpleName()+"]-->","long)tariffix.getTariff() : "+(long)tariffix.getTariff());


                        Log.d("["+this.getClass().getSimpleName()+"]-->","fixCompInfoTemp.getUnits() : "+fixCompInfoTemp.getUnits());
                        Log.d("["+this.getClass().getSimpleName()+"]-->","(long)fixCompInfoTemp.getRate() : "+(long)fixCompInfoTemp.getRate());
                        fixCompInfoTemp.setAmount((double)nUnits * (double)tariffix.getTariff());
                        fixCompInfoTemp.setAmount(fixCompInfoTemp.getAmount() / 10000.0);
                        fixCompInfoTemp.setAmount(fixCompInfoTemp.getAmount());

                        fixed.setAmount((double)nUnits * (double)tariffix.getTariff());
                        fixed.setAmount(fixCompInfoTemp.getAmount() / 10000.0);
                        fixed.setAmount(fixCompInfoTemp.getAmount());

                        bill_rec_ob.setTOTAL_FIXED_TARIFF(bill_rec_ob.getTOTAL_FIXED_TARIFF() + fixCompInfoTemp.getAmount());

                        FixedSlabCount ++;

                        fixedList.add(fixed);
                    }


                }

                bill_rec_ob.setFixedChargeListBOs(fixedList);

                ArrayList<FixedChargeListBO> fixedList2 = new ArrayList<FixedChargeListBO>();

                if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT2B ||
                        bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT2BI ||
                        bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT2BII ){

                    FixedChargeListBO fixedChargeList = new FixedChargeListBO();

                    tempKW =  tempKW  * fixCompInfoTemp.getRate() / 100 ;
                    if(tempKW < trfMain.getMinFixed()){

                        adj = (partround *  100 ) / 30.0 ;
                        adj = adj + 0.5 ;

                        fixCompInfoTemp.setUnits((long) adj);

                        fixCompInfoTemp.setAmount(trfMain.getMinFixed() * fixCompInfoTemp.getUnits() / 100.0);

                        bill_rec_ob.setTOTAL_FIXED_TARIFF(fixCompInfoTemp.getAmount());

                        fixCompInfoTemp.setRate(trfMain.getMinFixed() * 100);

                        fixedChargeList.setUnits(fixCompInfoTemp.getUnits());

                        fixedChargeList.setRate(fixCompInfoTemp.getRate());

                        fixedChargeList.setAmount(trfMain.getMinFixed() * fixCompInfoTemp.getUnits() / 100.0);

                        fixedList2.add(fixedChargeList);
                    }

                    bill_rec_ob.setFixedChargeListBOs(fixedList2);
                }



                //bill_rec_ob.setKW_Round(0);

                //bill_rec_ob.setKW_Round(bill_rec_ob.getTempKW_Round());
                //bill_rec_ob.setHP_Round(tempHPRound);
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeFixedTariff : "+e.toString());
        }
    }

    private void ComputeEnergyTariff() {

        long   nUnits = 0;
        long   nUnitsConsumed = 0;
        int    unitsinslab = 0;
        double diff = 0.0;
        double partmul = 0.0;
        double partround = 0.0;

        int    g_eccnt = 0;
        int    iloop = 0;
        int    jloop = 0;


        try
        {
            Log.d("["+this.getClass().getSimpleName()+"]-->","ComputeEnergyTariff");
            bill_rec_ob.setP_NTOTAL_ENAERGY_TARIFF(0.0);

            nUnitsConsumed = (long)bill_rec_ob.getN_UNITSCONSUMED();
		 /*EnergyChargeList EnergyList = new EnergyChargeList();
		 CompInfoTemp compInfoTemp =  new CompInfoTemp();*/

            ArrayList<EnergyChargeListBO> al = new ArrayList<EnergyChargeListBO>();

            for(iloop = 0; iloop < 6; iloop++)
            {


                EnergyChargeListBO EnergyList = new EnergyChargeListBO();
                CompInfoTempBO compInfoTemp =  new CompInfoTempBO();


                EnergyList.setUnits(0);
                EnergyList.setRate(0);
                compInfoTemp.setUnits(0L);
                compInfoTemp.setRate(0);

                al.add(EnergyList);

            }

            bill_rec_ob.setEnergyChargeListBOs(al);

            partround = Math.round(bill_rec_ob.getPARTFRACTION() * 30.0);


            //Fetching Tariff Slabs From Table Nothing but Energy slabs

            ArrayList<TariffSlabBO> TariffEnergySlab =  new ArrayList<TariffSlabBO>();


            ArrayList<EnergyChargeListBO> alenergy1 = new ArrayList<EnergyChargeListBO>();

            TariffEnergySlab  = databaseImplementation.getEnergySlabs();

            Iterator<TariffSlabBO> it = TariffEnergySlab.iterator();

            while(it.hasNext()){

                TariffSlabBO tariffslab = (TariffSlabBO)it.next();

                if(bill_rec_ob.getKEY_TARIFF_CODE() == tariffslab.getTariffCode()){

                    unitsinslab = (int) ((tariffslab.getToUnits() - tariffslab.getFromUnits()) * partround);

                    unitsinslab = unitsinslab / 30 ;

                    if(tariffslab.getToUnits() != -1 && nUnitsConsumed > unitsinslab){

                        if(tariffslab.getFromUnits() == 0){
                            diff = (tariffslab.getToUnits() - tariffslab.getFromUnits())  * partround / 30 ;
                        }
                        else{
                            diff = (tariffslab.getToUnits() - tariffslab.getFromUnits() + 1)  * partround / 30 ;
                        }
                        nUnits = (long) diff;

                    }
                    else{
                        nUnits = nUnitsConsumed;
                    }

                    nUnitsConsumed = nUnitsConsumed - nUnits;

                    CompInfoTempBO compInfoTemp =  new CompInfoTempBO();
                    EnergyChargeListBO EnergyList = new EnergyChargeListBO();

                    compInfoTemp.setUnits(nUnits);
                    EnergyList.setUnits(nUnits);
                    compInfoTemp.setRate(tariffslab.getTariff());
                    EnergyList.setRate(compInfoTemp.getRate());
                    compInfoTemp.setAmount(((double)nUnits *  (double)tariffslab.getTariff()) / 100.0);
                    EnergyList.setAmount(((double)nUnits *  (double)tariffslab.getTariff()) / 100.0);
                    bill_rec_ob.setP_NTOTAL_ENAERGY_TARIFF(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() + compInfoTemp.getAmount());

                    alenergy1.add(EnergyList);

                    EnergySlabCount++;

                    Log.d("["+this.getClass().getSimpleName()+"]-->",EnergyList.getUnits() + " ~ "+EnergyList.getRate()
                            + " ~ "+bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF());
                }
            }

            bill_rec_ob.setEnergyChargeListBOs(alenergy1);

            ArrayList<EnergyChargeListBO> alenergy2 = new ArrayList<EnergyChargeListBO>();

            if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A){


                CompInfoTempBO compInfoTemp =  new CompInfoTempBO();
                EnergyChargeListBO EnergyList = new EnergyChargeListBO();


                partmul = partround  / 30.0;

                partmul = partmul * 30.0;

                if(bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF()  <  partmul ){

                    bill_rec_ob.setP_NTOTAL_ENAERGY_TARIFF(0.0);
                    bill_rec_ob.setPRSTCKWH(bill_rec_ob.getPRSTCKWH() + partmul);

                    for (iloop=0; iloop<6; iloop++)
                    {
                        EnergyList.setRate(0);
                        EnergyList.setUnits(0);
                        compInfoTemp.setRate(0);
                        compInfoTemp.setUnits(0);

                        alenergy2.add(EnergyList);
                    }

                    bill_rec_ob.setEnergyChargeListBOs(alenergy2);

                }
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputeEnergyTariff : "+e.toString());
        }

    }

    private void ComputePFPenalty() {
        Log.d("["+this.getClass().getSimpleName()+"]-->","ComputePFPenalty");

        try
        {
            if(bill_rec_ob.getPF_FLAG().equals("Y") && bill_rec_ob.getKEY_POWER_FACTOR() < TariffConstants.POWERFACTLMT){
                bill_rec_ob.setPF_PENALTY((TariffConstants.POWERFACTLMT - bill_rec_ob.getKEY_POWER_FACTOR())*TariffConstants.LTPFPENAMT);
            }else{
                bill_rec_ob.setPF_PENALTY(0.0);
            }

            bill_rec_ob.setPF_PENALTY(bill_rec_ob.getPF_PENALTY() * 100.0);

            if(bill_rec_ob.getPF_PENALTY() > 0.30){
                bill_rec_ob.setPF_PENALTY(0.30);
            }

            bill_rec_ob.setPF_PENALTY(bill_rec_ob.getPF_PENALTY() * bill_rec_ob.getN_UNITSCONSUMED());

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in ComputePFPenalty : "+e.toString());
        }
    }

    private void GetAverageUnits() {
        double currentperc = 0.0;
        double actualunits = 0.0;
        double Units = 0.0;


        try
        {
            if(bill_rec_ob.getCREAPING_PERC() != 0){
                currentperc = 100.0 + 	bill_rec_ob.getCREAPING_PERC();
                actualunits = (bill_rec_ob.getN_UNITSCONSUMED() * 100.0) / currentperc;
                bill_rec_ob.setKEY_AVG_CONSUMPTION(actualunits);
            }
            bill_rec_ob.setKEY_AVG_CONSUMPTION(bill_rec_ob.getKEY_AVG_CONSUMPTION() * bill_rec_ob.getKEY_CT_PT());

            if(bill_rec_ob.getFIRST_RDG_FLG().equals("N") && bill_rec_ob.getLASTMONTHFRACTION() > 0){
                Units = bill_rec_ob.getKEY_AVG_CONSUMPTION() / 30.0;
                bill_rec_ob.setKEY_AVG_CONSUMPTION(Units * bill_rec_ob.getLASTMONTHFRACTION());
            }

        }catch(Exception e){
            Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in GetAverageUnits : "+e.toString());
        }
    }

    private void print_kannada_bill() {

        double EC_TOTAL = 0;
        double FC_TOTAL = 0;
        TariffConstants tariffConstants = new TariffConstants();

        try {
            if (mBtp.getState() != BluetoothPrinter.STATE_CONNECTED) {
                Toast.makeText(this.getActivity(), "Printer is not connected", Toast.LENGTH_SHORT).show();
                return;
            }

            //  Uri fileUri = Uri.fromFile(new File("file:///android_asset/fonts/bescom_logo.jpg"));
            String temp = "";
            AssetManager am = getActivity().getAssets();
            InputStream inputStream = am.open("fonts/mescom_logo.jpg");
            File file = createFileFromInputStream(inputStream);

            Log.d("file:",""+file.getPath());

            mBtp.printImage(file.getPath());

            String separator = "------------------------------------------";
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSansMono.ttf");

            TextPaint tp = new TextPaint();
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD_ITALIC));
            tp.setTextSize(30);
            tp.setColor(Color.BLACK);
            mBtp.addText("ಮಂಗಳೂರು ವಿದ್ಯುತ್ ಸರಬರಾಜು ಕಂಪನಿ" +
                    "\n" +
                    "ವಿದ್ಯುತ್ ಬಿಲ್ / Electricity Bill", Layout.Alignment.ALIGN_CENTER, tp);
            mBtp.addText("\n" +bill_rec_ob.getSUBDIV(), Layout.Alignment.ALIGN_CENTER, tp);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Account Details\n");
            stringBuilder.append("ಆರ್.ಆರ್ ಸಂಖ್ಯೆ/R.R Number          "+bill_rec_ob.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಖಾತೆ ಸಂಖ್ಯೆ/Acc Id                 "+bill_rec_ob.getIVRSID()+"\n");
            stringBuilder.append("ಮಾ.ಓ.ಸಂಕೇತ/M.R Code              "+bill_rec_ob.getKEY_READER_CODE()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Personal Details\n");
            stringBuilder.append("ಹೆಸರು ಮತ್ತು ವಿಳಾಸ /Name and Address\n");
            stringBuilder.append(bill_rec_ob.getKEY_CONSMR_NAME()+"\n "
                    +bill_rec_ob.getKEY_ADDRESS1()+" "
                    +bill_rec_ob.getKEY_ADDRESS2()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Connection Details\n");
            stringBuilder.append("ಜಕಾತಿ/Tariff                     "+tariffConstants.getTariffDescription(""+bill_rec_ob.getKEY_TARIFF_CODE())+"\n");
            if(trfMain.getTariffPowerUnit().equals("K")){
                stringBuilder.append("ಮಂ.ಪ್ರಮಾಣ/Sanc Load             "+bill_rec_ob.getKEY_SANCT_KW()+"KW + "+bill_rec_ob.getKEY_SANCT_HP()+"HP\n");
            }else{
                stringBuilder.append("ಮಂ.ಪ್ರಮಾಣ/Sanc Load             "+bill_rec_ob.getKEY_SANCT_KW()+"KW + "+bill_rec_ob.getKEY_SANCT_HP()+"HP\n");
            }
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Billing Details\n");
            temp = "";
            if(bill_rec_ob.getFLG_FL_rebate() == 1 )
            {
                temp = "FL";
                if(bill_rec_ob.getOLD_MTR_RDG() > 0.001){
                    temp = temp+"/MC";
                }
            }

            if( bill_rec_ob.getOLD_MTR_RDG() > 0.001 && bill_rec_ob.getFLG_FL_rebate() != 1){
                temp = "MC";
            }

            if( bill_rec_ob.getPARTFRACTION() != 1.0 )
            {
                temp = "" + bill_rec_ob.getPARTFRACTION() * 30.0 + " Of Days/";
            }
            else
            {
                if(bill_rec_ob.getOLD_MTR_RDG() > 0.001 || bill_rec_ob.getFLG_FL_rebate() == 1)
                {
                    //	temp = temp;
                }
            }

            String sDate1=bill_rec_ob.getKEY_READING_DATE();
            Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
            System.out.println(sDate1+"\t"+date1);

            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.add(Calendar.MONTH, -1);
            System.out.println(c.getTime());

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate=dateFormat.format(c.getTime());


            stringBuilder.append("ಬಿಲ್ ಅವಧಿ/Bill Period   "+(temp == "" || temp.length() == 0 ? formattedDate + " to " + bill_rec_ob.getKEY_READING_DATE() : temp )+"\n");
            stringBuilder.append("ರೀಡಿಂಗ್ ದಿನಾಂಕ/Reading Date           "+bill_rec_ob.getKEY_READING_DATE()+"\n");
            stringBuilder.append("ಬಿಲ್ ಸಂಖ್ಯೆ/Bill Number               "+bill_rec_ob.getKEY_BILL_NO()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Consumption Detail\n");
            temp = "";
            if(bill_rec_ob.getMETER_STS().equals("M")){
                temp = "MNR";
            }else if(bill_rec_ob.getMETER_STS().equals("D")){
                temp = "DL";
            }else if(bill_rec_ob.getDC_FLG().equals("Y")){
                temp = "DC";
            }else{
                temp = ""+bill_rec_ob.getPRESENT_RDG();
            }
            stringBuilder.append("ಇಂದಿನ ಮಾಪನ/Pres. Rdg              "+padLeft(String.valueOf(temp),10)+"\n");
            stringBuilder.append("ಹಿಂದಿನ ಮಾಪನ/Prev. Rdg              "+padLeft(String.valueOf(bill_rec_ob.getKEY_PREV_MTR_RDG()),10)+"\n");
            stringBuilder.append("ಮಾಪಕ ಸ್ಥಿರಾಂಕ/Constant               "+padLeft(String.valueOf(bill_rec_ob.getMETER_CONST()),10)+"\n");
            stringBuilder.append("ಬಳಕೆ/Consumption(Units)            "+padLeft(String.valueOf(bill_rec_ob.getN_UNITSCONSUMED()),10)+"\n");
            stringBuilder.append("ಸರಾಸರಿ/Average                      "+padLeft(String.valueOf(bill_rec_ob.getKEY_AVG_CONSUMPTION()),10)+"\n");
            stringBuilder.append("ದಾಖಲಿತ ಬೇಡಿಕೆ/Recorded MD            "+padLeft(String.valueOf("0.00"),10)+"\n");
            stringBuilder.append("ಪವರ್ ಫ್ಯಾಕ್ಟರ್/Power Factor           "+padLeft(String.valueOf(bill_rec_ob.getKEY_POWER_FACTOR()),10)+"\n");
            stringBuilder.append("ಸಂ. ಪ್ರಮಾಣ/Connected Load          "+padLeft(String.valueOf("0.00"),10)+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ನಿಗದಿತ ಶುಲ್ಕ/Fixed Charges(Unit,Rate,Amount)\n");
            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                if(FixedSlabCount > 3){
                    FixedSlabCount = 3;
                }

                ArrayList<FixedChargeListBO> fixed = new ArrayList<FixedChargeListBO>();
                fixed = bill_rec_ob.getFixedChargeListBOs();
                Log.d("["+this.getClass().getSimpleName()+"]-->","fixed.size() = "+fixed.size() + "   ---   "+fixed);
                if(fixed.size() > 0){
                    Iterator<FixedChargeListBO> it = fixed.iterator();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG:  ");
                    while(it.hasNext()){
                        Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG: Inside while : ");
                        /*if(FixedSlabCount != 3){*/
                        Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED CHRG: Inside if : ");
                        FixedChargeListBO fixedList = it.next();
                        /*Log.d(" "+fixedList.getRate()/100l+"_____"+fixedList.getUnits()/100l+"_____"+fixedList.getAmount();*/

                        String line1 = String.format("%1$6s %2$18s %3$18s", String.valueOf(fixedList.getUnits()/100l), String.valueOf(fixedList.getRate()/100l),String.valueOf(fixedList.getAmount()));

                        Log.d(" "+fixedList.getUnits()/100l+"               "+ fixedList.getRate()/100l+"               "+fixedList.getAmount(),"");
                        stringBuilder.append(line1+"\n");
                         FC_TOTAL= (FC_TOTAL + fixedList.getAmount());

                    }
                }
                Log.d("["+this.getClass().getSimpleName()+"]-->","FIXED TOTAL: "+FC_TOTAL);

            }else{
                Log.d("["+this.getClass().getSimpleName()+"]-->","NO FIXED CHARGES: ");
            }
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ವಿದ್ಯುತ್ ಶುಲ್ಕ/Energy Charges(Unit,Rate,Amount)\n");
            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                if(EnergySlabCount > 6){
                    EnergySlabCount = 6;
                }

                ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
                if(energy.size() > 0){
                    Iterator<EnergyChargeListBO> it = energy.iterator();
                    Log.d("["+this.getClass().getSimpleName()+"]-->","ENERGY CHRG:  ");
                    while(it.hasNext()){
                        EnergyChargeListBO energyList = it.next();

                        if(energyList.getUnits() == 0l || energyList.getRate() == 0){
                            break;
                        }
                        String line1 = String.format("%1$6s %2$18s %3$18s",String.valueOf(energyList.getUnits()), String.valueOf(energyList.getRate()/100.00),String.valueOf(energyList.getAmount()));
                        Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()/100.0+ "_____"+energyList.getAmount(),"");
                        stringBuilder.append(line1+"\n");
                        EC_TOTAL =  EC_TOTAL + energyList.getAmount();
                    }
                    Log.d("ENERGY TOTAL: ",""+EC_TOTAL);
                }
            }else{
                if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                    ArrayList<EnergyChargeListBO> energy = bill_rec_ob.getEnergyChargeListBOs();
                    if(energy.size() > 0){
                        Iterator<EnergyChargeListBO> it = energy.iterator();
                        Log.d("["+this.getClass().getSimpleName()+"]-->","ENERGY CHRG:  ");
                        while(it.hasNext()){
                            EnergyChargeListBO energyList = it.next();

                            if(energyList.getUnits() == 0l || energyList.getRate() == 0){
                                break;
                            }
                            String line1 = String.format("%1$6s %2$18s %3$18s",String.valueOf(energyList.getUnits()), String.valueOf(energyList.getRate()/100.00),String.valueOf(energyList.getAmount()));
                            Log.d(" "+energyList.getUnits()+"_____"+energyList.getRate()+ "_____"+energyList.getAmount(),"");
                            stringBuilder.append(line1+"\n");
                            EC_TOTAL =  EC_TOTAL + energyList.getAmount();
                        }
                    }
                    Log.d("ENERGY TOTAL: ",""+EC_TOTAL);
                }
            }
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಇಂದನ ಹೊಂದಾಣಿಕೆ ಶುಲ್ಕ/FAC Charges(Unit,Rate,Amount)\n");
           // stringBuilder.append(" "+RoundOffValue(((double)FAC_unit)/100.0)+"\n");
            String line1 = String.format("%1$6s %2$18s %3$18s", String.valueOf(bill_rec_ob.getN_UNITSCONSUMED()), String.valueOf(bill_rec_ob.getKEY_FAC_RATE()),String.valueOf(bill_rec_ob.getN_UNITSCONSUMED()*bill_rec_ob.getKEY_FAC_RATE()));

            stringBuilder.append(line1+"\n");


            int len1;
            double totalamount,Total;
            double interestclub,arrearsclub,temp_float;
            int i;
            double temp_debit = 0;
            double total_debt_reg_penalty;
            double ARRS_CONSMR_PAY;

            total_debt_reg_penalty = ARRS_CONSMR_PAY = 0.0;
            len1 = 0;
            totalamount = 0.0;
            interestclub = 0.0;
            arrearsclub = 0.0;
            temp_float = 0.0;
            Total = 0.0;
            ARRS_CONSMR_PAY = 0.0;
            i = 0;

            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಹೆಚ್ಚುವರಿ ಶುಲ್ಕ/Additional Charges\n");
            stringBuilder.append("ರಿಯಾಯಿತಿ/Rebate                    "+padLeft(String.valueOf(RoundOffValue(bill_rec_ob.getN_REBATE() * -1)),10)+"\n");
            stringBuilder.append("ಪಿ.ಎಫ್. ದಂಡ/PF Penalty              "+padLeft(String.valueOf(bill_rec_ob.getPF_PENALTY()),10)+"\n");
            stringBuilder.append("ಹೇ.ಲೋ.ದಂಡ/Ex.Load/MD Penalty      "+padLeft(String.valueOf(" 0.00"),10)+"\n");
            interestclub = interestclub + bill_rec_ob.getKEY_INT_ARREARS();
            interestclub = interestclub + bill_rec_ob.getKEY_DELAY_INTEREST()+ bill_rec_ob.getINT_TAX() ;
            interestclub = interestclub + bill_rec_ob.getINT_ON_TAX();


            stringBuilder.append("ಬಡ್ಡಿ/Interest                       "+padLeft(String.valueOf(RoundOffValue(interestclub)),10)+"\n");
            if (bill_rec_ob.getLESSCLAIMED() > 0.0) {
                bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() + bill_rec_ob.getLESSCLAIMED());
            }
            bill_rec_ob.setDEBITBF(bill_rec_ob.getDEBITBF() + bill_rec_ob.getPRSTCKWH() + bill_rec_ob.getREG_PENALTY());
            stringBuilder.append("ಇತರೇ/Others                        "+padLeft(String.valueOf(bill_rec_ob.getDEBITBF()),10)+"\n");
            stringBuilder.append("ತೆರಿಗೆ/Tax                            "+padLeft(String.valueOf(RoundOffValue((bill_rec_ob.getN_TAX()+FAC_TAX))),10)+"\n");
            if(bill_rec_ob.getFLG_FL_rebate() != '1'){
                Total = bill_rec_ob.getTOTAL_FIXED_TARIFF() + bill_rec_ob.getN_TAX() ;
                Total = Total + bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() + bill_rec_ob.getPF_PENALTY();
                Total = Total + bill_rec_ob.getREG_PENALTY() - bill_rec_ob.getDIFF_AMOUNT();
                Total = Total + bill_rec_ob.getKEY_AMT_PAID() ;
            }else{
                if(bill_rec_ob.getFLG_FL_rebate() == '1'){
                    Total = bill_rec_ob.getP_NTOTAL_ENAERGY_TARIFF() - bill_rec_ob.getDIFF_AMOUNT() + bill_rec_ob.getN_TAX();
                }
            }
            stringBuilder.append("ಬಿಲ್ ಮೊತ್ತ/Bill Amount               "+padLeft(String.valueOf(RoundOffValue((Total+FAC_TAX+((double)FAC_unit)/100.0))),10)+"\n");
            arrearsclub  = bill_rec_ob.getINT_ARREARS2()
                    +
                    bill_rec_ob.getKEY_DMD_ARREARS()
                    +
                    bill_rec_ob.getKEY_TAX_ARREARS();
            stringBuilder.append("ಬಾಕಿ/Arrears                        "+padLeft(String.valueOf(RoundOffValue(arrearsclub)),10)+"\n");
            stringBuilder.append("ಜಮೆ/Credits & Adjustments          "+padLeft(String.valueOf(RoundOffValue((bill_rec_ob.getCREDITBF() * -1)/100)),10)+"\n");

            ARRS_CONSMR_PAY = bill_rec_ob.getTemp_total() - temp_float ;

            temp_debit = temp_float - bill_rec_ob.getMORECLAIMED();

            if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT1A){
                if(bill_rec_ob.getN_UNITSCONSUMED() >= 0 && bill_rec_ob.getN_UNITSCONSUMED() <= 18 ){
                    Log.d(" GOK PAYABLE:", temp_debit+"");
                }
            }else if(bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A1
                    && bill_rec_ob.getKEY_TARIFF_CODE() == TariffConstants.LT4A2){
            }

            stringBuilder.append("ಸರ್ಕಾರದ ಸಹಾಯ ಧನ/GOK Subsidy      "+padLeft(String.valueOf(temp_debit),10)+"\n");

            stringBuilder.append(separator);
            stringBuilder.append("\n");

            tp.setTextSize(20);
            tp.setTypeface(tf);
            tp.setTypeface(Typeface.create(tf, Typeface.ITALIC));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);


            totalamount = bill_rec_ob.getN_REBATE();

            bill_rec_ob.setTemp_total(bill_rec_ob.getTOTALBILL() - totalamount);

            Log.d(" NET AMT  :  ",""+ Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0)));
            stringBuilder.setLength(0);
            stringBuilder.append("ಪಾ. ಮೊತ್ತ/Net Amount Due  : "+Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0))+" \n");
            stringBuilder.append("ಪಾವತಿಗೆ ಕಡೆಯ ದಿನಾಂಕ/Due Date : "+bill_rec_ob.getDUE_DATE()+"\n");

            tp.setTextSize(22);
            tp.setTypeface(tf);
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD_ITALIC));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);


            stringBuilder.setLength(0);
            if (bill_rec_ob.getMMD_CREDIT() > 0.01)
            {
                stringBuilder.append(" INT ON MMD/MSD:"+ bill_rec_ob.getMMD_CREDIT()+"\n");
                stringBuilder.append("CREDITED IS SUBJECT TO AUDI\n");
            }

            if(bill_rec_ob.getADDNL3MMD() > 0.01)
            {
                stringBuilder.append(" ADDNL. 2MMD DUE:"+ bill_rec_ob.getADDNL3MMD()+"\n");
            }

            if( bill_rec_ob.getFLG_ECS_USER() == '1')
            {
                stringBuilder.append(" ECS USER NOT FOR PAYMENT:"+ bill_rec_ob.getADDNL3MMD()+"\n");
            }

            if( bill_rec_ob.getCHEQDIS().equals("Y") )
            {
                stringBuilder.append(" NOT TO ACCEPT CHEQUES:"+ bill_rec_ob.getADDNL3MMD()+"\n");
            }






/*            stringBuilder.append("Place: Bangalore\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Particulars    Qty   Rate    Amt\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Reynolds Pen     2     10     20\n");
            stringBuilder.append("Nataraj Eraser  10      5     50\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Tot Items: 2      Amount: 66.50\n");
            stringBuilder.append("Tot Qty  :12     Vat Amt:  3.50\n");
            stringBuilder.append("                  -------------");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

            stringBuilder.setLength(0);
            stringBuilder.append("           Net Amt: 70.00");
            tp.setTextSize(25);
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);


            stringBuilder.setLength(0);
            stringBuilder.append("Payment Mode: CASH\n\n\n");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);*/

            tp.setTextSize(20);
            tp.setTypeface(tf);
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD_ITALIC));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
            mBtp.print();

            stringBuilder.setLength(0);
            int net_amount = (int) Math.round((bill_rec_ob.getTemp_total()+ FAC_TAX +((double)FAC_unit)/100.0));
            mBtp.printBarcode(bill_rec_ob.getKEY_RR_NO()+"~"+net_amount, NGXBarcodeCommands.CODE128, 100, 450,false);
           // mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

            stringBuilder.setLength(0);
            //stringBuilder.append("\nPowered By:Intellectual Info Solutions Pvt.Ltd."+"\n");
            stringBuilder.append("\nPowered By:SAV WOLKE Technologies Pvt.Ltd."+"\n");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            tp.setTypeface(Typeface.create(tf, Typeface.ITALIC));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
            mBtp.print();


            mBtp.printUnicodeText("\n\n\n\n\n");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    private File createFileFromInputStream(InputStream inputStream) {

        String my_file_name = "mescom_logo.jpg";

        String path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(path + "/Logo" );

        try{

            if (!directory.exists()) {
                directory.mkdirs();
                Log.d("[" + this.getClass().getSimpleName() + "]-->", "Making dirs");
            }

            final File myFile = new File(directory.getAbsolutePath(), my_file_name);
            myFile.createNewFile();

            OutputStream outputStream = new FileOutputStream(myFile);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();


            return myFile;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }
}
