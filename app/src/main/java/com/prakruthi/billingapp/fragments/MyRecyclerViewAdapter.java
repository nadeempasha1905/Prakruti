package com.prakruthi.billingapp.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prakruthi.billingapp.spotbilling.R;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder>  {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
  /*  private static MyClickListener myClickListener;*/

  /*  public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card_view, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.productname.setText(mDataset.get(position).getmProductName());
        holder.productprice.setText(mDataset.get(position).getmProductPrice());
        holder.productdiscountprice.setText(mDataset.get(position).getmProductDiscountPrice());
    }


    public void addItem(DataObject dataObj, int index) {
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

   /* public interface MyClickListener {
        public void onItemClick(int position, View v);

        ;
    }*/

    public class DataObjectHolder extends RecyclerView.ViewHolder  {

        TextView productname;
        TextView productprice;
        TextView productdiscountprice;

        public DataObjectHolder(View itemView) {
            super(itemView);

            productname = (TextView) itemView.findViewById(R.id.product_name);
            productprice = (TextView) itemView.findViewById(R.id.product_price);
            productdiscountprice = (TextView) itemView.findViewById(R.id.product_discount_price);
            Log.i(LOG_TAG, "Adding Listener");
           // itemView.setOnClickListener(this);
        }

      /*  @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }*/
    }


}
