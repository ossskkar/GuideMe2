package com.nctu.guideme;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Log;

public class HttpConnection implements Runnable
{
	private String url;
	public HttpConnection(String url) {
		this.url = url;
	}
	public void run() {
		HttpGet httpRequest = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) { /* 200 ok */
				// return EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e)	{	
			Log.d("b", "client exception.");
		} catch (IOException e) {				
			Log.d("b", "IO exception.");
		}
		return;
	}
}
