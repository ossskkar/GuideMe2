package com.nctu.guideme;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AudioInterface extends Activity {

	private MediaPlayer mp=null;
	private Context currentContext=null;
	private String currentAudio=null;
	
	public AudioInterface(Context thisContext, String audioName) {
		currentContext=thisContext;
		currentAudio=audioName;
		try {
			PlayAudio();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void PlayAudio() throws Exception {
		ClearMp();
		SetUpMp();
		StartAudio();
	}

	private void StartAudio() throws Exception{
		if (mp.isPlaying())
			mp.stop();
		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}

	private void SetUpMp() throws Exception{
		mp = new MediaPlayer();
		SelectAudioFile();
	}


	private void ClearMp() throws Exception{
		if (mp!=null)	{
			mp.stop();
			//mp.release();
		}
	}
	
	private void SelectAudioFile() throws Exception {
		if (currentAudio.equals("accept"))
			mp=MediaPlayer.create(currentContext, R.raw.accept);

		if (currentAudio.equals("beep7"))
			mp=MediaPlayer.create(currentContext, R.raw.beep7);
		
		if (currentAudio.equals("calibration"))
			mp=MediaPlayer.create(currentContext, R.raw.calibration);
		
		if (currentAudio.equals("cancel"))
			mp=MediaPlayer.create(currentContext, R.raw.cancel);
		
		if (currentAudio.equals("emergency_contact_information"))
			mp=MediaPlayer.create(currentContext, R.raw.emergency_contact_information);
		
		if (currentAudio.equals("enter_a_name_for_the_new_path"))
			mp=MediaPlayer.create(currentContext, R.raw.enter_a_name_for_the_new_path);
		
		if (currentAudio.equals("exit_application"))
			mp=MediaPlayer.create(currentContext, R.raw.exit_application);
		
		if (currentAudio.equals("exit"))
			mp=MediaPlayer.create(currentContext, R.raw.exit);
		
		if (currentAudio.equals("finish_save_path"))
			mp=MediaPlayer.create(currentContext, R.raw.finish_save_path);
		
		if (currentAudio.equals("finish"))
			mp=MediaPlayer.create(currentContext, R.raw.finish);
		
		if (currentAudio.equals("get_directions"))
			mp=MediaPlayer.create(currentContext, R.raw.get_directions);
		
		if (currentAudio.equals("next"))
			mp=MediaPlayer.create(currentContext, R.raw.next);
		
		if (currentAudio.equals("no"))
			mp=MediaPlayer.create(currentContext, R.raw.no);
		
		if (currentAudio.equals("panic_button"))
			mp=MediaPlayer.create(currentContext, R.raw.panic_button);
		
		if (currentAudio.equals("panic_message3"))
			mp=MediaPlayer.create(currentContext, R.raw.panic_message3);
		
		if (currentAudio.equals("pause"))
			mp=MediaPlayer.create(currentContext, R.raw.pause);
		
		if (currentAudio.equals("play"))
			mp=MediaPlayer.create(currentContext, R.raw.play);
			
		if (currentAudio.equals("prest_start_to_record_the_path"))
			mp=MediaPlayer.create(currentContext, R.raw.prest_start_to_record_the_path);
		
		if (currentAudio.equals("previous"))
			mp=MediaPlayer.create(currentContext, R.raw.previous);
		
		if (currentAudio.equals("record"))
			mp=MediaPlayer.create(currentContext, R.raw.record);
		
		if (currentAudio.equals("record_a_path"))
			mp=MediaPlayer.create(currentContext, R.raw.record_a_path);
		
		if (currentAudio.equals("record_name_for_new_path"))
				mp=MediaPlayer.create(currentContext, R.raw.record_name_for_new_path);
			
		if (currentAudio.equals("save"))
			mp=MediaPlayer.create(currentContext, R.raw.save);
		
		if (currentAudio.equals("select_path"))
			mp=MediaPlayer.create(currentContext, R.raw.select_path);
		
		if (currentAudio.equals("select"))
			mp=MediaPlayer.create(currentContext, R.raw.select);
		
		if (currentAudio.equals("settings"))
			mp=MediaPlayer.create(currentContext, R.raw.settings);
		
		if (currentAudio.equals("start"))
			mp=MediaPlayer.create(currentContext, R.raw.start);
		
		if (currentAudio.equals("stop"))
			mp=MediaPlayer.create(currentContext, R.raw.stop);
		
		if (currentAudio.equals("welcome_message"))
			mp=MediaPlayer.create(currentContext, R.raw.welcome_message);
		
		if (currentAudio.equals("welcome_message2"))
			mp=MediaPlayer.create(currentContext, R.raw.welcome_message2);
		
		if (currentAudio.equals("yes"))
			mp=MediaPlayer.create(currentContext, R.raw.yes);
		
		if (currentAudio.equals("you_have_arrived"))
			mp=MediaPlayer.create(currentContext, R.raw.you_have_arrived_to_your_destination);
		
		
		//if (currentAudio.equals(""))
		//	mp=MediaPlayer.create(currentContext, R.raw.);
	}
	
}
