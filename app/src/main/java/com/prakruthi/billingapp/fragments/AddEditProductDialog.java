package com.prakruthi.billingapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.prakruthi.billingapp.spotbilling.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEditProductDialog extends DialogFragment {

    EditText editText_add_edit_product_name_english;
    EditText editText_add_edit_product_name_kannada;
    EditText editText_add_edit_rate_per_quantity;
    EditText editText_add_edit_discount_rate;
    Spinner spinner_add_edit_measuringunit;
    Spinner spinner_add_edit_discount_type;

    TextInputLayout textinputlayout_productnameenglish;
    TextInputLayout textinputlayout_productnamekannada;
    TextInputLayout textinputlayout_measuringunit;
    TextInputLayout textinputlayout_rateperquantity;
    TextInputLayout textinputlayout_discounttype;
    TextInputLayout textinputlayout_discountrate;

    Button btn_addedit_saverecord;
    Button btn_addedit_cancel;

    ItemDataObject output = new ItemDataObject();
    String selected_measuring_unit;
    String selected_discount_type;

    private IProductAddEditDialogListener callback;

    public interface IProductAddEditDialogListener{
        void onSavingProductDetailsDialog(ItemDataObject object);
    }

    public void setIProductAddEditDialogListener(IProductAddEditDialogListener listener){
        callback = listener;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

       // getDialog().setTitle("Product Description");
        //getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
       // getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.add_edit_product, container, false);

        editText_add_edit_product_name_english = (EditText) view.findViewById(R.id.add_edit_product_name_english);
        editText_add_edit_product_name_kannada = (EditText) view.findViewById(R.id.add_edit_product_name_kannada);
        editText_add_edit_rate_per_quantity = (EditText) view.findViewById(R.id.add_edit_rate_per_quantity);
        editText_add_edit_discount_rate = (EditText) view.findViewById(R.id.add_edit_discount_rate);
        spinner_add_edit_measuringunit = (Spinner) view.findViewById(R.id.add_edit_measuringunit);
        spinner_add_edit_discount_type = (Spinner) view.findViewById(R.id.add_edit_discount_type);

        textinputlayout_productnameenglish = (TextInputLayout) view.findViewById(R.id.textinputlayout_productnameenglish);
        textinputlayout_productnamekannada = (TextInputLayout) view.findViewById(R.id.textinputlayout_productnamekannada);
        //textinputlayout_measuringunit = (TextInputLayout) view.findViewById(R.id.textinputlayout_measuringunit);
        textinputlayout_rateperquantity = (TextInputLayout) view.findViewById(R.id.textinputlayout_rateperquantity);
        textinputlayout_discounttype = (TextInputLayout) view.findViewById(R.id.textinputlayout_discounttype);
        textinputlayout_discountrate = (TextInputLayout) view.findViewById(R.id.textinputlayout_discountrate);

        final String[] measuring_units_list = getResources().getStringArray(R.array.measuring_units_list);

        ArrayAdapter arrayAdapter_measuringunit = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, measuring_units_list);
        arrayAdapter_measuringunit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_add_edit_measuringunit.setAdapter(arrayAdapter_measuringunit);
        spinner_add_edit_measuringunit.setBackground(getActivity().getDrawable(R.drawable.abc_edit_text_material));
        //spinner_add_edit_measuringunit.setBackgroundTintList(getActivity().getColorStateList(R.color.Pastel_Gray));

        final String[] discount_type_list = getResources().getStringArray(R.array.discount_type_list);

        ArrayAdapter arrayAdapter_discount_type_list = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, discount_type_list);
        arrayAdapter_discount_type_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_add_edit_discount_type.setAdapter(arrayAdapter_discount_type_list);
        spinner_add_edit_discount_type.setBackground(getActivity().getDrawable(R.drawable.abc_edit_text_material));
       // spinner_add_edit_discount_type.setBackgroundTintList(getActivity().getColorStateList(R.color.Pastel_Gray));

        spinner_add_edit_measuringunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_measuring_unit = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(adapterView.getContext(), selected_measuring_unit, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_add_edit_discount_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_discount_type = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(adapterView.getContext(), selected_discount_type, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_addedit_saverecord = (Button) view.findViewById(R.id.btn_add_edit_saverecord);
        btn_addedit_cancel = (Button) view.findViewById(R.id.btn_add_edit_cancel);

        btn_addedit_saverecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                output.setmProductNameEnglish(editText_add_edit_product_name_english.getText().toString().trim());
                output.setmProductNameKannada(editText_add_edit_product_name_kannada.getText().toString().trim());
                output.setmProductMeasuringUnit(selected_measuring_unit);
                output.setmProductPrice(editText_add_edit_rate_per_quantity.getText().toString().trim());
                output.setmProductDiscountPrice(editText_add_edit_discount_rate.getText().toString().trim());
                output.setmProductDiscountType(selected_discount_type);

                callback.onSavingProductDetailsDialog(output);
                dismiss();
            }
        });

        btn_addedit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;


        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface IAddEditProduct {

        public void OnFinishDialogListener();
    }
}
