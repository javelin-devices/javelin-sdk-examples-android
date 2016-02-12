## Javelin Tutorial 1.0
This tutorial covers toggling buttons for the LED and Vibrator as well as displaying the raw data from the X, Y, and Z components of the accelerometer. The tutorial is built off of the Javelin [Starter Code](https://github.com/javelin-devices/javelin-starter-code) and you can access the [API javadocs here](http://www.javelindevices.com/javelin_docs/index.html).

## Layout 
Let's begin by creating the buttons and display for the raw accelerometer data in the activity_main.xml file in the layout folder. There are two tabs under the activity_main.xml file called "Design" and "Text". The Design tab allows you to drag and drop buttons and specify other features onto an emulated screen similar to what will be shown on your phone whereas the Text tab shows the actual XML code that is being generated from the Design tab. In this tutorial, we are using the RelativeLayout which enables you to specify how child views are positioned relative to each other. The position of each view can be specified as relative to sibling elements or relative to the parent. Inside the RelativeLayout we have two toggle buttons used for turning the LED and Vibrator on and off. The XML code is shown below:

    <ToggleButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Vibrator"
        android:id="@+id/vibrator_toggle"
        android:enabled="true"
        android:textOff="Vibrator: Off"
        android:textOn="Vibrator: On"
        android:checked="false"
        android:clickable="true" />

    <ToggleButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="LED"
        android:id="@+id/led_toggle"
        android:clickable="true"
        android:enabled="true"
        android:layout_below="@+id/vibrator_toggle"
        android:layout_alignParentStart="true"
        android:textOff="LED: Off"
        android:textOn="LED: On" />
        
  We use TextViews in order to display the X,Y, and Z data from the accelerometer. The XML code is shown below:

    <EditText
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/x_acc"
        android:layout_below="@+id/led_toggle"
        android:layout_alignParentStart="true"
        android:layout_marginTop="41dp"
        android:text="X:"
        android:enabled="true"
        android:editable="false" />

    <EditText
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/y_acc"
        android:layout_below="@+id/x_acc"
        android:layout_alignEnd="@+id/x_acc"
        android:enabled="true"
        android:text="Y:" />

    <EditText
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/z_acc"
        android:enabled="true"
        android:text="Z:"
        android:layout_below="@+id/y_acc"
        android:layout_alignParentStart="true" />

## MainActivity
After the Layout has been finished we need to specify what actually happens when the buttons are clicked or what data shows up in the TextView's. This will be done in the java file MainActivity under the java folder in the project. First we create reference variables for the ToggleButton's and TextViews we have created:

    private ToggleButton vibrator;
    private ToggleButton led_button;
    private TextView xText,yText,zText;

In the OnCreate() method, we reference the variables to the ToggleButton and TextView widgets we have created in the activity_main.xml file using the findViewById method. 

    vibrator = (ToggleButton)findViewById(R.id.vibrator_toggle);
    led_button = (ToggleButton)findViewById(R.id.led_toggle);
    xText = (TextView)findViewById(R.id.x_acc);
    yText = (TextView)findViewById(R.id.y_acc);
    zText = (TextView)findViewById(R.id.z_acc);
    
After making the ToggleButton's clickable, we specify what happens when each ToggleButton is clicked. In this case, want to turn on the Vibrator and LED. We do this by specifying the vibration and blink type to solid and enabling each feature. 

    vibrator.setClickable(true);
        vibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.setVibrationType(JavelinSettings.VIB_TYPE_SOLID);
                mSensorManager.vibrationEnable(vibrator.isChecked());
            }
        });
        
    led_button.setClickable(true);
        led_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.setLedBlinkType(JavelinSettings.LED_TYPE_SOLID);
                mSensorManager.ledEnable(led_button.isChecked());
            }
        });

In order to display the accelerometer data, we go to the onSensorChanged() method where each specific sensor event, in this case the accelerometer, is detected and a switch case statement is used to specify what happens to do the raw data from the sensor. The length and contents of the values array depends on which sensor was returned. For the accelerometer, the values[] array length is 9 which stores 3 data values for each axis in the array. This data can then be displayed in the TextViews with the setText() method. 

    public void onSensorChanged(JavelinSensorEvent event) {
        float[] values = event.values;
        switch (event.sensor) {
            case ISensor.TYPE_ACCELEROMETER: //1
                //handle accelerometer data here
                float x = values[0]; // Do something with it
                float y = values[1]; // Do something with it
                float z = values[2]; // Do something with it
                xText.setText("X: " + event.values[0]/2 + " g");
                xText.setText("X: " + event.values[3]/2 + " g");
                xText.setText("X: " + event.values[6]/2 + " g");
                yText.setText("Y: " + event.values[1]/2 + " g");
                yText.setText("Y: " + event.values[4]/2 + " g");
                yText.setText("Y: " + event.values[7]/2 + " g");
                zText.setText("Z: " + event.values[2]/2 + " g");
                zText.setText("Z: " + event.values[5]/2 + " g");
                zText.setText("Z: " + event.values[8]/2 + " g");
                break;

...

You should now be able to run your application on your android device and display accelerometer data in the X,Y, and Z axis and toggle on and off the Vibrator and LED from the Javelin 1. Be sure to download the Tutorial 1.0 code which is ready to run on your device if you are having trouble.
