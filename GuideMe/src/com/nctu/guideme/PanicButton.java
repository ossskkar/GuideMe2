package com.nctu.guideme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PanicButton{
	
	private Context currentContext;
	private Boolean fileStatus;
	
	
	/* Constructor */
	public PanicButton(Context context){
		currentContext=context;
		SetFileStatus(false);
	}
	
	public void phoneCall(String number)
	{

		/* Make a phone call */
		String phoneCallUri="tel:"+number;  //call eric
		Intent phoneCallIntent=new Intent(Intent.ACTION_CALL);
		phoneCallIntent.setData(Uri.parse(phoneCallUri));
		currentContext.startActivity(phoneCallIntent);
	}
	
	public void SetFileStatus(Boolean status){
		fileStatus=status;
	}

}
