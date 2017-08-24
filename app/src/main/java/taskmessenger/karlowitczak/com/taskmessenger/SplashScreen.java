package taskmessenger.karlowitczak.com.taskmessenger;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        UserPreferences.saveMessageInput(this,"");
        UserPreferences.saveRoomName(this,"");
        if(UserPreferences.getLogin(this).length() == 0)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(homeIntent);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }, SPLASH_TIME_OUT);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(SplashScreen.this, NavigationActivity.class);
                    homeIntent.putExtra("userName", UserPreferences.getLogin(SplashScreen.this));
                    startActivity(homeIntent);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }, SPLASH_TIME_OUT);
        }


    }
}
