package com.prakruthi.billingapp.asynchronous;

import org.json.JSONObject;

/**
 * Created by Admin on 1/19/2017.
 */

public interface IRemoteCall {

    JSONObject callRemoteService(JSONObject object);
}
