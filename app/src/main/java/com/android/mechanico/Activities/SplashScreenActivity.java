package com.android.mechanico.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.mechanico.R;
import com.android.mechanico.utils.CommonUtils;



/*Created by akhil on 16/2/18.
 */

/*
 *splash screen for the app
  * respective layout:-activity_splash_screen
 */

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity();
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * starts respective activity
     */
    private void startActivity(){
        CommonUtils.getPreferences(SplashScreenActivity.this);
        if(CommonUtils.isLogin){
            /*Intent intent = new Intent(this,NavigationDrawerActivity.class);
            startActivity(intent);*/
        }

        else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }

}
