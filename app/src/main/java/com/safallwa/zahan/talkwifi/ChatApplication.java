package com.safallwa.zahan.talkwifi;

import android.app.Application;

public class ChatApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public ChatConnection connection;
}
