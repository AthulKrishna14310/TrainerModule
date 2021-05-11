package com.integrals.trainermodule;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Helper {
    private Context context;
    private Activity activity;
    private final String TAG="TRAINER_MAIN";
    private String result="";
    private ArrayList<DatabaseReference> databaseReferenceArrayList;
    private DatabaseReference databaseReference;
    private ListView list;
    private DatabaseReference temp;
    private String title=" Task : ";
    private String email;
    private String user;
    private MaterialButton graphButton;
    private ArrayList<String> graphValues;
    public String getUser() {
        return user;
    }
    private String xAxis;
    private String yAxis;

    public MaterialButton getGraphButton() {
        return graphButton;
    }

    public void setGraphButton(MaterialButton graphButton) {
        this.graphButton = graphButton;
    }

    public ArrayList<String> getGraphValues() {
        return graphValues;
    }

    public void setGraphValues(ArrayList<String> graphValues) {
        this.graphValues = graphValues;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<DatabaseReference> getDatabaseReferenceArrayList() {
        return databaseReferenceArrayList;
    }

    public void setDatabaseReferenceArrayList
            (ArrayList<DatabaseReference> databaseReferenceArrayList) {
        this.databaseReferenceArrayList = databaseReferenceArrayList;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getTAG() {
        return TAG;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Helper(Context context, Activity activity,
                  DatabaseReference databaseReference,
                  ListView list,MaterialButton graphButton) {
        this.context = context;
        this.activity = activity;
        this.databaseReference=databaseReference;
        this.list=list;
        databaseReferenceArrayList=new ArrayList<>();
        databaseReferenceArrayList.add(databaseReference);
        temp=databaseReference;
        this.graphButton=graphButton;

    }
    public void displayList(){
        if(databaseReferenceArrayList.size()==1){
           activity.findViewById(R.id.loadProgress).setVisibility(View.VISIBLE);
        }
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> keys=new ArrayList<>();
        ArrayList<String> imageUrls=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                activity,
                R.layout.list_item,
                arrayList);
        graphButton.setVisibility(View.INVISIBLE);

        databaseReferenceArrayList
                .get((databaseReferenceArrayList.size()-1))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        result="";
                        graphValues=new ArrayList<>();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String key = postSnapshot.getRef().getKey();
                            String value=postSnapshot.getValue().toString();
                            keys.add(key);
                            if(key.contains("email")){
                                email=value;
                            }else if(key.contains("name")){
                                user=value;
                            }

                            if(value.contains("{")){
                                arrayList.add(key);
                                result=result.concat("\n ").concat(key);
                            }else {
                                    result=result.concat("\n ").concat(key).concat(" : ").concat(value);
                                    if(value.contains("https://firebasestorage.googleapis.com")){
                                        arrayList.add(key + " : " + "Tap to view image");
                                        imageUrls.add(value);
                                    }else{
                                        arrayList.add(key + " : " + value);
                                    }
                                    if(value.contains("Score =")||value.contains("Error Produced ")
                                            ||value.contains("Accuracy value ")
                                            ||value.contains("Attempt-Failed")
                                            ||value.contains("Attempt-failed")
                                            ||value.contains("Attempt failed")
                                            ||value.contains("Incomplete Trace")){

                                        graphButton.setVisibility(View.VISIBLE);

                                        if(value.contains("Score = ")){
                                            graphValues.add(String.valueOf(value.subSequence(8,10)));
                                            yAxis="Score";
                                            xAxis="Attempts";
                                        }else if(value.contains("Error Produced ")){
                                            graphValues.add(String.valueOf(value.subSequence(15,17)));
                                            yAxis="Error Produced";
                                            xAxis="Attempts";

                                        }else if(value.contains("Accuracy value ")){
                                            graphValues.add(String.valueOf(value.subSequence(16,18)));
                                            yAxis="Accuracy Value";
                                            xAxis="Attempts";
                                        }else if(value.contains("Attempt-Failed")||
                                                value.contains("Attempt Failed")
                                                ||value.contains("Attempt-failed")
                                                ||value.contains("Attempt failed")||
                                                (value.contains("Incomplete Trace"))){
                                            graphValues.add(""+0);
                                        }

                                    }else{
                                        graphButton.setVisibility(View.INVISIBLE);
                                    }
                            }

                        }
                        if(databaseReferenceArrayList.size()==1) {
                            activity.findViewById(R.id.loadProgress).setVisibility(View.GONE);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "loadPost:onCancelled", databaseError.toException());

                    }
                });

        list.setOnItemClickListener((parent, view, position, id) -> {
            if(imageUrls.isEmpty()) {
                String clickedItem = keys.get(position);

                if(clickedItem.contains("@")){
                    //
                }else {
                    title = title.concat(clickedItem+" -> ");
                }
                temp = databaseReferenceArrayList.get(databaseReferenceArrayList.size() - 1).child(clickedItem);
                databaseReferenceArrayList.add(temp);
                Log.d(TAG, "Size = " + databaseReferenceArrayList.size() + "");
                displayList();
            }else{
                Log.d(TAG,imageUrls.get(position));
                 context.startActivity(new Intent(context,ImageView.class)
                .putExtra("url",imageUrls.get(position))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        list.setAdapter(arrayAdapter);
    }

    public DatabaseReference getTemp() {
        return temp;
    }

    public void setTemp(DatabaseReference temp) {
        this.temp = temp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected void sendEmail(String to, String message, String user) {
        String[] TO = {to};
        String[] CC = {""};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, user+"'s progress on Handex");
        intent.putExtra(Intent.EXTRA_TEXT,title+"\n"+message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }
}
