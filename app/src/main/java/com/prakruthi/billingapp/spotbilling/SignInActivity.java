package com.prakruthi.billingapp.spotbilling;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prakruthi.billingapp.asynchronous.IRemoteCall;
import com.prakruthi.billingapp.asynchronous.RemoteCallImpl;
import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.database.DatabaseUtil;
import com.prakruthi.billingapp.jobscheduler.JobSchedulerUpload;
import com.prakruthi.billingapp.listeners.LocationFinder;
import com.prakruthi.billingapp.network.ConnectionDetector;
import com.prakruthi.billingapp.utility.EncriptAndDecript;
import com.prakruthi.billingapp.utility.GenericClass;
import com.prakruthi.billingapp.utility.GlobalClass;

import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_CODE_READ_PHONE_STATE = 2;

    Button signin = null;
    Location location = null;
    LocationFinder locationFinder = null;
    LocationManager locationManager = null;

    // Get an instance of the JobScheduler, this will delegate to the system JobScheduler on api 21+
    // and to a custom implementataion on older api levels.
    //JobScheduler jobScheduler = JobScheduler.getInstance(getApplicationContext());
    public static JobScheduler jobScheduler = null;
    public static ComponentName jobService = null;
    public static int JOB_ID = 0;

    EditText username;
    EditText password;

    //JobInfo.Builder job = null;

    // Calling Application class (see application tag in AndroidManifest.xml)
    GlobalClass globalVariable;// = (GlobalClass) getApplication();

    TelephonyManager telephonyManager;

    ConnectionDetector connectionDetector;

    JSONObject jsonObject;
    IRemoteCall remoteObj;
    //UserSessionManagement session ;
    DatabaseUtil databaseUtil;
    DatabaseImplementation databaseImplementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        jsonObject = new JSONObject();
        remoteObj = new RemoteCallImpl();
        databaseImplementation = DatabaseImplementation.getInstance(getApplicationContext());
        connectionDetector = new ConnectionDetector(getApplicationContext());
        databaseUtil = new DatabaseUtil();
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        globalVariable = (GlobalClass) getApplicationContext();

        databaseImplementation.CreateDatabaseTables();

        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put("USER_ID", "admin");
            contentValues.put("USER_PASSWORD", EncriptAndDecript.encrypt("admin"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                contentValues.put("IMEI_NO", telephonyManager.getImei());
            }else{
                contentValues.put("IMEI_NO","");
            }
            contentValues.put("USER_ROLE", "admin");
            contentValues.put("LOGOUT_STATUS", "N");
            contentValues.put("CREATED_BY", "admin");
            contentValues.put("CREATED_ON", GenericClass.getDateTime());
            contentValues.put("UPDATED_BY", "");
            contentValues.put("UPDATED_ON", "");

        } catch (Exception e) {
            e.printStackTrace();
        }


        databaseImplementation.CheckandInsertAdminRecord(contentValues);

        username = (EditText) findViewById(R.id.text_username);
        password = (EditText) findViewById(R.id.text_password);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),SignInActivity.this)) {
            fetchLocationData();
        }
        else
        {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getApplicationContext(),SignInActivity.this);
        }


        if (checkPermission(Manifest.permission.READ_PHONE_STATE,getApplicationContext(),SignInActivity.this)) {
         //   telephonyManager.getDeviceId();
        }
        else
        {
            requestPermission(Manifest.permission.READ_PHONE_STATE,PERMISSION_REQUEST_CODE_READ_PHONE_STATE,getApplicationContext(),SignInActivity.this);
        }
        //job = new JobInfo.Builder(0 /*jobid*/, new ComponentName(this, JobSchedulerUpload.class));

        jobScheduler =  (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        Log.d("["+this.getClass().getSimpleName()+"]-->","*********jobscheduler : "+jobScheduler);
        jobService =     new ComponentName(this, JobSchedulerUpload.class);
        Log.d("["+this.getClass().getSimpleName()+"]-->","*********jobService : "+jobService);

        signin = (Button) findViewById(R.id.btn_signin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting MainActivity

                if(username.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please Enter Username !!!",Toast.LENGTH_SHORT).show();
                    username.setFocusable(true);
                    return;
                }

                if(password.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please Enter Password !!!",Toast.LENGTH_SHORT).show();
                    password.setFocusable(true);
                    return;
                }

                String _musername = username.getText().toString();
                String _mpassword = password.getText().toString();


                if((databaseImplementation.ValidateUsernameandPassword(_musername,_mpassword)) > 0){
                    //Set name and email in global/application context
                    globalVariable.setUsername(username.getText().toString().trim());
                    globalVariable.setPassword(password.getText().toString().trim());

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Username or Password !!!",Toast.LENGTH_SHORT).show();
                }


               /* if(username.getText().toString().equals("test") && password.getText().toString().equals("test")){
                    //Set name and email in global/application context
                    globalVariable.setUsername(username.getText().toString().trim());
                    globalVariable.setPassword(password.getText().toString().trim());


                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);


                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Username or Password !!!",Toast.LENGTH_SHORT).show();
                }*/


            }
        });
    }

    /*public ComponentName getComponentName(){

        jobService =     new ComponentName(this, JobSchedulerUpload.class.getName());

        return jobService;
    }*/

    public static void requestPermission(String strPermission,int perCode,Context _c,Activity _a){

        if(PERMISSION_REQUEST_CODE_LOCATION == perCode){
            if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
                Toast.makeText(_a,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
            } else {

                ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
            }
        }else if(PERMISSION_REQUEST_CODE_READ_PHONE_STATE == perCode){
            if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
                Toast.makeText(_a,"Allows to access phone state.",Toast.LENGTH_LONG).show();
            } else {

                ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
            }
        }




    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
            case PERMISSION_REQUEST_CODE_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access phone state.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }


    private void fetchLocationData()
    {
        //code to use the granted permission (location)
        locationFinder = LocationFinder.getInstance();

        startService(new Intent(this, LocationFinder.class));

        if(!locationFinder.CheckGpsOrNetworkEnabled(getContentResolver())){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "GPS Or Network Not Enabled.Please Check The Settings.", Toast.LENGTH_SHORT).show();
        }else{

            location = locationFinder.InitiazeLocationListener(getApplicationContext());
        }

        if(location == null){
            location = locationFinder.InitiazeLocationListener(getApplicationContext());
        }

        Log.d("location :",""+location);


    }
}
