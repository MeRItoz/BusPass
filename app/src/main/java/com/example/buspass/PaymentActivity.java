package com.example.buspass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    String[] items={"Thalassery","Kuthuparamba","Kannur","Mambram","Peralassery","Kaadachira","Chonadam","Fifth mail","Nayanar Road","Thazhe chovva","Mattanur","Uruvachal","Nirmalagiri"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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


}