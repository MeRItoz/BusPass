package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity<onSuccessListener> extends AppCompatActivity {


    TextView user_email,user_name,user_dep,user_phone;
    ImageView user_pic;
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
        GetImage();

        btnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
            }
        });

    }

    private void GetImage() {
        user_pic=findViewById(R.id.user_pic);
        int width=500;
        int height=500;

        ConstraintLayout.LayoutParams parms=new ConstraintLayout.LayoutParams(width,height);
        user_pic.setLayoutParams(parms);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference mImageRef =FirebaseStorage.getInstance().getReference("images/" +user_id);
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                user_pic.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
