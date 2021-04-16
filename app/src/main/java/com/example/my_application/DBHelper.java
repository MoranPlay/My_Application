package com.example.my_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cityDB";

    static final String TABLE_CITY = "cities";
    public static final String TABLE_DATA = "data_city";
    public static final String TABLE_COEFFICIENT = "coefficient";

    public static final String NAME = "city";
    public static final String NAME_ID = "city_id";

    public static final String DATA_ID = "data_id";
    public static final String CITY_ID = "data_city_id";
    public static final String YEAR = "year";

    public static final String PETROL = "petrol";
    public static final String PETROL_DD = "petrol_dd";
    public static final String PETROL_DPH = "petrol_dph";

    public static final String DIESEL = "diesel";
    public static final String DIESEL_DD = "diesel_dd";
    public static final String DIESEL_DPH = "diesel_dph";

    public static final String TREATED_WASTEWATER = "treated_waste_water";
    public static final String TREATED_WASTEWATER_DD = "treated_waste_water_dd";
    public static final String TREATED_WASTEWATER_DPH = "treated_waste_water_dph";

    public static final String UNTREATED_WASTEWATER = "untreated_waste_water";
    public static final String UNTREATED_WASTEWATER_DD = "untreated_waste_water_dd";
    public static final String UNTREATED_WASTEWATER_DPH = "untreated_waste_water_dph";

    public static final String ACTIVATED_SLUDGE = "activated_sludge";
    public static final String ACTIVATED_SLUDGE_DD = "activated_sludge_dd";
    public static final String ACTIVATED_SLUDGE_DPH = "activated_sludge_dph";

    public static final String COEF_ID = "coef_id";
    public static final String COEF_PET = "coef_pet";
    public static final String COEF_DIES = "coef_dies";
    public static final String COEF_TREAT = "coef_treat";
    public static final String COEF_UNTREAT = "coef_untreat";
    public static final String COEF_ACTIV = "coef_activ";


    // double a=(2.2*Math.pow(10,-6));

    public static final String CREATE_TABLE_CITY = "CREATE TABLE IF NOT EXISTS " + TABLE_CITY
            + " ( " + NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " + NAME
            + " TEXT NOT NULL )";

    public static final String CREATE_TABLE_DATA = "CREATE TABLE IF NOT EXISTS " +
            TABLE_DATA + " (" +
            DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CITY_ID + " INTEGER, " +
            YEAR + " INTEGER, " +
            PETROL + " REAL, " +
            DIESEL + " REAL, " +
            TREATED_WASTEWATER + " REAL, " +
            UNTREATED_WASTEWATER + " REAL, " +
            ACTIVATED_SLUDGE + " REAL, " +
            PETROL_DD + " REAL, " +
            PETROL_DPH + " REAL, " +
            DIESEL_DD + " REAL, " +
            DIESEL_DPH + " REAL, " +
            TREATED_WASTEWATER_DD + " REAL, " +
            TREATED_WASTEWATER_DPH + " REAL, " +
            UNTREATED_WASTEWATER_DD + " REAL, " +
            UNTREATED_WASTEWATER_DPH + " REAL, " +
            ACTIVATED_SLUDGE_DD + " REAL, " +
            ACTIVATED_SLUDGE_DPH + " REAL, " +
            "FOREIGN KEY(" + CITY_ID + ") REFERENCES " +
            TABLE_CITY + " (" + NAME_ID + ") " + ")";

    public static final String CREATE_TABLE_COEFFICIENT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COEFFICIENT
            + " ( " + COEF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COEF_PET + " REAL,"
            + COEF_DIES + " REAL,"
            + COEF_TREAT + " REAL,"
            + COEF_UNTREAT + " REAL,"
            + COEF_ACTIV + " REAL"
            + " )";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_DATA);
        db.execSQL(CREATE_TABLE_COEFFICIENT);

//        db.execSQL("INSERT INTO coefficient VALUES (" + (2.2*Math.pow(10,-6)) + "," +(0.1*Math.pow(10,-6))+ "," + (1*Math.pow(10,-11)) + ","+(0.5*Math.pow(10,-8)) + "," + (0.2*Math.pow(10,-3)) +")");
        db.execSQL("INSERT INTO coefficient (coef_pet,coef_dies,coef_treat,coef_untreat,coef_activ) VALUES (" + (2.2 * Math.pow(10, -6)) + "," + (0.1 * Math.pow(10, -6)) + "," + (1 * Math.pow(10, -11)) + "," + (0.5 * Math.pow(10, -8)) + "," + (0.2 * Math.pow(10, -3)) + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
