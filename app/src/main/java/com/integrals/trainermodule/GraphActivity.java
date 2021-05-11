package com.integrals.trainermodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collection;

public class GraphActivity extends AppCompatActivity {
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    private ArrayList<String> arrayList=new ArrayList<>();
    private String xAxis;
    private String yAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graphView=findViewById(R.id.graph);
        arrayList=getIntent().getStringArrayListExtra("data");
        xAxis=getIntent().getStringExtra("xAxis");
        yAxis=getIntent().getStringExtra("yAxis");
    }

    @Override
    protected void onStart(){
        super.onStart();
        DataPoint dataPoint=null;
        series  = new LineGraphSeries<>();
        for(int i=0;i<arrayList.size();i++){
            try {
                 dataPoint=new DataPoint(i+1,Integer.parseInt(arrayList.get(i)));
            }catch (NumberFormatException e){
                dataPoint=new DataPoint(i+1,Integer.parseInt(String.valueOf(arrayList.get(i).charAt(0))));
            }
            Log.d("DEMO","" +arrayList.get(i));
            series.appendData(dataPoint,true, 100);
        }
        graphView.setCursorMode(true);
        graphView.getGridLabelRenderer().setVerticalAxisTitle(yAxis);
        graphView.getGridLabelRenderer().setHorizontalAxisTitle(xAxis);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMinY(0);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        graphView.addSeries(series);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);

    }
}