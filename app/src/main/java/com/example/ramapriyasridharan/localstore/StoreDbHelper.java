package com.example.ramapriyasridharan.localstore;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.FetchByIdQuestionsTableStore;
import com.example.ramapriyasridharan.StoreValues.getCostPrivacyPointsTable;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 18.05.16.
 */
public class StoreDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "STORE";

    private static final String TABLE_QUESTION_INFO = "QUESTION_INFO";
    private static final String TABLE_STORE_ANSWERS = "STORE_ANSWERS";
    private static final String TABLE_STORE_POINTS = "STORE_POINTS";
    private static final String TABLE_WHICH_ANSWERED = "WHICH_ANSWERED";

    //column names common
    private static final String Q_ID = "Q_ID";

    //column names QUESTION_INFO
    private static final String SENSORS = "SENSORS";
    private static final String DC = "DATA_COLLECTORS";
    private static final String CONTEXTS = "CONTEXTS";
    private static final String MAX_COST = "MAX_COST";
    private static final String WEIGHT = "WEIGHT";

    //column names STORE_ANSWERS
    private static final String PRIVACY_LEVEL = "PRIVACY_LEVEL";
    private static final String COST_OBTAINED = "COST_OBTAINED";
    private static final String DAY_NO = "DAY_NO";

    //columns name STORE_POINTS
    private static final String COST = "COST";
    private  static final String PRIVACY = "PRIVACY";


    private static final String CREATE_TABLE_QUESTION_INFO = "CREATE TABLE "
            + TABLE_QUESTION_INFO + "( " + Q_ID + " INTEGER PRIMARY KEY," + SENSORS
            + " INTEGER," + DC + " INTEGER," + CONTEXTS + " INTEGER,"+ MAX_COST + " REAL," + WEIGHT + " REAL" + " )";

    private static  final String CREATE_TABLE_STORE_ANSWERS = "CREATE TABLE " + TABLE_STORE_ANSWERS
            + "( " + Q_ID + " INTEGER PRIMARY KEY," + PRIVACY_LEVEL + " INTEGER," + DAY_NO + " INTEGER," + COST_OBTAINED
            + " REAL" + " )";

    private static final String CREATE_TABLE_STORE_POINTS = "CREATE TABLE "
            + TABLE_STORE_POINTS +"( " + DAY_NO + " INTEGER PRIMARY KEY," + COST
            + " REAL," + PRIVACY + " REAL" + " )";

    private static final String CREATE_TABLE_WHICH_ANSWERD = "CREATE TABLE "
            + TABLE_WHICH_ANSWERED +"( " + Q_ID + " INTEGER PRIMARY KEY )";



    // create the DB
    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION_INFO);
        db.execSQL(CREATE_TABLE_STORE_ANSWERS);
        db.execSQL(CREATE_TABLE_STORE_POINTS);
        db.execSQL(CREATE_TABLE_WHICH_ANSWERD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHICH_ANSWERED);

        onCreate(db);
    }


    // return question id from answeres table
    // with certain level
    public ArrayList<Integer> getAnswersLevelAnswersTable(int level,int day){
        String q = "SELECT "+ Q_ID +" FROM "+TABLE_STORE_ANSWERS+" WHERE "+ PRIVACY_LEVEL + "=" +level +" AND "+DAY_NO+"="+day;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        ArrayList<Integer> questions = new ArrayList<Integer>();
        while(cursor.moveToNext()){
            Log.d("DB", "get question with level = " + cursor.getInt(0));
            questions.add(cursor.getInt(0));
        }
        cursor.close();
        return questions;
    }
    // insert questions if answered
    public void insertQuestionAnsweredTable(int q_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Q_ID, q_no);
        long _id = db.insert(TABLE_WHICH_ANSWERED, null, values);
        Log.d("DB", "_id of insertion question = " + _id);
    }

    // check if this question answered
    public boolean isQuestionPresentAnsweredTable(int q_no){
        // answered true
        // not answered false
        String q = "SELECT * FROM "+TABLE_WHICH_ANSWERED+" WHERE "+ Q_ID+"="+q_no;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if(cursor.moveToFirst()){
            Log.d("DB", "question present = " + q_no);
            cursor.close();
            return true; // data exists
        }
        else
        {
            Log.d("DB", "question not present = " + q_no);
            cursor.close();
            return false; // data not exist
        }
    }

    // all questions answered empty table and restart
    public void emptyAnsweredTable(){
        String q = "DELETE  FROM "+TABLE_WHICH_ANSWERED;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(q);
    }

    // check if all questions answered once
    public boolean allQuestionsPresentAnsweredTable(int num){
        // true - all answered
        // false - some not answered
        String q = "SELECT COUNT(*) FROM "+TABLE_WHICH_ANSWERED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);
        if(cursor.moveToFirst()){
            int temp = cursor.getInt(0);
            cursor.close();
            if(temp == num){
                Log.d("db", "temp==num");
                return true;
            }
            else{
                Log.d("db", "temp!=num");
                return false;
            }
        }
        cursor.close();
        Log.d("db", "no cursor.movetofirst()!"); // table empty?
        return false;
    }

    // return each cost one by one in arraylist
    public ArrayList<Double> returnCostAnswersTable(int day_no){
        ArrayList<Double> costs= new ArrayList<Double>();
        // get all levels from table_store_answers where day = day
        String q = "SELECT "+COST_OBTAINED+" FROM "+TABLE_STORE_ANSWERS+" WHERE "+ DAY_NO+"="+day_no;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);
        while(cursor.moveToNext()){
            double temp = cursor.getDouble(0);
            Log.d("Cost db","value = "+temp);
            costs.add(temp);
        }
        cursor.close();
        return costs;
    }

    public long insertPointsTable(int day_no, double cost, double privacy){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DAY_NO, day_no);
        values.put(COST,cost);
        values.put(PRIVACY, privacy);

        long _id = db.insert(TABLE_STORE_POINTS, null, values);
        Log.d("DB", "_id of insertion points = " + _id);
        return _id;
    }


    // get info from points table
    public getCostPrivacyPointsTable getCostPrivacyPointsTable(int day){
        String q = "SELECT * FROM "+TABLE_STORE_POINTS+" WHERE "+ DAY_NO+"="+day;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);
        cursor.moveToFirst();
        getCostPrivacyPointsTable t = new getCostPrivacyPointsTable(cursor.getInt(0),cursor.getDouble(1),cursor.getDouble(2));
        cursor.close();
        return t;
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


    // replace existing row with same id?
    public long replaceQuestionTable(int id, int sensors, int dc, int contexts, double max_cost, double weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Q_ID, id);
        values.put(SENSORS,sensors);
        values.put(DC, dc);
        values.put(CONTEXTS, contexts);
        values.put(MAX_COST, max_cost);
        values.put(WEIGHT, weight);
        long _id = db.replace(TABLE_QUESTION_INFO, null, values);
        Log.d("DB", "_id of insertion question = " + _id);
        return _id;
    }

    public long insertStoreAnswers(int id, int level, double cost, int day_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Q_ID, id);
        values.put(PRIVACY_LEVEL, level);
        values.put(COST_OBTAINED, cost);
        values.put(DAY_NO,day_no);

        long _id = db.insert(TABLE_STORE_ANSWERS, null, values);
        Log.d("DB", "_id of insertion answers = " + _id);
        return _id;
    }

    public long replaceStoreAnswers(int id, int level, double cost, int day_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Q_ID, id);
        values.put(PRIVACY_LEVEL, level);
        values.put(COST_OBTAINED, cost);
        values.put(DAY_NO,day_no);

        long _id = db.replace(TABLE_STORE_ANSWERS, null, values);
        Log.d("DB", "_id of insertion answers = " + _id);
        return _id;
    }

    // get all levels
    // for a particular day ie all answered answers
    public ArrayList<Double> getAllLevelsFromAnswersTable(int day){
        ArrayList<Double> levels= new ArrayList<Double>();
        // get all levels from table_store_answers where day = day
        String q = "SELECT "+PRIVACY_LEVEL+" FROM "+TABLE_STORE_ANSWERS+" WHERE "+ DAY_NO+"="+day;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);
        while(cursor.moveToNext()){
            double temp = cursor.getDouble(0);
            levels.add(temp);
        }
        cursor.close();
        return levels;
    }


    // remove all content of tables
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_STORE_ANSWERS, null, null);
        db.delete(TABLE_QUESTION_INFO, null, null);
        db.delete(TABLE_STORE_POINTS,null,null);
    }


    // fetch by id and get all information
    public FetchByIdQuestionsTableStore fetchByIdQuestionTable(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTION_INFO + " WHERE " + Q_ID + "=" + id, null);
        if(cursor.moveToFirst()){
            int s = cursor.getInt(cursor.getColumnIndex(SENSORS));
            //Log.d("db", "s = "+s);
            int d = cursor.getInt(cursor.getColumnIndex(DC));
            //Log.d("db", "dc = "+d);
            int c = cursor.getInt(cursor.getColumnIndex(CONTEXTS));
            //Log.d("db", "c = "+c);
//            double cost = cursor.getDouble(cursor.getColumnIndex(COST_OBTAINED));
//            Log.d("db", "cost = "+cost);
//            double weight = cursor.getDouble(cursor.getColumnIndex(WEIGHT));
//            Log.d("db", "weight = "+weight);

            //double cost = cursor.getColumnIndex(COST_OBTAINED);
            //double weight = cursor.getColumnIndex(WEIGHT);
            double cost = cursor.getDouble(4);
            double weight = cursor.getDouble(5);
            cursor.close();
            return new FetchByIdQuestionsTableStore(s,d,c,cost,weight);
        }
        else{
            cursor.close();
            return new FetchByIdQuestionsTableStore(-1,-1,-1,-1,-1);
        }
    }


}
