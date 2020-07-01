package com.prakruthi.billingapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prakruthi.billingapp.asynchronous.IRemoteCall;
import com.prakruthi.billingapp.asynchronous.RemoteCallImpl;
import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.database.DatabaseUtil;
import com.prakruthi.billingapp.network.ConnectionDetector;
import com.prakruthi.billingapp.router.ServiceURL;
import com.prakruthi.billingapp.spotbilling.R;
import com.prakruthi.billingapp.utility.GlobalClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DownloadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ConnectionDetector connectionDetector;

    JSONObject jsonObject;
    IRemoteCall remoteObj;
    //UserSessionManagement session ;
    DatabaseUtil databaseUtil;
    DatabaseImplementation databaseImplementation;
    Button btn_download_data;

    String user_id;
    String imei_no;
    String password;

    String Selected_Date;

    private OnFragmentInteractionListener mListener;

    CalendarView calendarView;
    TelephonyManager telephonyManager;

    // Calling Application class (see application tag in AndroidManifest.xml)
    GlobalClass globalVariable;//= (GlobalClass) getActivity().getApplication();

    public DownloadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment();
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
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        globalVariable = (GlobalClass) getActivity().getApplicationContext();

        jsonObject = new JSONObject();
        remoteObj = new RemoteCallImpl();
        databaseImplementation = DatabaseImplementation.getInstance(getActivity());
        connectionDetector = new ConnectionDetector(getActivity());
        databaseUtil = new DatabaseUtil();

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {

                Toast.makeText(getActivity(), date + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

                Selected_Date = date + "/" + (month+1) + "/" + year;
            }
        });
        btn_download_data = (Button) view.findViewById(R.id.btn_download_data);

        btn_download_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionDetector.isConnectingToInternet()) {
                    new MyDownloadTask(getActivity()).execute();
                } else {
                    ShowAlert("success", "Please Make Sure Your Device Is Connected To Internet!!!", "Internet  Error !!!");
                }

            }
        });

        return view;
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

    public class MyDownloadTask extends AsyncTask<Object, Integer, Boolean> {

        private ProgressDialog dialog;
        JSONObject response_json = new JSONObject();
        String handshake_message = "";

        public MyDownloadTask(FragmentActivity activity) {
            dialog = new ProgressDialog(activity);
        }


        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Download !!!");
            dialog.setIcon(R.drawable.ic_cloud_download_black_24dp);
            dialog.setMessage("Communicating Server... Please Wait...");
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            dialog.show();
            ;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (!aVoid) {
                ShowAlert("fail", handshake_message, "Error !!!");
            } else {
                Toast.makeText(getActivity(), "Data Downloaded Successfully...!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (values[0] == 99990) {
                dialog.setMessage("Downloading Records... Please Wait...");
            } else if (values[0] == 99991) {
                dialog.setMessage("Preparing Records... Please Wait...");
            } else if (values[0] == 99992) {
                dialog.setMessage("Loading Tariff Main... Please Wait...");
            } else if (values[0] == 99993) {
                dialog.setMessage("Loading Tariff Fix... Please Wait...");
            } else if (values[0] == 99994) {
                dialog.setMessage("Loading Tariff Rebate... Please Wait...");
            } else if (values[0] == 99995) {
                dialog.setMessage("Loading Tariff Slab... Please Wait...");
            } else if (values[0] == 99996) {
                dialog.setMessage("Loading Consumer Master... Please Wait...");
            } else {
                dialog.setProgress(values[0]);
            }

        }


        @SuppressLint("MissingPermission")
        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                String SDM_ID;
                String SDM_IMEI_NUMBER;
                String SDM_ASSIGNED_PHONE_NUMBER;
                String SDM_ASSIGNED_USER_ID;
                String SDM_FROM_DT;
                String SDM_TO_DT;
                String SDM_STATUS;
                String SDM_LOCATION_CODE;
                String SDM_MR_CODE;

                jsonObject.put("user_id", globalVariable.getUsername());
                jsonObject.put("imei_no", telephonyManager.getDeviceId());
                 jsonObject.put("service_url", ServiceURL.HANDSHAKE);

                 JSONObject handshake_result = remoteObj.callRemoteService(jsonObject);
                 if(!handshake_result.get("status").equals("success") || handshake_result.get("status") == null){
                     handshake_message = "Server Is Not Ready.Contact Administrator !!! \n"+(String)handshake_result.get("message");
                     return false;
                 }

                 jsonObject.put("user_id",globalVariable.getUsername());
                 jsonObject.put("imei_no", telephonyManager.getDeviceId());
                 jsonObject.put("service_url", ServiceURL.GET_SBD_DETAILS);

                 JSONObject getsbddetails_result = remoteObj.callRemoteService(jsonObject);
                 if(!getsbddetails_result.get("status").equals("success") || getsbddetails_result.get("status") == null){
                     handshake_message = "Server Is Not Ready.Contact Administrator !!! \n"+(String)handshake_result.get("message");
                     return false;
                 }else{
                     SDM_ID = (String) getsbddetails_result.get("SDM_ID");
                     SDM_IMEI_NUMBER = (String) getsbddetails_result.get("SDM_IMEI_NUMBER");
                     SDM_ASSIGNED_PHONE_NUMBER = (String) getsbddetails_result.get("SDM_ASSIGNED_PHONE_NUMBER");
                     SDM_ASSIGNED_USER_ID = (String) getsbddetails_result.get("SDM_ASSIGNED_USER_ID");
                     SDM_FROM_DT = (String) getsbddetails_result.get("SDM_FROM_DT");
                     SDM_TO_DT = (String) getsbddetails_result.get("SDM_TO_DT");
                     SDM_STATUS = (String) getsbddetails_result.get("SDM_STATUS");
                     SDM_LOCATION_CODE = (String) getsbddetails_result.get("SDM_LOCATION_CODE");
                     SDM_MR_CODE = (String) getsbddetails_result.get("SDM_MR_CODE");

                 }


                 publishProgress(99990);

                 jsonObject.put("user_id",globalVariable.getUsername());
                 jsonObject.put("imei_no", telephonyManager.getDeviceId());
                 jsonObject.put("meter_date", Selected_Date);
                 jsonObject.put("location_code",SDM_LOCATION_CODE);
                 jsonObject.put("meter_code", SDM_MR_CODE);
                 jsonObject.put("service_url", ServiceURL.DOWNLOAD_MASTER_DATA);

                 response_json = remoteObj.callRemoteService(jsonObject);
                 Log.d("response_json : ",""+response_json);

                 String status = null;

                 status = (String) response_json.get("status");
                 String message = (String) response_json.get("message");

                 if(status.equals("success")){

                     databaseImplementation.DropDatabaseTables();
                     databaseImplementation.CreateDatabaseTables();

                     publishProgress(99991);

                     Log.d("TARIFF_MAIN:",""+(JSONArray)response_json.get("TARIFF_MAIN"));
                     Log.d("TARIFF_FIX:",""+(JSONArray)response_json.get("TARIFF_FIX"));
                     Log.d("TARIFF_REBATE:",""+(JSONArray)response_json.get("TARIFF_REBATE"));
                     Log.d("TARIFF_SLAB:",""+(JSONArray)response_json.get("TARIFF_SLAB"));
                     Log.d("CONSUMER_DATA:",""+(JSONArray)response_json.get("CONSUMER_DATA"));

                     JSONArray TARIFF_MAIN    = (JSONArray)response_json.get("TARIFF_MAIN");
                     JSONArray TARIFF_FIX     = (JSONArray)response_json.get("TARIFF_FIX");
                     JSONArray TARIFF_REBATE  = (JSONArray)response_json.get("TARIFF_REBATE");
                     JSONArray TARIFF_SLAB    = (JSONArray)response_json.get("TARIFF_SLAB");
                     JSONArray CONSUMER_DATA  = (JSONArray)response_json.get("CONSUMER_DATA");

                     ArrayList<String> TARIFF_MAIN_LIST   = databaseUtil.getTariffMainStatementList(TARIFF_MAIN);
                     ArrayList<String> TARIFF_FIX_LIST    = databaseUtil.getTariffFixStatementList(TARIFF_FIX);
                     ArrayList<String> TARIFF_REBATE_LIST = databaseUtil.getTariffRebateStatementList(TARIFF_REBATE);
                     ArrayList<String> TARIFF_SLAB_LIST   = databaseUtil.getTariffSlabStatementList(TARIFF_SLAB);
                     ArrayList<String> CONSUMER_DATA_LIST = databaseUtil.getConsumerDataStatementList(CONSUMER_DATA);

                     int TARIFF_MAIN_LIST_COUNT = 0 ;
                     int TARIFF_FIX_LIST_COUNT = 0 ;
                     int TARIFF_REBATE_LIST_COUNT = 0 ;
                     int TARIFF_SLAB_LIST_COUNT = 0 ;
                     int CONSUMER_DATA_LIST_COUNT = 0 ;

                     int COUNT = 0 ;

                     int TOTAL_DOWNLOAD_RECORDS_LENGTH = 0 ;

                     TOTAL_DOWNLOAD_RECORDS_LENGTH = TARIFF_MAIN.length()+TARIFF_FIX.length()+TARIFF_REBATE.length()+TARIFF_SLAB.length()+CONSUMER_DATA.length();

                     publishProgress(99992);
                     Iterator<String> tariff_main_it = TARIFF_MAIN_LIST.iterator();

                     while(tariff_main_it.hasNext()){

                         TARIFF_MAIN_LIST_COUNT = TARIFF_MAIN_LIST_COUNT + databaseImplementation.InsertRecord(tariff_main_it.next());
                         COUNT++;
                         publishProgress((int) (COUNT * 100 / TOTAL_DOWNLOAD_RECORDS_LENGTH));
                     }

                     publishProgress(99993);
                     Iterator<String> tariff_fix_it = TARIFF_FIX_LIST.iterator();

                     while(tariff_fix_it.hasNext()){

                         TARIFF_FIX_LIST_COUNT = TARIFF_FIX_LIST_COUNT + databaseImplementation.InsertRecord(tariff_fix_it.next());
                         COUNT++;
                         publishProgress((int) (COUNT * 100 / TOTAL_DOWNLOAD_RECORDS_LENGTH));
                     }

                     publishProgress(99994);
                     Iterator<String> tariff_rebate_it = TARIFF_REBATE_LIST.iterator();

                     while(tariff_rebate_it.hasNext()){

                         TARIFF_REBATE_LIST_COUNT = TARIFF_REBATE_LIST_COUNT + databaseImplementation.InsertRecord(tariff_rebate_it.next());
                         COUNT++;
                         publishProgress((int) (COUNT * 100 / TOTAL_DOWNLOAD_RECORDS_LENGTH));
                     }

                     publishProgress(99995);
                     Iterator<String> tariff_slab_it = TARIFF_SLAB_LIST.iterator();

                     while(tariff_slab_it.hasNext()){

                         TARIFF_SLAB_LIST_COUNT = TARIFF_SLAB_LIST_COUNT + databaseImplementation.InsertRecord(tariff_slab_it.next());
                         COUNT++;
                         publishProgress((int) (COUNT * 100 / TOTAL_DOWNLOAD_RECORDS_LENGTH));
                     }

                     publishProgress(99996);
                     Iterator<String> consumer_data_it = CONSUMER_DATA_LIST.iterator();

                     while(consumer_data_it.hasNext()){

                         CONSUMER_DATA_LIST_COUNT = CONSUMER_DATA_LIST_COUNT + databaseImplementation.InsertRecord(consumer_data_it.next());
                         COUNT++;
                         publishProgress((int) (COUNT * 100 / TOTAL_DOWNLOAD_RECORDS_LENGTH));
                     }


                 }else{
                     if (dialog.isShowing()) {
                         dialog.dismiss();
                     }
                     handshake_message = message;
                     return  false;

                     //ShowAlert("fail",message,"Error !!!");
                 }
             } catch (JSONException e) {
                 if (dialog.isShowing()) {
                     dialog.dismiss();
                 }
                 e.printStackTrace();
             }

             return true;
         }
     }


     private void ShowAlert(String status,String message,String title){

         AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

         // Setting Dialog Title
         alertDialog.setTitle(title);

         // Setting Dialog Message
         alertDialog.setMessage(message);

         if(status.equals("success")){
             // Setting Icon to Dialog
             alertDialog.setIcon(R.drawable.succes);
         }else{
             // Setting Icon to Dialog
             alertDialog.setIcon(R.drawable.fail);
         }

         // Setting Positive "Yes" Button
         alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog,int which) {

             }
         });
         // Showing Alert Message
         alertDialog.show();
     }
}
