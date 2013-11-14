package com.nctu.guideme;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class Settings_layoutActivity extends BaseActivity {

	/* Declare views in current layout */
	Button emergencyContact_button;
	Button calibration_button;
	Button sync_button;
	Button cancel_button;
	Button panic_button;
	Button lighting_button;
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		/* Find id of views */
		emergencyContact_button=(Button)findViewById(R.id.emergencyContact_button);
		calibration_button=(Button)findViewById(R.id.calibration_button);
		sync_button=(Button)findViewById(R.id.sync_button);
		cancel_button=(Button)findViewById(R.id.cancel_button);
		panic_button=(Button)findViewById(R.id.panic_button);

		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* PreferencesManager class*/
		preferences = new PreferenceManager(this, "SettingsFile");
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Initial message */
		audioInterface=new AudioInterface(getApplicationContext(),"settings");
		
		emergencyContact_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Return to initial layout */
				startActivity(new Intent(getApplicationContext(), EmergencyContact_layoutActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		emergencyContact_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"emergency_contact_information");
				return true;
			}
		});
		
		/* Discard settings and return to initial layout */
		calibration_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				startActivity(new Intent(getApplicationContext(), Calibration_layoutActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		calibration_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"calibration");
				return true;
			}
		});
		
		/* Activate Live feedback */
		sync_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Activate Live feedback */
				if (preferences.GetPreference("live_feedback", 0)==0)
					preferences.SetPreference("live_feedback",1);
				/* Deactivate Live feedback */
				else 
					preferences.SetPreference("live_feedback",0);
				
			}
		});
		
		/* Discard settings and return to initial layout */
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
				audioInterface=new AudioInterface(getApplicationContext(),"exit");
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
}
