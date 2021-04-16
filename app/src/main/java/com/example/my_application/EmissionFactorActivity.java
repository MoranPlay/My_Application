package com.example.my_application;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.my_application.MainActivity.data;

public class EmissionFactorActivity extends AppCompatActivity {

    private EditText Petrol;
    private EditText Diesel;
    private EditText Treated;
    private EditText Untreated;
    private EditText Activated;
    private EditText Petrol1;
    private EditText Diesel1;
    private EditText Treated1;
    private EditText Untreated1;
    private EditText Activated1;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emission_factor);

        Petrol = findViewById(R.id.petrol_koph);
        Diesel = findViewById(R.id.diesel_koph);
        Treated = findViewById(R.id.treat_koph);
        Untreated = findViewById(R.id.untreat_koph);
        Activated = findViewById(R.id.activ_koph);
        Petrol1 = findViewById(R.id.petrol_koph1);
        Diesel1 = findViewById(R.id.diesel_koph1);
        Treated1 = findViewById(R.id.treat_koph1);
        Untreated1 = findViewById(R.id.untreat_koph1);
        Activated1 = findViewById(R.id.activ_koph1);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    Intent intent = new Intent(EmissionFactorActivity.this, BaseActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(EmissionFactorActivity.this, TableOfResults.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(EmissionFactorActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(EmissionFactorActivity.this, EmissionFactorActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ref_base_button_petrol:
                database.execSQL("UPDATE coefficient SET coef_pet =" + (Double.parseDouble(Petrol.getText().toString()) * Math.pow(10, Double.parseDouble(Petrol1.getText().toString()))));
                break;
            case R.id.ref_base_button_diesel:
                database.execSQL("UPDATE coefficient SET coef_dies =" + (Double.parseDouble(Diesel.getText().toString()) * Math.pow(10, Double.parseDouble(Diesel1.getText().toString()))));
                break;
            case R.id.ref_base_button_treat:
                database.execSQL("UPDATE coefficient SET coef_treat =" + (Double.parseDouble(Treated.getText().toString()) * Math.pow(10, Double.parseDouble(Treated1.getText().toString()))));
                break;
            case R.id.ref_base_button_untreat:
                database.execSQL("UPDATE coefficient SET coef_untreat =" + (Double.parseDouble(Untreated.getText().toString()) * Math.pow(10, Double.parseDouble(Untreated1.getText().toString()))));

                break;
            case R.id.ref_base_button_activ:
                database.execSQL("UPDATE coefficient SET coef_activ =" + (Double.parseDouble(Activated.getText().toString()) * Math.pow(10, Double.parseDouble(Activated1.getText().toString()))));
                break;
        }
    }

}
