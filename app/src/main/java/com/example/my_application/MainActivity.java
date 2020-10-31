package com.example.my_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    static String[] data = {"-Вибрати-", "База даних", "Таблиця результатів", "Графік", "Коефіцієнти емісії"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,BaseActivity.class);
                 startActivity(intent);
//        ArrayAdapter<String> adapter  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner spinner = findViewById(R.id.spinner);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                if(position==0){
////                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
////                    startActivity(intent);
////                }
//                if(position==1){
//                    Intent intent = new Intent(MainActivity.this,BaseActivity.class);
//                    startActivity(intent);
//
//                }
//                if(position==2){
//                    Intent intent = new Intent(MainActivity.this,TableOfResults.class);
//                    startActivity(intent);
//
//                }
//                if(position==3){
//                    Intent intent = new Intent(MainActivity.this,ScheduleActivity.class);
//                    startActivity(intent);
//
//                }
//                if(position==4){
//                    Intent intent = new Intent(MainActivity.this,EmissionFactorActivity.class);
//                    startActivity(intent);
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
}
