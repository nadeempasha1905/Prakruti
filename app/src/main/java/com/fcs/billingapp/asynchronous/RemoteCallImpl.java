package com.fcs.billingapp.asynchronous;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Admin on 1/19/2017.
 */

public class RemoteCallImpl implements  IRemoteCall{

    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httpPost = null;
    HttpResponse response = null;
    InputStream instream = null;
    JSONObject json = new JSONObject();
    String service_url = null;
    JSONObject input_json = new JSONObject();
    JSONObject json_result = new JSONObject();
    JSONObject jsonError = new JSONObject();

    @Override
    public JSONObject callRemoteService(JSONObject object) {

        try {
            service_url = (String) object.get("service_url");
            Log.d("Remote Call : ",service_url);

            httpPost = new HttpPost(service_url);

           /* input_json.put("user_id",(String) object.get("user_id"));
            input_json.put("user_id",(String) object.get("user_id"));
            input_json.put("imei_no",(String) object.get("imei_no"));*/

            StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println("response : "+response.getStatusLine().getStatusCode());

            if(statusCode == 200 ){

                String jsonResponsestring = EntityUtils.toString(response.getEntity());
                JSONObject json_returned =  new JSONObject(jsonResponsestring);
                //instream = entity.getContent();
                //String result = convertStreamToString(instream);
                json_result = json_returned;
                System.out.println("result : "+json_returned);

            }else{
                if(statusCode == 404){
                    //Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    Log.d("RemoteCall","Error : 404 , Requested resource not found");
                    json_result.put("status","404");
                    json_result.put("message","Error : 404 , Requested resource not found");
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    //Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    Log.d("RemoteCall","Error : 500 , Something went wrong at server end");
                    json_result.put("status","500");
                    json_result.put("message","Error : 500 , Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else{
                    //Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    Log.d("RemoteCall","Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                    json_result.put("status","444");
                    json_result.put("message","Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("RemoteCall","Error Occured In Http-Call JSONException: "+e);
            try {
                json_result.put("status","JSONException");
                json_result.put("message","Error : "+e);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("RemoteCall","Error Occured In Http-Call UnsupportedEncodingException: "+e);
            try {
                json_result.put("status","UnsupportedEncodingException");
                json_result.put("message","Error : "+e);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.d("RemoteCall","Error Occured In Http-Call ClientProtocolException: "+e);
            try {
                json_result.put("status","ClientProtocolException");
                json_result.put("message","Error : "+e);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("RemoteCall","Error Occured In Http-Call IOException: "+e);
            try {
                json_result.put("status","IOException");
                json_result.put("message","Error : "+e);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {
                    //System.out.println("Error Occured In Http-Call 2: "+exc);
                    Log.d("RemoteCall","Error Occured In Http-Call 2: "+exc);
                    try {
                        json_result.put("status","Exception");
                        json_result.put("message","Error : 500 , Something went wrong at server end");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return json_result;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }
}
