package javelindevices.com.javelin_tutorial_3_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.javelindevices.javelinsdk.JavelinSensorManager;
import com.javelindevices.javelinsdk.model.ISensor;
import com.javelindevices.javelinsdk.model.ISensorManager;
import com.javelindevices.javelinsdk.model.JavelinSensorEvent;
import com.javelindevices.javelinsdk.model.JavelinSettings;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javelindevices.com.javelin_tutorial_3_0.DataProcessing.Sensor1DData;
import javelindevices.com.javelin_tutorial_3_0.DataProcessing.Sensor3DData;
import javelindevices.com.javelin_tutorial_3_0.DataProcessing.SignalProcessing1DData;
import javelindevices.com.javelin_tutorial_3_0.DataProcessing.SignalProcessing3DData;


public class MainActivity extends AppCompatActivity implements ISensorManager.JavelinEventListener {

    private final String TAG = "Javelin Tutorial 3.0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String mDeviceAddress;
    JavelinSensorManager mSensorManager;

    private ToggleButton vibrator;
    private ToggleButton led_button;
    private Button max_acc_reset;
    private TextView xText,yText,zText,batteryText,max_accText;

    Sensor3DData accelerometerData;
    Sensor3DData gyroscopeData;
    Sensor1DData audioEnergyData;
    Sensor1DData batteryData;
    SignalProcessing3DData processedaccelerometerData;
    SignalProcessing1DData maxaccelerometerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        /*** Sensor Management**/
        mSensorManager = new JavelinSensorManager(this, mDeviceAddress);
        mSensorManager.setListener(this);
        mSensorManager.enable();


        setContentView(R.layout.activity_main);


        /*** Assign Buttons ***/

        vibrator = (ToggleButton)findViewById(R.id.vibrator_toggle);
        vibrator.setClickable(true);
        vibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.setVibrationType(JavelinSettings.VIB_TYPE_SOLID);
                mSensorManager.vibrationEnable(vibrator.isChecked());
            }
        });

        led_button = (ToggleButton)findViewById(R.id.led_toggle);
        led_button.setClickable(true);
        led_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.setLedBlinkType(JavelinSettings.LED_TYPE_SOLID);
                mSensorManager.ledEnable(led_button.isChecked());
            }
        });

        max_acc_reset = (Button)findViewById(R.id.max_acc_reset);
        max_acc_reset.setClickable(true);
        max_acc_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processedaccelerometerData.max=0;
            }
        });

        /*** Assign TextView ***/
//        xText = (TextView)findViewById(R.id.x_acc);
//        yText = (TextView)findViewById(R.id.y_acc);
//        zText = (TextView)findViewById(R.id.z_acc);
        //batteryText = (TextView)findViewById(R.id.batterylevel);
        max_accText = (TextView)findViewById(R.id.max_acc);
    }


    /***
     * JavelinEventListener Methods
     ***/
    @Override
    public void onSensorManagerConnected() {
        accelerometerData = new Sensor3DData("Accelerometer", ISensor.TYPE_ACCELEROMETER, 100);
        processedaccelerometerData = new SignalProcessing3DData("Accelerometer", ISensor.TYPE_ACCELEROMETER, 100);
        gyroscopeData = new Sensor3DData("Gyroscope", ISensor.TYPE_GYROSCOPE, 100);
        audioEnergyData = new Sensor1DData("Audio Energy",ISensor.TYPE_AUDIO_ENERGY,50);
        batteryData = new Sensor1DData("Battery Level", ISensor.TYPE_BATTERY,.1);

        Log.v(TAG, "Successfully connected to Javelin: " + mDeviceAddress);
        mSensorManager.setLedBlinkNumber(5);
        mSensorManager.setLedBlinkRate(75);
        mSensorManager.setLedIntensity(75);
        mSensorManager.ledEnable(true);

        mSensorManager.setVibrationIntensity(75);
        mSensorManager.setVibrationRate(10);
        mSensorManager.setVibrationType(JavelinSettings.VIB_TYPE_PULSE);
        mSensorManager.setVibrationRepeatNumber(2);
        mSensorManager.vibrationEnable(true);



        mSensorManager.setAccelerometerFullScaleRange(4);


        mSensorManager.registerListener(this, ISensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, ISensor.TYPE_AUDIO_ENERGY);
        mSensorManager.registerListener(this, ISensor.TYPE_GYROSCOPE);
        //mSensorManager.registerListener(this, ISensor.TYPE_MAGNETIC_FIELD);
        //mSensorManager.registerListener(this, ISensor.TYPE_TEMPERATURE);
        mSensorManager.registerListener(this, ISensor.TYPE_BATTERY);
        //mSensorManager.registerListener(this, ISensor.TYPE_QUATERNION);

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
            case ISensor.TYPE_ACCELEROMETER:
                //accelerometerData.parseStream(values);
                processedaccelerometerData.parseStream(values);
                max_accText.setText(String.format("Max Acceleration:  %.4f g ", processedaccelerometerData.Max3D(processedaccelerometerData.Mag3D(processedaccelerometerData.MovingAvgFilt3D(20, values)))));

                //max_accText.setText(String.format("Max Acceleration:  %.4f g ", processedaccelerometerData.MovingAvgFilt3D(10, values)));
                //processedaccelerometerData.MovingAvgFilt(10, processedaccelerometerData.Mag3D(values));
//                xText.setText("X: " + event.values[0] + " g");
//                xText.setText("X: " + event.values[3] + " g");
//                xText.setText("X: " + event.values[6] + " g");
//                yText.setText("Y: " + event.values[1] + " g");
//                yText.setText("Y: " + event.values[4] + " g");
//                yText.setText("Y: " + event.values[7] + " g");
//                zText.setText("Z: " + event.values[2] + " g");
//                zText.setText("Z: " + event.values[5] + " g");
//                zText.setText("Z: " + event.values[8] + " g");
                break;
//            case ISensor.TYPE_MAGNETIC_FIELD: //2
//                magneticFieldData.parseStream(values);
//                break;
            case ISensor.TYPE_GYROSCOPE: //4
                gyroscopeData.parseStream(values);
                break;
            case ISensor.TYPE_AUDIO_ENERGY:
                audioEnergyData.parseStream(values);
                break;
//            case ISensor.TYPE_TEMPERATURE:
//                temperatureData.parseStream(values);
//                break;
            case ISensor.TYPE_BATTERY:
                batteryData.parseStream(values);
                //batteryText.setText("Battery Level: " + event.values[0] + " %");
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //** Register Listener for each Javelin Service **//
        mSensorManager.registerListener(this, ISensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,ISensor.TYPE_AUDIO_ENERGY);
        mSensorManager.registerListener(this,ISensor.TYPE_GYROSCOPE);
//        mSensorManager.registerListener(this,ISensor.TYPE_MAGNETIC_FIELD);
//        mSensorManager.registerListener(this,ISensor.TYPE_TEMPERATURE);
        mSensorManager.registerListener(this,ISensor.TYPE_BATTERY);

    }

    @Override
    public void onPause(){
        //** Register Listener for each Javelin Service **//
        mSensorManager.unregisterListener(this, ISensor.TYPE_ACCELEROMETER);
        mSensorManager.unregisterListener(this, ISensor.TYPE_AUDIO_ENERGY);
        mSensorManager.unregisterListener(this, ISensor.TYPE_GYROSCOPE);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_MAGNETIC_FIELD);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_TEMPERATURE);
        mSensorManager.unregisterListener(this, ISensor.TYPE_BATTERY);

        super.onPause();
    }

    @Override
    public void onStop(){
        mSensorManager.disable();
        super.onStop();
    }


}