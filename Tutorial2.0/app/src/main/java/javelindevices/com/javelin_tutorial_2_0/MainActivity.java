package javelindevices.com.javelin_tutorial_2_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.javelindevices.javelinsdk.JavelinSensorManager;
import com.javelindevices.javelinsdk.model.ISensor;
import com.javelindevices.javelinsdk.model.ISensorManager;
import com.javelindevices.javelinsdk.model.JavelinSensorEvent;
import com.javelindevices.javelinsdk.model.JavelinSettings;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "Javelin Tutorial 1.0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String mDeviceAddress;
    public static ArrayList<String> mDeviceAddresses = new ArrayList<String>();
    public static ArrayList<JavelinSensorManager> mSensorManagers = new ArrayList<JavelinSensorManager>();
    public static ArrayList<JavelinDeviceAdapter> mDeviceAdapters = new ArrayList<JavelinDeviceAdapter>();

    private ToggleButton vibrator;
    private ToggleButton led_button;
    private TextView xText,yText,zText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        mDeviceAddresses = (ArrayList<String>) intent.getSerializableExtra(EXTRAS_DEVICE_ADDRESS);
        for(int i=0;i<mDeviceAddresses.size();i++)
        {
            /*** Sensor Management**/
            mSensorManagers.add(new JavelinSensorManager(this, mDeviceAddresses.get(i)));
            mDeviceAdapters.add(new JavelinDeviceAdapter(mDeviceAddresses.get(i)));
            mSensorManagers.get(i).setListener(mDeviceAdapters.get(i));
            mSensorManagers.get(i).enable();
        }




        setContentView(R.layout.activity_main);


        /*** Assign Buttons ***/

        vibrator = (ToggleButton)findViewById(R.id.vibrator_toggle);
        vibrator.setClickable(true);
        vibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i< mSensorManagers.size();i++){
                    mSensorManagers.get(i).vibrationEnable(vibrator.isChecked());
                }
            }
        });

        led_button = (ToggleButton)findViewById(R.id.led_toggle);
        led_button.setClickable(true);
        led_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i< mSensorManagers.size();i++){
                    mSensorManagers.get(i).setLedBlinkType(JavelinSettings.LED_TYPE_SOLID);
                    mSensorManagers.get(i).ledEnable(led_button.isChecked());
                }
            }
        });

        /*** Assign TextView ***/
        xText = (TextView)findViewById(R.id.x_acc);
        yText = (TextView)findViewById(R.id.y_acc);
        zText = (TextView)findViewById(R.id.z_acc);
    }



    @Override
    public void onResume(){
        super.onResume();
        //** Register Listener for each Javelin Service **//
        for(int i=0;i< mSensorManagers.size();i++){
            mSensorManagers.get(i).registerListener(mDeviceAdapters.get(i), ISensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    public void onPause(){
        //** Register Listener for each Javelin Service **//
        for(int i=0;i< mSensorManagers.size();i++){
            mSensorManagers.get(i).unregisterListener(mDeviceAdapters.get(i), ISensor.TYPE_ACCELEROMETER);
        }
        super.onPause();
    }

    @Override
    public void onStop(){
        for(int i=0;i< mSensorManagers.size();i++){
            mSensorManagers.get(i).disable();
        }
        super.onStop();
    }

    class JavelinDeviceAdapter implements ISensorManager.JavelinEventListener
    {
        private String deviceAddress;

        public JavelinDeviceAdapter(String deviceAddress){
            this.deviceAddress = deviceAddress;
        }
        public String getDeviceAddress(){
            return deviceAddress;
        }

        /***
         * JavelinEventListener Methods
         ***/
        @Override
        public void onSensorManagerConnected() {
            // for(int i=0;i< mSensorManagers.size();i++){
            int i = mDeviceAddresses.indexOf(deviceAddress);
            Log.v(TAG, "Successfully connected to Javelin: " + deviceAddress);
            mSensorManagers.get(i).setLedBlinkNumber(5);
            mSensorManagers.get(i).setLedBlinkRate(75);
            mSensorManagers.get(i).setLedIntensity(75);
            mSensorManagers.get(i).ledEnable(true);
            // }
        }

        @Override
        public void onSensorManagerDisconnected() {
            Log.d(TAG, "Javelin device disconnected!");
        }

        @Override
        public void onReadRemoteRssi(int rssi) {
        }

        @Override
        public void onSensorChanged(JavelinSensorEvent event) {

            float[] values = event.values;
            //Log.v(TAG, "Sensor changed.\nSensor: " + event.sensor + "\nValues: " + values.length + "\nValue: " + values[0] +" "+ values[3]+ " "+ values[6]);
            int i = mDeviceAddresses.indexOf(deviceAddress);
            switch (event.sensor) {
                case ISensor.TYPE_ACCELEROMETER: //1
                    //handle accelerometer data here
                    float x = values[0]; // Do something with it
                    float y = values[1]; // Do something with it
                    float z = values[2]; // Do something with it
                    if(i==0)
                        xText.setText("X: " + event.values[0]/2 + " g");
                    else if(i==1){
                        yText.setText("Y: " + event.values[1]/2 + " g");
                        zText.setText("Z: " + event.values[2]/2 + " g");
                    }
    //                xText.setText("X: " + event.values[3]/2 + " g");
    //                xText.setText("X: " + event.values[6]/2 + " g");
    //                yText.setText("Y: " + event.values[4]/2 + " g");
    //                yText.setText("Y: " + event.values[7]/2 + " g");
    //                zText.setText("Z: " + event.values[5]/2 + " g");
    //                zText.setText("Z: " + event.values[8]/2 + " g");
                    break;
    //            case ISensor.TYPE_MAGNETIC_FIELD: //2
    //                magneticFieldData.parseStream(values);
    //                break;
    //            case ISensor.TYPE_GYROSCOPE: //4
    //                gyroscopeData.parseStream(values);
    //                break;
    //            case ISensor.TYPE_AUDIO_ENERGY: //202
    //                audioEnergyData.parseStream(values);
    //                break;
    //            case ISensor.TYPE_TEMPERATURE:
    //                temperatureData.parseStream(values);
    //                break;
    //            case ISensor.TYPE_BATTERY:
    //                break;
            }
        }
    }
}