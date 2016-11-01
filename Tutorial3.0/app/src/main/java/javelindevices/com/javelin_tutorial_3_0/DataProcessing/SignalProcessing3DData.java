package javelindevices.com.javelin_tutorial_3_0.DataProcessing;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Andrew Wang on 9/7/2016.
 */
public class SignalProcessing3DData extends Sensor3DData {
    private transient final String TAG = "Tutorial 3.0";
    ArrayList<Float> Magxyz;
    ArrayList<Double> Filtxyz;
    ArrayList<Float> bufferX = new ArrayList<>();
    ArrayList<Float> bufferY = new ArrayList<>();
    ArrayList<Float> bufferZ = new ArrayList<>();
    public double max = 0;


    public SignalProcessing3DData(String sensor_name, int sensor_number, double sampling_rate) {
        super(sensor_name, sensor_number, sampling_rate);
        Magxyz = new ArrayList<>();
        Filtxyz = new ArrayList<>();
    }

    public float[] Mag3D(float[] stream) {
        float[] magArray = new float[stream.length/3];
        if (stream.length % 3 == 0) {
            for (int i = 0; i < stream.length; i = i + 3) {
                Magxyz.add((float) (Math.sqrt(Math.pow(stream[i], 2) + Math.pow(stream[i + 1], 2) + Math.pow(stream[i + 2], 2)) - .975));
            }
            for (int j = 0; j < Magxyz.size(); j++){
                magArray[j] = Magxyz.get(j);
            }
            Magxyz.clear();
        } else {
            Log.v(TAG, "Invalid stream length");
        }
        return magArray;
    }

    //    public double MovingAvgFilt(int windowSize,ArrayList<Double>input){
//        double total = 0;
//        double[] avg = new double[3];
//        if(input.size() >= windowSize) {
//            for (int j = 0; j < 3; j++) {
//                for (int i = windowSize-1; i >= 0; i--) {
//                    total = total + input.get(input.size()-i+j-3);
//
//                }
//                avg[j] = total/windowSize;
//                total=0;
//            }
//            if(Math.max(Math.max(avg[0],avg[1]),avg[2]) > max && input.size()>600){
//                max=Math.max(Math.max(avg[0],avg[1]),avg[2]);
//            }
//            Filtxyz.add(avg[0]);
//            Filtxyz.add(avg[1]);
//            Filtxyz.add(avg[2]);
//        }
//        return max;
//    }
    public float[] MovingAvgFilt3D(int buffSize, float[] input) {

        float[] avg3D = new float[input.length];
        float totalX = 0;
        float totalY = 0;
        float totalZ = 0;
        if (bufferX.size() < buffSize) {
            for (int k = 0; k < buffSize; k++) {
                bufferX.add((float) 0);
                bufferY.add((float) 0);
                bufferZ.add((float) 0);
            }
        } else {
            if (input.length % 3 == 0) {

                for (int i = 0; i < input.length; i = i + 3) {

                    for (int j = buffSize - 1; j > 0; j--) {

                        bufferX.set(j, bufferX.get(j - 1));
                        totalX = totalX + bufferX.get(j);
                        bufferY.set(j, bufferY.get(j - 1));
                        totalY = totalY + bufferY.get(j);
                        bufferZ.set(j, bufferZ.get(j - 1));
                        totalZ = totalZ + bufferZ.get(j);

                    }
                    bufferX.set(0, input[i]);
                    totalX = totalX + bufferX.get(0);
                    avg3D[i] = totalX / buffSize;

                    bufferY.set(0, input[i + 1]);
                    totalY = totalY + bufferY.get(0);
                    avg3D[i + 1] = totalY / buffSize;

                    bufferZ.set(0, input[i + 2]);
                    totalZ = totalZ + bufferZ.get(0);
                    avg3D[i + 2] = totalZ / buffSize;

                    totalX = totalY = totalZ = 0;
                }
            } else {
                Log.v(TAG, "Invalid stream length");
            }
        }


        return avg3D;

    }

    public double Max3D(float[] stream) {
        for(int j = 0; j < stream.length; j++){
            if(stream[j] > max){
                max = stream[j];
            }
        }
        return max;
    }
}

