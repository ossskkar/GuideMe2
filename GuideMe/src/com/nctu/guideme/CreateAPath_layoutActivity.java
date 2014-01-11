package com.nctu.guideme;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAPath_layoutActivity extends BaseActivity implements LocationListener{

	/* Declare buttons in current layout */
	TextView pathName_textView;
	EditText pathName_editText;
	Button   record_button;
	Button   play_button;
	Button   ok_button;
	Button   cancel_button;
	Button   panic_button;
	String   recordIcon, playIcon; 

	/* Location Manager */
	private LocationManager locationManager;
	private String provider;
	private float lat;
	private float lng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_a_path);

		/* Get the locationManager */
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		/* Criteria to select location provider */
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
		/* Initialize the location fields */
		if (location != null) {
		      System.out.println("Provider " + provider + " has been selected.");
		      onLocationChanged(location);
		} else {
		      Toast toast = Toast.makeText(this, "Location not available.", 1000);
		      toast.show();
		}
		
		/* Find id of views */
		pathName_textView = (TextView)findViewById(R.id.pathName_textView);
		pathName_editText = (EditText)findViewById(R.id.pathName_editText);
		record_button     = (Button)findViewById(R.id.reset_button);
		play_button       = (Button)findViewById(R.id.play_button);
		ok_button         = (Button)findViewById(R.id.ok_button);
		cancel_button     = (Button)findViewById(R.id.cancel_button);
		panic_button      = (Button)findViewById(R.id.panic_button);

		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* Variable to change icon in runtime */
		recordIcon = "mic";
		playIcon = "play";
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Initial message */
		audioInterface=new AudioInterface(this,"record_name_for_new_path");
		
		/* Object for recording audio */
		recorder=new RecordAudio(this);
		
		/* Object for playing audio */
		playAudio=new PlayAudio();
		
		/* record button, records a name for the new path*/
		record_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Press Record */
				if (recordIcon.equals("mic")) {
					/* Change icon */
					recordIcon="stop";
					record_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.stop), null, null);
				
					/* Start recording */
					recorder.StartRecording();
				}
				/* Press Stop */
				else {
					/* Change icon */					
					recordIcon="mic";
					record_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.mic), null, null);
					
					/* Stop recording */
					recorder.StopRecording();
				}
			}
		});
		
		/* Play the sound help */
		record_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				if (record_button.getText().equals("Record"))
					audioInterface=new AudioInterface(getApplicationContext(),"record");
				else 
					audioInterface=new AudioInterface(getApplicationContext(),"stop");
				return true;
			}
		});
		
		play_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				/* Haptic feedback */
				vibrator.vibrate(50);

				/* Press Play */
				if (playIcon.equals("play")) {
					/* Change icon */
					playIcon="pause";
					play_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.pause), null, null);
					
					/* play the file just recorded */
					playAudio.StartPlaying(recorder.GetFileName());
				}
				/* Press Stop */
				else {
					/* Change icon */
					playIcon="play";
					play_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play), null, null);
				
					/* Stop playing */
					playAudio.StopPlaying();
				}
			}
		});
		
		/* Play the sound help */
		play_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				if (play_button.getText().equals("Play"))
					audioInterface=new AudioInterface(getApplicationContext(),"play");
				else 
					audioInterface=new AudioInterface(getApplicationContext(),"stop");
				return true;
			}
		});
		
		/* Verify path name and execute next layout */
		ok_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Insert path_h*/
				String url;
				url = "http://0160811.bugs3.com/guideme/insert_path_h.php?lat="
						+ String.valueOf(lat) + "&lng=" + String.valueOf(lng);
				
				/* Send data to external server */
				HttpConnection con = new HttpConnection(url);
					(new Thread(con)).start();
				
				/*Verify parameters */
				//if (!recorder.GetFileStatus()) 
				//	audioInterface=new AudioInterface(getApplicationContext(),"record_name_for_new_path");
				//else {
					currentFileName=recorder.GetFileName();
					currentFileName="fromhere.3ggp";
					/* Execute next layout and exit current layout */
					startActivity(new Intent(getApplicationContext(), RecordAPath_layoutActivity.class));
					finish();
				//}
			}
		});
		
		/* Play the sound help */
		ok_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"accept");
				return true;
			}
		});
		
		/* Exit */
		cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
					
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		cancel_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
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
				audioInterface=new AudioInterface(getApplicationContext(),"panic_message3");
				
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
				audioInterface=new AudioInterface(getApplicationContext(),"panic_button");
				
				return true;
			}
		});
	}

	 /* Request updates at startup */
	@Override
	protected void onResume() {
	  super.onResume();
	  locationManager.requestLocationUpdates(provider, 400, 1, this);
	}
	
	  /* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
	  super.onPause();
	  locationManager.removeUpdates(this);
	}
	  
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lat = (float) (location.getLatitude());
	    lng = (float) (location.getLongitude());
	    
	    Toast.makeText(this, "Lat = " + String.valueOf(lat) + " Long = " + String.valueOf(lng),
			        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disabled provider " + provider,
		        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Enabled new provider " + provider,
		        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
