package com.prakruthi.billingapp.spotbilling;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.prakruthi.billingapp.fragments.BillingFragment;
import com.prakruthi.billingapp.fragments.DownloadFragment;
import com.prakruthi.billingapp.fragments.InvoiceGenerateFragment;
import com.prakruthi.billingapp.fragments.ItemMasterFragment;
import com.prakruthi.billingapp.fragments.ProductCardViewFragment;
import com.prakruthi.billingapp.fragments.SettingsFragment;
import com.prakruthi.billingapp.fragments.SummaryFragment;
import com.ngx.BluetoothPrinter;
import com.ngx.PrinterWidth;

public class MainActivity extends AppCompatActivity implements
        DownloadFragment.OnFragmentInteractionListener,
        BillingFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener,
        ItemMasterFragment.OnFragmentInteractionListener,
        ProductCardViewFragment.OnFragmentInteractionListener,
        InvoiceGenerateFragment.OnFragmentInteractionListener


{

    private TextView mTextMessage;

    String user_id = "";
    String imei_no = "";
    String password = "";

    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";

  //  private TextView tvStatus;
    private String mConnectedDeviceName = "";

    public static BluetoothPrinter mBtp = BluetoothPrinter.INSTANCE;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("LongLogTag")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           /* switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;*/

            int id = item.getItemId();

            displaySelectedScreenItems(id);

            Log.d("displaySelectedScreenItems : ",""+id);
            return true;
        }
    };

    private void displaySelectedScreenItems(int ItemID) {

        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("user_id", user_id);//put string, int, etc in bundle with a key value
        data.putString("imei_no", imei_no);//put string, int, etc in bundle with a key valuE
        data.putString("password",password);


        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (ItemID) {
            case R.id.navigation_billing:
                fragment = new InvoiceGenerateFragment();
                //fragment = new BillingFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;
            case R.id.navigation_dashboard:

               // fragment = new ProductCardViewFragment();
                fragment = new SummaryFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;
           /* case R.id.navigation_download:
                fragment = new DownloadFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;*/
            case R.id.navigation_additem:
              //  fragment = new ItemMasterFragment();
                fragment = new ProductCardViewFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;
           /* case R.id.navigation_bluetooth:
                fragment = new SettingsFragment();
                fragment.setArguments(data);//Finally set argument bundle to fragment
                break;*/
           /* case R.id.nav_Logout:
                LogOutFunction();
                break;*/
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.bottom_navigation_content_frame, fragment);
            ft.commit();
        }

       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        // tvStatus = (TextView) findViewById(R.id.tvStatus);

        try {
            mBtp.initService(this, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
        // BluetoothPrinterMain.mSp.edit().putInt("PRINTER_SELECTION", 3).commit();
        if (r) {
            Toast.makeText(getApplicationContext(), "Three Inch Printer Selected",
                    Toast.LENGTH_SHORT).show();
        }
        mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        displaySelectedScreenItems(R.id.navigation_dashboard);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {

        LogOutFunction();

    }


    private void LogOutFunction(){

        boolean result = false;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Logout...");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure ? You Want To Logout ?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.fail);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

                logoutActivity();


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();



            }
        });

        // Showing Alert Message
        alertDialog.show();


    }

    private void logoutActivity(){
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothPrinter.STATE_CONNECTED:
                            //tvStatus.setText(title_connected_to);
                           // tvStatus.append(mConnectedDeviceName);
                            Toast.makeText(
                                    getApplicationContext(),
                                    ""+title_connected_to+" " +mConnectedDeviceName ,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothPrinter.STATE_CONNECTING:
                           // tvStatus.setText(title_connecting);
                            Toast.makeText(
                                    getApplicationContext(),
                                    ""+title_connecting,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothPrinter.STATE_LISTEN:
                        case BluetoothPrinter.STATE_NONE:
                          //  tvStatus.setText(title_not_connected);
                            Toast.makeText(
                                    getApplicationContext(),
                                    ""+title_not_connected ,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case BluetoothPrinter.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(
                            BluetoothPrinter.DEVICE_NAME);
                    break;
                case BluetoothPrinter.MESSAGE_STATUS:
                  //  tvStatus.setText(msg.getData().getString(
                  //          BluetoothPrinter.STATUS_TEXT));

                    Toast.makeText(
                            getApplicationContext(),
                            ""+msg.getData().getString(BluetoothPrinter.STATUS_TEXT) ,
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth_printer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear_device:
                // deletes the last used printer, will avoid auto connect
                android.app.AlertDialog.Builder d = new android.app.AlertDialog.Builder(this);
                d.setTitle("NGX Bluetooth Printer");
                // d.setIcon(R.drawable.ic_launcher);
                d.setMessage("Are you sure you want to delete your preferred Bluetooth printer ?");
                d.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mBtp.clearPreferredPrinter();
                                Toast.makeText(getApplicationContext(),
                                        "Preferred Bluetooth printer cleared",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                d.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                d.show();
                return true;
            case R.id.action_connect_device:
                // show a dialog to select from the list of available printers
                try {
                    mBtp.showDeviceList(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_unpair_device:
                android.app.AlertDialog.Builder u = new android.app.AlertDialog.Builder(this);
                u.setTitle("Bluetooth Printer unpair");
                // d.setIcon(R.drawable.ic_launcher);
                u.setMessage("Are you sure you want to unpair all Bluetooth printers ?");
                u.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mBtp.unPairBluetoothPrinters()) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "All NGX Bluetooth printer(s) unpaired",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                u.setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                u.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        mBtp.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBtp.onActivityResult(requestCode, resultCode, this);
    }

}
