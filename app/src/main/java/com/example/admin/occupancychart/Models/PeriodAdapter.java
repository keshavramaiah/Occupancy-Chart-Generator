package com.example.admin.occupancychart.Models;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.admin.occupancychart.Activities.BookConfirm;
import com.example.admin.occupancychart.Activities.CancelClass;
import com.example.admin.occupancychart.R;

import java.util.ArrayList;

public class PeriodAdapter extends RecyclerView.Adapter <PeriodAdapter.PeriodHolder>{
    public Context context;
    public static ArrayList<Period> periods;
    boolean click = false;


    public PeriodAdapter(Context context, ArrayList<Period> periods,boolean click) {
        this.context = context;
        this.periods = periods;
        this.click = click;
    }

    public PeriodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.classlist, parent, false);
        return new PeriodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodHolder holder, int position) {
        Period period = periods.get(position);
        holder.setdetails(period,context,click,periods);
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class PeriodHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title,roomno,time;
        private ArrayList <Period> periods;
        public PeriodHolder(@NonNull View itemView) {
            super(itemView);
            periods = new ArrayList<>();
            title=(TextView)itemView.findViewById(R.id.CourseTitleText);
            roomno = itemView.findViewById(R.id.RoomNumText);
            time = itemView.findViewById(R.id.ClassTimeText);
        }

        public void setdetails(Period period, Context context, boolean click, ArrayList<Period> periods) {
            this.periods = periods;
            title.setText(period.getTitle());
            roomno.setText(period.getRoom());
            time.setText(period.getTime());
            if(click)
            {
                title.setOnClickListener(this);
                roomno.setOnClickListener(this);
                time.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            Intent  i = new Intent(v.getContext(), CancelClass.class);
            i.putExtra("Period",periods.get(getAdapterPosition()).getTime());
            i.putExtra("Room",periods.get(getAdapterPosition()).getRoom());
            v.getContext().startActivity(i);
        }
    }
}