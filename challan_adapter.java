package com.krishbarcode.firebase_realtime;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by kritesh on 28/3/18.
 */

public class challan_adapter extends RecyclerView.Adapter<challan_adapter.MyViewHolder>
{

    private ArrayList<Challan> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,duedate,location,time;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.duedate = itemView.findViewById(R.id.duedate );
            this.location = itemView.findViewById(R.id.location);
            this.time =  itemView.findViewById(R.id.time);
            this.imageViewIcon = itemView.findViewById(R.id.imageView);
        }
    }
    public challan_adapter(ArrayList<Challan> data) {
        this.dataSet = data;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder; }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {

        TextView datename = holder.date;
        TextView duedatename = holder.duedate;
        TextView loacationname = holder.location;
        TextView timename = holder.time;
        ImageView imageView = holder.imageViewIcon;

        Log.d("card",dataSet.get(listPosition).getDate()+"  "+dataSet.get(listPosition).getLoc()+"  "+dataSet.get(listPosition).getTime()+"   "+dataSet.get(listPosition).isIs_paid());
        datename.setText(dataSet.get(listPosition).getDate());
        loacationname.setText(dataSet.get(listPosition).getLoc());
        timename.setText(dataSet.get(listPosition).getTime());
        duedatename.setText("6/4/18");
        if(dataSet.get(listPosition).isIs_paid())
            imageView.setImageResource(R.mipmap.paid_banner_round);
        else
            imageView.setImageResource(R.mipmap.unpaid_banner_round);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public String duedate(String date)
    {
        int incr_due= 7 ,int_day;
        String day = date.substring(0,2);
        Log.d("day",day);
        String month = date.substring(3,5);

        String year = date.substring(6,8);

        int_day=Integer.valueOf(day);
        int_day = int_day + incr_due;
        return date;


    }


}