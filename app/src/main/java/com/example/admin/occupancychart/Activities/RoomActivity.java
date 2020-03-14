package com.example.admin.occupancychart.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.occupancychart.Models.Constants;
import com.example.admin.occupancychart.Models.MySingleton;
import com.example.admin.occupancychart.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    Spinner RoomSpinner,ChoiceSpinner,DaySpinner;
    private String roomselection;
    private ProgressDialog dialog;
    ArrayList<Entry> values = new ArrayList<>();
    private LineChart chart;
    private ListView listView;
    int currentHourIn24Format;
    String room[]={"None","A203","A204","A303","A304","C203","C204"};
    String choice[]={"None","DayWise","Weekly"};
    String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        listView = findViewById(R.id.ExtraClassesList);
        dialog= new ProgressDialog(RoomActivity.this);
        Calendar rightNow = Calendar.getInstance();
        currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        System.out.println("Current hour is " + currentHourIn24Format);
        RoomSpinner=findViewById(R.id.s1);
        ChoiceSpinner=findViewById(R.id.s2);
        DaySpinner=findViewById(R.id.s3);

        ChoiceSpinner.setVisibility(View.INVISIBLE);
        DaySpinner.setVisibility(View.INVISIBLE);
        dialog.setMessage("Please wait");
        dialog.show();
        getExtra();
        RoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    if(currentHourIn24Format>16) {
                        dialog.setMessage("Please wait");
                        dialog.show();
                        clearTemp();
                    }
                    ChoiceSpinner.setVisibility(View.VISIBLE);
                    roomselection = room[i];
                    System.out.println("room is " + roomselection);

                }
                else
                {
                    ChoiceSpinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2)
                {
                    DaySpinner.setVisibility(View.INVISIBLE);
                    dialog.setMessage("Getting data, please wait.");
                    dialog.show();
                    getData();
                    chart.notifyDataSetChanged();
                    chart.setVisibility(View.VISIBLE);

                }
                if(i==1)
                {

                    DaySpinner.setVisibility(View.VISIBLE);
                    dialog.setMessage("Getting data, please wait.");
                    dialog.show();
                    getDailyData();
                    chart.notifyDataSetChanged();
                    chart.setVisibility(View.VISIBLE);

                }
                if(i==0)
                {
                    chart.refreshDrawableState();
                    chart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter sRoomSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,room);
        sRoomSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoomSpinner.setAdapter(sRoomSpinner);

        ArrayAdapter sChoiceSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,choice);
        sChoiceSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ChoiceSpinner.setAdapter(sChoiceSpinner);

        ArrayAdapter dayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DaySpinner.setAdapter(dayAdapter);

        setTitle("LineChartActivity1");

        {
            chart = findViewById(R.id.chart1);
            chart.setVisibility(View.GONE);
            chart.setBackgroundColor(Color.WHITE);
            chart.getDescription().setEnabled(false);
            chart.setTouchEnabled(true);
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);
            MyMarkerView mv = new MyMarkerView(this, R.layout.activity_my_marker_view);
            mv.setChartView(chart);
            chart.setMarker(mv);
            chart.setDragEnabled(true);

            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {
            xAxis = chart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {
            yAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);
            yAxis.enableGridDashedLine(10f, 10f, 0f);
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisMinimum(0f);
        }


        chart.animateX(1500);

        Legend l = chart.getLegend();

        l.setForm(Legend.LegendForm.LINE);



    }

    private void setData() {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            Log.d("RR","1");
        } else {
            set1 = new LineDataSet(values, "Number of Periods in a Particular day");

            set1.setDrawIcons(false);

            set1.enableDashedLine(10f, 5f, 0f);

            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);


            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            set1.setDrawCircleHole(false);

            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }


    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // Log.i("Entry selected", e.toString());
        //Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        //Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {

    }


    private void getData() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.ROOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Get data Response is : " + response.toString());
                String[] rep= response.split(";");
                for (int i = 0; i < rep.length; i++) {
                    String[] temp = rep[i].split(",");
                    values.add(new Entry(Integer.parseInt(temp[0]),Integer.parseInt(temp[1])));
                }
                setData();

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
                params.put(Constants.KEY_ROOM,roomselection);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void getDailyData() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.DAILYDATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                 System.out.println("get daily data Response is : " + response.toString());
                String[] rep= response.split(";");
                for (int i = 0; i < rep.length; i++) {
                    String temp[]=rep[i].split(",");
                    values.add(new Entry(Integer.parseInt(temp[0]),Integer.parseInt(temp[1])));
                }
                setData();

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
                params.put(Constants.KEY_ROOM,roomselection);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void clearTemp() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.CLEARTEMP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();



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
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
    private void getExtra() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.EXTRACLASSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                System.out.println("Response is "+response);
                String[] rep = response.split(":");

                List<String> list = new ArrayList<String>(Arrays.asList(rep));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        list );

                listView.setAdapter(arrayAdapter);

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
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }


    public String getExtracpy() {

        final String[] rep1 = new String[1];
        StringRequest request = new StringRequest(Request.Method.POST, Constants.EXTRACLASSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                String[] rep = response.split(":");
                rep1[0] = response;
                List<String> list = new ArrayList<String>(Arrays.asList(rep));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        list );

                listView.setAdapter(arrayAdapter);

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
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
return rep1[0];
    }
}