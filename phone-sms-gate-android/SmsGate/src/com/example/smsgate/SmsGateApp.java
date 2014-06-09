package com.example.smsgate;

import java.util.LinkedList;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class SmsGateApp extends Application{	
	
	private static Intent i = new Intent();
	
	@Override
	public void onCreate() {
		super.onCreate();
		ContactsManager.init(getApplicationContext());
		ContactsManager.getInstance();
		i.putExtra("refresh_log", true);
	}
	
}

class LogWriter
{
	
	private static LogWriter lw;
	
	private TextView tv;
	private ScrollView sv;
	
	private LinkedList<String> log = new LinkedList<String>();
	
	private LogWriter() {}
    
	public static synchronized LogWriter getInstance() {
    	if(lw == null) {
            lw = new LogWriter();
        }
        return lw;
    }
	
    public void init(TextView tv, ScrollView sv) {
    	this.tv = tv;
    	this.sv = sv;
    	log("");  	
    }   
	
	synchronized void log(String msg) {
		AsyncTask<String, Void, Void> at = new AsyncTask<String, Void, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				doLog(params[0]);
				return null;
			}
				

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				updateText();
			}		
		};
		at.execute(msg, null, null);
	}
	
	private synchronized void doLog(String msg)
	{
		if (log.size() < 1024)
			log.add(msg);
		else {
			log.remove(0);
			log.add(msg);
		}				
	}
	
	private synchronized void updateText() {
		tv.setText("");
		for (String s : log) {
			tv.append(s); tv.append("\n");
		}
		
		sv.fullScroll(View.FOCUS_DOWN);
		
	}
	
}
