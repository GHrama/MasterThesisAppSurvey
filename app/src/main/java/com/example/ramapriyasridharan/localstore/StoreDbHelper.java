package com.example.ramapriyasridharan.localstore;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.CostQno;
import com.example.ramapriyasridharan.StoreValues.FetchByIdQuestionsTableStore;
import com.example.ramapriyasridharan.StoreValues.PrivacyQno;
import com.example.ramapriyasridharan.StoreValues.UserResponseTableStore;
import com.example.ramapriyasridharan.StoreValues.getCostPrivacyPointsTable;
import com.example.ramapriyasridharan.collections.UserResponseClass;
import com.example.ramapriyasridharan.helpers.Privacy;
import com.example.ramapriyasridharan.helpers.ToByteStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
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
    private static final String TABLE_USER_RESPONSE_CACHE = "USER_RESPONSE_CACHE";
    private static final String TABLE_STORE_ACCELEROMETER = "STORE_ACCELEROMETER";

    // column names for STORE_ACCELEROMETER
    private static final String ACC_X = "X";
    private static final String ACC_Y = "Y";
    private static final String ACC_Z = "Z";
    private static final String TIMESTAMP = "TIMESTAMP";

    // column names for USER_RESPONSE_CACHE
    private static final String KEY = "KEY"; // just some id
    private static final String USER_RESPONSE = "USER_RESPONSE"; // store as BLOB
    private static final String IS_SENT = "IS_SENT"; // if sent or not?

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

    private static final String CREATE_TABLE_USER_RESPONSE_CACHE = "CREATE TABLE "
            + TABLE_USER_RESPONSE_CACHE +"( "+KEY+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ USER_RESPONSE +" BLOB,"+ IS_SENT +" INTEGER"+" )";

    private static final String CREATE_TABLE_STORE_ACCELEROMETER = "CREATE TABLE "+TABLE_STORE_ACCELEROMETER+"( "+
            ACC_X+" REAL,"+ACC_Y+" REAL,"+ACC_Z+" REAL,"+TIMESTAMP+" REAL )";


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
        db.execSQL(CREATE_TABLE_USER_RESPONSE_CACHE);
        db.execSQL(CREATE_TABLE_STORE_ACCELEROMETER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHICH_ANSWERED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_RESPONSE_CACHE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ACCELEROMETER);

        onCreate(db);
    }

    // insert intro UserResponse Table
    public void insertIntoUserResponseTable(UserResponseClass ur){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //convert to byte stream
        byte[] yourBytes = ToByteStream.objectToByte(ur);
        values.put(USER_RESPONSE, yourBytes);
        values.put(IS_SENT, 0);
        long id = db.insert(TABLE_USER_RESPONSE_CACHE, null, values);
        Log.d("DB", "inserted id for cache table = " + id);
    }

    public void insertIntoAccelerometerTable(double timestamp, float x,float y,float z){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, timestamp);
        values.put(ACC_X,x);
        values.put(ACC_Y,y);
        values.put(ACC_Z,z);
        db.insert(TABLE_STORE_ACCELEROMETER, null, values);
        Log.d("DB", "insertint acc_table acc_X = " + x);
        Log.d("DB", "insertint acc_table acc_X = " + y);
        Log.d("DB", "insertint acc_table acc_X = " + z);
    }



    public ArrayList<PrivacyQno> getPrivacyConvertedQnoAnswersTable(int day_no){
        String q = "SELECT "+Q_ID+","+PRIVACY_LEVEL+" FROM "+TABLE_STORE_ANSWERS+" WHERE "+DAY_NO+"="+day_no;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        ArrayList<PrivacyQno> p = new ArrayList<PrivacyQno>();
        while(cursor.moveToNext()){
            PrivacyQno temp = new PrivacyQno(Privacy.convertLevelToPrivacy(cursor.getInt(1)),cursor.getInt(0));
            p.add(temp);
        }
        cursor.close();
        return p;
    }


    // return cost for question
    public double getCostIfAnsweredAnswersTable(int day_no,int qno){
        String q = "SELECT "+COST_OBTAINED+" FROM "+TABLE_STORE_ANSWERS+" WHERE "+DAY_NO+"="+day_no+" AND "+Q_ID+"="+qno;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        double cost = -1;

        if(cursor.moveToFirst()){
            cost = cursor.getDouble(0);
        }
        cursor.close();
        return cost;
    }

    public double getCostForQnoQuestionsTable(int qno){
        String q = "SELECT "+MAX_COST+" FROM "+TABLE_QUESTION_INFO+" WHERE "+Q_ID+"="+qno;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        double cost = -1;
        if(cursor.moveToFirst()){
            cost = cursor.getDouble(0);
        }
        cursor.close();
        return cost;
    }

    // Get first from UserResponseTable which is not sent
    // if ur = null && key = -1 then nothign to send
    public ArrayList<UserResponseTableStore> getUserResponseAndSendTable(){
        ArrayList<UserResponseTableStore> ur_array = new ArrayList<UserResponseTableStore>();
        UserResponseTableStore urts = null;
        String q = "SELECT * FROM "+TABLE_USER_RESPONSE_CACHE+" WHERE "+ IS_SENT + "="+0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        int key = -1;
        UserResponseClass ur = null;
        while(cursor.moveToNext()){
            byte[] array_ur = cursor.getBlob(1);
            ur = (UserResponseClass) ToByteStream.byteToObject(array_ur);
            Log.d("DB", "retrieved ur id = " + cursor.getInt(0));
            Log.d("DB", "is_sent = " + cursor.getInt(2));
            Log.d("DB", "byte array to ur = " + ur);
            key = cursor.getInt(0);
            urts = new UserResponseTableStore(ur, key, cursor.getInt(2));
            ur_array.add(urts);
        }
        cursor.close();
        return ur_array;
    }


    // Update the is_sent to 1 if sent successfully to kinvey
    public void updateSentUserResponse(int key){
        String q = "UPDATE "+TABLE_USER_RESPONSE_CACHE+" SET "+ IS_SENT +"="+1+" WHERE "+KEY+"="+key;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(q);
    }

    // return question id from answers table
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
        Cursor cursor = db.rawQuery(q, null);
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

    // get answer from previous day_no or current_dayno
    public int getLevelFromAnswersTable(int q_no,int day){
        String q = "SELECT "+PRIVACY_LEVEL+" FROM "+TABLE_STORE_ANSWERS+" WHERE "+DAY_NO+"="+day+" AND "+Q_ID+"="+q_no;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        cursor.moveToFirst();
        int level = cursor.getInt(0);
        cursor.close();
        return level;
    }

    public ArrayList<Integer> getAllAnswered(){
        ArrayList<Integer> qno= new ArrayList<Integer>();
        String q = "SELECT * FROM "+TABLE_WHICH_ANSWERED+" ORDER BY "+Q_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);
        while(cursor.moveToNext()){
            int temp = cursor.getInt(0);
            Log.d("DB", "get answer id from answered table" + temp);
            qno.add(temp);
        }
        return qno;
    }

    // get info from points table
    public getCostPrivacyPointsTable getCostPrivacyPointsTable(int day){
        String q = "SELECT * FROM "+TABLE_STORE_POINTS+" WHERE "+DAY_NO+"="+day;
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

    // get all levels.
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
        db.delete(TABLE_USER_RESPONSE_CACHE,null,null);
        db.delete(TABLE_WHICH_ANSWERED,null,null);
        db.delete(TABLE_STORE_ANSWERS,null,null);
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
