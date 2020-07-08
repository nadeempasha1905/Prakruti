package com.prakruthi.billingapp.spotbilling;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.utility.GenericClass;
import com.prakruthi.billingapp.utility.GlobalClass;

public class UserDetailsActivity extends AppCompatActivity {



    EditText editText_user_details_user_id;
    EditText editText_user_details_user_pw;
    EditText editText_user_details_user_pw_retype;
    EditText editText_user_details_imei;
    EditText getEditText_user_details_user_role;

    TextInputLayout textinputlayout_userid;
    TextInputLayout textinputlayout_userpw;
    TextInputLayout textinputlayout_userpwRetype;
    TextInputLayout textinputlayout_imei;
    TextInputLayout textinputlayout_userrole;


    Button btn_user_edit_saverecord;
    Button btn_user_cancel;

    DatabaseImplementation databaseImplementation;

    GlobalClass globalClass=new GlobalClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        editText_user_details_user_id = (EditText) findViewById(R.id.user_details_user_id);
        editText_user_details_user_pw= (EditText) findViewById(R.id.user_details_user_pw);
        editText_user_details_user_pw_retype = (EditText) findViewById(R.id.user_details_user_pw_retype);
        editText_user_details_imei=(EditText) findViewById(R.id.user_details_imei);
        getEditText_user_details_user_role=(EditText) findViewById(R.id.user_details_user_role);


        textinputlayout_userid=(TextInputLayout) findViewById(R.id.textinputlayout_userid);
        textinputlayout_userpw=(TextInputLayout) findViewById(R.id.textinputlayout_userpw);
        textinputlayout_userpwRetype=(TextInputLayout) findViewById(R.id.textinputlayout_userpwRetype);
        textinputlayout_imei=(TextInputLayout) findViewById(R.id.textinputlayout_imei);
        textinputlayout_userrole=(TextInputLayout) findViewById(R.id.textinputlayout_userrole);
        btn_user_edit_saverecord = (Button) findViewById(R.id.btn_user_edit_saverecord);

        btn_user_edit_saverecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues contentValues_userdetails = new ContentValues();

                contentValues_userdetails.put("USER_ID",editText_user_details_user_id.getText().toString().trim());
                contentValues_userdetails.put("USER_PASSWORD",editText_user_details_user_pw.getText().toString().trim());
                contentValues_userdetails.put("IMEI_NO TEXT",editText_user_details_imei.getText().toString().trim());
                contentValues_userdetails.put("USER_ROLE_ID",getEditText_user_details_user_role.getText().toString().trim());
                contentValues_userdetails.put("CREATED_BY",globalClass.getUsername() );
                contentValues_userdetails.put("CREATED_ON", GenericClass.getDateTime());
                contentValues_userdetails.put("UPDATED_BY", "");
                contentValues_userdetails.put("UPDATED_ON", "");


                databaseImplementation=DatabaseImplementation.getInstance(getApplicationContext());
                long res=databaseImplementation.updateUserDetailsToTable(contentValues_userdetails);
                if (res > 0) {
                    ShowAlert("success","User Details updated","Company");

                }else {
                    if(res==0){
                        ShowAlert("fail","User Details not updated ","Company");
                    }
                    else if(res==-10){
                        ShowAlert("fail","Error occured ","Company");
                    }
                }


            }
        });



    }

    private void ShowAlert(String status, final String message, String title) {

        android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());


        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status.equals("success")) {
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.succes);

            // Setting Dialog Title
            alertDialog.setTitle(title);

        } else {
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.fail);
            alertDialog.setTitle(Html.fromHtml("<font color='#FF0800'>"+title+"</font>"));
        }

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Showing Alert Message
        alertDialog.show();





    }
}