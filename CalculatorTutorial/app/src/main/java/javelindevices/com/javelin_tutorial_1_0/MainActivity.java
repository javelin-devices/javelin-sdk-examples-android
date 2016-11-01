package javelindevices.com.javelin_tutorial_1_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.javelindevices.javelinsdk.JavelinSensorManager;
import com.javelindevices.javelinsdk.model.ISensor;
import com.javelindevices.javelinsdk.model.ISensorManager;
import com.javelindevices.javelinsdk.model.JavelinSensorEvent;
import com.javelindevices.javelinsdk.model.JavelinSettings;


public class MainActivity extends AppCompatActivity implements ISensorManager.JavelinEventListener {

    private final String TAG = "Javelin Tutorial 1.0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String mDeviceAddress;
    JavelinSensorManager mSensorManager;
    HelloSum Hello;

    private ToggleButton vibrator;
    private ToggleButton led_button;
    private TextView xText,yText,zText;

    private TextView display,numAdd,sum;

    private EditText num1,num2;
    int number1;
    int number2;

    HelloWorldSum Orion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        /*** Sensor Management**/
        mSensorManager = new JavelinSensorManager(this, mDeviceAddress);
        mSensorManager.setListener(this);
        mSensorManager.enable();

        Hello = new HelloSum();

        Orion = new HelloWorldSum();

        setContentView(R.layout.activity_main);


        /*** Assign Buttons ***/

        vibrator = (ToggleButton)findViewById(R.id.vibrator_toggle);
        vibrator.setClickable(true);
        vibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                number1 = Integer.parseInt(num1.getText().toString());
                number2 = Integer.parseInt(num2.getText().toString());
                sum.setText((String.format("Sum = %d  ", Hello.Sum(number1,number2))));

            }
        });

        /*** Assign TextView ***/
        xText = (TextView)findViewById(R.id.x_acc);
        yText = (TextView)findViewById(R.id.y_acc);
        zText = (TextView)findViewById(R.id.z_acc);

        display =  (TextView)findViewById(R.id.hello);
        num1 = (EditText)findViewById(R.id.Num1);
        num2 = (EditText)findViewById(R.id.Num2);
        sum =  (TextView)findViewById(R.id.totSum);
        numAdd =  (TextView)findViewById(R.id.numAdd);

    }


    /***
     * JavelinEventListener Methods
     ***/
    @Override
    public void onSensorManagerConnected() {
        Log.v(TAG, "Successfully connected to Javelin: " + mDeviceAddress);
        mSensorManager.setLedBlinkNumber(5);
        mSensorManager.setLedBlinkRate(75);
        mSensorManager.setLedIntensity(75);
        mSensorManager.ledEnable(true);

        //xText.setText(String.format(" Sum:  %d  ", Hello.Sum_1_to_N(5)));
        //yText.setText(String.format(" Text: %s  ", Hello.HelloWorld()));
        number1 = Integer.parseInt(num1.getText().toString());
        number2 = Integer.parseInt(num2.getText().toString());
        sum.setText((String.format("Sum = %d  ", Hello.Sum(number1,number2))));

        display.setText(String.format(" Text: %s ", Orion.HelloWorld()));
        mSensorManager.setAccelerometerFullScaleRange(4);



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
//                float x = values[0]; // Do something with it
//                float y = values[1]; // Do something with it
//                float z = values[2]; // Do something with it
//                xText.setText(String.format("X:  %.4f g ", event.values[0]));
//                xText.setText(String.format("X:  %.4f g ", event.values[3]));
//                xText.setText(String.format("X:  %.4f g ", event.values[6]));
//                yText.setText(String.format("Y:  %.4f g ", event.values[1]));
//                yText.setText(String.format("Y:  %.4f g ", event.values[4]));
//                yText.setText(String.format("Y:  %.4f g ", event.values[7]));
//                zText.setText(String.format("Z:  %.4f g ", event.values[2]));
//                zText.setText(String.format("Z:  %.4f g ", event.values[5]));
//                zText.setText(String.format("Z:  %.4f g ", event.values[8]));

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

    @Override
    public void onResume(){
        super.onResume();
        //** Register Listener for each Javelin Service **//
        mSensorManager.registerListener(this, ISensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(this,ISensor.TYPE_AUDIO_ENERGY);
//        mSensorManager.registerListener(this,ISensor.TYPE_GYROSCOPE);
//        mSensorManager.registerListener(this,ISensor.TYPE_MAGNETIC_FIELD);
//        mSensorManager.registerListener(this,ISensor.TYPE_TEMPERATURE);
//        mSensorManager.registerListener(this,ISensor.TYPE_BATTERY);

    }

    @Override
    public void onPause(){
        //** Register Listener for each Javelin Service **//
        mSensorManager.unregisterListener(this, ISensor.TYPE_ACCELEROMETER);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_AUDIO_ENERGY);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_GYROSCOPE);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_MAGNETIC_FIELD);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_TEMPERATURE);
//        mSensorManager.unregisterListener(this, ISensor.TYPE_BATTERY);

        super.onPause();
    }

    @Override
    public void onStop(){
        mSensorManager.disable();
        super.onStop();
    }


}