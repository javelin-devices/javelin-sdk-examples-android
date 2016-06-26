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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "Javelin Tutorial 1.0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String mDeviceAddress;
    public static ArrayList<String> mDeviceAddresses = new ArrayList<String>();
    public static ArrayList<JavelinSensorManager> mSensorManagers = new ArrayList<JavelinSensorManager>();
    public static ArrayList<JavelinDeviceAdapter> mDeviceAdapters = new ArrayList<JavelinDeviceAdapter>();

    private ToggleButton vibrator1, vibrator2, vibrator3, led_button1, led_button2, led_button3;
    private TextView xText1,yText1,zText1,xText2,yText2,zText2,xText3,yText3,zText3;


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

        vibrator1 = (ToggleButton)findViewById(R.id.vibrator_toggle1);
        vibrator1.setClickable(true);
        vibrator1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mSensorManagers.get(0).vibrationEnable(vibrator1.isChecked());
            }
        });

        vibrator2 = (ToggleButton)findViewById(R.id.vibrator_toggle2);
        vibrator2.setClickable(true);
        vibrator2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSensorManagers.size()>=2){
                    mSensorManagers.get(1).vibrationEnable(vibrator2.isChecked());
                }
            }
        });

        vibrator3 = (ToggleButton)findViewById(R.id.vibrator_toggle3);
        vibrator3.setClickable(true);
        vibrator3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSensorManagers.size()>=3){
                    for(int i=2;i< mSensorManagers.size();i++){
                        mSensorManagers.get(i).vibrationEnable(vibrator3.isChecked());
                    }
                }
            }
        });


        led_button1 = (ToggleButton)findViewById(R.id.led_toggle1);
        led_button1.setClickable(true);
        led_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mSensorManagers.get(0).setLedBlinkType(JavelinSettings.LED_TYPE_SOLID);
                    mSensorManagers.get(0).ledEnable(led_button1.isChecked());
            }
        });

        led_button2 = (ToggleButton)findViewById(R.id.led_toggle2);
        led_button2.setClickable(true);
        led_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSensorManagers.size()>=2){
                    mSensorManagers.get(1).setLedBlinkType(JavelinSettings.LED_TYPE_SOLID);
                    mSensorManagers.get(1).ledEnable(led_button2.isChecked());
                }
            }
        });

        led_button3 = (ToggleButton)findViewById(R.id.led_toggle3);
        led_button3.setClickable(true);
        led_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSensorManagers.size()>=3)
                for(int i=2;i< mSensorManagers.size();i++){
                    mSensorManagers.get(i).setLedBlinkType(JavelinSettings.LED_TYPE_SOLID);
                    mSensorManagers.get(i).ledEnable(led_button3.isChecked());
                }
            }
        });

        /*** Assign TextView ***/
        xText1 = (TextView)findViewById(R.id.x_acc1);
        yText1 = (TextView)findViewById(R.id.y_acc1);
        zText1 = (TextView)findViewById(R.id.z_acc1);

        xText2 = (TextView)findViewById(R.id.x_acc2);
        yText2 = (TextView)findViewById(R.id.y_acc2);
        zText2 = (TextView)findViewById(R.id.z_acc2);

        xText3 = (TextView)findViewById(R.id.x_acc3);
        yText3 = (TextView)findViewById(R.id.y_acc3);
        zText3 = (TextView)findViewById(R.id.z_acc3);
    }



    // @Override
    // public void onResume(){
    //     super.onResume();
        
    // }

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
        int connectedCount = 0;
        @Override
        public void onSensorManagerConnected() {
            // for(int i=0;i< mSensorManagers.size();i++){
            connectedCount++;
            if(connectedCount==mSensorManagers.size()){
                //** Register Listener for each Javelin Service **//
                for(int i=0;i< mSensorManagers.size();i++){
                    mSensorManagers.get(i).setLedBlinkNumber(5);
                    mSensorManagers.get(i).setLedBlinkRate(75);
                    mSensorManagers.get(i).setLedIntensity(75);
                    mSensorManagers.get(i).ledEnable(true);
                    mSensorManagers.get(i).registerListener(mDeviceAdapters.get(i), ISensor.TYPE_ACCELEROMETER);
                }
                connectedCount = 0;
            }
            Log.v(TAG, "Successfully connected to Javelin: " + deviceAddress);
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
            switch (event.sensor) {
                case ISensor.TYPE_ACCELEROMETER: //1
                    //handle accelerometer data here
                    float x = values[0]; // Do something with it
                    float y = values[1]; // Do something with it
                    float z = values[2]; // Do something with it
                    
                    NumberFormat df = DecimalFormat.getInstance();
                    df.setMaximumFractionDigits(3);
                    df.setMinimumFractionDigits(3);
                    
                    int i = mDeviceAddresses.indexOf(event.deviceAddress);

                    if(i==0) {
                        xText1.setText("X: " + df.format(event.values[0]) + " g");
                        yText1.setText("Y: " + df.format(event.values[1]) + " g");
                        zText1.setText("Z: " + df.format(event.values[2]) + " g");
                    }
                    else if(i==1){
                        xText2.setText("X: " + df.format(event.values[0]) + " g");
                        yText2.setText("Y: " + df.format(event.values[1]) + " g");
                        zText2.setText("Z: " + df.format(event.values[2]) + " g");
                    }
                    else if(i==2)
                    {
                        xText3.setText("X: " + df.format(event.values[0]) + " g");
                        yText3.setText("Y: " + df.format(event.values[1]) + " g");
                        zText3.setText("Z: " + df.format(event.values[2]) + " g");
                    }

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