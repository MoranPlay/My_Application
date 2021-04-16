package com.example.my_application;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static com.example.my_application.MainActivity.data;


public class BaseActivity extends AppCompatActivity {


    public void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public int get_city(Cursor cursor) {
        int city_id = 0;
        if (cursor.moveToFirst()) {
            do {
                city_id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return city_id;
    }

    public Cursor get_cursor() {
        Cursor cursor;
        cursor = database.rawQuery("SELECT city_id FROM cities WHERE city = '" + citySpinner.getSelectedItem().toString() + "';", null);
        return cursor;
    }

    public Cursor get_cursor_coef() {
        Cursor cursor;
        cursor = database.rawQuery("SELECT * FROM coefficient;", null);
        return cursor;
    }


    @SuppressLint("DefaultLocale")
    public void Select() {
        query_spin1 = database.rawQuery("SELECT * FROM data_city WHERE data_city_id=" + get_city(get_cursor()) + " AND year=" + Integer.parseInt(spinner_year.getSelectedItem().toString()) + ";", null);
        if (query_spin1.getCount() != 0) {
            if (query_spin1.moveToFirst()) {
                do {
                    mPetrol.setText(String.format("%.4f", query_spin1.getDouble(3)));
                    mDiesel.setText(String.format("%.4f", query_spin1.getDouble(4)));
                    mTreated.setText(String.format("%.4f", query_spin1.getDouble(5)));
                    mUntreated.setText(String.format("%.4f", query_spin1.getDouble(6)));
                    mActivated.setText(String.format("%.4f", query_spin1.getDouble(7)));
                } while (query_spin1.moveToNext());

            }
        } else {
            mPetrol.setText(null);
            mDiesel.setText(null);
            mTreated.setText(null);
            mUntreated.setText(null);
            mActivated.setText(null);
        }
    }

    private EditText mEnterCity;
    private EditText mPetrol;
    private EditText mDiesel;
    private EditText mTreated;
    private EditText mUntreated;
    private EditText mActivated;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter_city;
    ArrayAdapter<String> adapter_year;
    SQLiteDatabase database;
    Spinner spinner;
    Spinner citySpinner;
    Spinner spinner_year;
    static String[] city_mass;
    static String[] date_mas;
    Cursor query1;
    Cursor query_spin1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mEnterCity = findViewById(R.id.enter_city);
        mPetrol = findViewById(R.id.petrol_edit);
        mDiesel = findViewById(R.id.diesel_edit);
        mTreated = findViewById(R.id.treated_wastewater_edit);
        mUntreated = findViewById(R.id.untreated_waste_water_edit);
        mActivated = findViewById(R.id.activated_sludge_edit);

        dbHelper = new DBHelper(this);
        date_mas = new String[Calendar.getInstance().get(Calendar.YEAR) - 1999];
        int y = 2000;
        for (int i = 0; i < date_mas.length; i++) {
            date_mas[i] = Integer.toString(y++);
        }

        adapter_year = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, date_mas);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year = findViewById(R.id.spinner_year);
        spinner_year.setAdapter(adapter_year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // query_spin=get_cursor();
                if (get_cursor().getCount() != 0) {
                    Select();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(BaseActivity.this, BaseActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(BaseActivity.this, TableOfResults.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(BaseActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(BaseActivity.this, EmissionFactorActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        database = dbHelper.getWritableDatabase();
        query1 = database.rawQuery("SELECT * FROM cities;", null);
        if (query1.getCount() == 0) {
            city_mass = new String[1];
            city_mass[0] = "Міста відсутні";
        } else if (query1.getCount() != 0) {
            city_mass = new String[query1.getCount()];
            if (query1.moveToFirst()) {
                int i = 0;
                do {
                    city_mass[i++] = (query1.getString(1));
                }
                while (query1.moveToNext());
            }
        }
        adapter_city = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, city_mass);
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner = findViewById(R.id.spinner_city);
        citySpinner.setAdapter(adapter_city);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (get_cursor().getCount() != 0) {
                    Select();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        query1.close();
    }

    @SuppressLint({"NonConstantResourceId", "Recycle"})
    public void onClick(View view) {
        double petrol_dd = 0, petrol_dph, diesel_dd = 0, diesel_dph, treated_dd = 0, treated_dph, untreated_dd = 0, untreated_dph, activated_dd = 0, activated_dph, dph = 0.1;
        database = dbHelper.getWritableDatabase();

        Cursor cursor;
        cursor = get_cursor_coef();

        ContentValues contentValues = new ContentValues();
        switch (view.getId()) {
            case R.id.ad_city_button:
                contentValues.put(DBHelper.NAME, mEnterCity.getText().toString());
                database.insert(DBHelper.TABLE_CITY, null, contentValues);
                restart();
                break;

            case R.id.del_city_button:
                database.execSQL("DELETE FROM cities WHERE city_id=" + get_city(get_cursor()));
                restart();
                break;


            case R.id.add_base_button:

                if (cursor.moveToFirst()) {
                    do {
                        petrol_dd = cursor.getDouble(1) * Double.parseDouble(mPetrol.getText().toString());
                        diesel_dd = cursor.getDouble(2) * Double.parseDouble(mDiesel.getText().toString());
                        treated_dd = cursor.getDouble(3) * Double.parseDouble(mTreated.getText().toString());
                        untreated_dd = cursor.getDouble(4) * Double.parseDouble(mUntreated.getText().toString());
                        activated_dd = cursor.getDouble(5) * Double.parseDouble(mActivated.getText().toString());
                    }
                    while (cursor.moveToNext());
                }
                petrol_dph = petrol_dd * dph;
                diesel_dph = diesel_dd * dph;
                treated_dph = treated_dd * dph;
                untreated_dph = untreated_dd * dph;
                activated_dph = activated_dd * dph;

                contentValues.put(DBHelper.CITY_ID, get_city(get_cursor()));
                contentValues.put(DBHelper.YEAR, Integer.parseInt(spinner_year.getSelectedItem().toString()));

                contentValues.put(DBHelper.PETROL, Double.valueOf(mPetrol.getText().toString()));
                contentValues.put(DBHelper.PETROL_DD, petrol_dd);
                contentValues.put(DBHelper.PETROL_DPH, petrol_dph);

                contentValues.put(DBHelper.DIESEL, Double.valueOf(mDiesel.getText().toString()));
                contentValues.put(DBHelper.DIESEL_DD, diesel_dd);
                contentValues.put(DBHelper.DIESEL_DPH, diesel_dph);

                contentValues.put(DBHelper.TREATED_WASTEWATER, Double.valueOf(mTreated.getText().toString()));
                contentValues.put(DBHelper.TREATED_WASTEWATER_DD, treated_dd);
                contentValues.put(DBHelper.TREATED_WASTEWATER_DPH, treated_dph);

                contentValues.put(DBHelper.UNTREATED_WASTEWATER, Double.valueOf(mUntreated.getText().toString()));
                contentValues.put(DBHelper.UNTREATED_WASTEWATER_DD, untreated_dd);
                contentValues.put(DBHelper.UNTREATED_WASTEWATER_DPH, untreated_dph);

                contentValues.put(DBHelper.ACTIVATED_SLUDGE, Double.valueOf(mActivated.getText().toString()));
                contentValues.put(DBHelper.ACTIVATED_SLUDGE_DD, activated_dd);
                contentValues.put(DBHelper.ACTIVATED_SLUDGE_DPH, activated_dph);

                database.insert(DBHelper.TABLE_DATA, null, contentValues);
                restart();
                break;

            case R.id.ref_base_button:
                cursor = database.rawQuery("SELECT * FROM coefficient;", null);
                if (cursor.moveToFirst()) {
                    do {
                        petrol_dd = cursor.getDouble(1) * Double.parseDouble(mPetrol.getText().toString());
                        diesel_dd = cursor.getDouble(2) * Double.parseDouble(mDiesel.getText().toString());
                        treated_dd = cursor.getDouble(3) * Double.parseDouble(mTreated.getText().toString());
                        untreated_dd = cursor.getDouble(4) * Double.parseDouble(mUntreated.getText().toString());
                        activated_dd = cursor.getDouble(5) * Double.parseDouble(mActivated.getText().toString());
                    }
                    while (cursor.moveToNext());
                }
                petrol_dph = petrol_dd * dph;
                diesel_dph = diesel_dd * dph;
                treated_dph = treated_dd * dph;
                untreated_dph = untreated_dd * dph;
                activated_dph = activated_dd * dph;
                database.execSQL("UPDATE data_city SET petrol =" + Double.valueOf(mPetrol.getText().toString())
                        + ", diesel=" + Double.valueOf(mDiesel.getText().toString())
                        + ", treated_waste_water=" + Double.valueOf(mTreated.getText().toString())
                        + ", untreated_waste_water=" + Double.valueOf(mUntreated.getText().toString())
                        + ", activated_sludge=" + Double.valueOf(mActivated.getText().toString())
                        + ", petrol_dd=" + petrol_dd
                        + ", petrol_dph=" + petrol_dph
                        + ", diesel_dd=" + diesel_dd
                        + ", diesel_dph=" + diesel_dph
                        + ", treated_waste_water_dd=" + treated_dd
                        + ", treated_waste_water_dph=" + treated_dph
                        + ", untreated_waste_water_dd=" + untreated_dd
                        + ", untreated_waste_water_dph=" + untreated_dph
                        + ", activated_sludge_dd=" + activated_dd
                        + ", activated_sludge_dph=" + activated_dph
                        + " WHERE data_city_id=" + get_city(get_cursor()) + " AND year=" + Integer.parseInt(spinner_year.getSelectedItem().toString()));
                restart();
                break;
            case R.id.del_base_button:
                database.execSQL("DELETE FROM data_city WHERE data_city_id=" + get_city(get_cursor()) + " AND year=" + Integer.parseInt(spinner_year.getSelectedItem().toString()));
                restart();
                break;

            case R.id.null_button:
                mPetrol.setText(null);
                mDiesel.setText(null);
                mTreated.setText(null);
                mUntreated.setText(null);
                mActivated.setText(null);
                break;
        }

        dbHelper.close();

    }


}
