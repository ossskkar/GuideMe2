package com.nctu.guideme;

import java.util.ArrayList;
import java.util.Locale;

import android.R.string;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmergencyContact_layoutActivity extends BaseActivity implements OnInitListener {

	protected static final int RESULT_SPEECH = 1;
	
	/* Declare views in current layout */
	//EditText contactName_editText;
	EditText contactPhone_editText;
	//EditText contactEmail_editText;
	Button ok_button;
	Button cancel_button;
	Button record_button;
	Button play_button;
	/*  for text-to-speech  */
	private TextToSpeech tts;
	
	//string original_str;
	String split_str;
	int speech = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency_contact);
		/*  for text-to-speech  */
		tts = new TextToSpeech(this,  this);
		
		/* Find id of views */
		//contactName_editText = (EditText) findViewById(R.id.contactName_editText);
		contactPhone_editText = (EditText) findViewById(R.id.contactPhone_editText);
		//contactEmail_editText = (EditText) findViewById(R.id.contactEmail_editText);
		ok_button        	  = (Button)findViewById(R.id.ok_button);
		cancel_button         = (Button)findViewById(R.id.cancel_button);

		record_button         = (Button)findViewById(R.id.record_button);
		play_button           = (Button)findViewById(R.id.play_button);
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Initial message */
		audioInterface=new AudioInterface(getApplicationContext(),"emergency_contact_information");
		
		/* PreferencesManager class*/
		preferences = new PreferenceManager(this, "SettingsFile");
		
		/* Load preferences */
		//contactName_editText.setText(preferences.GetPreference("contactName", null));
		contactPhone_editText.setText(preferences.GetPreference("contactPhone", null));
		//contactEmail_editText.setText(preferences.GetPreference("contactEmail", null));
		
		/* Save settings and executes the initial layout */
		ok_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Save data to preferences */
				//preferences.SetPreference("contactName", contactName_editText.getText().toString());
				preferences.SetPreference("contactPhone", contactPhone_editText.getText().toString());
				//preferences.SetPreference("contactEmail", contactEmail_editText.getText().toString());
				
				/* Return to initial layout */
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
		
		/* Discard settings and return to initial layout */
		cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
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
		
		
		
		/*  speech to text demo */

		
		record_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				speech = 0;
				
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					contactPhone_editText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Ops! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
		
		
		play_button.setOnClickListener(new View.OnClickListener() {
                  
						
			public void onClick(View arg0) {
				
				speakOut();
			}

		});
	
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				contactPhone_editText.setText(text.get(0));
			}
			break;
		}

		}
	}
	
	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	public void onInit(int status) {
		// TODO Auto-generated method stub

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);
			
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show();
				Log.e("TTS", "Language is not supported");
			} else {
				play_button.setEnabled(true);

			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}

	private void speakOut() {

		String text  = contactPhone_editText.getText().toString();
		if (text.length() == 0) {
			tts.speak("You haven't typed text", TextToSpeech.QUEUE_FLUSH, null);
		} else {
			if (speech == 0)
			{
			split_str = "";
				for (int i = 0; i < text.length(); i=i+1)
					split_str = split_str + text.substring(i,i+1)+" ";
			contactPhone_editText.setText(split_str);
			speech = 1;
			}
			tts.speak(split_str, TextToSpeech.QUEUE_FLUSH, null);
		}

	}
		
	}


