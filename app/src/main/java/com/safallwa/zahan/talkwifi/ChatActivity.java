package com.safallwa.zahan.talkwifi;



import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class ChatActivity extends SampleActivityBase implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
	
	private static final String TAG = "MainActivity";
	ChatFragment mChatFrag = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
        getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Talk Wifi Chatroom" + "</font>"));
		initFragment("");
	}

	public void initFragment(String initMsg) {
    	// to add fragments to your activity layout, just specify which viewgroup to place the fragment.
    	final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	if( mChatFrag == null ){
    		//mChatFrag = ChatFragment.newInstance(this, ConnectionService.getInstance().mConnMan.mServerAddr);
    		mChatFrag = new ChatFragment();
    	}
    	
    	// chat fragment on top, do not do replace, as frag_detail already hard coded in layout.
        ft.replace(R.id.frag_chat, mChatFrag);
        ft.commit();
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, " onDestroy: nothing... ");
	}


    @Override
    public void onEmojiconBackspaceClicked(View v) {

       ChatFragment.goback(v);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {


        ChatFragment.setinput(emojicon);

    }
}
