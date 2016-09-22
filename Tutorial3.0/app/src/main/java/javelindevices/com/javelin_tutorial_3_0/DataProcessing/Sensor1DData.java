package javelindevices.com.javelin_tutorial_3_0.DataProcessing;

import java.util.ArrayList;

/**
 * Created by Nadeem on 11/3/2015.
 **/
public class Sensor1DData {
    private final String TAG = "Tutorial 3.0";
    String sensor_name;
    int sensor_number;
    ArrayList<Float> data;
    double sampling_rate; //f

    public Sensor1DData(String sensor_name,
                        int sensor_number,
                        double sampling_rate) {
        this.sensor_name = sensor_name;
        this.sensor_number = sensor_number;
        this.sampling_rate = sampling_rate;
        data = new ArrayList<>();
    }

    public void parseStream(float[] stream){
        for(int i = 0;i<stream.length;i++){
            data.add(stream[i]);
        }
    }
    public void reset(){
        data = new ArrayList<>();
    }
}
