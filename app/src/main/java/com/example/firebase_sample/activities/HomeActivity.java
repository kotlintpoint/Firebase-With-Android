package com.example.firebase_sample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase_sample.model.Product;
import com.example.firebase_sample.other.Constants;
import com.example.firebase_sample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignout, btnSave, btnSaveProduct;
    private TextView tvHome, tvMessage;
    private FirebaseAuth firebaseAuth;
    private EditText etMessage, etProductName;
    private FirebaseDatabase database;
    private ArrayList<String> values=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();
        btnSignout=findViewById(R.id.btnSignout);
        btnSaveProduct=findViewById(R.id.btnSaveProduct);

        btnSignout.setOnClickListener(this);

        tvHome=findViewById(R.id.tvHome);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        tvHome.setText("Display Name : "+currentUser.getDisplayName());

        etProductName=findViewById(R.id.etProductName);
        etMessage=findViewById(R.id.etMessage);
        btnSave=findViewById(R.id.btnSave);
        tvMessage=findViewById(R.id.tvMessage);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //writeToFirebase();
                //writeListToFirebase();
                removeFromFirebase();
            }
        });

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
        readFromFirebase();
        readListFromFirebase();
    }

    private void saveProduct() {
        String name=etProductName.getText().toString();
        String message=etMessage.getText().toString();
        Product product=new Product(name, message);

    }

    private void removeFromFirebase(){
        DatabaseReference reference = database.getReference("message");;
        reference.removeValue()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }     else{
                            Toast.makeText(HomeActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void readFromFirebase() {

        DatabaseReference reference = database.getReference("message");;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value=dataSnapshot.getValue(String.class);
                tvMessage.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void writeListToFirebase(){
        DatabaseReference reference = database.getReference("cities");
        String value=etMessage.getText().toString();
        values.add(value);
        reference.setValue(values)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }     else{
                            Toast.makeText(HomeActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void readListFromFirebase(){
        DatabaseReference reference = database.getReference(Constants.CITIES);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> type = new GenericTypeIndicator<List<String>>() {};

                List<String> valueList = dataSnapshot.getValue(type);
                Toast.makeText(HomeActivity.this, valueList.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void writeToFirebase() {

        DatabaseReference reference = database.getReference("message");;
        String message=etMessage.getText().toString();
        reference.setValue(message)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }     else{
                            Toast.makeText(HomeActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        firebaseAuth.signOut();
        finish();
    }
}
