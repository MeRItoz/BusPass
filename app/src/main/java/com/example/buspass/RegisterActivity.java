package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    EditText inputRegisterno, inputPassword;
    private EditText inputName,inputDepart,inputPhone,inputAddress;
    private ImageView choosePic;
    private Uri filePath;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
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

        inputRegisterno=findViewById(R.id.inputRegisterno);
        inputPassword=findViewById(R.id.inputPassword);
        inputName=findViewById(R.id.inputName);
        inputDepart=findViewById(R.id.inputDepart);
        inputPhone=findViewById(R.id.inputPhone);
        inputAddress=findViewById(R.id.inputAddress);
        btnRegister=findViewById(R.id.btnRegister);
        choosePic = findViewById(R.id.choosePic);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("StudentInfo");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

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

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                choosePic.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    private void PerforAuth() {

        String email=inputRegisterno.getText().toString();
        String password=inputPassword.getText().toString();
        String name=inputName.getText().toString();
        String depart=inputDepart.getText().toString();
        String phone=inputPhone.getText().toString();
        String address=inputAddress.getText().toString();

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
                        uploadImage();
                        firebaseDatabase=FirebaseDatabase.getInstance();
                        databaseReference=firebaseDatabase.getReference("users");
                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email=inputRegisterno.getText().toString();
                        String password=inputPassword.getText().toString();
                        String name=inputName.getText().toString();
                        String depart=inputDepart.getText().toString();
                        String phone=inputPhone.getText().toString();
                        String address=inputAddress.getText().toString();
                        UserHelperClass helperClass=new UserHelperClass(name,email,depart,phone,password,address);

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

    private void uploadImage() {
        if (filePath != null) {
            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference ref= storageReference.child("images/" +user_id.toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(RegisterActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(RegisterActivity.this, "Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}




