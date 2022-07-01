package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {


    EditText inputRegisterno, inputPasswordstd;
    Button btnLogin;
    String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        inputRegisterno = findViewById(R.id.inputRegisterno);
        inputPasswordstd = findViewById(R.id.inputPasswordstd);
        btnLogin = findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        TextView btn = findViewById(R.id.signUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        TextView btn2 = findViewById(R.id.forgotPassword);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotpasswordActivity.class));
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforLogin();
            }
        });
    }

    private void perforLogin() {

        String email = inputRegisterno.getText().toString();
        String password = inputPasswordstd.getText().toString();

        if (!email.matches(emailPattern)) {
            inputRegisterno.setError("Enter Correct Email ");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPasswordstd.setError("Enter correct password over length 6");
        } else
        {

            progressDialog.setMessage("Please wait While Login....");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        isUser();
                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void isUser() {
        String userEnteredUsername= inputRegisterno.getText().toString().trim();
        String userEnteredPassword= inputPasswordstd.getText().toString().trim();
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUser=reference.orderByChild("email").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String userFromDB=snapshot.child(user_id).child("email").getValue(String.class);
                    if(userFromDB.equals(userEnteredUsername)){
                        String departmentFromDB=snapshot.child(user_id).child("department").getValue(String.class);
                        String emailFromDB=snapshot.child(user_id).child("email").getValue(String.class);
                        String nameFromDB=snapshot.child(user_id).child("name").getValue(String.class);
                        String phoneFromDB=snapshot.child(user_id).child("phone").getValue(String.class);
                        String addressFromDB=snapshot.child(user_id).child("address").getValue(String.class);

                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        intent.putExtra("department",departmentFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("phone",phoneFromDB);
                        intent.putExtra("address",addressFromDB);

                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}