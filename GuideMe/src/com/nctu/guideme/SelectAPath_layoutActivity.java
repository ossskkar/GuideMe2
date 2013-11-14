package com.nctu.guideme;

import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SelectAPath_layoutActivity extends BaseActivity {
	
	/* Declare views in current layout */
	TextView status_textView;
	Button ok_button;
	Button previous_button;
	Button next_button;
	Button cancel_button;
	Button panic_button;
	int currentIndex=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_a_path);
		
		/* Find id of views */
		status_textView       = (TextView) findViewById(R.id.status_textView);
		previous_button       = (Button)   findViewById(R.id.previous_button);
		next_button           = (Button)   findViewById(R.id.next_button);
		ok_button             = (Button)   findViewById(R.id.ok_button);
		cancel_button         = (Button)   findViewById(R.id.cancel_button);
		panic_button          = (Button)   findViewById(R.id.panic_button);
		
		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Initial message */
		audioInterface=new AudioInterface(this,"select_path");
		
		/* Database object */
		dataSource_h=new Path_h_dataSource(this);
		dataSource_h.open();
		paths_h=dataSource_h.getAllPath_h();
		dataSource_h.close();
		currentIndex=0;
		//while (currentIndex<paths_h.size()){
		//	dataSource.deletePath_h(paths_h.get(currentIndex));
		//	currentIndex++;
		//}
		//paths_h=dataSource.getAllPath_h();
		
		/* Create AudioPlay object */
		playAudio=new PlayAudio();
		
		/* Select previous/first path */
		previous_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Rerduce index */
				if (currentIndex>0)
					currentIndex--;
		
				/* play audio file */
				playAudio.StartPlaying(paths_h.get(currentIndex).getFileName().toString());
				status_textView.setText(paths_h.get(currentIndex).getFileName().toString());
			}
		});
		
		/* Play the sound help */
		previous_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"previous");
				return true;
			}
		});
		
		/* Select next/last path */
		next_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Increase index */
				if (currentIndex<paths_h.size()-1)
					currentIndex++;
		
				/* play audio file */
				playAudio.StartPlaying(paths_h.get(currentIndex).getFileName().toString());
				status_textView.setText(paths_h.get(currentIndex).getFileName().toString());
			}
		});
		
		/* Play the sound help */
		next_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"next");
				return true;
			}
		});
		
		/* Confirm the path selected and executes the next layout */
		ok_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				lPath_h=paths_h.get(currentIndex).getId();
				
				startActivity(new Intent(getApplicationContext(), GetDirections_layoutActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		ok_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"select");
				return true;
			}
		});
		
		/* Cancel the path selection and return to initial layout */
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
