package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final int CAMERA_REQ_CODE = 100;
    EditText inputRegisterno, inputPassword;
    private EditText inputName,inputDepart,inputPhone;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button btnRegister;
    String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //FOR CAMERA ROLL
        image=findViewById(R.id.choosePic);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQ_CODE);

            }
        });


        inputRegisterno=findViewById(R.id.inputRegisterno);
        inputPassword=findViewById(R.id.inputPassword);
        inputName=findViewById(R.id.inputName);
        inputDepart=findViewById(R.id.inputDepart);
        inputPhone=findViewById(R.id.inputPhone);
        btnRegister=findViewById(R.id.btnRegister);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("StudentInfo");


        //FOR ALREADY USER PAGE
        TextView btn=findViewById(R.id.alreadyUser);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        //FOR REGISTRATION BUTTON
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }
        });

    }
    private void PerforAuth() {

        String email=inputRegisterno.getText().toString();
        String password=inputPassword.getText().toString();
        String name=inputName.getText().toString();
        String depart=inputDepart.getText().toString();
        String phone=inputPhone.getText().toString();

        if(!email.matches(emailPattern))
        {
            inputRegisterno.setError("Enter Correct Email ");
            Toast.makeText(RegisterActivity.this,"Enter correct Email Address",Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty() || password.length()<6)
        {
            inputPassword.setError("Password Should be over length 6");
            Toast.makeText(RegisterActivity.this,"Password Should be over length 6",Toast.LENGTH_SHORT).show();
        }else if (name.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
        }else if (depart.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Enter Your Department",Toast.LENGTH_SHORT).show();
        }else if (phone.length() != 10){
            Toast.makeText(RegisterActivity.this,"Phone Number Should be 10 Digits",Toast.LENGTH_SHORT).show();
        }else
        {
            progressDialog.setMessage("Please wait While Registration....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();

                        firebaseDatabase=FirebaseDatabase.getInstance();
                        databaseReference=firebaseDatabase.getReference("users");
                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email=inputRegisterno.getText().toString();
                        String password=inputPassword.getText().toString();
                        String name=inputName.getText().toString();
                        String depart=inputDepart.getText().toString();
                        String phone=inputPhone.getText().toString();
                        UserHelperClass helperClass=new UserHelperClass(name,email,depart,phone,password);

                        databaseReference.child(user_id).setValue(helperClass);

                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //FOR CAMERA ROLL PART2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==CAMERA_REQ_CODE){

                Bitmap img= (Bitmap) (data.getExtras().get("data"));
                image.setImageBitmap(img);
            }
        }
    }
}




