package com.example.my_application;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static android.graphics.Color.GREEN;
import static com.example.my_application.MainActivity.data;

public class ScheduleActivity extends AppCompatActivity {

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


    ArrayAdapter<String> adapter;
    DBHelper dbHelper;
    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> series1;
    SQLiteDatabase database;
    ArrayAdapter<String> adapter_city;
    Spinner citySpinner;
    static String[] city_mass;
    Cursor query;
    Cursor query1;
    String[] variant = {"Бензин", "Дизель", "Сточные воды оч.", "Сточные воды не оч.", "Сточные воды АИ"};
    ArrayAdapter<String> adapter_variant;
    Spinner variantSpinner;
    TableOfResults table = new TableOfResults();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        dbHelper = new DBHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0){
//                    Intent intent = new Intent(ScheduleActivity.this,MainActivity.class);
//                    startActivity(intent);
//                }
                if (position == 1) {
                    Intent intent = new Intent(ScheduleActivity.this, BaseActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(ScheduleActivity.this, TableOfResults.class);
                    startActivity(intent);
                }
//                if(position==3){
//                    Intent intent = new Intent(ScheduleActivity.this,ScheduleActivity.class);
//                    startActivity(intent);
//                }
                if (position == 4) {
                    Intent intent = new Intent(ScheduleActivity.this, EmissionFactorActivity.class);
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
            city_mass[0] = "Очень пусто";
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
        citySpinner = findViewById(R.id.spinner_city3);
        citySpinner.setAdapter(adapter_city);


        adapter_variant = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, variant);
        adapter_variant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        variantSpinner = findViewById(R.id.spinner_variant);
        variantSpinner.setAdapter(adapter_variant);


    }

    public void onClick(View view) {
        database = dbHelper.getWritableDatabase();
        int count = 0;

        query = database.rawQuery("SELECT * FROM data_city WHERE data_city_id= " + get_city(get_cursor()) + " ORDER BY year ", null);
        series = new LineGraphSeries<>();
        double[] mas_pet = new double[query.getCount()];
        double[] mas_dies = new double[query.getCount()];
        double[] mas_treat = new double[query.getCount()];
        double[] mas_untreat = new double[query.getCount()];
        double[] mas_activ = new double[query.getCount()];
        if (query.moveToFirst()) {
            int i = 0;
            do {
                mas_pet[i] = (query.getDouble(8));
                mas_dies[i] = Double.parseDouble(query.getString(10));
                mas_treat[i] = Double.parseDouble(query.getString(12));
                mas_untreat[i] = Double.parseDouble(query.getString(14));
                mas_activ[i] = Double.parseDouble(query.getString(16));
                i++;
            }
            while (query.moveToNext());
        }
        table.result(mas_pet, query);
        table.result(mas_dies, query);
        table.result(mas_treat, query);
        table.result(mas_untreat, query);
        table.result(mas_activ, query);
        double[][] array = new double[5][query.getCount()];

        for (int j = 0; j < query.getCount(); j++) {
            array[0][j] = mas_pet[j];
            array[1][j] = mas_dies[j];
            array[2][j] = mas_treat[j];
            array[3][j] = mas_untreat[j];
            array[4][j] = mas_activ[j];
        }

        DataPoint[] DP = new DataPoint[query.getCount()];
        DataPoint[] DP1 = new DataPoint[query.getCount()];
        switch (variantSpinner.getSelectedItemPosition()) {
            case 0:
                count = 0;
                break;

            case 1:
                count = 1;
                break;

            case 2:
                count = 2;
                break;

            case 3:
                count = 3;
                break;

            case 4:
                count = 4;
                break;
        }

        if (query.moveToFirst()) {
            int i = 0;
            int column = 0;
            do {
                DP[i] = new DataPoint(Integer.parseInt(query.getString(2)), array[count][column]);
                DP1[i] = new DataPoint(Integer.parseInt(query.getString(2)), array[count][column] * 0.1);
                i++;
                column++;
            }
            while (query.moveToNext());
        }
        series = new LineGraphSeries<>(DP);
        series1 = new LineGraphSeries<>(DP1);
        GraphView graph = (GraphView) findViewById(R.id.graph1);
        graph.setVisibility(View.VISIBLE);
        graph.removeAllSeries();

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setAnimated(true);
        series1.setColor(GREEN);
        series1.setDrawDataPoints(true);
        series1.setDataPointsRadius(10);
        series1.setAnimated(true);

        graph.getGridLabelRenderer().setNumHorizontalLabels(query.getCount());

        graph.addSeries(series);
        graph.addSeries(series1);
        query.close();
        dbHelper.close();

    }

}
