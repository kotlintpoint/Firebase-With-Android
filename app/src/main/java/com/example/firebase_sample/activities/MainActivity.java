package com.example.firebase_sample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase_sample.model.User;
import com.example.firebase_sample.other.Constants;
import com.example.firebase_sample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // https://github.com/firebase/snippets-android/tree/90e205c8b1a72c862095ad04f028afd3bfe00d15
    private static final String TAG = "MainActivity";
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){


            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void onAuthSuccess(final FirebaseUser currentUser){

        String email=etEmail.getText().toString();
        String userName=email.split("@")[0];
        User user=new User(userName, email);
        DatabaseReference reference=database.getReference()
                .child(Constants.USERS).child(currentUser.getUid());
        reference.setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("MESSAGE","Success");
                        updateUI(currentUser);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        signIn();
    }

    public void signIn(){
        pd=ProgressDialog.show(this,"Wait","Authenticating!!!");
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            onAuthSuccess(user);

                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(MainActivity.this, "Try Again!!!", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            signUp();
                        }
                    }
                });
    }

    public void signUp(){
        pd=ProgressDialog.show(this,"Wait","Registrering...");
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            onAuthSuccess(user);
                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Try Again!!!", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void signInWithEmail(){

    }
}
