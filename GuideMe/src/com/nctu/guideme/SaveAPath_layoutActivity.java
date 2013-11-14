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

public class SaveAPath_layoutActivity extends BaseActivity {
	/* Declare views in current layout */
	TextView status_textView;
	Button   ok_button;
	Button   cancel_button;
	Button   panic_button;
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_record_a_path);
		
		/* Find id of views */
		status_textView = (TextView) findViewById(R.id.status_textView);
		ok_button       = (Button)   findViewById(R.id.ok_button);
		cancel_button   = (Button)   findViewById(R.id.cancel_button);
		panic_button    = (Button)   findViewById(R.id.panic_button);
		
		/* Initialize panic button */
		panic=new PanicButton(this);
		
		/* Create vibrator for haptic feedback */
		vibrator=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		/* Initial message */
		audioInterface=new AudioInterface(getApplicationContext(),"finish_save_path");
		
		/* Database objects */
		dataSource_h=new Path_h_dataSource(this);
		dataSource_h.open();
		
		dataSource_d=new Path_d_dataSource(this);
		dataSource_d.open();
		
		/* Confirm recording of a path and return to initial layout */
		ok_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* insert path_h to database */
				Path_h path_h=null;
				path_h=dataSource_h.createPath_h(currentFileName);
				dataSource_h.close();
				
				/* Update column path_h in paths_d*/
				int currentIndex=0;
				while (currentIndex<paths_d.size()){
					paths_d.get(currentIndex).setPath_h(path_h.getId());
					currentIndex++;
				}
				
				/* EXPLANATION: in order to record the path, we assign an orientation block each entry, */
				/*              that is each entry will belong to North, West, South or East block.     */
				/*              I call this process QUADRICUALIZATION                                   */
				
				/* Process path_d */
				//float tmp_totalDirX=0;
				//float tmp_maxDirX=0;
				//float tmp_minDirX=0;
				
				/* Steps counter */
				int tmp_steps=0;
				int tmp_angle=0;  
				
				/* Reference angle variables */
				int initialAngle, currentAngle;
				int NWBoundary, /* North-West boundary */
					NEBoundary, /* North-East boundary */
					SEBoundary, /* South-East boundary */
					SWBoundary; /* South-West boundary */
				
				int NAngle, /* reference North angle */
					EAngle, /* reference East angle  */
					SAngle, /* reference South angle */
					WAngle; /* reference West angle  */
					
				/* a block can be N-north, S-sout, E-east, W-west*/
				String currentBlock, previousBlock;
				
				/* Capture the initial angle */
				initialAngle=(int)paths_d.get(0).getDirectionX();
				
				/*Initialize variables */
				currentIndex=0;
				currentBlock="";
				previousBlock="";
				
				/* Reference angles */
				NAngle=initialAngle;
				EAngle=initialAngle+90;
				SAngle=initialAngle+180;
				WAngle=initialAngle+270;
				
				/* Calculate the boundaries for each block*/
				NEBoundary=initialAngle+45;
				SEBoundary=initialAngle+135;
				SWBoundary=initialAngle+225;
				NWBoundary=initialAngle+315;
				
				/*Correct angles */
				if (EAngle>359)
					EAngle=EAngle-359;
				
				if (SAngle>359)
					SAngle=SAngle-359;
				
				if (WAngle>359)
					WAngle=WAngle-359;
				
				/*
				if (NEBoundary>359)
					NEBoundary=NEBoundary-359;
				
				if (SEBoundary>359)
					SEBoundary=SEBoundary-359;
				
				if (SWBoundary>359)
					SWBoundary=SWBoundary-359;
				
				if (NWBoundary>359)
					NWBoundary=NWBoundary-359;
				*/
				
				/* Determine previous block */
				if (initialAngle<NEBoundary)
					previousBlock="N";
				else if (initialAngle<SEBoundary)
					previousBlock="E";
				else if (initialAngle<SWBoundary)
					previousBlock="S";
				else if (initialAngle<NWBoundary)
					previousBlock="W";
				//else if (initialAngle>NWBoundary)
				//	previousBlock="N";
				
				/* Group and insert path_d into database */
				while (currentIndex<paths_d.size()){

					/* Read current angle */
					currentAngle=(int)paths_d.get(currentIndex).getDirectionX();
					
					/* Add compensation */
					if (currentAngle<NAngle)
						currentAngle=currentAngle+359;
					
					/* Determine current block */
					if (currentAngle<NEBoundary)
						currentBlock="N";
					else if (currentAngle<SEBoundary)
						currentBlock="E";
					else if (currentAngle<SWBoundary)
						currentBlock="S";
					else if (currentAngle<NWBoundary)
						currentBlock="W";
					else if (currentAngle>NWBoundary)
						currentBlock="N";
					
					/* if the steps is in the same direction */
					if (currentBlock.equals(previousBlock)){
						tmp_steps++;
					}
					/* if the step is in a different direction */
					else {
						if (tmp_steps!=0) {
							
							/* Obtain block's angle */
							if (previousBlock.equals("N"))
								tmp_angle=NAngle;
							else if (previousBlock.equals("E"))
								tmp_angle=EAngle;
							else if (previousBlock.equals("S"))
								tmp_angle=SAngle;
							else if (previousBlock.equals("W"))
								tmp_angle=WAngle;
							
							/* Insert path_d */
							dataSource_d.createPath_d(paths_d.get(currentIndex).getPath_h(), tmp_steps, tmp_angle, 0, 0);
								
							/* Initialize steps counter */
							tmp_steps=1;
						}
					}
					
					/* Update block */
					previousBlock=currentBlock;
					
					/* Update index */
					currentIndex++;
				}
				
				/* Obtain block's angle of last steps */
				if (previousBlock.equals("N"))
					tmp_angle=NAngle;
				else if (previousBlock.equals("E"))
					tmp_angle=EAngle;
				else if (previousBlock.equals("S"))
					tmp_angle=SAngle;
				else if (previousBlock.equals("W"))
					tmp_angle=WAngle;
				
				/* Insert path_d of last steps */
				dataSource_d.createPath_d(paths_d.get(currentIndex-1).getPath_h(), tmp_steps, tmp_angle, 0, 0);
				
				/* Update pathFileNameCounter preference*/
				preferences=new PreferenceManager(getApplicationContext(),"pathFileNameCounter");
				preferences.IncrementPreference("pathFileNameCounter", 0);
				
				/* Return to initial layout */
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		ok_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"yes");
				return true;
			}
		});
		
		cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/* Haptic feedback */
				vibrator.vibrate(50);
				
				/* Cancel the recording of the path, delete data */
				//PENDING 
				
				/* Return to initial layout */
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});
		
		/* Play the sound help */
		cancel_button.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				audioInterface=new AudioInterface(getApplicationContext(),"no");
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
				
				/* Audio interface*/
				audioInterface=new AudioInterface(getApplicationContext(),"panic_button");
				return true;
			}
		});
	}
}
