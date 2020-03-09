package com.example.firebase_sample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firebase_sample.other.Constants;
import com.example.firebase_sample.R;
import com.example.firebase_sample.model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etFirstName, etLastName, etMobile;
    private Button btnSave;
    private List<Person> people;
    private FirebaseDatabase database;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        listView=findViewById(R.id.listView);
        database=FirebaseDatabase.getInstance();

        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etMobile=findViewById(R.id.etMobile);
        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        readPeople();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                people.remove(position);
                writeRemainingPersons();
            }
        });
    }

    private void writeRemainingPersons() {
        DatabaseReference reference=database.getReference(Constants.PEOPLE);
        reference.setValue(people)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PersonActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readPeople() {
        DatabaseReference reference=database.getReference(Constants.PEOPLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               try {
                   GenericTypeIndicator<List<Person>> type = new GenericTypeIndicator<List<Person>>() {};
                   people = dataSnapshot.getValue(type);
                   if(people==null)
                       people = new ArrayList<>();
                   setAdapter();
               }catch (Exception ex){
                   Log.i("ERROR", ex.toString());
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PersonActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        ArrayAdapter<Person> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,people);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Person person=new Person();
        person.setFirstName(etFirstName.getText().toString());
        person.setLastName(etLastName.getText().toString());
        person.setMobile(etMobile.getText().toString());
        people.add(person);
        DatabaseReference reference=database.getReference(Constants.PEOPLE);
        reference.setValue(people)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PersonActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
