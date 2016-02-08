package com.javelindevices.javelinexamples.AppFragments;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.javelindevices.javelinexamples.R;
import com.javelindevices.javelinexamples.ScreenSlideActivity;
import com.javelindevices.javelinexamples.Util.ScreenSlidePageFragment;
import com.javelindevices.javelinsdk.JavelinSensorManager;
import com.javelindevices.javelinsdk.model.ISensor;
import com.javelindevices.javelinsdk.model.ISensorManager;
import com.javelindevices.javelinsdk.model.JavelinSensorEvent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;


public class GraphFragment extends ScreenSlidePageFragment implements
        OnChartValueSelectedListener, ISensorManager.JavelinEventListener {

    public static final String TAG = "GraphFragment";
    private int sensorSelected = ISensor.TYPE_ACCELEROMETER;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mSensorManager != null) {
            if (isVisibleToUser) {
                mSensorManager.enable();
                mSensorManager.setAcceleromGyroRate(ACC_GYRO_RATE);
                mSensorManager.registerListener(this, sensorSelected);
            } else {
                mSensorManager.unregisterAll();
                mSensorManager.disable();
            }
        }
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterAll();
        mSensorManager.disable();
        super.onPause();
    }

    LineChart mChart;
    private LineData mLineData;

    @Bind(R.id.spinner)
    Spinner mSpinnerChartType;

    private int maxEntries;
    private final int ACC_GYRO_RATE = 60;

    JavelinSensorManager mSensorManager;

    @Override
    public void onViewCreated(View view, Bundle savedinstancestate) {
        // Javelin setup
        mSensorManager = new JavelinSensorManager(getActivity(), ScreenSlideActivity.mDeviceAddress);
        mSensorManager.setListener(this);
        mSensorManager.registerListener(this, ISensor.TYPE_ACCELEROMETER);


        maxEntries = (int) (ACC_GYRO_RATE * 1.5);

        // Create the chart spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.chart_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChartType.setAdapter(adapter);

        mChart = (LineChart) view.findViewById(R.id.linechart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        initLineData();

        // add empty data (Accelerometer by default)
        mChart.setData(mLineData);
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        //   xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(false);


        YAxis leftAxis = mChart.getAxisLeft();
        //   leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(10000f); //100
        leftAxis.setAxisMinValue(-10000f); //0
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    @SuppressWarnings("unused")
    @OnItemSelected(R.id.spinner)
    public void onChartSelected(int position) {
        switchData(); //switches sensor
    }

    private void switchData() {
        switch (mSpinnerChartType.getSelectedItemPosition()) {

            case 0: {
                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setAxisMaxValue(2);
                leftAxis.setAxisMinValue(-2);
                mSensorManager.unregisterAll();
                mSensorManager.registerListener(this, ISensor.TYPE_ACCELEROMETER);
                sensorSelected = ISensor.TYPE_ACCELEROMETER;
                break;
            }
            case 1: {
                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setAxisMaxValue(2000);
                leftAxis.setAxisMinValue(-2000);
                mSensorManager.unregisterAll();
                mSensorManager.registerListener(this, ISensor.TYPE_GYROSCOPE);
                sensorSelected = ISensor.TYPE_GYROSCOPE;
                break;
            }
            case 2: {
                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setAxisMaxValue(5000);
                leftAxis.setAxisMinValue(-5000);
                mSensorManager.unregisterAll();
                mSensorManager.registerListener(this, ISensor.TYPE_MAGNETIC_FIELD);
                sensorSelected = ISensor.TYPE_MAGNETIC_FIELD;
                break;
            }
            case 3: {
                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setAxisMaxValue(5000f); //100
                leftAxis.setAxisMinValue(0000f); //0
                mSensorManager.unregisterAll();
                mSensorManager.registerListener(this, ISensor.TYPE_AUDIO_ENERGY);
                sensorSelected = ISensor.TYPE_AUDIO_ENERGY;
                break;
            }
            default: {

            }
        }
    }

    private void addEntry(float[] values) {

        LineData data = mChart.getData();

        if (data != null) {
            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well
            // add a new x-value first
            data.addXValue("" + values[0]);
            int index = data.getXValCount();
            data.addEntry(new Entry(values[0], index), 0);// set.getEntryCount()), 0);
            data.addEntry(new Entry(values[1], index), 1);
            data.addEntry(new Entry(values[2], index), 2);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(maxEntries); //120

            // move to the latest entry
            mChart.moveViewToX(data.getXValCount() - (maxEntries + 1));
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    private void initLineData() {
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(new LineDataSet(new ArrayList<Entry>(),"x"));
        lineDataSets.add(new LineDataSet(new ArrayList<Entry>(),"y"));
        lineDataSets.add(new LineDataSet(new ArrayList<Entry>(),"z"));
        lineDataSets.add(new LineDataSet(new ArrayList<Entry>(), "db"));

        initLineDataSet(lineDataSets.get(0), Color.BLUE);
        initLineDataSet(lineDataSets.get(1),Color.GREEN);
        initLineDataSet(lineDataSets.get(2),Color.RED);
        initLineDataSet(lineDataSets.get(3),Color.BLACK);

        mLineData = new LineData(new ArrayList<String>(),lineDataSets);
    }


    private void initLineDataSet(LineDataSet lineDataSet,int color) {
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);

        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setColor(color);
        lineDataSet.setHighLightColor(Color.GRAY);
    }

    @Override
    public void onSensorChanged(JavelinSensorEvent event) {
        float[] values = event.values;
        int spinnerPos = mSpinnerChartType.getSelectedItemPosition();
        switch (event.sensor) {
            case ISensor.TYPE_ACCELEROMETER:
                if(spinnerPos == 0)
                    addEntry(values);
                break;
            case ISensor.TYPE_GYROSCOPE:
                if(spinnerPos == 1)
                    addEntry(values);
                break;
            case ISensor.TYPE_MAGNETIC_FIELD:
                if(spinnerPos == 2) {
                    addEntry(values);
                }
                break;
            case ISensor.TYPE_AUDIO_ENERGY: { //max val = 2^12.15
                Log.d(TAG, "rECEIVED AUDIO ENERGY " + values[0]);
                if(spinnerPos == 3) {
                    Log.d(TAG, "spinner pos 3");
                    LineData data = mChart.getData();
                    LineDataSet set = data.getDataSetByIndex(3);
                    // set.addEntry(...); // can be called as well
                    int index = data.getXValCount();

                    // add new x values with each sample.
                    // An audio energy array will have 5 samples.
                    for(int i = 0; i < 5; i++) {
                        data.addXValue("" + values[i]);
                        data.addEntry(new Entry(values[i], index + i), 3);
                    }
                    mChart.notifyDataSetChanged();

                    // limit the number of visible entries
                    mChart.setVisibleXRangeMaximum(maxEntries);
                    // move to the latest entry
                    mChart.moveViewToX(data.getXValCount() - (maxEntries + 5));
                }
                break;
            }
        }
    }

    @Override
    public void onSensorManagerConnected() {
    }

    @Override
    public void onSensorManagerDisconnected() {
        Log.d(TAG, "Javelin device disconnected!");
    }

    @Override
    public void onReadRemoteRssi(int rssi) {
    }
}
