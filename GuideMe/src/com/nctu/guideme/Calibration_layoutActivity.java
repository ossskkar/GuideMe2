package com.nctu.guideme;



import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Calibration_layoutActivity extends BaseActivity {

	/* Declare buttons in current layout */
	TextView status_textView;
	SeekBar  stepValue_seekBar;
	Button   ok_button;
	Button   cancel_button;
	Button   panic_button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibration);
		
		/* Find id of views */
		status_textView   = (TextView)findViewById(R.id.status_textView);
		stepValue_seekBar = (SeekBar)findViewById(R.id.calibration_seekBar);
		ok_button         = (Button)findViewById(R.id.ok_button);
		cancel_button     = (Button)findViewById(R.id.cancel_button);
		panic_button      = (Button)findViewById(R.id.panic_button);
		
		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);

		/* PreferencesManager class*/
		preferences = new PreferenceManager(this,"SettingsFile");
		
		/* Welcome message */
		audioInterface=new AudioInterface(this,"calibration");
		
		/* object for panic button */
		panic = new PanicButton(this);
		
		/* Load stepValue */
		stepValue_seekBar.setProgress((int)(fStepValue*100));
		//status_textView.setText(stepValue_seekBar.getProgress());
		
		/* Verify path name and execute next layout */
		ok_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Get value to global variable */
				fStepValue=stepValue_seekBar.getProgress();
				fStepValue=fStepValue/100;

				/* Load preferences */
				preferences.SetPreference("stepValue", fStepValue);
								
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Execute next layout and exit current layout */
				startActivity(new Intent(getApplicationContext(), Settings_layoutActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		ok_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"save");
				return true;
			}
		});
		
		/* Exit */
		cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Start activity */
				startActivity(new Intent(getApplicationContext(), Settings_layoutActivity.class));
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
			
				/* Make a call for emergency contact */
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
}
