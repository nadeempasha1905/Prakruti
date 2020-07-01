package com.prakruthi.billingapp.spotbilling;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prakruthi.billingapp.bean.BillingMasterBO;
import com.prakruthi.billingapp.bean.ResultMasterBO;
import com.prakruthi.billingapp.fragments.BillingFragment;
import com.prakruthi.billingapp.fragments.DividerItemDecoration;
import com.ngx.BluetoothPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    List<ResultMasterBO> RESULT_LIST = new ArrayList();

    private LoadResult asyncLoadResult = null;

    ResultRecycler resultRecycler  = null;

    private BluetoothPrinter mBtp = MainActivity.mBtp;

    private Button print_bill ;

    BillingMasterBO billingMasterBO ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.result_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Billed Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BillingFragment.class));
            }
        });

        Bundle bundle = getIntent().getBundleExtra("bundle");
        billingMasterBO = (BillingMasterBO) bundle.getParcelable("billedrecord");
        List<ResultMasterBO> resultMasterBOList = (ArrayList<ResultMasterBO>)bundle.getSerializable("resultMasterBOList");

        Log.d("billingMasterBO",billingMasterBO+"");
        Log.d("resultMasterBOList",resultMasterBOList+"");


        /*GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.success_gif);*/

        /*Table Defination*/
        /*TableLayout MainTable =  (TableLayout)findViewById(R.id.billdetail_table);
        TableRow tr_head  =  null;*/


    /*Adding Table Columns*/
        /*TextView right_label = new TextView(this);
        TextView left_label  = new TextView(this);

        tr_head = new TableRow(this);
        tr_head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tr_head.setBackgroundResource(R.drawable.table_row_bg);
        right_label = new TextView(this);left_label  = new TextView(this);
        right_label.setText("Rr Number");right_label.setBackgroundResource(R.drawable.table_cell_bg);right_label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);tr_head.addView(right_label);
        left_label.setText(billingMasterBO.getKEY_RR_NO());left_label.setBackgroundResource(R.drawable.table_cell_bg);tr_head.addView(left_label);
        MainTable.addView(tr_head, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        tr_head = new TableRow(this);tr_head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tr_head.setBackgroundResource(R.drawable.table_row_bg);
        right_label = new TextView(this);left_label  = new TextView(this);
        right_label.setText("Account ID");right_label.setBackgroundResource(R.drawable.table_cell_bg);right_label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);tr_head.addView(right_label);
        left_label.setText(billingMasterBO.getIVRSID());left_label.setBackgroundResource(R.drawable.table_cell_bg);tr_head.addView(left_label);
        MainTable.addView(tr_head, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        tr_head = new TableRow(this);tr_head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tr_head.setBackgroundResource(R.drawable.table_row_bg);
        right_label = new TextView(this);left_label  = new TextView(this);
        right_label.setText("Meter Code");right_label.setBackgroundResource(R.drawable.table_cell_bg);right_label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);tr_head.addView(right_label);
        left_label.setText(billingMasterBO.getKEY_READER_CODE());left_label.setBackgroundResource(R.drawable.table_cell_bg);tr_head.addView(left_label);
        MainTable.addView(tr_head, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        tr_head = new TableRow(this);tr_head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tr_head.setBackgroundResource(R.drawable.table_row_bg);
        right_label = new TextView(this);left_label  = new TextView(this);
        right_label.setText("Name");right_label.setBackgroundResource(R.drawable.table_cell_bg);right_label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);tr_head.addView(right_label);
        left_label.setText(billingMasterBO.getKEY_CONSMR_NAME());left_label.setBackgroundResource(R.drawable.table_cell_bg);tr_head.addView(left_label);
        MainTable.addView(tr_head, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
*/
        recyclerView = (RecyclerView) findViewById(R.id.result_recyclerview);


       // RESULT_LIST.clear();

      //  asyncLoadResult = new LoadResult(resultMasterBOList);



        resultRecycler = new ResultRecycler(resultMasterBOList);


        Log.d("Testing","Testing RESULT_LIST");
        Iterator<ResultMasterBO> it = resultMasterBOList.iterator();
        while(it.hasNext()){
            ResultMasterBO bo = it.next();

            Log.d(""+bo.getKey(),"---"+bo.getValue());
        }


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        assert recyclerView != null;

        recyclerView.setAdapter(resultRecycler);

       /* try {
            asyncLoadResult.execute((Void) null).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/



        resultRecycler.notifyDataSetChanged();
        recyclerView.setFocusable(true);


        print_bill = findViewById(R.id.result_print_bill);

        print_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                printEnglishBill();
                //printKannadaBill();
            }
        });



    }

    private class LoadResult extends AsyncTask<Void, Void, Boolean> {

        List<ResultMasterBO> tempreResultMasterBOList ;

        public LoadResult(List<ResultMasterBO> resultMasterBOList) {
            this.tempreResultMasterBOList = resultMasterBOList;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            RESULT_LIST = tempreResultMasterBOList;

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }
    }



    private void printEnglishBill() {
        try {
            if (mBtp.getState() != BluetoothPrinter.STATE_CONNECTED) {
                Toast.makeText(this.getApplicationContext(), "Printer is not connected", Toast.LENGTH_SHORT).show();
                return;
            }

          //  Uri fileUri = Uri.fromFile(new File("file:///android_asset/fonts/bescom_logo.jpg"));

            AssetManager am = getAssets();
            InputStream inputStream = am.open("fonts/mescom_logo.jpg");
            File file = createFileFromInputStream(inputStream);

            Log.d("file:",""+file.getPath());

            mBtp.printImage(file.getPath());

            String separator = "--------------------------------";
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSansMono.ttf");

            TextPaint tp = new TextPaint();
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
            tp.setTextSize(30);
            tp.setColor(Color.BLACK);
            mBtp.addText("ಮಂಗಳೂರು ವಿದ್ಯುತ್ ಸರಬರಾಜು ಕಂಪನಿ" +
                    "\n" +
                    "ವಿದ್ಯುತ್ ಬಿಲ್ / Electricity Bill", Layout.Alignment.ALIGN_CENTER, tp);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Account Details\n");
            stringBuilder.append("ಆರ್.ಆರ್ ಸಂಖ್ಯೆ/R.R Number       "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಖಾತೆ ಸಂಖ್ಯೆ/Acc Id              "+billingMasterBO.getIVRSID()+"\n");
            stringBuilder.append("ಮಾ.ಓ.ಸಂಕೇತ/M.R Code          "+billingMasterBO.getKEY_READER_CODE()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Personal Details\n");
            stringBuilder.append("ಹೆಸರು ಮತ್ತು ವಿಳಾಸ /Name and Address\n");
            stringBuilder.append(billingMasterBO.getKEY_CONSMR_NAME()+" "
                    +billingMasterBO.getKEY_ADDRESS1()+" "
                    +billingMasterBO.getKEY_ADDRESS2()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Connection Details\n");
            stringBuilder.append("ಜಕಾತಿ/Tariff                  "+billingMasterBO.getKEY_TARIFF_CODE()+"\n");
            stringBuilder.append("ಮಂ.ಪ್ರಮಾಣ/Sanc Load          "+billingMasterBO.getKEY_SANCT_KW()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Billing Details\n");
            stringBuilder.append("ಬಿಲ್ ಅವಧಿ/Bill Period          "+billingMasterBO.getPARTFRACTION()+"\n");
            stringBuilder.append("ರೀಡಿಂಗ್ ದಿನಾಂಕ/Reading Date      "+billingMasterBO.getKEY_READING_DATE()+"\n");
            stringBuilder.append("ಬಿಲ್ ಸಂಖ್ಯೆ/Bill Number          "+billingMasterBO.getKEY_BILL_NO()+"\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Consumption Detail\n");
            stringBuilder.append("ಇಂದಿನ ಮಾಪನ/Pres. Rdg          "+billingMasterBO.getPRESENT_RDG()+"\n");
            stringBuilder.append("ಹಿಂದಿನ ಮಾಪನ/Prev. Rdg          "+billingMasterBO.getKEY_PREV_MTR_RDG()+"\n");
            stringBuilder.append("ಮಾಪಕ ಸ್ಥಿರಾಂಕ/Constant          "+billingMasterBO.getMETER_CONST()+"\n");
            stringBuilder.append("ಬಳಕೆ/Consumption(Units)         "+billingMasterBO.getN_UNITSCONSUMED()+"\n");
            stringBuilder.append("ಸರಾಸರಿ/Average                  "+billingMasterBO.getKEY_AVG_CONSUMPTION()+"\n");
            stringBuilder.append("ದಾಖಲಿತ ಬೇಡಿಕೆ/Recorded MD        -\n");
            stringBuilder.append("ಪವರ್ ಫ್ಯಾಕ್ಟರ್/Power Factor      "+billingMasterBO.getKEY_POWER_FACTOR()+"\n");
            stringBuilder.append("ಸಂ. ಪ್ರಮಾಣ/Connected Load      -\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ನಿಗದಿತ ಶುಲ್ಕ/Fixed Charges(Unit,Rate,Amount)\n");

            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ವಿದ್ಯುತ್ ಶುಲ್ಕ/Energy Charges(Unit,Rate,Amount)\n");

            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಇಂದನ ಹೊಂದಾಣಿಕೆ ಶುಲ್ಕ/FAC Charges(Unit,Rate,Amount)\n");

            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಹೆಚ್ಚುವರಿ ಶುಲ್ಕ/Additional Charges\n");
            stringBuilder.append("ರಿಯಾಯಿತಿ/Rebate                  "+billingMasterBO.getN_REBATE() * -1+"\n");
            stringBuilder.append("ಪಿ.ಎಫ್. ದಂಡ/PF Penalty          "+billingMasterBO.getPF_PENALTY()+"\n");
            stringBuilder.append("ಹೇ.ಲೋ.ದಂಡ/Ex.Load/MD Penalty  0.00\n");
            stringBuilder.append("ಬಡ್ಡಿ/Interest                    "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಇತರೇ/Others                     "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ತೆರಿಗೆ/Tax                         "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಬಿಲ್ ಮೊತ್ತ/Bill Amount            "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಬಾಕಿ/Arrears                     "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಜಮೆ/Credits & Adjustments       "+billingMasterBO.getKEY_RR_NO()+"\n");
            stringBuilder.append("ಸರ್ಕಾರದ ಸಹಾಯ ಧನ/GOK Subsidy    "+billingMasterBO.getKEY_RR_NO()+"\n");

            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಪಾ. ಮೊತ್ತ/Net Amount Due         "+billingMasterBO.getKEY_RR_NO()+" \n");
            stringBuilder.append("ಪಾವತಿಗೆ ಕಡೆಯ ದಿನಾಂಕ/Due Date      "+billingMasterBO.getKEY_RR_NO()+"\n");




/*            stringBuilder.append("Place: Bangalore\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Particulars    Qty   Rate    Amt\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Reynolds Pen     2     10     20\n");
            stringBuilder.append("Nataraj Eraser  10      5     50\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("Tot Items: 2      Amount: 66.50\n");
            stringBuilder.append("Tot Qty  :12     Vat Amt:  3.50\n");
            stringBuilder.append("                  -------------");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

            stringBuilder.setLength(0);
            stringBuilder.append("           Net Amt: 70.00");
            tp.setTextSize(25);
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);


            stringBuilder.setLength(0);
            stringBuilder.append("Payment Mode: CASH\n\n\n");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);*/

            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
            mBtp.print();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private File createFileFromInputStream(InputStream inputStream) {

        String my_file_name = "mescom_logo.jpg";

        String path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(path + "/Logo" );

        try{

            if (!directory.exists()) {
                directory.mkdirs();
                Log.d("[" + this.getClass().getSimpleName() + "]-->", "Making dirs");
            }

            final File myFile = new File(directory.getAbsolutePath(), my_file_name);
            myFile.createNewFile();

            OutputStream outputStream = new FileOutputStream(myFile);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();


            return myFile;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    private void printKannadaBill() {

        try {
            if (mBtp.getState() != BluetoothPrinter.STATE_CONNECTED) {
                Toast.makeText(this.getApplicationContext(), "Printer is not connected", Toast.LENGTH_SHORT).show();
                return;
            }
            String separator = "--------------------------------";
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSansMono.ttf");

            TextPaint tp = new TextPaint();

            tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
            tp.setTextSize(30);
            tp.setColor(Color.BLACK);
            mBtp.addText("ನಗದು ಬಿಲ್ಲು", Layout.Alignment.ALIGN_CENTER, tp);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ದಿನಾಂಕ: 31-05-2017  ಬಿಲ್ ಸಂಖ್ಯೆ: 001\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಹೆಸರು: ವಿನಾಯಕ\n");
            stringBuilder.append("ಸ್ಥಳ: ಬೆಂಗಳೂರು\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ವಿವರಣೆ        ಪ್ರಮಾಣ   ದರ    ಮೊತ್ತ\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ರೆನಾಲ್ಡ್ಸ್ ಪೆನ್        2   10     20\n");
            stringBuilder.append("ನಟರಾಜ್ ಎರೇಸರ್    10    5     50\n");
            stringBuilder.append(separator);
            stringBuilder.append("\n");
            stringBuilder.append("ಒಟ್ಟು ಐಟಂಗಳು: 2       ಮೊತ್ತ:  66.50\n");
            stringBuilder.append("ಒಟ್ಟು ಪ್ರಮಾಣ: 12   ವ್ಯಾಟ್ ಮೊತ್ತ: 3.50\n");
            stringBuilder.append("                  -------------");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

            stringBuilder.setLength(0);
            stringBuilder.append("         ನಿವ್ವಳ ಮೊತ್ತ: 70.00");
            tp.setTextSize(25);
            tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

            stringBuilder.setLength(0);
            stringBuilder.append("ಪಾವತಿ ಮೋಡ್: ನಗದು\n\n\n");
            tp.setTextSize(20);
            tp.setTypeface(tf);
            mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
            mBtp.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
