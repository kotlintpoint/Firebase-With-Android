package com.example.firebase_sample.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.firebase_sample.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ImageListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        pd=ProgressDialog.show(this,"Wait","Fetching Data");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("images");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.dismiss();
                GenericTypeIndicator<Map<String,String>> type = new GenericTypeIndicator<Map<String,String>>() {};
                Map<String,String> valueList = dataSnapshot.getValue(type);
                Log.i("DATA", valueList.toString());
                Toast.makeText(ImageListActivity.this, valueList.toString(), Toast.LENGTH_SHORT).show();
                setAdapter(valueList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(ImageListActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(Map<String, String> valueList) {
        ArrayList<String> values = new ArrayList<>(valueList.values());
        MyImageAdapter adapter=new MyImageAdapter(values);
        recyclerView.setAdapter(adapter);
    }
}
