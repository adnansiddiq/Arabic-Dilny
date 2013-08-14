package com.yahia.libs.InternetConnections;

public interface onConnectionController {
	public abstract void onConnectionDone(int code,String response);
 	public abstract void onConnectionError(int code,String response);
 	public abstract void onConnectionStarted(int code,String response);
}
