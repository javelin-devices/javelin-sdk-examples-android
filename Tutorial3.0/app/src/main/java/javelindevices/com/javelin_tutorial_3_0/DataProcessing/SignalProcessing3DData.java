package javelindevices.com.javelin_tutorial_3_0.DataProcessing;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andrew Wang on 9/7/2016.
 */
public class SignalProcessing3DData extends Sensor3DData{
    private transient final String TAG = "Tutorial 3.0";
    ArrayList<Double> Magxyz;
    ArrayList<Double> Filtxyz;
    public double max = 0;


    public SignalProcessing3DData(String sensor_name, int sensor_number, double sampling_rate){
        super(sensor_name,sensor_number,sampling_rate);
        Magxyz = new ArrayList<>();
        Filtxyz = new ArrayList<>();
    }

     public ArrayList<Double> Mag3D(float[] stream){
        if(stream.length % 3 == 0) {
            for (int i = 0; i < stream.length; i=i+3) {
            Magxyz.add(Math.sqrt(Math.pow(stream[i], 2) + Math.pow(stream[i+1], 2) + Math.pow(stream[i+2], 2))-.975);
            }
        }
        else{
            Log.v(TAG,"Invalid stream length");
        }
         return Magxyz;
    }

    public double MovingAvgFilt(int windowSize,ArrayList<Double>input){
        double total = 0;
        double[] avg = new double[3];
        if(input.size() >= windowSize) {
            for (int j = 0; j < 3; j++) {
                for (int i = windowSize-1; i >= 0; i--) {
                    total = total + input.get(input.size()-i+j-3);

                }
                avg[j] = total/windowSize;
                total=0;
            }
            if(Math.max(Math.max(avg[0],avg[1]),avg[2]) > max && input.size()>600){
                max=Math.max(Math.max(avg[0],avg[1]),avg[2]);
            }
            Filtxyz.add(avg[0]);
            Filtxyz.add(avg[1]);
            Filtxyz.add(avg[2]);
        }
        return max;
    }
}
