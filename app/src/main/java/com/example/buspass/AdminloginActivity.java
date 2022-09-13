package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AdminloginActivity extends AppCompatActivity {


    EditText inputRegisterno, inputPasswordstd;
    Button btnLogin;
    String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String uid="PoSkbmIF0tgWuN4Wnxar5uYBy7z1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);


        inputRegisterno = findViewById(R.id.inputRegisterno);
        inputPasswordstd = findViewById(R.id.inputPasswordstd);
        btnLogin = findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



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
                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(Objects.equals(uid, user_id))
                        {
                            progressDialog.dismiss();
                            sendUserToNextActivity();
                            Toast.makeText(AdminloginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(AdminloginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(AdminloginActivity.this,AdminhomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
