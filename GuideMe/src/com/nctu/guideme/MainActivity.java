package com.nctu.guideme;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity {

	/* Declare views in current layout */
	Button recordAPath_button;
	Button getDirections_button;
	Button settings_button;
	Button exit_button;
	Button panic_button;
	MediaPlayer mp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(android.R.style.Theme_Black);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial_screen);
		
		/* Find id of views */
		recordAPath_button   = (Button)findViewById(R.id.recordAPath_button);
		getDirections_button = (Button)findViewById(R.id.getDirections_button);
		settings_button      = (Button)findViewById(R.id.settings_button);
		exit_button          = (Button)findViewById(R.id.exit_button);
		panic_button         = (Button)findViewById(R.id.panic_button);

		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* PreferencesManager class*/
		preferences = new PreferenceManager(this,"SettingsFile");
		
		/* Load preferences for stepValue if it exits */
		fStepValue=preferences.GetPreference("stepValue", fDefaultStepValue);
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Initial message */
		audioInterface=new AudioInterface(this,"welcome_message2");

		/* Database object */
		dataSource_h=new Path_h_dataSource(this);
		dataSource_h.open();
		paths_h=dataSource_h.getAllPath_h();
		
		/* Execute create a path layout */
		recordAPath_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				/* Start activity */
				startActivity(new Intent(getApplicationContext(), CreateAPath_layoutActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		recordAPath_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"record_a_path");
				return true;
			}
		});
		
		/* Execute get directions layout */
		getDirections_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				if (!paths_h.isEmpty())
				{
					/* Start activity */
					startActivity(new Intent(getApplicationContext(), SelectAPath_layoutActivity.class));
					finish();
				}
			}
		});
		
		/* Play the sound help */
		getDirections_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"get_directions");
				return true;
			}
		});
		
		/* Execute settings layout */
		settings_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				/* Start activity */
				startActivity(new Intent(getApplicationContext(), Settings_layoutActivity.class));
				finish();				
			}
		});
		
		/* Play the sound help */
		settings_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"settings");
				return true;
			}
		});
		
		/* Exit */
		exit_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				/* Exit activity */
				finish();
			}
		});
		
		/* Play the sound help */
		exit_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"exit_application");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
