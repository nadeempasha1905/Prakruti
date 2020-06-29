package com.fcs.billingapp.jobscheduler;

import com.fcs.billingapp.spotbilling.SignInActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.PersistableBundle;
import android.util.Log;

public class AndroidUploadService {
	
	static JobScheduler  jobScheduler = SignInActivity.jobScheduler ;
	
	static ComponentName componentNameService = SignInActivity.jobService ;
	
	static int MY_JOB_ID = SignInActivity.JOB_ID;
	
	private static final String TAG = "AndroidUploadService";
	
	public static void SendJobToQueue(String RrNumber){
		
		Log.i( TAG,"SendJobToQueue 1");
		
		// Extras for your job.
		PersistableBundle extras = new PersistableBundle();//System.out.println( "SendJobToQueue 2");
		extras.putString("KEY_RR_NO", RrNumber);//System.out.println( "SendJobToQueue 3");

		// Construct a new job with your service and some constraints.
		// See the javadoc for more detail.
		//System.out.println( "SendJobToQueue 4");
		JobInfo job = new JobInfo.Builder((++MY_JOB_ID), componentNameService)
		  .setMinimumLatency(1000)
		  .setOverrideDeadline(2000)
		  .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
		  .setRequiresCharging(false)
		  .setExtras(extras)
		  .build();
		Log.i(TAG, "SendJobToQueue 5");

		 int jobId = jobScheduler.schedule(job);
		 
		if(jobId > 0){
			Log.i(TAG, " Successfully scheduled job : "+jobId);
		}else{
			Log.i(TAG, " Job Not Scheduled  : "+jobId);
		}
		
		
	}

}
