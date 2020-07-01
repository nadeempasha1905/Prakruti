/**
 * 
 */
package com.prakruthi.billingapp.jobscheduler;

import com.prakruthi.billingapp.asynchronous.IRemoteCall;
import com.prakruthi.billingapp.asynchronous.RemoteCallImpl;
import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.router.ServiceURL;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nadeem
 *
 */

/**
 * JobService to be scheduled by the JobScheduler. Requests scheduled with
 * the JobScheduler ultimately land on this service's "onStartJob" method.
 * Currently all this does is write a log entry
 */

public class JobSchedulerUpload extends JobService {

	DatabaseImplementation databaseImplementation;
	
	private UpdateAppsAsyncTask updateTask = new UpdateAppsAsyncTask();

	private static final String TAG = "Upload_SyncService";
	
	JSONObject response_json = new JSONObject();

	IRemoteCall remoteObj;
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		databaseImplementation = DatabaseImplementation.getInstance(this);
		remoteObj  = new RemoteCallImpl();
		Log.i(TAG, " ####################Jobscheduler Service Created And Started.######################");
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, " ****************************Jobscheduler Destroyed.************************");
		super.onDestroy();
	}

	@Override
	public boolean onStartJob(JobParameters params) {
		// TODO Auto-generated method stub


		 // We don't do any real 'work' in this sample app. All we'll
	    // do is track which jobs have landed on our service, and
	    // update the UI accordingly.
	    Log.i(TAG, "on start job: " + params.getJobId());
	    
	    
	    updateTask.execute(params);

	    
	    return true;
	}

	@Override
	public boolean onStopJob(JobParameters params) {
		// TODO Auto-generated method stub


		Log.i(TAG, "on stop job: " + params.getJobId());
	    //return true;
		
		boolean shouldReschedule = updateTask.stopJob(params);

        return shouldReschedule;
	    
	}

    private class UpdateAppsAsyncTask extends AsyncTask<JobParameters, Void, JobParameters[]> {

    	PersistableBundle extras = null;
    	String sts = null;

        @Override
        protected JobParameters[] doInBackground(JobParameters... params) {

            // Do updating and stopping logical here.
            Log.i(TAG, "AsyncTask ,Updating apps in the background");
            
            return params;
        }

        @Override
        protected void onPostExecute(JobParameters[] result) {
/*            for (JobParameters params : result) {
                if (!hasJobBeenStopped(params)) {
                    Log.i(TAG, "AsyncTask , finishing job with id=" + params.getJobId());
                    jobFinished(params, false);
                }
            }*/
        	
        	
        	for(JobParameters params : result){
				try {
        		extras = params.getExtras();
        		
        		String RrNumber = extras.getString("KEY_RR_NO");
        		Log.d("["+this.getClass().getSimpleName()+"]-->","KEY_RR_NO : "+RrNumber);

				JSONObject jsonObject = databaseImplementation.SetBilledRecordToUpload(RrNumber);
				jsonObject.put("service_url", ServiceURL.UPLOAD_SINGLE_RECORD_FROM_JOBSCHEDULER);
				response_json = remoteObj.callRemoteService(jsonObject);
				Log.d("response_json : ",""+response_json);

				String status = null;

					status = (String) response_json.get("status");

				if (status.equals("success")){

					databaseImplementation.UpdateUploadStatus(RrNumber);

					jobFinished(params, false);

				}else{
					// On Failure
					jobFinished(params, true); //Reschedule The Job.
				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        }

        private boolean hasJobBeenStopped(JobParameters params) {
            // Logic for checking stop.
            return false;
        }

        public boolean stopJob(JobParameters params) {
            Log.i(TAG, "AsyncTask , stopJob id=" + params.getJobId());
            // Logic for stopping a job. return true if job should be rescheduled.
            //return false;
            return true;
        }
    }
}


