package com.prakruthi.billingapp.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prakruthi.billingapp.spotbilling.R;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder>  {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<ItemDataObject> mDataset;
  /*  private static MyClickListener myClickListener;*/

  /*  public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerViewAdapter(List<ItemDataObject> myDataset) {
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
        holder.productnameenglish.setText(mDataset.get(position).getmProductNameEnglish());
        holder.productnamekannnada.setText("("+mDataset.get(position).getmProductNameKannada()+")");
        holder.productmeasuringunit.setText(mDataset.get(position).getmProductMeasuringUnit());
        holder.productprice.setText(mDataset.get(position).getmProductPrice());
        holder.productdiscounttype.setText(mDataset.get(position).getmProductDiscountType());
        holder.productdiscountprice.setText(mDataset.get(position).getmProductDiscountPrice());
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

   /* public interface MyClickListener {
        public void onItemClick(int position, View v);

        ;
    }*/

    public class DataObjectHolder extends RecyclerView.ViewHolder  {

        TextView productnameenglish;
        TextView productprice;
        TextView productdiscountprice;
        TextView productnamekannnada;
        TextView productmeasuringunit;
        TextView productdiscounttype;

        public DataObjectHolder(View itemView) {
            super(itemView);

            productnameenglish = (TextView) itemView.findViewById(R.id.product_name_english);
            productprice = (TextView) itemView.findViewById(R.id.product_price);
            productdiscountprice = (TextView) itemView.findViewById(R.id.product_discount_price);
            productnamekannnada = (TextView) itemView.findViewById(R.id.product_name_kannada);
            productmeasuringunit = (TextView) itemView.findViewById(R.id.product_measuring_unit);
            productdiscounttype = (TextView) itemView.findViewById(R.id.product_discount_type);
            Log.i(LOG_TAG, "Adding Listener");
           // itemView.setOnClickListener(this);
        }

      /*  @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }*/
    }


}
