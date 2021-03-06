package com.example.admin.occupancychart.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.occupancychart.Models.Constants;
import com.example.admin.occupancychart.Models.MySingleton;
import com.example.admin.occupancychart.Models.Period;
import com.example.admin.occupancychart.Models.PeriodAdapter;
import com.example.admin.occupancychart.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private PieChart chart;
    private SharedPreferences pref ;
    private ArrayList<Period>listOfPeriods;
    private ProgressDialog dialog;
    private String name;
    private FloatingActionButton floatingActionButton;
    private PeriodAdapter periodAdapter;
    private RecyclerView recyclerView;
    private String[] times = new String[]{"0","8:40am-9:30am","9:30am-10:20am","10:20am-11:10am","11:20am-12:10pm","12:10pm-1:00pm","2:00pm-2:50pm","2:50pm-3:40pm","3:40pm-4:30pm","4:30pm-5:30pm",};
    String[] rep;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        dialog= new ProgressDialog(MainActivity.this);
        day =  5; // calendar.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println("Day is " + day);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.TeacherBookRoomBtn);
        setTitle("PieChartActivity");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        name=pref.getString("Name",null);
        recyclerView = findViewById(R.id.TeacherRecycler);
        if(name==null)
        {
            Toast.makeText(getApplicationContext(),"Registration error,please register again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Register.class));
        }
        else
            getData();
        Toast.makeText(getApplicationContext(),"Welcome back "+name,Toast.LENGTH_SHORT).show();

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1400, Easing.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BookRoom.class));
            }
        });

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Work Load\n For today");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 9, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 9, s.length() - 10, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 9, s.length() - 10, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 9, s.length() - 10, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 10, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 10, s.length(), 0);
        return s;
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Log.d("CHECK",rep.length+" ");
        for(int i=0;i<listOfPeriods.size();i++)
        {
            entries.add(new PieEntry(Integer.valueOf(1),listOfPeriods.get(i).getTitle()));
        }
        entries.add(new PieEntry((9-Integer.valueOf(listOfPeriods.size())),"Free Hour"));


        PieDataSet dataSet = new PieDataSet(entries, "Periods");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();



        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();

    }

    private void getData() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.TEACHERDATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Teacher Response is : " + response);
                listOfPeriods = new ArrayList<>();
                if(response.length()>5) {
                    rep = response.split("@");
                    String[] classrooms = new String[rep.length];
                    for (int i = 0; i < rep.length; i++) {
                        if (rep[i].length() >= 5) {
                            System.out.println(rep[i].substring(0,4));
                            classrooms[i] = rep[i].substring(0, 4);
                            rep[i] = rep[i].substring(5, rep[i].length() - 1);
                            System.out.println(rep[i]);
                            String[] temp = rep[i].split(";");
                            String period = temp[0];
                            String c = temp[1];
                            period = times[Integer.valueOf(period)];
                            System.out.println("Classroom is " +classrooms[i]);
                            System.out.println("Period is " + period);
                            System.out.println("C is " +c);
                            listOfPeriods.add(new Period(classrooms[i], period, c));
                        }
                    }
                    dispadapter(listOfPeriods);
                   setData();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No classes today ",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_DAY,String.valueOf(day));
                params.put(Constants.KEY_NAME,name);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
    private void dispadapter(ArrayList<Period> listperiods) {
        periodAdapter = new PeriodAdapter(getApplicationContext(),listperiods,true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(periodAdapter);
        periodAdapter.notifyDataSetChanged();
    }
}