package com.example.wayoflife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workout";
    private static final String TABLE_NAME = "history";
    public static final String ID_COLUMN = "id";
    public static final String NOME_COLUMN = "nome";
    public static final String DATA_COLUMN = "data";
    public static final String DURATA_COLUMN = "durata";
    public static final String TIPOLOGIA_COLUMN = "tipologia";
    public static final String CALORIE_COLUMN = "calorie";
    public static final String CHILOMETRI_COLUMN = "chilometri";
    public static final String NUM_FLESSIONI_COLUMN = "numFlessioni";
    public static final String STATO_FINE_ALLENAMENTO_COLUMN = "statoFineAllenamento";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                " " + ID_COLUMN + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                " " + NOME_COLUMN + " TEXT," +
                " " + DATA_COLUMN + " TEXT," +
                " " + DURATA_COLUMN + " TEXT," +
                " " + TIPOLOGIA_COLUMN + " TEXT," +
                " " + CALORIE_COLUMN + " INTEGER," +
                " " + CHILOMETRI_COLUMN + " FLOAT," +
                " " + NUM_FLESSIONI_COLUMN + " INTEGER," +
                " " + STATO_FINE_ALLENAMENTO_COLUMN + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addWorkout(CustomerModel customerModel){
        // Get WriteAble Database
        SQLiteDatabase db = this.getWritableDatabase();

        //Add Values into Database
        ContentValues cv = new ContentValues();

        cv.put(NOME_COLUMN, customerModel.getNome());
        cv.put(DATA_COLUMN, customerModel.getData());
        cv.put(DURATA_COLUMN, customerModel.getDurata());
        cv.put(TIPOLOGIA_COLUMN, customerModel.getTipologia());
        cv.put(CALORIE_COLUMN, customerModel.getCalorie());
        cv.put(STATO_FINE_ALLENAMENTO_COLUMN, customerModel.getState());

        //Dati da verificare se sono presenti negli allenamenti
        //Se == -1, vuol dire che non sono stati inseriti nella fine dell'allenamento
        if(customerModel.getChilometri() != -1)
            cv.put(CHILOMETRI_COLUMN, customerModel.getChilometri());

        if(customerModel.getN_flessioni() != -1)
            cv.put(NUM_FLESSIONI_COLUMN, customerModel.getN_flessioni());

        db.insert(TABLE_NAME, null, cv);

        return true;
    }

    public List<CustomerModel> getAllWorkout(){
        //Variabili
        List<CustomerModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Create Cursor to Select All Values
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                String customerNome = cursor.getString(1);
                String customerNData = cursor.getString(2);
                String customerDurata = cursor.getString(3);
                String customerTipologia = cursor.getString(4);
                int customerCalorie = cursor.getInt(5);
                int customerState = cursor.getInt(8);

                CustomerModel model = new CustomerModel(customerNome, customerNData, customerDurata, customerTipologia, customerCalorie, customerState);
                returnList.add(model);

            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public List<CustomerModel> getWorkoutForDate(String data){    //da controllare query
        //Variabili
        List<CustomerModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + ", WHERE " + DATA_COLUMN + " = " + data;

        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Create Cursor to Select All Values
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                String customerNome = cursor.getString(1);
                String customerNData = cursor.getString(2);
                String customerDurata = cursor.getString(3);
                String customerTipologia = cursor.getString(4);
                int customerCalorie = cursor.getInt(5);
                int customerState = cursor.getInt(8);

                CustomerModel model = new CustomerModel(customerNome, customerNData, customerDurata, customerTipologia, customerCalorie, customerState);
                returnList.add(model);

            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }
}
