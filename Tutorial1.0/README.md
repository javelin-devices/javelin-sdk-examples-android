## Javelin Tutorial 1.0
This tutorial covers toggling buttons for the LED and Vibrator as well as displaying the raw data from the X, Y, and Z components of the accelerometer. The tutorial is built off of the Javelin [Starter Code](https://github.com/javelin-devices/javelin-starter-code).

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
