package com.prakruthi.billingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prakruthi.billingapp.bean.KeyValueAdapter;
import com.prakruthi.billingapp.database.DatabaseImplementation;
import com.prakruthi.billingapp.spotbilling.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressLint("ValidFragment")
public class InvoiceGenerateFragment extends Fragment implements InvoiceResultFragment.OnFragmentInteractionListener {


    DatabaseImplementation databaseImplementation;

    SearchableSpinner searchablespinner;
    String selected_product_name;
    ItemDataObject selected_product = null;

    TextView textView_productname;
    TextView textView_productprice;
    TextView textView_productmeasureunit;
    TextView textView_productdiscounttype;
    TextView textView_productdiscountrate;

    EditText productquantity_invoicegenerate;
    EditText INVOICENUMBER_invoicegenerate;
    EditText INVOICEDATE_invoicegenerate;

    List<KeyValueAdapter> productlistspinner_adapter = new ArrayList<>();
    ArrayAdapter<KeyValueAdapter> productlistKeyValueAdapterArrayAdapter;

    List<ItemDataObject> INVOICE_OBJECT = new ArrayList<>();

    List<InvoiceDataObject> INVOICE_RESULT_OBJECT = new ArrayList<>();

    Button btn_addtoinvoice;
    Button btn_clearselected;
    Button btn_generateinvoice;
    Button btn_cancelinvoice;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Integer INVOICE_NUMBER = 0;
    String INVOICE_DATE = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invoice_generate, container, false);


        searchablespinner = (SearchableSpinner) view.findViewById(R.id.searchablespinner);

        //textView_productname = (TextView) view.findViewById(R.id.productname_invoicegenerate);
        textView_productprice = (TextView) view.findViewById(R.id.productprice_invoicegenerate);
        //textView_productmeasureunit = (TextView) view.findViewById(R.id.productmeasureunit_invoicegenerate);
        //textView_productdiscounttype = (TextView) view.findViewById(R.id.productdiscounttype_invoicegenerate);
        //textView_productdiscountrate = (TextView) view.findViewById(R.id.productdiscountrate_invoicegenerate);

        productquantity_invoicegenerate = (EditText) view.findViewById(R.id.productquantity_invoicegenerate);
        INVOICENUMBER_invoicegenerate = (EditText) view.findViewById(R.id.INVOICENUMBER_invoicegenerate);
        INVOICEDATE_invoicegenerate = (EditText) view.findViewById(R.id.INVOICEDATE_invoicegenerate);

        databaseImplementation = DatabaseImplementation.getInstance(getActivity());

        INVOICE_NUMBER = databaseImplementation.getMaxInvoiceNumber();
        Toast.makeText(getActivity(),"INVOICE_NUMBER : "+INVOICE_NUMBER,Toast.LENGTH_SHORT).show();

        INVOICENUMBER_invoicegenerate.setText(""+INVOICE_NUMBER);

        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        System.out.println(formatter.format(date));
        INVOICE_DATE = formatter.format(date);
        INVOICEDATE_invoicegenerate.setText(INVOICE_DATE);

        JSONArray productlist_array = databaseImplementation.getProductListForSpinner();
        for (int j = 0; j < productlist_array.length(); j++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) productlist_array.get(j);
                productlistspinner_adapter.add(new KeyValueAdapter((String) jsonObject.get("value"), (String) jsonObject.get("key")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        productlistKeyValueAdapterArrayAdapter = new ArrayAdapter<KeyValueAdapter>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, productlistspinner_adapter);
        productlistKeyValueAdapterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchablespinner.setAdapter(productlistKeyValueAdapterArrayAdapter);

        searchablespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                KeyValueAdapter getkeyValueAdapter = (KeyValueAdapter) parent.getItemAtPosition(position);
                String key = (String) getkeyValueAdapter.tag;

                selected_product = (ItemDataObject) databaseImplementation.getProductDetailById(Integer.parseInt(key));

                //textView_productname.setText(selected_product.getmProductNameEnglish() + "(" + selected_product.getmProductNameKannada() + ")");
                textView_productprice.setText(selected_product.getmProductPrice());
                //textView_productmeasureunit.setText(selected_product.getmProductMeasuringUnit());
                //textView_productdiscounttype.setText(selected_product.getmProductDiscountType());
                //textView_productdiscountrate.setText(selected_product.getmProductDiscountPrice());

                //Toast.makeText(parent.getContext(), key, Toast.LENGTH_SHORT).show();

                if(selected_product != null){
                    btn_addtoinvoice.setVisibility(View.VISIBLE);
                    btn_clearselected.setVisibility(View.VISIBLE);
                }else{
                    btn_addtoinvoice.setVisibility(View.GONE);
                    btn_clearselected.setVisibility(View.GONE);
                }

                productquantity_invoicegenerate.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(productquantity_invoicegenerate, InputMethodManager.SHOW_IMPLICIT);
//                InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_addtoinvoice = (Button) view.findViewById(R.id.btn_addtoinvoice_invoicegenerate);
        btn_clearselected = (Button) view.findViewById(R.id.btn_clearselected_invoicegenerate);
        btn_generateinvoice = (Button) view.findViewById(R.id.btn_generateinvoice_invoicegenerate);
        btn_cancelinvoice = (Button) view.findViewById(R.id.btn_cancelinvoice_invoicegenerate);

        btn_addtoinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((textView_productprice.getText().toString().trim()).length() == 0){
                    ShowAlert("fail","Price is empty","Invoice");
                    return;
                }
                if((productquantity_invoicegenerate.getText().toString().trim()).length() == 0){
                    ShowAlert("fail","Enter Quantity","Invoice");
                    return;
                }

                selected_product.setQuantity(productquantity_invoicegenerate.getText().toString().trim());

                INVOICE_OBJECT.add(selected_product);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setFocusable(true);
                Log.d("INVOICE_OBJECT",""+INVOICE_OBJECT);

                selected_product = null;
                //productquantity_invoicegenerate.requestFocus();
                //InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(productquantity_invoicegenerate, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
              // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,InputMethodManager.HIDE_IMPLICIT_ONLY);

                InputMethodManager imm =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                searchablespinner.setId(0);
                textView_productprice.setText("");
                productquantity_invoicegenerate.setText("");

                if(selected_product != null){
                    btn_addtoinvoice.setVisibility(View.VISIBLE);
                    btn_clearselected.setVisibility(View.VISIBLE);
                }else{
                    btn_addtoinvoice.setVisibility(View.GONE);
                    btn_clearselected.setVisibility(View.GONE);
                }

                if(INVOICE_OBJECT.size() == 0){
                    //hide
                    btn_generateinvoice.setVisibility(View.GONE);
                    btn_cancelinvoice.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }else if(INVOICE_OBJECT.size() > 0){
                    //show
                    btn_generateinvoice.setVisibility(View.VISIBLE);
                    btn_cancelinvoice.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_generateinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure to genereate invoice?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @SuppressLint("LongLogTag")
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Iterator it = INVOICE_OBJECT.iterator();

                                while (it.hasNext()){
                                    ItemDataObject item = (ItemDataObject) it.next();

                                    InvoiceDataObject invoice = new InvoiceDataObject();

                                    invoice.setInvoiceDate(INVOICE_DATE);
                                    invoice.setInvoiceNumber(INVOICE_NUMBER);
                                    invoice.setItemId(item.getmId());
                                    invoice.setmProductNameEnglish(item.getmProductNameEnglish());
                                    invoice.setmProductNameKannada(item.getmProductNameKannada());
                                    invoice.setmProductMeasuringUnit(item.getmProductMeasuringUnit());
                                    invoice.setmProductDiscountPrice(item.getmProductDiscountPrice());
                                    invoice.setmProductDiscountType(item.getmProductDiscountType());
                                    invoice.setmProductPrice(item.getmProductPrice());
                                    invoice.setTotalItems(INVOICE_OBJECT.size());
                                    invoice.setPrice(new BigDecimal(item.getmProductPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    invoice.setQuantity(new BigDecimal(item.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP));

                                    if(item.getmProductDiscountType().equals("Percentage")){
                                        Double itemprice = Double.parseDouble(item.getmProductPrice());
                                        Double itemqty = Double.parseDouble(item.getQuantity());
                                        Double itemdiscountprice = Double.parseDouble(item.getmProductDiscountPrice());

                                        Double grossamount =  itemprice * itemqty;
                                        Double grossamount_discount =  ((itemdiscountprice/100)*grossamount);
                                        Double netamount = grossamount - grossamount_discount;

                                        invoice.setGrossAmount(new BigDecimal(grossamount).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        invoice.setDiscountAmount(new BigDecimal(grossamount_discount).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        invoice.setNetAmount(new BigDecimal(netamount).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    }
                                    else if(item.getmProductDiscountType().equals("Price")){
                                        Double itemprice = Double.parseDouble(item.getmProductPrice());
                                        Double itemqty = Double.parseDouble(item.getQuantity());
                                        Double itemdiscountprice = Double.parseDouble(item.getmProductDiscountPrice());

                                        Double grossamount =  itemprice * itemqty;
                                        Double grossamount_discount =  itemdiscountprice; //((itemdiscountprice/100)*grossamount);
                                        Double netamount = grossamount - grossamount_discount;

                                        invoice.setGrossAmount(new BigDecimal(grossamount).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        invoice.setDiscountAmount(new BigDecimal(grossamount_discount).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        invoice.setNetAmount(new BigDecimal(netamount).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    }

                                    Log.d("setInvoiceDate",""+invoice.getInvoiceDate());
                                    Log.d("setInvoiceNumber",""+invoice.getInvoiceNumber());
                                    Log.d("setItemId",""+invoice.getItemId());
                                    Log.d("setmProductNameEnglish",""+invoice.getmProductNameEnglish());
                                    Log.d("setmProductNameKannada",""+invoice.getmProductNameKannada());
                                    Log.d("setmProductMeasuringUnit",""+invoice.getmProductMeasuringUnit());
                                    Log.d("setmProductDiscountPrice",""+invoice.getmProductDiscountPrice());
                                    Log.d("setmProductDiscountType",""+invoice.getmProductDiscountType());
                                    Log.d("setmProductPrice",""+invoice.getmProductPrice());
                                    Log.d("setTotalItems",""+invoice.getTotalItems());
                                    Log.d("setPrice",""+invoice.getPrice());
                                    Log.d("setQuantity",""+invoice.getQuantity());
                                    Log.d("setGrossAmount",""+invoice.getGrossAmount());
                                    Log.d("setDiscountAmount",""+invoice.getDiscountAmount());
                                    Log.d("setNetAmount",""+invoice.getNetAmount());

                                    INVOICE_RESULT_OBJECT.add(invoice);
                                }

                                Bundle data = new Bundle();//Use bundle to pass data
                                data.putParcelableArrayList("INVOICE_RESULT_OBJECT", (ArrayList<? extends Parcelable>) INVOICE_RESULT_OBJECT);
                                //creating fragment object
                                Fragment fragment = null;
                                fragment = new InvoiceResultFragment();
                                fragment.setArguments(data);
                                if (fragment != null) {
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.bottom_navigation_content_frame, fragment);
                                    ft.commit();
                                }
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog,int id)
                            {
                                dialog.cancel();
                            }
                        });
                AlertDialog  alertDialog = builder.create();
                alertDialog.show();


            }
        });

        mAdapter = new InvoiceRecyclerViewAdapter(INVOICE_OBJECT,getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_invoicegenerate);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        /*mAdapter = new MyRecyclerViewAdapter(getDataSet());*/
        mRecyclerView.setAdapter(mAdapter);

        if(selected_product != null){
            btn_addtoinvoice.setVisibility(View.VISIBLE);
            btn_clearselected.setVisibility(View.VISIBLE);
        }else{
            btn_addtoinvoice.setVisibility(View.GONE);
            btn_clearselected.setVisibility(View.GONE);
        }

        if(INVOICE_OBJECT.size() == 0){
            //hide
            btn_generateinvoice.setVisibility(View.GONE);
            btn_cancelinvoice.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }else if(INVOICE_OBJECT.size() > 0){
            //show
            btn_generateinvoice.setVisibility(View.VISIBLE);
            btn_cancelinvoice.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListenerForEdit(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
               // Toast.makeText(getActivity(),"on single click:"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                if(INVOICE_OBJECT.size()>0){
                    final int deletePosition = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Alert!");
                    builder.setMessage("Do you want delete this item?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    INVOICE_OBJECT.remove(deletePosition);
                                    mAdapter.notifyDataSetChanged();
                                    mRecyclerView.setFocusable(true);

                                    if(INVOICE_OBJECT.size() == 0){
                                        //hide
                                        btn_generateinvoice.setVisibility(View.GONE);
                                        btn_cancelinvoice.setVisibility(View.GONE);
                                        mRecyclerView.setVisibility(View.GONE);
                                    }else if(INVOICE_OBJECT.size() > 0){
                                        //show
                                        btn_generateinvoice.setVisibility(View.VISIBLE);
                                        btn_cancelinvoice.setVisibility(View.VISIBLE);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                    }
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,int id)
                                {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog  alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }));

        return view;

        //return inflater.inflate(R.layout.fragment_invoice_generate, container, false);
    }

    private void ShowAlert(String status, final String message, String title) {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());


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

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setFocusable(true);
    }



    public interface OnFragmentInteractionListener {
    }

}




