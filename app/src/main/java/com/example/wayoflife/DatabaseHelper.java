package com.example.wayoflife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workout";
    private static final String TABLE_NAME = "history";

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "    _id          INTEGER  PRIMARY KEY AUTOINCREMENT," +
                "    nome      TEXT," +
                "    data      DATE," +
                "    durata    TIME," +
                "    tipologia      TEXT," +
                "    calorie       INTEGER," +
                "    chilometri       FLOAT," +
                "    numFlessioni       INTEGER," +
                "    statoFineAllenamento INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addWorkout(String nome, Date data, Time durata, String tipologia, int calorie, float km, int n_flessioni, int state){
        // Get WriteAble Database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Add Values into Database
        String insert1="INSERT INTO " + TABLE_NAME + " (nome, data, durata, tipologia, calorie, chilometri, numFlessioni, statoFineAllenamento) " +
                "VALUES (nome, data, durata, tipologia, calorie, km, n_flessioni, state)";
        sqLiteDatabase.execSQL(insert1);
        return true;
    }

    public ArrayList getAllWorkout(){       //da sistemare
        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        //Create Cursor to Select All Values
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public ArrayList getWorkoutForDate(){
        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();


        return arrayList;
    }
}
