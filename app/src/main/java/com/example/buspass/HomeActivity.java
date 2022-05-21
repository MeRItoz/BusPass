package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {


    TextView user_email,user_name,user_dep,user_phone;
    Button btnRenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user_name=findViewById(R.id.user_name);
        user_email=findViewById(R.id.user_email);
        user_dep=findViewById(R.id.user_dep);
        user_phone=findViewById(R.id.user_phone);
        btnRenew=findViewById(R.id.btnRenew);

        showAllData();

        btnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
            }
        });

    }

    private void showAllData() {
        Intent intent=getIntent();
        String user_useremail=intent.getStringExtra("email");
        String user_username=intent.getStringExtra("name");
        String user_userdepartment=intent.getStringExtra("department");
        String user_userphone=intent.getStringExtra("phone");

        user_email.setText(user_useremail);
        user_phone.setText(user_userphone);
        user_dep.setText(user_userdepartment);
        user_name.setText(user_username);
    }
}
