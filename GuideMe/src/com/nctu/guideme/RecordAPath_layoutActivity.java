package com.nctu.guideme;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RecordAPath_layoutActivity extends BaseActivity implements SensorEventListener{

	/* Declare views in current layout */
	Button orientation_button;
	Button steps_button;
	Button ok_button;
	Button finish_button;
	Button panic_button;

	/* Sensor Variables*/
	SensorManager sensorManager;
	Sensor sensorAccelerometer;
	Sensor sensorOrientation;
	float[] valuesAccelerometer;
	float[] previousAcceleration;
	float[] currentAcceleration;
	float[] cumulativeAcceleration;
	float[] valuesOrientation;
	float[] previousOrientation;
	int sampleCounter;
	
	/*temp variable to chose server in live feedback */
	int fserver=1;
	
	String playIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_a_path);
		
		/* Find id of views */
		orientation_button     = (Button)   findViewById(R.id.orientation_button);
		steps_button           = (Button)   findViewById(R.id.steps_button);
		ok_button              = (Button)   findViewById(R.id.ok_button);
		finish_button          = (Button)   findViewById(R.id.cancel_button);
		panic_button           = (Button)   findViewById(R.id.panic_button);
		
		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* PreferencesManager class*/
		preferences = new PreferenceManager(this, "SettingsFile");
		
		/* Live Feedback */
		if (preferences.GetPreference("live_feedback", 0)==1) {
			
			/* Create the URL */
			String url;
			if (fserver==1)
				url = "http://0160811.bugs3.com/guideme/init_live_h.php";
			else
				url = "http://hyhome.com.tw/hyhome/_lib/init_live_h.php";
				
			/* Send data to external server */
			HttpConnection con = new HttpConnection(url);
				(new Thread(con)).start();
		}
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);

		/* Initialize variables */
		InitializeVariables();
		valuesAccelerometer    = new float[3];
		previousAcceleration   = new float[3];
		currentAcceleration    = new float[3];
		cumulativeAcceleration = new float[3];
		valuesOrientation      = new float[3];
		previousOrientation    = new float[3];

		/* Initialize variable for counting samples */
		sampleCounter=0;
		
		/* Variable to control change of icon */
		playIcon = "play";
		
		/* PreferencesManager class*/
		preferences = new PreferenceManager(this,"SettingsFile");
		
		/* Load fStepValue */
		preferences.SetPreference("stepValue", fStepValue);
		
		/* Sensor Manager */
		sensorManager       = (SensorManager)getSystemService(SENSOR_SERVICE);
	    sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	    sensorOrientation   = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    
		/* Initialize temporal object to store path_d data*/
		paths_d=null;
		paths_d=new ArrayList<Path_d>();
		
		/* Initial message */
		audioInterface=new AudioInterface(getApplicationContext(),"prest_start_to_record_the_path");
	
		/* Start/pause the recording of a path */
		ok_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Press Start */
				if (playIcon.equals("play")) {
					/* Change icon */
					playIcon="pause";
					ok_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.pause), null, null);
				}
				/* Press Pause */
				else {
					/* Change icon */
					playIcon="play";
					ok_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play), null, null);
				}
			}
		});
		
		/* Play the sound help */
		ok_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				
				fserver*=-1;
				
				if (fserver==1) 
					Toast.makeText(getApplicationContext(), "Using FREE SERVERS", Toast.LENGTH_SHORT).show();
				else 
					Toast.makeText(getApplicationContext(), "Using HYHOME", Toast.LENGTH_SHORT).show();
				
				if (playIcon.equals("play"))
					audioInterface=new AudioInterface(getApplicationContext(),"start");
				else 
					audioInterface=new AudioInterface(getApplicationContext(),"pause");
				return true;
			}
		});
		
		/* Finish the recording of a path  */
		finish_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
			
				/* Return to initial layout */
				startActivity(new Intent(getApplicationContext(), SaveAPath_layoutActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		finish_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"finish");
				return true;
			}
		});
		
		/* Execute panic button function */
		panic_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Audio interface */
				audioInterface=new AudioInterface(getApplicationContext(),"panic_button");
				
				/* Make a call to emergency contact */
				switch(v.getId())
				{
					case R.id.panic_button:
						panic.phoneCall(preferences.GetPreference("contactPhone", null));
						break;
					default:
						break;
				}
			}
		});
		
		/* Play the sound help */
		panic_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				
				/* Audio interface */
				audioInterface=new AudioInterface(getApplicationContext(),"panic_message3");
				
				return true;
			}
		});
	}

	@Override
	public void onResume() {

		sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	    sensorManager.registerListener(this, sensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	    
	    super.onResume();
	}

	@Override
	 protected void onPause() {
		
		sensorManager.unregisterListener(this, sensorAccelerometer);
	    sensorManager.unregisterListener(this, sensorOrientation);
   
	    super.onPause();
	 }
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		switch(event.sensor.getType()){

		/* Accelerometer Sensor */
		case Sensor.TYPE_LINEAR_ACCELERATION:
			
			/* read data only when the button start has been pressed */
			if (playIcon.equals("pause")) {

				sampleCounter++;
				
				/* Read data from sensor */
				for(int i =0; i < 3; i++){
					valuesAccelerometer[i] = event.values[i];
				}
				
				/*---------------------------------------------- CUMULATIVE ACCELERATION METHOD USING Y --------------------------------------------------*/
				
				/* Update current and previous accelerations */
				previousAcceleration[1]=currentAcceleration[1];
				previousAcceleration[2]=currentAcceleration[2];

				currentAcceleration[1]=valuesAccelerometer[1];
				currentAcceleration[2]=valuesAccelerometer[2];
				
				/* We accumulate only decreasing acceleration of Y and Z */
				if (currentAcceleration[1]<=previousAcceleration[1]){
					cumulativeAcceleration[1]=cumulativeAcceleration[1]+previousAcceleration[1]-currentAcceleration[1];
				}
				
				if (currentAcceleration[2]<=previousAcceleration[2]){
					cumulativeAcceleration[2]=cumulativeAcceleration[2]+previousAcceleration[2]-currentAcceleration[2];
				}
				
				/* If acceleration is rising then we reset the cumulative acceleration */
				if (Math.abs((cumulativeAcceleration[1]+cumulativeAcceleration[2]))>3){
						
						/* A step detected */
						iStepsCounter++;
						
						/* Resete comulative acceleration */
						cumulativeAcceleration[1]=0;
						cumulativeAcceleration[2]=0;
						
						/*Save data to temporal array*/
						//Path_d path_d=new Path_d(0,0,fDirection[0], fDirection[1], fDirection[2]);
						Path_d path_d=new Path_d(0,0,valuesOrientation[0], 0, 0);
						
						paths_d.add(path_d);
						
						/* Live Feedback */
						if (preferences.GetPreference("live_feedback", 0)==1) {
							
							/* Create the URL */
							String url;
							if (fserver==1)
								url = "http://0160811.bugs3.com/guideme/insert_live_d.php?"
								+ "degree=" + valuesOrientation[0];
							else
								url = "http://www.hyhome.com.tw/hyhome/_lib/insert_live_d.php?"
								+ "degree=" + valuesOrientation[0];
								
							/* Send data to external server */
							HttpConnection con = new HttpConnection(url);
								(new Thread(con)).start();
						}
						
						/* Send data to external server */
						String url2;
						url2 = "http://0160811.bugs3.com/guideme/insert_path_d.php?"
								+ "degree=" + valuesOrientation[0];
						HttpConnection con2 = new HttpConnection(url2);
						(new Thread(con2)).start();
						
				}
					
				if (currentAcceleration[1]>previousAcceleration[1]) {
					cumulativeAcceleration[1]=0;
				}
				
				if (currentAcceleration[2]>previousAcceleration[2]) {
					cumulativeAcceleration[2]=0;
				}
				/*--------------------------------------------------------------------------------------------------------------------------------*/

				/* Send data to external server --------------------------------------------------------------------------------------------------*/

				/* Obtain system time */
				Date dCurrentTime = new Date();
				CharSequence sCurrentTime = DateFormat.format("hh:mm:ss.SSS", dCurrentTime.getTime());
				
				/* Create the URL */
				String url = "http://guideme.0160811.bugs3.com/insert_accelerometer.php?time="
					+ sCurrentTime
					+ "&x=" + valuesAccelerometer[0]
					//+ "&x=" + stepYReady
					+ "&y=" + valuesAccelerometer[1]
					+ "&z=" + valuesAccelerometer[2]
					//+ "&azimuth=" + stepZReady
					+ "&azimuth=" + valuesOrientation[0]
					+ "&pitch=" + cumulativeAcceleration[1]
					+ "&roll=" + cumulativeAcceleration[2]
					//+ "&pitch=" + valuesOrientation[1]
					//+ "&roll=" + valuesOrientation[2]
					+ "&steps=" + iStepsCounter
					//+ "&stepValue=" + fStepValue
					+ "&stepValue=" + sampleCounter
					+ "&comment="+cCurrentPath;
					
				/* Send data to external server */
				//HttpConnection con = new HttpConnection(url);
					//(new Thread(con)).start();
				//}
				/*--------------------------------------------------------------------------------------------------------------------------------*/

				/* Update data in orientation_button */
				steps_button.setText("Steps: "+String.valueOf(iStepsCounter));
				
			}
		   break;
		
		/* Orientation Sensor */   
		case Sensor.TYPE_ORIENTATION:
			/* Flag to indicate data is ready */
			iDirectionDataReady = 1;

			/* Make use of data only when start button is pressed */
			if (playIcon.equals("pause")) {

				/* Read data from sensor */
				for(int i =0; i < 3; i++){
					valuesOrientation[i] = event.values[i];
				}
				
				String sOrientation="";
				if ((valuesOrientation[0]>350) || (valuesOrientation[0]<10))
					sOrientation="N";
				else if ((valuesOrientation[0]>=10) && (valuesOrientation[0]<80))
					sOrientation="NE";
				else if ((valuesOrientation[0]>=80) && (valuesOrientation[0]<100))
					sOrientation="E";
				else if ((valuesOrientation[0]>=100) && (valuesOrientation[0]<170))
					sOrientation="SE";
				else if ((valuesOrientation[0]>=170) && (valuesOrientation[0]<190))
					sOrientation="S";
				else if ((valuesOrientation[0]>=190) && (valuesOrientation[0]<260))
					sOrientation="SW";
				else if ((valuesOrientation[0]>=260) && (valuesOrientation[0]<280))
					sOrientation="W";
				else if ((valuesOrientation[0]>=280) && (valuesOrientation[0]<350))
					sOrientation="NW";
				
				/* Update data in orientation_button */
				orientation_button.setText(String.valueOf(Math.round(valuesOrientation[0]))+"°"+sOrientation);
				
				/* This is done to reduce the http connections*/
				if (Math.abs((previousOrientation[0]-valuesOrientation[0]))>10) {
					
					previousOrientation[0] = valuesOrientation[0];
				
					/* Live Feedback */
					if (preferences.GetPreference("live_feedback", 0)==1) {
					//if (Math.abs(previousOrientation[0]-valuesOrientation[0])>5) {
						
						/* Create the URL */
						String url;
						if (fserver==1)
							url = "http://0160811.bugs3.com/guideme/update_live_h.php?"
							+ "degree=" + valuesOrientation[0];
						else
							url = "http://www.hyhome.com.tw/hyhome/_lib/update_live_h.php?"
							+ "degree=" + valuesOrientation[0];
							
						/* Send data to external server */
						HttpConnection con = new HttpConnection(url);
							(new Thread(con)).start();
					}
				}
				
			}
			break;
		}
	}
}

