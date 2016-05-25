package com.example.ramapriyasridharan.StoreValues;



import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ramapriyasridharan.helpers.Weights;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;


/**
 * Created by ramapriyasridharan on 17.05.16.
 */
public class WeightCostAnswersMatrix {

    public static double[][][] weight_matrix = new double[Sensors.sensor_list.length][DataCollectors.dc_list.length][Contexts.contexts_list.length];
    public static double[][][] cost_matrix = new double[Sensors.sensor_list.length][DataCollectors.dc_list.length][Contexts.contexts_list.length];
    public static double[][][][] answers = new double[Sensors.sensor_list.length][DataCollectors.dc_list.length][Contexts.contexts_list.length][Settings.no_days];

    WeightCostAnswersMatrix(){
        for(int i=0;i<Sensors.sensor_list.length;i++){
            for(int j=0;j<DataCollectors.dc_list.length;j++){
                for(int k=0;k<Contexts.contexts_list.length;k++){
                    weight_matrix[i][j][k] = -1;
                    cost_matrix[i][j][k] = -1;
                    for(int h = 0;h < Settings.no_days;h++){
                        answers[i][j][k][h] = -1;
                        //this_round_privacy[h] = 0;
                        //this_round_num[h] = 0;
                    }
                }
            }
        }
    }


    public static void calcWeightMatrix(Context c){
        //store values locally
        SharedPreferences settings = c.getSharedPreferences("categorizing", Context.MODE_PRIVATE);

        for(int i = 0;i < Sensors.sensor_list.length; i++){
            // sensors
            double fs = Weights.getWeights(settings.getInt("sensors",5), Features.features_list.length);
            String current_sensor = Sensors.sensor_list[i];
            double s = Weights.getWeights(settings.getInt(current_sensor,5), Sensors.sensor_list.length );
            double ss = fs * s;

            for(int j = 0;j < DataCollectors.dc_list.length; j++){
                // data_collectors
                double fdc = Weights.getWeights(settings.getInt("data_collectors",5), Features.features_list.length);
                String current_data_collector = DataCollectors.dc_list[j];
                double dc = Weights.getWeights(settings.getInt(current_data_collector,5), DataCollectors.dc_list.length);
                double dd = fdc * dc;

                for(int k = 0;k < Contexts.contexts_list.length; k++){
                    // contexts
                    double fc = Weights.getWeights(settings.getInt("contexts",5), Features.features_list.length);
                    String current_context = Contexts.contexts_list[k];
                    double co = Weights.getWeights(settings.getInt(current_context,5),Contexts.contexts_list.length);
                    double cc = fc * co;

                    // add to weight matrix
                    weight_matrix[i][j][k] = ss + dd + cc;
                    Log.d("weight"," w = "+weight_matrix[i][j][k]);

                }
            }
        }


    }

    public static void getCostMatrix(){
        double weight_matrix_sum = 0.0;
        for(int i=0;i<Sensors.sensor_list.length;i++){
                for(int j=0;j<DataCollectors.dc_list.length;j++){
                for(int k=0;k<Contexts.contexts_list.length;k++){
                    weight_matrix_sum += weight_matrix[i][j][k];
                }
            }
        }

        for(int i=0;i<Sensors.sensor_list.length;i++){
            for(int j=0;j<DataCollectors.dc_list.length;j++){
                for(int k=0;k<Contexts.contexts_list.length;k++){
                    cost_matrix[i][j][k] += ((double)weight_matrix[i][j][k] / weight_matrix_sum) * Settings.daily_budget;
                    Log.d("cost"," c = "+cost_matrix[i][j][k]);
                }
            }
        }


    }

    public static void insertWeightCostDb(Context c) {
        int counter = 0;
        StoreDbHelper db = new StoreDbHelper(c);
        for (int i = 0; i < Sensors.sensor_list.length; i++) {
            for (int j = 0; j < DataCollectors.dc_list.length; j++) {
                for (int k = 0; k < Contexts.contexts_list.length; k++) {
                    db.insertQuestionTable(counter, i, j, k, cost_matrix[i][j][k], weight_matrix[i][j][k]);
                    Log.d("weight","inserted question= "+counter+" s= "+i+" b= "+j+" c= "+k+" cost= "+cost_matrix[i][j][k]+" weight= "+weight_matrix[i][j][k]);
                    counter++;
                }
            }
        }
        db.close();
    }


}
