package com.example.ramapriyasridharan.localstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ramapriyasridharan on 18.05.16.
 */
public class StoreDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "STORE";

    private static final String TABLE_QUESTION_INFO = "QUESTION_INFO";
    private static final String TABLE_STORE_ANSWERS = "STORE_ANSWERS";

    //column names common
    private static final String Q_ID = "Q_ID";

    //column names QUESTION_INFO
    private static final String SENSORS = "SENSORS";
    private static final String DC = "DATA_COLLECTORS";
    private static final String CONTEXTS = "CONTEXTS";
    private static final String MAX_COST = "MAX_COST";
    private static final String WEIGHT = "WEIGHT";

    //column names STORE_ANSWERS_ONE
    private static final String PRIVACY_LEVEL = "PRIVACY_LEVEL";
    private static final String COST_OBTAINED = "COST_OBTAINED";

    private static final String CREATE_TABLE_QUESTION_INFO = "CREATE TABLE "
            + TABLE_QUESTION_INFO + "(" + Q_ID + " INTEGER PRIMARY KEY," + SENSORS
            + " INTEGER," + DC + " INTEGER," + CONTEXTS + " INTEGER,"+ MAX_COST + " DOUBLE," + WEIGHT + " DOUBLE" + ")";

    private static  final String CREATE_TABLE_STORE_ANSWERS = "CREATE TABLE " + TABLE_STORE_ANSWERS
            + "(" + Q_ID + " INTEGER PRIMARY KEY," + PRIVACY_LEVEL + " INTEGER," + COST_OBTAINED
            + " DOUBLE" + ")";



    // create the DB
    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION_INFO);
        db.execSQL(CREATE_TABLE_STORE_ANSWERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_INFO);

        onCreate(db);
    }


    //insert data into questions table
    public long insertQuestionTable(int id, int sensors, int dc, int contexts, double max_cost, double weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Q_ID, id);
        values.put(SENSORS,sensors);
        values.put(DC, dc);
        values.put(CONTEXTS, contexts);
        values.put(MAX_COST, max_cost);
        values.put(WEIGHT, weight);

        long _id = db.insert(TABLE_QUESTION_INFO, null, values);
        Log.d("DB", "_id of insertion question = " + _id);
        return _id;
    }

    public long insertStoreAnswers(int id, int level, double cost){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Q_ID, id);
        values.put(PRIVACY_LEVEL,level);
        values.put(COST_OBTAINED, cost);

        long _id = db.insert(TABLE_STORE_ANSWERS, null, values);
        Log.d("DB","_id of insertion answers = "+_id);
        return _id;
    }


    // remove all content of tables
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_STORE_ANSWERS, null, null);
        db.delete(TABLE_QUESTION_INFO, null, null);
    }


}
