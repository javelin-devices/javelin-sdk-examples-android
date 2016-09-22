package javelindevices.com.javelin_tutorial_3_0.DataProcessing;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Andrew Wang on 8/31/2016.
 */
public class Sensor3DData {
    private transient final String TAG = "Tutorial 3.0";
    String sensor_name;
    int sensor_number;
    ArrayList<ArrayList<Float>> xyz;
    double sampling_rate; //f

    public Sensor3DData(String sensor_name,
                        int sensor_number,
                        double sampling_rate) {
        this.sensor_name = sensor_name;
        this.sensor_number = sensor_number;
        this.sampling_rate = sampling_rate;
        xyz = new ArrayList<>();
    }
    public boolean addSample(ArrayList<Float> xyzSample){
        if(xyzSample.size()==3){
            xyz.add(xyzSample);
            return true;
        }
        else{
            Log.v(TAG, "Wrong size sample given");
            return false;
        }
    }
    public void parseStream(float[] stream){
        if(stream.length %3==0){
            for(int i = 0;i<stream.length;i=i+3){
                ArrayList<Float> sample = new ArrayList<>(3);
                sample.add(stream[i]);
                sample.add(stream[i+1]);
                sample.add(stream[i+2]);
                addSample(sample);
            }
        }
        else{
            Log.v(TAG,"Invalid stream length");
        }
    }

    public void reset(){
        xyz = new ArrayList<>();
    }
}
