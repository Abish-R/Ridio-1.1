/** Ridio v1.0.1
 * 	Purpose	   : SplashScreen 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 
 * **/


package helix.ridioandroidstudio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends Activity {
	Ridiologin rid_login=new Ridiologin();
	/**Class Variables*/
	String status_user,status_login;
	String fontPath = "fonts/Lato-Bold.ttf";
	TextView helix_textf,helix_textanager,helix_made_with,helix_at;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        initializeViews();
        /**Getting Vendor ID*/
        SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
        final String value = sp.getString("value", "");
        Log.e("Username", value);
//        Log.d(status_login, status_user);
        Thread background = new Thread() {
            public void run() {                 
                try {
                    /** Thread will sleep for 3 seconds*/
                    sleep(3000);
                     
                    /**Validation and Conditions for login*/
                    if(value!=""){
                    	/**Starts Main Screen*/
                    	Intent i=new Intent(getBaseContext(),MainScreen.class);
                    	startActivity(i);
                    	finish();
                    }
                    else{
                    	/**Starts SignIn&UP Controller Screen*/
                    	Intent i=new Intent(getBaseContext(),SignInSignUpController.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        };
         
        /** start thread*/
        background.start();
    }
    /**initialize and set views as per the requirement alignment*/
	private void initializeViews() {
		helix_textf= (TextView)findViewById(R.id.helix_textf);
        helix_made_with= (TextView)findViewById(R.id.helix_made_with);
        helix_at= (TextView)findViewById(R.id.helix_at);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        helix_made_with.setTypeface(tf);
        helix_at.setTypeface(tf);
        helix_textf.setTypeface(tf);
        String s= helix_textf.getText().toString();
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.3f), 0,1, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.3f), 6,7, 0); // set size
        helix_textf.setText(ss1);
		
	}

}
