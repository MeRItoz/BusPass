package com.example.buspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {


    private AutoCompleteTextView dropMenu;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserlocationHelper userlocationHelper;
    String[] items={"Thalassery","Kuthuparamba","Kannur","Mambram","Peralassery","Kaadachira","Chonadam","Fifth mail","Nayanar Road","Thazhe chovva","Mattanur","Uruvachal","Nirmalagiri"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dropMenu=findViewById(R.id.dropMenu);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("user location");
        userlocationHelper=new UserlocationHelper();

        btnFinish=findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location= dropMenu.getText().toString();
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(PaymentActivity.this, "select data", Toast.LENGTH_SHORT).show();

                }else{
                    addDatatoFirebase(location);
                }
                startActivity(new Intent(PaymentActivity.this, LoginActivity.class));
            }
        });


        autoCompleteTextView=findViewById(R.id.dropMenu);
        adapterItems=new ArrayAdapter<String>(this,R.layout.dropdown_item,items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),"location:"+item,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDatatoFirebase(String location) {
        userlocationHelper.setLocation(location);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(userlocationHelper);
                Toast.makeText(PaymentActivity.this,"message sended",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this,"error",Toast.LENGTH_SHORT).show();


            }
        });
    }


}