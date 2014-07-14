/*
 * FCTBoat Android App
 */
package com.fctboat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Send commands of navigation to remote server
 * 
 * @author luiz
 */
public class ServerCom {

	private Socket socket;	// Communication with remote service
	private OutputStream out; // Out stream to send commands 
	
	public ServerCom() {
	}
	
	/**
	 * Create connection with remote service
	 * 
	 * @param ip IP of remote service
	 * @param port Port of remote service
	 * @return True, if connection establish, or false, if not
	 */
	public boolean openConn(String ip, String port) {
		try {
			int rPort = Integer.parseInt(port);
			socket = new Socket(ip, rPort);
			out = socket.getOutputStream();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			socket = null;
			out = null;
		}
		
		return false;
	}
	
	/**
	 * Send a command to remote service (in Raspberry Pi)
	 * 
	 * @param cmd Command string
	 * @return True, if command was sent, or false, if error occurred
	 */
	public boolean sendCommand(String cmd) {
		try {
			out.write(cmd.getBytes());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Close remote connection with server
	 * 
	 * @return True, if connection was closed, or false, if error occurred
	 */
	public boolean closeConn() {
		if (socket == null) {
			return true;
		}
		
		try {
			socket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Indicate if connection to remote server is open
	 * 
	 * @return True, if open, or false, if not
	 */
	public boolean isConnected() {
		if (socket != null && out != null) {
			return true;
		} else {
			return false;
		}
	}
}
