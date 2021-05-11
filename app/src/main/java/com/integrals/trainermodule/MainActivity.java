package com.integrals.trainermodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TRAINER_MAIN";
    private MaterialButton mailButton;
    private Helper helper;
    private ListView list;
    private ArrayList<String> arrayList;
    private DatabaseReference databaseReference;
    private ImageButton homeButton;
    private MaterialButton graphButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;
        getSupportActionBar().hide();
        list = findViewById(R.id.dataList);
        graphButton=findViewById(R.id.graphButton);
        mailButton=findViewById(R.id.mailButton);
        arrayList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        homeButton=findViewById(R.id.homeButton);
        helper=new Helper(getApplicationContext(),
                MainActivity.this,
                databaseReference,
                list,
                graphButton
                );

    }

    @Override
    protected void onStart() {
        super.onStart();
        mailButton.setOnClickListener(v -> {
                helper.sendEmail(helper.getEmail(),helper.getResult(),helper.getUser());
        });
        homeButton.setOnClickListener(v -> {
            helper.getDatabaseReferenceArrayList().clear();
            helper.getDatabaseReferenceArrayList().add(databaseReference);
            helper.displayList();
        });
        graphButton.setOnClickListener(v -> startActivity(new
                Intent(MainActivity.this, GraphActivity.class)
        .putStringArrayListExtra("data",helper.getGraphValues())
        .putExtra("xAxis",helper.getxAxis())
        .putExtra("yAxis",helper.getyAxis())));

        helper.displayList();
    }

    @Override
    public void onBackPressed() {
        if(helper.getDatabaseReferenceArrayList().size()==1){
            super.onBackPressed();

        } else if(helper.getDatabaseReferenceArrayList().size()==2){
            helper.setTitle(" Task : ");
            helper.getDatabaseReferenceArrayList()
                    .remove(helper.getDatabaseReferenceArrayList().size() - 1);
            helper.displayList();
        } else {
            helper.getDatabaseReferenceArrayList()
                    .remove(helper.getDatabaseReferenceArrayList().size() - 1);
            helper.displayList();
        }
    }
}