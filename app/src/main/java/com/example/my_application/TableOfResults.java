package com.example.my_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static com.example.my_application.MainActivity.data;

public class TableOfResults extends AppCompatActivity {

    public int get_city(Cursor cursor){
        int city_id=0;
        if(cursor.moveToFirst()){
            do{
                city_id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return city_id;
    }

    public Cursor get_cursor(){
        Cursor cursor;
        cursor = database.rawQuery("SELECT city_id FROM cities WHERE city = '" + citySpinner.getSelectedItem().toString() + "';", null);
        return cursor;
    }

    public double[] result(double[] mas, Cursor query_table_data){
        for(int i=0; i<query_table_data.getCount(); i++) {
            double z=0;
            for(int y=0; y<=i;y++){
                z+=mas[i-y]*Math.exp((-1)*y/14.5);
            }
            mas[i]=z;
        }
        return mas;
    }

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter_city;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Spinner citySpinner;
    static String[] city_mass_select;
    Cursor query_select;
    Cursor query_table_data;
    TableLayout table_dataCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_of_results);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==1){
                    Intent intent = new Intent(TableOfResults.this,BaseActivity.class);
                    startActivity(intent);
                }
                if(position==2){
                    Intent intent = new Intent(TableOfResults.this,TableOfResults.class);
                    startActivity(intent);
                }
                if(position==3){
                    Intent intent = new Intent(TableOfResults.this,ScheduleActivity.class);
                    startActivity(intent);
                }
                if(position==4){
                    Intent intent = new Intent(TableOfResults.this,EmissionFactorActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbHelper= new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        query_select = database.rawQuery("SELECT * FROM cities;", null);
        if(query_select.getCount()==0) {
            city_mass_select = new String[1];
            city_mass_select[0]= "Очень пусто";
        }
        else  if(query_select.getCount()!=0){
            city_mass_select = new String[query_select.getCount()];
            if (query_select.moveToFirst()) {
                int i = 0;
                do {
                    city_mass_select[i++] = (query_select.getString(1));
                }
                while (query_select.moveToNext());
            }
        }
        adapter_city = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, city_mass_select);
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner = findViewById(R.id.spinner_city2);
        citySpinner.setAdapter(adapter_city);
    }

    public void onClick(View view){
        database = dbHelper.getWritableDatabase();
         table_dataCity =findViewById(R.id.table_base);

        String[] titles = {"Рік", "Бензин", "Дизель",
                "Стічні води оч.", "Стічні води не оч.","Стічні води АМ" ,
                "ПХДД Бензин", "ПХДФ Бензин",
                "ПХДД Дизель", "ПХДФ Дизель",
                "ПХДД Стічні води оч.", "ПХДФ Стічні води оч.",
                "ПХДД Стічні води не оч.","ПХДФ Стічні води не оч.",
 "ПХДД Стічні води АИ", "ПХДФ Стічні води АИ",
                "НР ПХДД Бензин", "НР ПХДФ Бензин",
                "НР ПХДД Дизель", "НР ПХДФ Дизель",
                "НР ПХДД Стічні води оч.", "НР ПХДФ Стічні води оч.",
                "НР ПХДД Стічні води не оч.","НР ПХДФ Стічні води не оч." ,
                "НР ПХДД Стічні води АИ", "НР ПХДФ Стічні води АИ"
        };
        table_dataCity.removeAllViews();

        TableRow title = new TableRow(this);
        for(int i=0; i<titles.length; i++){
            TextView yearLabel1 = new TextView(this);
            yearLabel1.setText(titles[i]);
            yearLabel1.setTypeface(Typeface.DEFAULT_BOLD);
            yearLabel1.setGravity(Gravity.CENTER);
            yearLabel1.setBackgroundResource(R.drawable.border);
            yearLabel1.setPadding(8,0,8,0);
            title.addView(yearLabel1);
        }
            table_dataCity.addView(title);

        query_table_data=database.rawQuery("SELECT * FROM data_city WHERE data_city_id= " + get_city(get_cursor()) + " ORDER BY year ", null);
        double[] mas_pet = new double[query_table_data.getCount()];
        double[] mas_dies = new double[query_table_data.getCount()];
        double[] mas_treat = new double[query_table_data.getCount()];
        double[] mas_untreat = new double[query_table_data.getCount()];
        double[] mas_activ = new double[query_table_data.getCount()];
        if(query_table_data.moveToFirst()){
            int i=0;
            do{
                mas_pet[i] = (query_table_data.getDouble(8));
                mas_dies[i] =Double.parseDouble(query_table_data.getString(10));
                mas_treat[i] = Double.parseDouble(query_table_data.getString(12));
                mas_untreat[i] = Double.parseDouble(query_table_data.getString(14));
                mas_activ[i] = Double.parseDouble(query_table_data.getString(16));
                i++;
            }
            while (query_table_data.moveToNext());
        }

result(mas_pet,query_table_data);
        result(mas_dies,query_table_data);
        result(mas_treat,query_table_data);
        result(mas_untreat,query_table_data);
        result(mas_activ,query_table_data);
        double[][] array = new double[5][query_table_data.getCount()];

            for (int j = 0; j < query_table_data.getCount(); j++) {
                array[0][j]=mas_pet[j];
                array[1][j]=mas_dies[j];
                array[2][j]=mas_treat[j];
                array[3][j]=mas_untreat[j];
                array[4][j]=mas_activ[j];
            }

        if(query_table_data.moveToFirst()){
            int count=0;
            do{
                TableRow rowYearLabels = new TableRow(this);
                TextView rik = new TextView(this);
                rik.setText(query_table_data.getString(2));
                rik.setTypeface(Typeface.DEFAULT_BOLD);
                rik.setGravity(Gravity.CENTER);
                rik.setBackgroundResource(R.drawable.border);
                rowYearLabels.addView(rik);
                for(int i=3; i<8; i++) {
                    TextView year_ = new TextView(this);
                    year_.setText(String.format("%.4f",query_table_data.getDouble(i)));
                    year_.setTypeface(Typeface.DEFAULT_BOLD);
                    year_.setGravity(Gravity.CENTER);
                    year_.setBackgroundResource(R.drawable.border);
                    rowYearLabels.addView(year_);
                }
for(int i=8; i<=17; i++) {
    TextView year = new TextView(this);
    year.setText(String.format("%.10f",query_table_data.getDouble(i)));
    year.setTypeface(Typeface.DEFAULT_BOLD);
    year.setGravity(Gravity.CENTER);
    year.setBackgroundResource(R.drawable.border);
    rowYearLabels.addView(year);
}
                for(int j=0; j<array.length; j++){
                TextView year_half_life1 = new TextView(this);
                    year_half_life1.setText(String.format("%.15f",array[j][count]));
                    year_half_life1.setTypeface(Typeface.DEFAULT_BOLD);
                    year_half_life1.setGravity(Gravity.CENTER);
                    year_half_life1.setBackgroundResource(R.drawable.border);
                    rowYearLabels.addView(year_half_life1);

                    TextView year_half_life = new TextView(this);
                    year_half_life.setText(String.format("%.15f",array[j][count] * 0.1));
                    year_half_life.setTypeface(Typeface.DEFAULT_BOLD);
                    year_half_life.setGravity(Gravity.CENTER);
                    year_half_life.setBackgroundResource(R.drawable.border);
                    rowYearLabels.addView(year_half_life);
                }
count++;
                table_dataCity.addView(rowYearLabels);

            } while (query_table_data.moveToNext());
        }
            table_dataCity.setVisibility(View.VISIBLE);
    }

}
