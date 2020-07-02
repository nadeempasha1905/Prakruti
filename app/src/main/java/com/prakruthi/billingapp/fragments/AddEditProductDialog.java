package com.prakruthi.billingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prakruthi.billingapp.spotbilling.R;

public class AddEditProductDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

       // getDialog().setTitle("Product Description");

        View view = inflater.inflate(R.layout.add_edit_product, container, false);

        return view;


        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface IAddEditProduct {

        public void OnFinishDialogListener();
    }
}
