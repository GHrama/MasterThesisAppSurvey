package com.example.ramapriyasridharan.StoreValues;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.example.ramapriyasridharan.trialapp04.R;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 17.05.16.
 */
public class Questions {
    public static MessageFormat question = new MessageFormat("The Data Collector {1} would like to get your {0} sensor data for the purpose of {2}.\nPlease choose one of the following data sharing levels :");


    /**
     * TO DO
     * makes the underscores into spaces
     * and makes the first letter of each word capital (Presentation)
     * @param s
     * @return
     */
    static public String refineString(String s){
        s  = s.replaceAll("_", " ").toLowerCase();
        String[] words = s.split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if(i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public static String returnQuestion(String a, String b, String c){
        Object[] args = {refineString(a),refineString(b),refineString(c)};
        String q = question.format(args);
        return q;
    }

    // imprve privacy or credit
    // from unanswered questions pick with low or high privacy
    public static QuestionStore getQuestionWc(WhichClicked wc, StoreDbHelper db, int num,Context c){
        SharedPreferences settings =  c.getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        int current_day = settings.getInt("current_day", 2);
        // this is the day from which we get previous answers for credit/privacy
        // once we get answers for today fully we use today instead of yesterday
        if(db.allQuestionsPresentAnsweredTable(num)){
            Log.d("Question wc", "questions all_done = true");
            SharedPreferences.Editor e = settings.edit();
            e.putInt("prev_day",current_day);
            e.commit();
            db.emptyAnsweredTable();
        }

        int prev_day = settings.getInt("prev_day", (current_day-1)); // defaults to previous day
        Log.d("Question wc","prev_day = "+prev_day);
        Log.d("Question wc","current_day = "+current_day);
        int q_no = -1;

        if(wc.isCredit()){
            Log.d("Question wc","entered improve credit");
            // look for high privacy setting (5,4,3,2,1) order
            int level = 5;
            boolean flag = false;

            while(level >= 1){
                Log.d("Question wc", "level = " + level);
                ArrayList<Integer> questions = db.getAnswersLevelAnswersTable(level,prev_day);


                for(int i = 0;i < questions.size() ; i++){
                    Log.d("DB", "processing question = " + questions.get(i));
                    //check if question i answered
                    if(!db.isQuestionPresentAnsweredTable(questions.get(i))){
                        Log.d("Question wc","found question i = "+questions.get(i));
                        flag = true;
                        q_no = questions.get(i); // 0 indexed
                        break;
                    }
                }

                if(flag){
                    break;
                }
                else{
                    Log.d("Question wc","level --");
                    level--;
                }
            }

        }
        else if(wc.isPrivacy()){
            Log.d("Question wc","entered improve privacy");
            // look for low privacy setting (1,2,3,4,5) order
            int level = 1;
            boolean flag = false;

            while(level <= 5){
                Log.d("Question wc","level = "+level);
                ArrayList<Integer> questions = db.getAnswersLevelAnswersTable(level,prev_day);

                for(int i = 0;i < questions.size() ; i++){
                    Log.d("DB", "processing question = " + questions.get(i));
                    //check if question i answered
                    if(!db.isQuestionPresentAnsweredTable(questions.get(i))){
                        Log.d("Question wc","found question i = "+questions.get(i));
                        flag = true;
                        q_no = questions.get(i); // 0 indexed
                        break;
                    }
                }

                if(flag){ // if found something or not
                    break;
                }
                else{
                    Log.d("Question wc","level ++");
                    level++;
                }
            }

        }

        FetchByIdQuestionsTableStore q = db.fetchByIdQuestionTable(q_no);
        return new QuestionStore(returnQuestion(Sensors.sensor_list[q.s], DataCollectors.dc_list[q.d], Contexts.contexts_list[q.c]),q,q_no);
    }

    // keep getting next question
    // in order
    public static QuestionStore getQuestion(StoreDbHelper db, int current_question_number){
        // Database helper
        final FetchByIdQuestionsTableStore temp = db.fetchByIdQuestionTable(current_question_number);
        Log.d("question", "sensor id = " + temp.s);
        Log.d("question", "dc id = " + temp.d);
        Log.d("question", "context id = " + temp.c);
        String q = Questions.returnQuestion(Sensors.sensor_list[temp.s], DataCollectors.dc_list[temp.d], Contexts.contexts_list[temp.c]);
        return new QuestionStore(q,temp,current_question_number);
    }

    // if question unanswered send maximum privacy
    public static void fillUnansweredQuestions(StoreDbHelper db,int num,int day){
        ArrayList<Integer> qno = db.getAllAnswered();
        for(int i = 0;i<num;i++){
            if(i != qno.get(i)){
                // send to answers table
                db.replaceStoreAnswers(i,5,0,day);
                Log.d("question", "question not answered = " + i);
            }
        }
    }

}
