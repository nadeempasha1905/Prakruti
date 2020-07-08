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
import android.widget.Spinner;

import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.utility.GenericClass;
import com.prakruthi.billingapp.utility.GlobalClass;

public class CompanyDetailsActivity extends AppCompatActivity {


    EditText editText_details_company_name_english;
    EditText editText_details_company_name_kannada;
    EditText editText_details_company_address;
    EditText editText_details_company_email;
    EditText editText_details_company_number;

    TextInputLayout textinputlayout_companynameenglish;
    TextInputLayout textinputlayout_companynamekannada;
    TextInputLayout textinputlayout_companyaddress;
    TextInputLayout textinputlayout_companyemail;
    TextInputLayout textinputlayout_companynumber;


    Button btn_company_saverecord;
    Button btn_company_cancel;

    DatabaseImplementation databaseImplementation;

    GlobalClass globalClass=new GlobalClass();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);



        editText_details_company_name_english = (EditText) findViewById(R.id.details_company_name_english);
        editText_details_company_name_kannada = (EditText) findViewById(R.id.details_company_name_kannada);
        editText_details_company_address = (EditText) findViewById(R.id.details_company_address);
        editText_details_company_email = (EditText) findViewById(R.id.details_company_email);
        editText_details_company_number = (EditText) findViewById(R.id.details_company_number);

        textinputlayout_companynameenglish=(TextInputLayout) findViewById(R.id.textinputlayout_companynameenglish);
        textinputlayout_companynamekannada=(TextInputLayout) findViewById(R.id.textinputlayout_companyNameKannada);
        textinputlayout_companyaddress=(TextInputLayout) findViewById(R.id.textinputlayout_companyAddress);
        textinputlayout_companyemail=(TextInputLayout) findViewById(R.id.textinputlayout_companyEmail);
        textinputlayout_companynumber=(TextInputLayout) findViewById(R.id.textinputlayout_companyNumber);

        btn_company_saverecord = (Button) findViewById(R.id.btn_company_saverecord);

        btn_company_saverecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues_companydetails = new ContentValues();

                contentValues_companydetails.put("COMPANY_NAME_ENGLISH",editText_details_company_name_english.getText().toString().trim());
                contentValues_companydetails.put("COMPANY_NAME_KANNADA",editText_details_company_name_kannada.getText().toString().trim());
                contentValues_companydetails.put(" COMPANY_ADDRESS TEXT",editText_details_company_address.getText().toString().trim());

                contentValues_companydetails.put("COMPANY_PHONE",editText_details_company_number.getText().toString().trim());
                contentValues_companydetails.put("COMPANY_EMAIL_ID",editText_details_company_email.getText().toString().trim());
                contentValues_companydetails.put("CREATED_BY",globalClass.getUsername() );
                contentValues_companydetails.put("CREATED_ON", GenericClass.getDateTime());
                contentValues_companydetails.put("UPDATED_BY", "");
                contentValues_companydetails.put("UPDATED_ON", "");


                databaseImplementation=DatabaseImplementation.getInstance(getApplicationContext());
                long res=databaseImplementation.updateCompanyDetailsToTable(contentValues_companydetails);
                if (res > 0) {
                    ShowAlert("success","Company Details updated","Company");

                }else {
                    if(res==0){
                        ShowAlert("fail","Company Details not updated ","Company");
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