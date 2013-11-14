package com.nctu.guideme;

import java.io.File;

import android.app.Activity;
import android.media.MediaPlayer;

public class PlayAudio extends Activity {

	private MediaPlayer mp;
	private String fileName;
	private File pathNameFile;
	
	public PlayAudio(){
	}

	public void StartPlaying(String fileName){
		this.fileName=fileName;
		try {
			InitializePlayer();
			if (pathNameFile.exists())
			{
				mp.prepare();
				mp.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void InitializePlayer()throws Exception{
		if (mp!=null) 
			mp.release();
		SetupPlayer();
	}
	
	public void SetupPlayer()throws Exception{
		mp=new MediaPlayer();
		mp.setDataSource(fileName);
		pathNameFile = new File(fileName);
	}
	
	public void StopPlaying(){
		if (mp!=null)
			mp.stop();
	}
}
