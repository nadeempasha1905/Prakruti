package com.prakruthi.billingapp.router;

/**
 * Created by Nadeem on 8/6/2017.
 */

public class ServiceURL {

    public static String DOMAIN_ADDRESS =

            //   "http://192.168.0.102:8080";
            //   "http://192.168.43.102:8080";
            //   "http://192.168.15.3:8080";
               "http://43.247.156.66:82";

    public static String PROJECT_NAME = "/CloudbillWebServices/cloud/services/";

    public static String DOMAIN_NAME = DOMAIN_ADDRESS + PROJECT_NAME;

    public static String HANDSHAKE = DOMAIN_NAME + "handshake";

    public static String GET_SBD_DETAILS = DOMAIN_NAME + "getSBDDetails";

    public static String DOWNLOAD_MASTER_DATA = DOMAIN_NAME + "getBillingDataFromTable";

    public static String UPLOAD_SINGLE_RECORD_FROM_JOBSCHEDULER = DOMAIN_NAME + "uploadsinglerecordfromjobscheduler";


}
