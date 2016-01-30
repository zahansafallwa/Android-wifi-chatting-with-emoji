
package com.safallwa.zahan.talkwifi;

/**
 * Created by Zahan on 1/22/2016.
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SampleActivityBase extends FragmentActivity {

    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        initializeLogging();
    }

    public void initializeLogging() {

        //not needed

    }
}
