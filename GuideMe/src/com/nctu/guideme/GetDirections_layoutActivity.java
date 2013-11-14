package com.nctu.guideme;

import java.util.ArrayList;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class GetDirections_layoutActivity extends BaseActivity implements SensorEventListener{

	/* Declare views in current layout */
	Button orientation_button;
	Button steps_button;
	Button ok_button;
	Button cancel_button;
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
	

	String playIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_directions);
		
		/* Find id of views */
		orientation_button = (Button)   findViewById(R.id.orientation_button);
		steps_button       = (Button)   findViewById(R.id.steps_button);
		ok_button          = (Button)   findViewById(R.id.ok_button);
		cancel_button      = (Button)   findViewById(R.id.cancel_button);
		panic_button       = (Button)   findViewById(R.id.panic_button);
		
		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Database object */
		dataSource_d=null;
		dataSource_d=new Path_d_dataSource(this);
		dataSource_d.open();
		paths_d=dataSource_d.getAllPath_d(lPath_h);
		dataSource_d.close();
		
		bFinishPath=false;
		bDirectionReady=false;
		bDirectionMessage=false;

		/* Initialize variables */
		InitializeVariables();
		
		valuesAccelerometer    = new float[3];
		previousAcceleration   = new float[3];
		currentAcceleration    = new float[3];
		cumulativeAcceleration = new float[3];
		valuesOrientation      = new float[3];

		/* Variable to control change of icon */
		playIcon = "play";
		
		/* PreferencesManager class*/
		preferences = new PreferenceManager(this,"SettingsFile");
		
		/* Load fStepValue */
		preferences.SetPreference("stepValue", fStepValue);
		
		/* Sensor Manager */
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	    sensorOrientation   = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		/* Initial message */
		audioInterface=new AudioInterface(getApplicationContext(),"");
		
		/* Steps button */
		steps_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Activate flag */
				if (bDirectionReady)
					bDirectionMessage=true;
			}
		});
		
		/* Orientation button */
		orientation_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Activate flag */
				//NOTE: deactivated cus it causes crash in the app, no time to fix it now. check it later
				//bDirectionReady=false;
			}
		});
		
		/* Start/pause the directions of a path */
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
				if (playIcon.equals("play"))
					audioInterface=new AudioInterface(getApplicationContext(),"start");
				else
					audioInterface=new AudioInterface(getApplicationContext(),"pause");
				return true;
			}
		});
		
		/* cancel the directions of a path  */
		cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);

				/* Return to initial layout */
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		cancel_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				if (bFinishPath)
					audioInterface=new AudioInterface(getApplicationContext(),"accept");
				else
					audioInterface=new AudioInterface(getApplicationContext(),"cancel");
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
			if (playIcon.equals("pause") && !bFinishPath && bDirectionReady) {

				//sampleCounter++;
				
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
						
					/* Reset cumulative acceleration */
					cumulativeAcceleration[1]=0;
					cumulativeAcceleration[2]=0;
				
					/* If you walk all the steps in path_d then it moves to the next path_d and initialize steps counter */
					if (iStepsCounter>=paths_d.get(currentIndex).getSteps()) {
						iStepsCounter=0;

						/* finish the path */
						if (currentIndex>=(paths_d.size()-1)) {
							bFinishPath=true; 
							
							/* Reset text in buttons*/
							//orientation_button.setText("");
							steps_button.setText("Finish");
							
							/* PLay audio interface */
							audioInterface=new AudioInterface(getApplicationContext(),"you_have_arrived");
							
							/* Change icon */
							playIcon="play";
							ok_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play), null, null);
						}
						
						/* Change of path_d */
						else {
							
							/* Check for orientation */
							bDirectionReady=false;
							
							/* Increment index of path_d */
							currentIndex++;
						}
					}
				}
				
				/* We consider only when acceleration decreases, if it increases we reset the cumulative values */
				if (currentAcceleration[1]>previousAcceleration[1]) {
					cumulativeAcceleration[1]=0;
				}
				
				if (currentAcceleration[2]>previousAcceleration[2]) {
					cumulativeAcceleration[2]=0;
				}
				/*--------------------------------------------------------------------------------------------------------------------------------*/

				/* Update data in steps */
				if (!bFinishPath)
					steps_button.setText("Walk:\n"+String.valueOf(paths_d.get(currentIndex).getSteps()-iStepsCounter));
				
			}
			
			break;
		
		/* Orientation Sensor */   
		case Sensor.TYPE_ORIENTATION:

			/* Make use of data only when start button is pressed or before the path has ended */
			if (playIcon.equals("pause") && !bFinishPath) {

				/* Read data from sensor */
				for(int i =0; i < 3; i++){
					valuesOrientation[i] = event.values[i];
				}    

				/* Here we only take care of changing color of text depending on the orientation */
				
				/* Correct orientation */
				if ((paths_d.get(currentIndex).getDirectionX()<(valuesOrientation[0]+iBoundaryTolerance))
						&&(paths_d.get(currentIndex).getDirectionX()>(valuesOrientation[0]-iBoundaryTolerance))) {
					
					/* Change color of text to green */
					orientation_button.setTextAppearance(this, R.style.GreenText);
					steps_button.setTextAppearance(this, R.style.GreenText);
					
					/* Activate walking directions */
					if (currentIndex>previousIndex) {
						
						/*Activate flag*/
						bDirectionMessage=true;
						
						/* Update previousIndex */
						previousIndex=currentIndex;
					}
				}
				/* Incorrect orientation */
				else if ((paths_d.get(currentIndex).getDirectionX()<(valuesOrientation[0]-iBoundaryTolerance))
						||(paths_d.get(currentIndex).getDirectionX()>(valuesOrientation[0]+iBoundaryTolerance))){
					
					/* Change color of text to red */
					orientation_button.setTextAppearance(this, R.style.RedText);
					steps_button.setTextAppearance(this, R.style.RedText);
					
					/* Haptic feedback */
					vibrator.vibrate(50);
				}
				
				/* Determine Orientation NORTH, SOUTH, EAST, WEST */
				DegreeToText dtt=new DegreeToText();
				String sOrientation=dtt.Convert((int) valuesOrientation[0]);
				
				String sOrientation2=dtt.Convert((int) paths_d.get(currentIndex).getDirectionX());
				
				/* Update data in orientation_button */
				orientation_button.setText(String.valueOf(Math.round(paths_d.get(currentIndex).getDirectionX())+"°"+sOrientation2+"\n")
						+String.valueOf(Math.round(valuesOrientation[0]))+"°"+sOrientation);
			}
			
			/* Variables for spoken directions */
			final ArrayList<String> AudioList =new ArrayList<String>();
			int audioIndex=0;			
			
			/* Walking directions */
			if (bDirectionMessage) {
				
				/*Convert number to text to obtain audio file name */
				NumberToText ntt=new NumberToText();
				
				/* add audio files to stack */
				AudioList.add(audioIndex,"walk");
				audioIndex++;
				AudioList.add(audioIndex,String.valueOf(ntt.Convert((int) paths_d.get(currentIndex).getSteps()-iStepsCounter)));
				audioIndex++;
				if ((paths_d.get(currentIndex).getSteps()-iStepsCounter)>1)
					AudioList.add(audioIndex,"steps");
				else 
					AudioList.add(audioIndex,"step");
				audioIndex++;
				
				/* Play audio files */
				PlayAudioStack playStack;
				playStack=new PlayAudioStack(this);
		 		playStack.playList(AudioList);
				
				/* Deactivate flag */
				bDirectionMessage=false;
			}
			
			/* Here we only take care of audio interface depending on the orientation */			
			if (playIcon.equals("pause") && !bDirectionReady) {
				
				/* Initial check of orientation */
				if (currentIndex==0) {
					//DO NOTHING 
				}

				/* Change of orientation */
				else {
				
					/* Add audio file to list */
					AudioList.add(audioIndex,"stop");
					audioIndex++;
				}
				
				/* Find orientation, determines where to turn, that is left of right */
				FindOrientation fo=new FindOrientation();
				String turnTo=fo.Find((int)valuesOrientation[0], (int)paths_d.get(currentIndex).getDirectionX());
				
				/* Correct orientation */
				if ((paths_d.get(currentIndex).getDirectionX()<(valuesOrientation[0]+iBoundaryTolerance))
						&&(paths_d.get(currentIndex).getDirectionX()>(valuesOrientation[0]-iBoundaryTolerance))) {
					//DO NOTHING 
				}
				
				/* Turn left */
				else if (turnTo.equals("left")) {
					
					/* Add audio file to list */
					AudioList.add(audioIndex,"turn");
					audioIndex++;
					AudioList.add(audioIndex,"left");
					audioIndex++;
				}
				
				/* Turn right */
				else if (turnTo.equals("right")) {
				
					/* Add audio file to list */
					AudioList.add(audioIndex,"turn");
					audioIndex++;
					AudioList.add(audioIndex,"right");
					audioIndex++;
				}
				
				/* We activate the flag */
				bDirectionReady=true;
				
				/* Play audio files */
				PlayAudioStack playStack;
				playStack=new PlayAudioStack(this);
		 		playStack.playList(AudioList);
			}
			break;
		}
	}
}
