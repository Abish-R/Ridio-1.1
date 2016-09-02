/** Ridio v1.0.1
 * 	Purpose	   : SignIn and SignUp Controller
 *  Created by : Abish 
 *  Created Dt : 15-01-2016
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SignInSignUpController extends Activity {
	Button signin,signup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Window window = this.getWindow();
//		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		
		setContentView(R.layout.signin_signup_control);
//		window.setStatusBarColor(this.getResources().getColor(R.color.maroon));
		//setContentView(R.layout.test);
		signin=(Button)findViewById(R.id.signin);
		signup=(Button)findViewById(R.id.signup);
		/**Navigate to login page*/
		signin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent start_signin=new Intent(getApplicationContext(),Ridiologin.class);
				startActivity(start_signin);
				finish();
			}
		});
		/**Navigate to sign Up page*/
		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent start_signup=new Intent(getApplicationContext(),SignUp.class);
				startActivity(start_signup);
				finish();
			}
		});
	}
}
