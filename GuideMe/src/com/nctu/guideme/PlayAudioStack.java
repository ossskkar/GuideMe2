package com.nctu.guideme;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class PlayAudioStack {
	
	private MediaPlayer mp=null;
	private Context currentContext=null;
	private int resID,pos=0;
	
	public PlayAudioStack(Context thisContext){
		currentContext = thisContext;
	}
	
	public void playList(final ArrayList<String> fileList){
		
		resID=currentContext.getResources().getIdentifier(fileList.get(pos).toString(),"raw",currentContext.getPackageName());
			
		mp = new MediaPlayer();
		mp=MediaPlayer.create(currentContext, resID);
		mp.start();
			
		mp.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp){
				mp.release();
				pos++;
				if(pos<fileList.size())
				{
					playList(fileList);
				}
			}
		});
	}
}
