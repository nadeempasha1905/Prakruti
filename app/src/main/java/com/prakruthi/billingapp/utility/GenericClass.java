package com.prakruthi.billingapp.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GenericClass {

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
