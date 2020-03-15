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
import com.example.admin.occupancychart.R;

import java.util.ArrayList;

public class BookRoomAdapter extends RecyclerView.Adapter <BookRoomAdapter.BookRoomHolder>{
    private Context context;
    ArrayList<Period> periods;
     ArrayList<Integer> periodNumbers;
     String room;
     String day ;


    public BookRoomAdapter(Context context, ArrayList<Period> periods, ArrayList<Integer> periodNumbers, String roomselection, String s) {
        this.context = context;
        this.periods = periods;
        this.periodNumbers=periodNumbers;
        room = roomselection;
        day = s;
    }

    public BookRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.classlist, parent, false);
        return new BookRoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRoomHolder holder, int position) {
        Period period = periods.get(position);
        holder.setdetails(period,context,position,periodNumbers,room,day);
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
    public static class BookRoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title,roomno,time;
        private String room,day;
        private ArrayList<Integer> p;
        public BookRoomHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.CourseTitleText);
            p= new ArrayList<>();
            roomno = itemView.findViewById(R.id.RoomNumText);
            time = itemView.findViewById(R.id.ClassTimeText);
            title.setOnClickListener(this);
            time.setOnClickListener(this);
            roomno.setOnClickListener(this);
        }

        public void setdetails(Period period, Context context, int pos, ArrayList<Integer> periodNumbers, String room, String day) {
            title.setText(period.getTitle());
            roomno.setText(period.getRoom());
            time.setText(period.getTime());
            p = periodNumbers;
            this.room = room;
            this.day = day;

        }

        @Override
        public void onClick(View v) {
            Intent  i = new Intent(v.getContext(), BookConfirm.class);
            i.putExtra("Room",room);
            i.putExtra("Day",day);
            i.putExtra("Period",p.get(getAdapterPosition()));
            v.getContext().startActivity(i);
        }
    }
}