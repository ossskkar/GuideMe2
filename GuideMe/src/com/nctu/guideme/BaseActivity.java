package com.nctu.guideme;

import java.util.List;

import android.app.Activity;
import android.os.Vibrator;

public abstract class BaseActivity extends Activity {
	public AudioInterface audioInterface;
	public Vibrator vibrator;
	public PreferenceManager preferences;
	public RecordAudio recorder;
	public PlayAudio playAudio;
	public static String currentFileName;
	public PanicButton panic; 
	
	public Path_h_dataSource dataSource_h;
	public Path_d_dataSource dataSource_d;
	public static List<Path_h> paths_h;
	public static List<Path_d> paths_d;
	public static long lPath_h;
	
	/* Global variables */
	public static CharSequence cCurrentPath;
	public static String sHttpStatus;

	public static int     iStepsCounter;
	public static int     iStepStatus; /* 0 = initial, 1 => start step */
	public static int     iDirectionDataReady;
	public static int     iBoundaryTolerance=30;
	
	public static float  fStepValue;
	public static float  fDefaultStepValue=1;
	
	public static int currentIndex;
	public static int previousIndex;
	
	public static boolean bDirectionReady=false;
	public static boolean bDirectionMessage=false;
	public static boolean bFinishPath=false;

	public void InitializeVariables(){

		currentIndex=0;
		previousIndex=-1;
		iStepsCounter=0;
	}
}
