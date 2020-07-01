package com.prakruthi.billingapp.fragments;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prakruthi.billingapp.constants.Print;
import com.prakruthi.billingapp.spotbilling.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

   // BluetoothAdapter mBluetoothAdapter;
  //  BluetoothSocket mBluetoothSocket;
 //   BluetoothDevice mBluetoothDevice;

 //   OutputStream mOutputStream;
 //   InputStream mInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBuffferPosition;
    int counter;
  //  volatile boolean stopWorker;


    TextView myLabel;

    EditText myTextBox;

    TextView myLabel1;

    public SettingsFragment() {
        // Required empty public constructor
       /* Print.mBluetoothAdapter = mBluetoothAdapter;
        Print.mBluetoothSocket = mBluetoothSocket;
        Print. mBluetoothDevice = mBluetoothDevice;
        Print.mOutputStream = mOutputStream;
        Print.mInputStream = mInputStream;
        Print.stopWorker = stopWorker;*/
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
       // return inflater.inflate(R.layout.fragment_settings, container, false);

        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        try{

            Button openButton = (Button) view.findViewById(R.id.open);
            //Button sendButton = (Button) view.findViewById(R.id.send);
            Button closeButton = (Button) view.findViewById(R.id.close);

            myLabel = (TextView) view.findViewById(R.id.label);
            //myTextBox = (EditText) view.findViewById(R.id.entry);
            myLabel1 = (TextView) view.findViewById(R.id.label1);

                if(Print.mBluetoothSocket != null){
                    if(Print.mBluetoothSocket.isConnected()){
                        myLabel.setText(Print.label);
                        myLabel1.setText(Print.label1);
                    }
                }

            Log.d("Print.mBluetoothSocket",""+Print.mBluetoothSocket);
            if(Print.mBluetoothSocket == null){
                ShowAlert("fail","Please connect to bluetooth printer !!!!","Error..!!");
            }else{
                if(!Print.mBluetoothSocket.isConnected()){
                    ShowAlert("fail","Please connect to bluetooth printer !!!!","Error..!!");
                }
            }

            openButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try{
                        findBT();
                        openBT();
                    }catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });

            /*sendButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        sendData();
                    } catch (Exception ex) {
                    }

                }
            });*/


            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        closeBT();
                    } catch (Exception ex) {
                    }

                }
            });




        }catch (NullPointerException e) {
            // TODO: handle exception
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return view;
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

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    protected void findBT() {
        // TODO Auto-generated method stub
        try{

            Print.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(Print.mBluetoothAdapter == null){
                myLabel.setText(" No Bluetooth Adapter available");
                Print.label = "No Bluetooth Adapter available";
            }

            if(!Print.mBluetoothAdapter.isEnabled()){
                Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBlueTooth,0);
            }

            Set<BluetoothDevice> pairedDevices = Print.mBluetoothAdapter.getBondedDevices();

            myLabel1.setText("SIZE : " +pairedDevices.size());
            Print.label1 = "SIZE : " +pairedDevices.size();

            if(pairedDevices.size() > 0){
                for(BluetoothDevice device:pairedDevices){
                    myLabel1.setText("Device Name  "+device.getName());
                    Print.label1 = "Device Name  "+device.getName();
					/*if(device.getName().equals("ESYS0064")){
						mBluetoothDevice = device;
						break;
					}*/

					/*if(device.getName().equals("GT-S7262")){
						mBluetoothDevice = device;
						break;
					}*/

                    if(device.getName().equals("AB330M Printer")){
                        Print.mBluetoothDevice = device;
                        break;
                    }
                    else if(device.getName().equals("FireFly-E044")){
                        Print.mBluetoothDevice = device;
                        break;
                    }
                    else if(device.getName().equals("ESYS0064")){
                        Print.mBluetoothDevice = device;
                        break;
                    }
                    else if(device.getName().equals("G-8BT3")){
                        Print.mBluetoothDevice = device;
                        break;
                    }

                }

            }
            myLabel.setText("Bluetooth Device Found");
            Print.label = "Bluetooth Device Found";

        }catch (NullPointerException e) {
            // TODO: handle exception
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    protected void openBT() throws IOException {
        // TODO Auto-generated method stub
        try{

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

            Print.mBluetoothSocket = Print.mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            Print.mBluetoothSocket.getRemoteDevice();
            Print.mBluetoothSocket.connect();

            //beginListenForData();

            myLabel.setText("Bluetooth Opened");
            Print.label = "Bluetooth Opened";

        }catch (NullPointerException e) {
            // TODO: handle exception
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    protected void closeBT() {
        // TODO Auto-generated method stub
        try {
            Print.stopWorker = true;
            if(Print.mOutputStream != null) {
                Print.mOutputStream.close();
            }
            if(Print.mInputStream != null) {
                Print.mInputStream.close();
            }
            if(Print.mBluetoothSocket != null){
                Print.mBluetoothSocket.close();
            }

            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected void sendData() {
        // TODO Auto-generated method stub

        try {

            Print.mOutputStream = Print.mBluetoothSocket.getOutputStream();
            OutputStream  os = Print.mBluetoothSocket.getOutputStream();


            // the text typed by the user
            String msg = myTextBox.getText().toString();
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
            Print. mOutputStream.write(BOLD_START);
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
            myLabel.setText("Data Sent");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
