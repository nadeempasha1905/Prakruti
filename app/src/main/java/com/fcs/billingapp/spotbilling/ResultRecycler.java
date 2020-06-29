package com.fcs.billingapp.spotbilling;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fcs.billingapp.bean.ResultMasterBO;

import java.util.List;

public class ResultRecycler extends RecyclerView.Adapter<ResultRecycler.MyViewHolder> {

    List<ResultMasterBO> resultMasterBOList ;

    public ResultRecycler(List<ResultMasterBO> result_list) {
        this.resultMasterBOList = result_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_result_record_style,parent,false);
        return new ResultRecycler.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ResultMasterBO resultMasterBO = resultMasterBOList.get(position);

        holder.result_label.setText(resultMasterBO.getKey());
        holder.result_value.setText(resultMasterBO.getValue());

        Log.d(""+resultMasterBO.getKey(),"-"+resultMasterBO.getValue());

    }

    @Override
    public int getItemCount() {
        return resultMasterBOList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView result_label;
        public TextView result_value;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.result_label = (TextView) itemView.findViewById(R.id.result_label);
            this.result_value = (TextView) itemView.findViewById(R.id.result_value);



        }
    }
}
