package com.prakruthi.billingapp.spotbilling;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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

import com.prakruthi.billingapp.jobscheduler.JobSchedulerUpload;
import com.prakruthi.billingapp.listeners.LocationFinder;
import com.prakruthi.billingapp.utility.GlobalClass;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        globalVariable = (GlobalClass) getApplicationContext();

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


                if(username.getText().toString().equals("test") && password.getText().toString().equals("test")){
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
