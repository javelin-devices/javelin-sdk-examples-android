package javelindevices.com.javelin_tutorial_3_0.DataProcessing;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by apwan_000 on 10/10/2016.
 */

public class SignalProcessing1DData extends Sensor1DData {
    private transient final String TAG = "Tutorial 3.0";
    public double max = 0;

    public SignalProcessing1DData(String sensor_name, int sensor_number, double sampling_rate) {
        super(sensor_name, sensor_number, sampling_rate);

    }

    public double Max(float[] stream) {
        for(int j = 0; j < stream.length; j++){
            if(stream[j] > max){
                max = stream[j];
            }
        }
        return max;
    }
}