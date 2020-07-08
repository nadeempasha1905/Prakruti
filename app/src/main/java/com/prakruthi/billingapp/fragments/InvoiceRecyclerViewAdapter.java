package com.prakruthi.billingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.prakruthi.billingapp.spotbilling.R;

import org.w3c.dom.Text;

import java.util.List;

public class InvoiceRecyclerViewAdapter extends RecyclerView.Adapter<InvoiceRecyclerViewAdapter.DataObjectHolder>  {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<ItemDataObject> mDataset;
    private Context context;

    public InvoiceRecyclerViewAdapter(List<ItemDataObject> myDataset, FragmentActivity activity) {
        mDataset = myDataset;
        context = activity;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_row_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.productnameenglish.setText(mDataset.get(position).getmProductNameEnglish());
        holder.productnamekannnada.setText("("+mDataset.get(position).getmProductNameKannada()+")");
        holder.productquantity.setText(" - "+mDataset.get(position).getQuantity()+" Qty");

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                }
            });

        }

    public void addItem(ItemDataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder  {

        TextView productnameenglish;
        TextView productnamekannnada;
        ImageButton imageButton;
        TextView productquantity;

        public DataObjectHolder(View itemView) {
            super(itemView);

            productnameenglish = (TextView) itemView.findViewById(R.id.productname_english_invoicerow);
            productnamekannnada = (TextView) itemView.findViewById(R.id.productname_kannada_invoicerow);
            imageButton = (ImageButton) itemView.findViewById(R.id.imgbtnDelete_invoicerow);
            productquantity = (TextView) itemView.findViewById(R.id.productname_quantity_invoicerow);
            Log.i(LOG_TAG, "Adding Listener");
        }

    }


}
