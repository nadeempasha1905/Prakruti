/**
 *
 */
package com.fcs.billingapp.listeners;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Nadeem
 *
 */
public class LocationFinder extends Service implements LocationListener {

	private static LocationFinder singleton;

	private static LocationManager locationManager = null;
	private static Location        location        = null ;
	private static String          provider        = null ;

	private static double LATITUDE  = 0.0;
	private static double LONGITUDE = 0.0;
	private static double ALTITUDE  = 0.0;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    /** this criteria will settle for less accuracy, high power, and cost */
    public static Criteria createCoarseCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(true);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }

    /** this criteria needs high accuracy, high power, and cost */
    public static Criteria createFineCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(true);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }





    public static LocationFinder getInstance(){
		if(singleton == null){
			singleton = new LocationFinder();
		}
		Log.d("[Location Finder]-->","LocationFinder Instance created....!");
		return singleton;
	}

	public boolean CheckGpsOrNetworkEnabled(ContentResolver contentResolver){

		try {

			provider = Settings.Secure.getString(contentResolver,Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

			if(provider == null || provider.equals("")){
				Log.d("["+this.getClass().getSimpleName()+"]-->","GPS Or Network Is Not Enabled...!");
				return false;
			}else{
				Log.d("["+this.getClass().getSimpleName()+"]-->","GPS Or Network Connection Enabled...!");
				return true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured In GPS/Network Conection : "+e.getMessage());
			return false;
		}
	}



    public Location InitiazeLocationListener(Context context){

        try {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);


            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            String provider = locationManager.getBestProvider(createFineCriteria(), true);

            Log.d("provider listener: ", ""+provider);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled

                Log.d("NO GPS OR NETWORK ENABLED: ", ""+provider);
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            LATITUDE  = location.getLatitude();
                            LONGITUDE = location.getLongitude();
                            ALTITUDE  = location.getAltitude();
                          ///  Log.d("in Network listener : LATITUDE ", ""+LATITUDE);
                          //  Log.d("in Network listener : LONGITUDE ", ""+LONGITUDE);
                          //  Log.d("in Network listener : ALTITUDE ", ""+ALTITUDE);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                LATITUDE  = location.getLatitude();
                                LONGITUDE = location.getLongitude();
                                ALTITUDE  = location.getAltitude();

                            //    Log.d("in GPS listener : LATITUDE ", ""+LATITUDE);
                            //    Log.d("in GPS listener : LONGITUDE ", ""+LONGITUDE);
                            //    Log.d("in GPS listener : ALTITUDE ", ""+ALTITUDE);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;

    }


    public Location InitializeLocationListenerByCriteria(Context context){
        LocationManager locMgr = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);

        // get low accuracy provider
        LocationProvider low=
                locMgr.getProvider(locMgr.getBestProvider(createCoarseCriteria(),true));

        // get high accuracy provider
        LocationProvider high=
                locMgr.getProvider(locMgr.getBestProvider(createFineCriteria(),true));

        // using low accuracy provider... to listen for updates
        locMgr.requestLocationUpdates(low.getName(), 0, 0f,
                new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // do something here to save this new location
                    }
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }
                    public void onProviderEnabled(String s) {
                        // try switching to a different provider
                    }
                    public void onProviderDisabled(String s) {
                        // try switching to a different provider
                    }
                });

        // using high accuracy provider... to listen for updates
        locMgr.requestLocationUpdates(high.getName(), 0, 0f,
                new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // do something here to save this new location
                    }
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }
                    public void onProviderEnabled(String s) {
                        // try switching to a different provider
                    }
                    public void onProviderDisabled(String s) {
                        // try switching to a different provider
                    }
                });

        return location;
    }


    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationFinder.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            LATITUDE = location.getLatitude();
        }

        // return latitude
        return LATITUDE;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            LONGITUDE = location.getLongitude();
        }

        // return longitude
        return LONGITUDE;
    }

    public double GetLatitude(){

        return LATITUDE ;
    }

    public double GetLongitude(){

        return LONGITUDE;
    }


    public double GetAltitude(){

        return ALTITUDE;
    }

    @Override
    public void onLocationChanged(Location loc) {
        // TODO Auto-generated method stub

        //Log.d("onLocationChanged ", ""+LATITUDE+":"+LONGITUDE+":"+ALTITUDE);
        LATITUDE   = loc.getLatitude();
        LONGITUDE  = loc.getLongitude();
        ALTITUDE   = loc.getAltitude();
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

	public LocationManager InitializeLocationManager(Context context,String providerName){

		try {

			if(providerName != null){

				locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

				if(providerName.contains("gps")){

					Criteria criteria = new Criteria();
					criteria.setAccuracy(Criteria.ACCURACY_FINE);
					criteria.setAltitudeRequired(true);
					criteria.setBearingRequired(false);
					criteria.setCostAllowed(false);
					criteria.setPowerRequirement(Criteria.POWER_LOW);
					provider = locationManager.getBestProvider(criteria, true);
					//locationManager.requestLocationUpdates(provider,2 * 60 * 1000, 10, this);
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
					Log.d("["+this.getClass().getSimpleName()+"]-->","GPS_PROVIDER Enabled ...!");
					Log.d("["+this.getClass().getSimpleName()+"]-->", "Best Provider is " + provider);
					//Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();

				}else
					{
						if(providerName.contains("network")){

							locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
							Log.d("["+this.getClass().getSimpleName()+"]-->","NETWORK_PROVIDER Enabled ...!");
						}
						else{
							Log.d("["+this.getClass().getSimpleName()+"]-->","Cannot Initialize.No Provider Enabled.");
							locationManager = null;
						}
				}
				
				location = new Location(providerName);
					
					location = new Location(provider);
			}else{
				Log.d("["+this.getClass().getSimpleName()+"]-->","Cannot Initialize.No Provider Enabled.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			locationManager = null;
			Log.d("["+this.getClass().getSimpleName()+"]-->","Error Occured in InitializeLocationManager : "+e.getMessage());
			e.printStackTrace();
		}finally{
			
			if(locationManager == null){
				Log.d("["+this.getClass().getSimpleName()+"]-->","Finally , Cannot Initialize.No Provider Enabled.");
			}
		}
		return locationManager;
	}
	


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void onCreate() {
        Toast.makeText(this, "Location Listener MyService Created ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Location Listener  MyService Started", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Location Listener Service Started ","");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("Location Listener Service Stopped ","");
        super.onDestroy();
    }
}
