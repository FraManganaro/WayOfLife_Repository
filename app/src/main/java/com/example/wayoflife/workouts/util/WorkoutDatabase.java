package com.example.wayoflife.workouts.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDatabase extends SQLiteOpenHelper {

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
    public static final String NUM_SQUAT_COLUMN = "numSquat";
    public static final String STATO_FINE_ALLENAMENTO_COLUMN = "statoFineAllenamento";
    public static final String LIKE_ALLENAMENTO_COLUMN = "likeAllenamento";

    public WorkoutDatabase(Context context){
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
                " " + NUM_SQUAT_COLUMN + " INTEGER," +
                " " + STATO_FINE_ALLENAMENTO_COLUMN + " INTEGER," +
                " " + LIKE_ALLENAMENTO_COLUMN + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Aggiungo un elemento al DB
     */
    public boolean addWorkout(WorkoutModel workoutModel){
        // Get WriteAble Database
        SQLiteDatabase db = this.getWritableDatabase();

        //Add Values into Database
        ContentValues cv = new ContentValues();

        cv.put(NOME_COLUMN, workoutModel.getNome());
        cv.put(DATA_COLUMN, workoutModel.getData());
        cv.put(DURATA_COLUMN, workoutModel.getDurata());
        cv.put(TIPOLOGIA_COLUMN, workoutModel.getTipologia());
        cv.put(CALORIE_COLUMN, workoutModel.getCalorie());
        cv.put(STATO_FINE_ALLENAMENTO_COLUMN, workoutModel.getState());
        cv.put(LIKE_ALLENAMENTO_COLUMN, workoutModel.getLike());

        //Dati da verificare se sono presenti negli allenamenti
        //Se == -1, vuol dire che non sono stati inseriti nella fine dell'allenamento
        if(workoutModel.getChilometri() != -1)
            cv.put(CHILOMETRI_COLUMN, workoutModel.getChilometri());
        else
            cv.put(CHILOMETRI_COLUMN, -1);

        if(workoutModel.getN_flessioni() != -1)
            cv.put(NUM_FLESSIONI_COLUMN, workoutModel.getN_flessioni());
        else
            cv.put(NUM_FLESSIONI_COLUMN, -1);

        if(workoutModel.getN_squat() != -1)
            cv.put(NUM_SQUAT_COLUMN, workoutModel.getN_squat());
        else
            cv.put(NUM_SQUAT_COLUMN, -1);

        db.insert(TABLE_NAME, null, cv);
        db.close();


        return true;
    }

    /**
     * Lista completa degli allenamenti presenti nel DB
     */
    public List<WorkoutModel> getAllWorkout(){
        //Variabili
        List<WorkoutModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Create Cursor to Select All Values
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String customerNome = cursor.getString(1);
                String customerNData = cursor.getString(2);
                String customerDurata = cursor.getString(3);
                String customerTipologia = cursor.getString(4);
                int customerCalorie = cursor.getInt(5);
                float customerChilometri = cursor.getFloat(6);
                int customerFlessioni = cursor.getInt(7);
                int customerSquat = cursor.getInt(8);
                int customerState = cursor.getInt(9);
                int customerLike = cursor.getInt(10);

                WorkoutModel model = new WorkoutModel(id, customerNome, customerNData, customerSquat,
                        customerDurata, customerChilometri, customerTipologia,
                        customerCalorie, customerFlessioni, customerState, customerLike);

                returnList.add(model);

            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    /**
     * Query per allenamenti in data -data-
     */
    public List<WorkoutModel> getWorkoutForDate(String data){
        //Variabili
        data = "\"" + data + "\"";

        List<WorkoutModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + DATA_COLUMN + " LIKE " + data;

        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Create Cursor to Select All Values
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String customerNome = cursor.getString(1);
                String customerNData = cursor.getString(2);
                String customerDurata = cursor.getString(3);
                String customerTipologia = cursor.getString(4);
                int customerCalorie = cursor.getInt(5);
                float customerChilometri = cursor.getFloat(6);
                int customerFlessioni = cursor.getInt(7);
                int customerSquat = cursor.getInt(8);
                int customerState = cursor.getInt(9);
                int customerLike = cursor.getInt(10);

                WorkoutModel model = new WorkoutModel(id, customerNome, customerNData, customerSquat,
                        customerDurata, customerChilometri, customerTipologia,
                        customerCalorie, customerFlessioni, customerState, customerLike);

                returnList.add(model);

            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    /**
     * Query per allenamenti preferiti
     */
    public List<WorkoutModel> getWorkoutForLike(){
        //Variabili
        List<WorkoutModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + LIKE_ALLENAMENTO_COLUMN + " = 1";

        //Get Readable Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Create Cursor to Select All Values
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String customerNome = cursor.getString(1);
                String customerNData = cursor.getString(2);
                String customerDurata = cursor.getString(3);
                String customerTipologia = cursor.getString(4);
                int customerCalorie = cursor.getInt(5);
                float customerChilometri = cursor.getFloat(6);
                int customerFlessioni = cursor.getInt(7);
                int customerSquat = cursor.getInt(8);
                int customerState = cursor.getInt(9);
                int customerLike = cursor.getInt(10);

                WorkoutModel model = new WorkoutModel(id, customerNome, customerNData, customerSquat,
                        customerDurata, customerChilometri, customerTipologia,
                        customerCalorie, customerFlessioni, customerState, customerLike);

                returnList.add(model);

            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    /**
     * Eliminare un elemento del DB dato un id
     */
    public boolean deleteWorkout(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " LIKE " + id;
        db.execSQL(query);
        db.close();

        return true;
    }

    /**
     * Aggiornare campo preferiti
     */
    public boolean updateFavorites(int numb, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_NAME + " SET " + LIKE_ALLENAMENTO_COLUMN + " = " + numb + " WHERE " + ID_COLUMN + " LIKE " + id;
        db.execSQL(query);
        db.close();

        return true;
    }
}
