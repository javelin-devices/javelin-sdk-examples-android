package com.javelindevices.javelinexamples.AppFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.javelindevices.javelinexamples.R;
import com.javelindevices.javelinexamples.ScreenSlideActivity;
import com.javelindevices.javelinexamples.Util.ScreenSlidePageFragment;
import com.javelindevices.javelinsdk.JavelinSensorManager;

import com.javelindevices.javelinsdk.model.ISensorManager;
import com.javelindevices.javelinsdk.model.JavelinSensorEvent;
import com.javelindevices.javelinsdk.model.JavelinSettings;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class LEDVibrationFragment extends ScreenSlidePageFragment implements ISensorManager.JavelinEventListener {
    @Bind(R.id.toggleButtonVib)
    ToggleButton buttonVib;
    @Bind(R.id.toggleButtonLED)
    ToggleButton buttonLED;

    @Bind(R.id.seekBarFrequencyLED)
    SeekBar seekBarFreqLED;
    @Bind(R.id.seekBarIntensityLED)
    SeekBar seekBarIntensityLED;

    @Bind(R.id.seekBarFrequencyVib)
    SeekBar seekBarFreqVib;
    @Bind(R.id.seekBarIntensityVib)
    SeekBar seekBarIntensityVib;

    @Bind(R.id.spinnerLEDType)
    Spinner spinnerLEDType;
    @Bind(R.id.spinnerVibType)
    Spinner spinnerVibType;

    @Bind(R.id.editLedRepeat)
    EditText ledRepeatText;

    @Bind(R.id.editVibRepeat)
    EditText vibRepeatText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_led_vib, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    int[] vibTypes = {JavelinSettings.VIB_TYPE_PULSE, JavelinSettings.VIB_TYPE_SAWTOOTH, JavelinSettings.VIB_TYPE_SOLID, JavelinSettings.VIB_TYPE_TRIANGLE};
    int[] ledTypes = {JavelinSettings.LED_TYPE_PULSE, JavelinSettings.LED_TYPE_SAWTOOTH, JavelinSettings.LED_TYPE_SOLID, JavelinSettings.LED_TYPE_TRIANGLE};

    String[] typeStringArray = {"Pulse", "Sawtooth", "Solid", "Triangle"};

    JavelinSensorManager mSensorManager;
    @Override
    public void onViewCreated(View view, Bundle savedinstancestate) {
        mSensorManager = new JavelinSensorManager(getActivity(), ScreenSlideActivity.mDeviceAddress);
        mSensorManager.setListener(this);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        Collections.addAll(spinnerArray, typeStringArray);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLEDType.setAdapter(spinnerArrayAdapter);
        spinnerVibType.setAdapter(spinnerArrayAdapter);


        seekBarFreqLED.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // LED rate is in .1HZ. To make it vibrate once a second we need to send in a value of 10.
                // The range of values we can send are from 1-255

                // clamp 1 to 4 hz
                int convertedValue = (int)(progress / 100.0 * 4);
                if(convertedValue > 0) {
                    Log.d(TAG, "led freq: " + convertedValue + " Hz");
                    mSensorManager.setLedBlinkRate(convertedValue * 10); //convert to .1HZ values
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarFreqVib.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // clamp 1 to 4 hz
                int convertedValue = (int)(progress / 100.0 * 4);
                if(convertedValue > 0) {
                    Log.d(TAG, "led freq: " + convertedValue + " Hz");
                    // Multiply the frequency we want by 10 since the SDK reads the value as ".1HZ" increments
                    mSensorManager.setVibrationRate(convertedValue * 10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarIntensityLED.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSensorManager.setLedIntensity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarIntensityVib.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSensorManager.setVibrationIntensity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.toggleButtonLED)
    public void ledToggle() {
        mSensorManager.setLedBlinkNumber(Integer.parseInt(ledRepeatText.getText().toString()));
        mSensorManager.ledEnable(buttonLED.isChecked());
    }

    @OnClick(R.id.toggleButtonVib)
    public void vibToggle() {
        mSensorManager.setVibrationRepeatNumber(Integer.parseInt(vibRepeatText.getText().toString()));
        mSensorManager.vibrationEnable(buttonVib.isChecked());
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.disable();
    }

    public void onResume() {
        super.onResume();
        mSensorManager.enable();
    }

    public static String TAG = "LEDVibrationFrag";
    @OnItemSelected(R.id.spinnerVibType)
    public void onVibTypeSelected(int position) {
        mSensorManager.setVibrationType(vibTypes[position]);
    }

    @OnItemSelected(R.id.spinnerLEDType)
    public void onLEDTypeSelected(int position) {
        mSensorManager.setLedBlinkType(ledTypes[position]);
    }



    @Override
    public void onSensorChanged(JavelinSensorEvent event) {

    }

    @Override
    public void onSensorManagerConnected() {

    }

    @Override
    public void onSensorManagerDisconnected() {

    }

    @Override
    public void onReadRemoteRssi(int rssi) {

    }
}
