package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminnotifiActivity extends AppCompatActivity {

    private EditText inputNotification;
    private Button btnSubmit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AdminHelperClass adminHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminnotifi);

        inputNotification=findViewById(R.id.inputNotification);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Admin msg");

        adminHelperClass=new AdminHelperClass();

        btnSubmit=findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=inputNotification.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(AdminnotifiActivity.this, "add some data", Toast.LENGTH_SHORT).show();

                }else{
                    addDatatoFirebase(msg);
                }
            }
        });


    }

    private void addDatatoFirebase(String msg) {
        adminHelperClass.setMsg(msg);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(adminHelperClass);
                Toast.makeText(AdminnotifiActivity.this,"message sended",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminnotifiActivity.this,"error",Toast.LENGTH_SHORT).show();


            }
        });
    }


}