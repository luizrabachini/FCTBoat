/*
 * FCTBoat Android App
 */
package com.fctboat;

import com.fctboat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Navigation activity
 * 
 * @author luiz
 */
public class NavActivity extends Activity {
	
	private ServerCom serverCom; // Server communication
	private BoatNavView boatNavView; // View controllers to navigation
	
	private RemoteConnection remoteConnection;
	private TextView labelMessage;
	
	private String ip, // Remote server IP
					port; // Remote server port

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_nav);
	    
	    Intent intent= getIntent();
	    ip = intent.getStringExtra("IP");
	    port = intent.getStringExtra("PORT");
	    
	    labelMessage = (TextView)findViewById(R.id.text_message);
	    
	    serverCom = new ServerCom();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		remoteConnection = new RemoteConnection(this);
		remoteConnection.execute();
	}
	
	@Override 
	public void onStop(){
		super.onStop();
		
		serverCom.closeConn();
	}
	
	private class RemoteConnection extends AsyncTask<NavActivity, Integer, Long> {
		
		private NavActivity navActivity;
		
		public RemoteConnection(NavActivity navActivity) {
			this.navActivity = navActivity;
		}
		
		protected Long doInBackground(NavActivity... activity) {
			serverCom.openConn(ip, port);
			
			return null;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	     }

	     protected void onPostExecute(Long result) {
	         if (serverCom.isConnected()) {
	        	 boatNavView = new BoatNavView(navActivity, serverCom);
	        	 setContentView(boatNavView);
	         } else {
	        	 labelMessage.setText(R.string.conn_error);
	         }
	     }
    }
}
