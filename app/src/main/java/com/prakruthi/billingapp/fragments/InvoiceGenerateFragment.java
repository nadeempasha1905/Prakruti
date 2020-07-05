package com.prakruthi.billingapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.prakruthi.billingapp.spotbilling.R;

@SuppressLint("ValidFragment")
public class InvoiceGenerateFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invoice_generate, container, false);

        return view;

        //return inflater.inflate(R.layout.fragment_invoice_generate, container, false);
    }

    public interface OnFragmentInteractionListener {
    }
}