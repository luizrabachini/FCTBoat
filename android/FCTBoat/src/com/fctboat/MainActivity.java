/*
 * FCTBoat Android App
 */
package com.fctboat;

import com.fctboat.R;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Main activity (show connection options)
 * 
 * @author luiz
 */
public class MainActivity extends Activity implements OnClickListener {

	private Button btnConnect;
	
	private EditText txtIp, // Text filed to insert IP address
					 txtPort; // Text field to insert Port of remote service
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addButtonsFunctions();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Add buttons functions
	 */
	private void addButtonsFunctions() {
		btnConnect = (Button)findViewById(R.id.btn_connect);
		txtIp = (EditText)findViewById(R.id.txt_ip);
		txtPort = (EditText)findViewById(R.id.txt_port);
		
		btnConnect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_connect:
			String ip = txtIp.getText().toString();
			String  port = txtPort.getText().toString();
			
			// Check if IP and Port are defined
			if (ip == null || ip.isEmpty() ||
					port == null || port.isEmpty()) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"IP or Port invalids!", Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			
			// Start view of navigation
			Intent intent = new Intent(this, NavActivity.class);
			intent.putExtra("IP", ip);
			intent.putExtra("PORT", port);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
