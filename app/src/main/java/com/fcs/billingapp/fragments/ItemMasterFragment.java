package com.fcs.billingapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.fcs.billingapp.asynchronous.IRemoteCall;
import com.fcs.billingapp.asynchronous.RemoteCallImpl;
import com.fcs.billingapp.constants.DatabaseConstants;
import com.fcs.billingapp.database.DatabaseImplementation;
import com.fcs.billingapp.database.DatabaseUtil;
import com.fcs.billingapp.network.ConnectionDetector;
import com.fcs.billingapp.spotbilling.MainActivity;
import com.fcs.billingapp.spotbilling.R;
import com.fcs.billingapp.utility.GlobalClass;
import com.ngx.BluetoothPrinter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemMasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemMasterFragment extends Fragment {

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

    private DownloadFragment.OnFragmentInteractionListener mListener;

    CalendarView calendarView;
    TelephonyManager telephonyManager;

    // Calling Application class (see application tag in AndroidManifest.xml)
    GlobalClass globalVariable;//= (GlobalClass) getActivity().getApplication();

    Button btn_save;
    Button btn_get_data;
    EditText index;
    EditText description_english;
    EditText description_other;
    private BluetoothPrinter mBtp = MainActivity.mBtp;

    public ItemMasterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemMasterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemMasterFragment newInstance(String param1, String param2) {
        ItemMasterFragment fragment = new ItemMasterFragment();
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

        View view = inflater.inflate(R.layout.fragment_item_master, container, false);

        globalVariable = (GlobalClass) getActivity().getApplicationContext();

        jsonObject = new JSONObject();
        remoteObj = new RemoteCallImpl();
        databaseImplementation = DatabaseImplementation.getInstance(getActivity());
        connectionDetector = new ConnectionDetector(getActivity());
        databaseUtil = new DatabaseUtil();

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

       /* calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {

                Toast.makeText(getActivity(), date + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

                Selected_Date = date + "/" + (month+1) + "/" + year;
            }
        });*/

        databaseImplementation.CreateDatabaseTables();

        index = (EditText) view.findViewById(R.id.key_index);
        description_english = (EditText) view.findViewById(R.id.description_english);
        description_other = (EditText) view.findViewById(R.id.description_other);

       btn_save = (Button) view.findViewById(R.id.save_button);

       btn_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String insert_query = " INSERT INTO " + DatabaseConstants.TABLE_ITEM_MASTER_DETAILS +
                       " (KEY_INDEX ,DESCRIPTION_ENGLISH ,DESCRIPTION_OTHER ) " +
                       "  VALUES" +
                       " (" +  Integer.parseInt(index.getText().toString() )+ "," +
                       " '"  + description_english.getText().toString() + "'," +
                       " '"  + description_other.getText().toString()+ "'" +
                       ")";

               int v = databaseImplementation.InsertRecord(insert_query);
               Log.d("Insert Record : - V : ",""+v);
               if(v > 0){
                   Log.d("Record Inserted ",""+v);
                   Toast.makeText(getActivity(),"Record Inserted", Toast.LENGTH_LONG);
               }
           }
       });

        btn_get_data = (Button) view.findViewById(R.id.cancel_button);

        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<HashMap<String, String>> list = databaseImplementation.getItemMasterDetails();

                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSansMono.ttf");
                StringBuilder stringBuilder = new StringBuilder();
                TextPaint tp = new TextPaint();

                Iterator it = list.iterator();
                int k = 0;
                stringBuilder.setLength(0);
                while (it.hasNext()){
                    HashMap<String,String> item = (HashMap<String, String>) it.next();
                    stringBuilder.append(item.get("KEY_INDEX")+"\t"+item.get("DESCRIPTION_ENGLISH")+"\t"+item.get("DESCRIPTION_OTHER")+"");
                }
                tp.setTextSize(20);
                tp.setTypeface(tf);
                tp.setTypeface(Typeface.create(tf, Typeface.ITALIC));
                mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
                mBtp.print();


/*                tp.setTypeface(Typeface.create(tf, Typeface.BOLD_ITALIC));
                tp.setTextSize(30);
                tp.setColor(Color.BLACK);
                mBtp.addText("ಮಂಗಳೂರು ವಿದ್ಯುತ್ ಸರಬರಾಜು ಕಂಪನಿ" +
                        "\n" +
                        "ವಿದ್ಯುತ್ ಬಿಲ್ / Electricity Bill", Layout.Alignment.ALIGN_CENTER, tp);

                stringBuilder.setLength(0);
                //stringBuilder.append("\nPowered By:Intellectual Info Solutions Pvt.Ltd."+"\n");
                stringBuilder.append("\nPowered By:SAV WOLKE Technologies Pvt.Ltd."+"\n");
                tp.setTextSize(20);
                tp.setTypeface(tf);
                tp.setTypeface(Typeface.create(tf, Typeface.ITALIC));
                mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
                mBtp.print();*/

            }
        });

        return view;



        //return inflater.inflate(R.layout.fragment_item_master, container, false);
    }

    public interface OnFragmentInteractionListener {
    }



}