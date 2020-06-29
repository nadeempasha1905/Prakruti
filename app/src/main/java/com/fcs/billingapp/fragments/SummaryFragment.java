package com.fcs.billingapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fcs.billingapp.database.DatabaseImplementation;
import com.fcs.billingapp.database.DatabaseUtil;
import com.fcs.billingapp.spotbilling.MainActivity;
import com.fcs.billingapp.spotbilling.R;
import com.ngx.BluetoothPrinter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    DatabaseUtil databaseUtil ;
    DatabaseImplementation databaseImplementation;

    Button btnTestPrint ;

    private BluetoothPrinter mBtp = MainActivity.mBtp;

    public SummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();
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


        View view =  inflater.inflate(R.layout.fragment_summary, container, false);

        databaseImplementation = DatabaseImplementation.getInstance(getActivity());
        databaseUtil           = new DatabaseUtil();


        TextView textView_downloaded = (TextView) view.findViewById(R.id.summary_downloaded);
        TextView textView_uploaded   = (TextView) view.findViewById(R.id.summary_uploaded);
        TextView textView_billed     = (TextView) view.findViewById(R.id.summary_billed);
        TextView textView_notbilled  = (TextView) view.findViewById(R.id.summary_notbilled);


        TableLayout MainTable =  (TableLayout)view.findViewById(R.id.summary_table);

        int downloadCount 	= databaseImplementation.getDownloadedRecordCount();
        int uploadCount   	= databaseImplementation.getUploadedToServerRecordCount();
        int billedCount   	= databaseImplementation.getBilledRecordCount();
        int notBilledCount	= downloadCount - billedCount ;

        textView_downloaded.setText(String.valueOf(downloadCount));
        textView_uploaded.setText(""+uploadCount);
        textView_billed.setText(""+billedCount);
        textView_notbilled.setText(""+notBilledCount);

      /*  btnTestPrint = (Button) view.findViewById(R.id.testprint);
        btnTestPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TestPrint();
            }
        });*/



        // Inflate the layout for this fragment
        return view;
    }

    private void TestPrint() {


        if (mBtp.getState() != BluetoothPrinter.STATE_CONNECTED) {
            Toast.makeText(this.getActivity(), "Printer is not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        String separator = "   -------------------------------------------";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSansMono.ttf");

        TextPaint tp = new TextPaint();
        tp.setTypeface(Typeface.create(tf, Typeface.ITALIC));
        tp.setTextSize(30);
        tp.setColor(Color.BLACK);
        mBtp.addText("ಮಂಗಳೂರು ವಿದ್ಯುತ್ ಸರಬರಾಜು ಕಂಪನಿ" +
                "\n" +
                "ವಿದ್ಯುತ್ ಬಿಲ್ / Electricity Bill", Layout.Alignment.ALIGN_CENTER, tp);

        StringBuilder stringBuilder = new StringBuilder();
        String line ;

        tp = new TextPaint();
        tp.setTypeface(Typeface.create(tf, Typeface.ITALIC));
        tp.setTextSize(20);
        tp.setTypeface(tf);

/*        String s = "ಆರ್.ಆರ್ ಸಂಖ್ಯೆ/R.R Number";
        for(int i = 0;i < s.length();i++){
            testline1[i] = s.charAt(i);
        }
        mBtp.addText(String.valueOf(testline1), Layout.Alignment.ALIGN_NORMAL, tp);
        stringBuilder.append("\n");

        s = "ಆರ್.ಆರ್ ಸಂಖ್ಯೆ/R.R Number";
        for(int i = 0;i < s.length();i++){
            testline1[i] = s.charAt(i);
        }
        mBtp.addText(String.valueOf(testline1), Layout.Alignment.ALIGN_NORMAL, tp);
        stringBuilder.append("\n");*/

        String[] colors1 = { "   -------------------------------------------","   ಆರ್. ಆರ್. ಸಂಖ್ಯೆ/R.R Number", "   ಖಾತೆ ಸಂಖ್ಯೆ/Acc Id", "   ಮಾ.ಓ.ಸಂಕೇತ/M.R Code",
                "   ಹೆಸರು ಮತ್ತು ವಿಳಾಸ /Name and Address",
                                "   ಜಕಾತಿ/Tariff","   ಮಂ.ಪ್ರಮಾಣ/Sanc Load","   ಬಿಲ್ ಅವಧಿ/Bill Period","   ರೀಡಿಂಗ್ ದಿನಾಂಕ/Reading Date","   ಬಿಲ್ ಸಂಖ್ಯೆ/Bill Number",
                            "   ಇಂದಿನ ಮಾಪನ/Pres. Rdg","   ಹಿಂದಿನ ಮಾಪನ/Prev. Rdg","   ಮಾಪಕ ಸ್ಥಿರಾಂಕ/Constant","   ಬಳಕೆ/Consumption(Units)","   ಸರಾಸರಿ/Average","   ದಾಖಲಿತ ಬೇಡಿಕೆ/Recorded MD",
                            "   ಪವರ್ ಫ್ಯಾಕ್ಟರ್/Power Factor","   ಸಂ. ಪ್ರಮಾಣ/Connected Load","   ಹೆಚ್ಚುವರಿ ಶುಲ್ಕ/Additional Charges","   ರಿಯಾಯಿತಿ/Rebate","   ಪಿ.ಎಫ್. ದಂಡ/PF Penalty",
                            "   ಹೇ.ಲೋ.ದಂಡ/Ex.Load/MD Penalty","   ಬಡ್ಡಿ/Interest","   ಇತರೇ/Others","   ತೆರಿಗೆ/Tax","   ಬಿಲ್ ಮೊತ್ತ/Bill Amount","   ಬಾಕಿ/Arrears",
                "   ಜಮೆ/Credits & Adjustments",
                            "   ಸರ್ಕಾರದ ಸಹಾಯ ಧನ/GOK Subsidy","   ಪಾ. ಮೊತ್ತ/Net Amount Due","   ಪಾವತಿಗೆ ಕಡೆಯ ದಿನಾಂಕ/Due Date"};
        String[] colors = { "red", "orange", "blue", "green" };
        int[] numbers = { 42, 100, 200, 90 };

        // Loop over both arrays.
        for (int i = 0; i < colors1.length; i++) {
            // Left-justify the color string.
            // ... Right-justify the number.
            char testline1[] = separator.toCharArray();
            String s = colors1[i];
            System.out.println(colors1[i]+","+colors1[i].length());
            for(int j = 0;j < s.length();j++){
                testline1[j] = s.charAt(j);
            }
            mBtp.addText(String.valueOf(testline1), Layout.Alignment.ALIGN_NORMAL, tp);
            stringBuilder.append("\n\n\n\n");
            System.out.println(String.valueOf(testline1));

            /*String line1 = String
                    .format("%1$-30s %2$10d",colors1[i],  numbers[0]);
            System.out.println(line1);
            mBtp.addText(line1, Layout.Alignment.ALIGN_NORMAL, tp);*/
        }
        mBtp.print();

    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
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
}
