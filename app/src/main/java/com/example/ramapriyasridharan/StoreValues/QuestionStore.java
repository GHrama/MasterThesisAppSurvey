package com.example.ramapriyasridharan.StoreValues;

/**
 * Created by ramapriyasridharan on 23.05.16.
 */
public class QuestionStore {
    public String q;
    public FetchByIdQuestionsTableStore temp;
    public int q_no;
    public QuestionStore(String q, FetchByIdQuestionsTableStore temp, int q_no){
        this.q = q;
        this.temp = temp;
        this.q_no = q_no;
    }


    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public FetchByIdQuestionsTableStore getTemp() {
        return temp;
    }

    public void setTemp(FetchByIdQuestionsTableStore temp) {
        this.temp = temp;
    }

    public int getQ_no() {
        return q_no;
    }

    public void setQ_no(int q_no) {
        this.q_no = q_no;
    }
}
