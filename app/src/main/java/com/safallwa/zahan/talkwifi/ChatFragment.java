package com.safallwa.zahan.talkwifi;

import java.util.ArrayList;
import java.util.List;

import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.safallwa.zahan.talkwifi.ChatConnection.ConnectionListener;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * chat fragment attached to main activity.
 */
public class ChatFragment extends ListFragment implements ConnectionListener {
	private static final String TAG = "ChatFrag";
	
	private ArrayList<String> mMessageList = new ArrayList<String>();   // a list of chat msgs.
    private ArrayAdapter<String> mAdapter= null;
	private CheckBox mCheckBox;
    private static EditText  inputEditText;
	private View hiddenPanel;
    
    private Handler mUpdateHandler;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// inflate the fragment's res layout. 
        View contentView = inflater.inflate(R.layout.chat_frag, container, false);  // no care whatever container is.
        inputEditText = (EditText)contentView.findViewById(R.id.edit_input);
		hiddenPanel = contentView.findViewById(R.id.hidden_panel);
		mCheckBox = (CheckBox) contentView.findViewById(R.id.use_system_default);
		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

				slideUpDown(hiddenPanel);

			}
		});
		setEmojiconFragment(false);
        final ImageButton sendBtn = (ImageButton)contentView.findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// send the chat text in current line to the server
				String inputMsg = inputEditText.getText().toString();
				inputEditText.setText("");
				ChatApplication application = (ChatApplication) getActivity().getApplication();
		    	application.connection.sendMessage(inputMsg);
			}
        });
        
        mAdapter = new ChatMessageAdapter(getActivity(), mMessageList);
        setListAdapter(mAdapter);  // list fragment data adapter 
        
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
                appendChatMessage(chatLine);
            }
        };
        ChatApplication application = (ChatApplication) getActivity().getApplication();
        application.connection.setHandler(mUpdateHandler);
        application.connection.registerListener(this);
            
        return contentView;
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        FragmentManager fragManager = getFragmentManager();
        fragManager
                .beginTransaction()
                .replace(R.id.hidden_panel, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }


    @Override 
    public void onDestroyView(){ 
    	super.onDestroyView(); 
    	
    	ChatApplication application = (ChatApplication) getActivity().getApplication();
        application.connection.setHandler(null);
        application.connection.unregisterListener(this);
    	Log.d(TAG, "onDestroyView: ");
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {  // invoked after fragment view created.
        super.onActivityCreated(savedInstanceState);
        
        if( mMessageList.size() > 0){
        	getListView().smoothScrollToPosition(mMessageList.size()-1);
        }
        
        setHasOptionsMenu(true);
        Log.d(TAG, "onActivityCreated: chat fragment displayed ");
    }
    
    /**
     * add a chat message to the list, return the format the message as " sender_addr : msg "
     */
    public void appendChatMessage(String row) {
    	mMessageList.add(row);
    	getListView().smoothScrollToPosition(mMessageList.size() - 1);
    	mAdapter.notifyDataSetChanged();  // notify the attached observer and views to refresh.
    	return;
    }

    public static void setinput(Emojicon emojicon) {
        EmojiconsFragment.input(inputEditText, emojicon);


    }

    public static void goback(View v) {
        EmojiconsFragment.backspace(inputEditText);

    }

    final class ChatMessageAdapter extends ArrayAdapter<String> {

    	public static final int VIEW_TYPE_MYMSG = 0;
    	public static final int VIEW_TYPE_INMSG = 1;
    	public static final int VIEW_TYPE_COUNT = 2;    // msg sent by me, or all incoming msgs
    	private LayoutInflater mInflater;
    	
		public ChatMessageAdapter(Context context, List<String> objects){
			super(context, 0, objects);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
		
		@Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }
		
		@Override
        public int getItemViewType(int position) {
			String item = this.getItem(position);
			if(item.startsWith("me: ")){
				return VIEW_TYPE_MYMSG;
			} else {
				return VIEW_TYPE_INMSG;
			}
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;  // old view to re-use if possible. Useful for Heterogeneous list with diff item view type.
			String item = this.getItem(position);
			boolean mymsg = false;
			
			if ( getItemViewType(position) == VIEW_TYPE_MYMSG){
				if( view == null ){
	            	view = mInflater.inflate(R.layout.chat_row_mymsg, null);  // inflate chat row as list view row.
	            }
				mymsg = true;
				// view.setBackgroundResource(R.color.my_msg_background);
			} else {
				if( view == null ){
	            	view = mInflater.inflate(R.layout.chat_row_inmsg, null);  // inflate chat row as list view row.
	            }
				// view.setBackgroundResource(R.color.in_msg_background);
			}
			
            TextView sender = (TextView)view.findViewById(R.id.sender);
            
            TextView msgRow = (TextView)view.findViewById(R.id.msg_row);
            msgRow.setText(item);
            TextView time = (TextView)view.findViewById(R.id.time);
            
            return view;
		}
    }

	@Override
	public void onConnectionReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionDown() {
		// TODO Auto-generated method stub
		getActivity().finish();
	}

    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        else {

            Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_down);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }


}
