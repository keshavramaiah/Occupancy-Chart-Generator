package com.example.admin.occupancychart.Models;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.admin.occupancychart.R;

import java.util.ArrayList;

public class PeriodAdapter extends RecyclerView.Adapter <PeriodAdapter.PeriodHolder>{
    public Context context;
    public static ArrayList<Period> periods;


    public PeriodAdapter(Context context, ArrayList<Period> periods) {
        this.context = context;
        this.periods = periods;
    }

    public PeriodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.classlist, parent, false);
        return new PeriodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodHolder holder, int position) {
        Period period = periods.get(position);
        holder.setdetails(period,context);
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class PeriodHolder extends RecyclerView.ViewHolder{
        private TextView title,roomno,time;
        public PeriodHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.CourseTitleText);
            roomno = itemView.findViewById(R.id.RoomNumText);
            time = itemView.findViewById(R.id.ClassTimeText);
        }

        public void setdetails(Period period, Context context) {
            title.setText(period.getTitle());
            roomno.setText(period.getRoom());
            time.setText(period.getTime());

        }
    }
}