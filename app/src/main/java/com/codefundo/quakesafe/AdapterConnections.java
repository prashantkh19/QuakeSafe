package com.codefundo.quakesafe;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterConnections extends RecyclerView.Adapter<AdapterConnections.ViewHolder> {

    private ArrayList<ItemConnection> arrayList;
    private Context context;

    private static final String TAG = "AdapterConnections";

    public AdapterConnections(Context context, ArrayList<ItemConnection> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_connection_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemConnection itemConnection = arrayList.get(position);

       if(itemConnection.status){
           holder.tv_status.setText("SAFE");
           holder.tv_status.setVisibility(View.VISIBLE);
       }else{
           holder.tv_status.setVisibility(View.GONE);
       }

        holder.tv_mobile.setText(itemConnection.getMob()+"");
        holder.tv_name.setText(itemConnection.getName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_mobile, tv_status;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_mobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }

}