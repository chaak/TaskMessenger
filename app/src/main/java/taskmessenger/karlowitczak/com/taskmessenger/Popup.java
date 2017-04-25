package taskmessenger.karlowitczak.com.taskmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class Popup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        TextView taskTitle = (TextView)findViewById(R.id.taskTitle);
        taskTitle.setText(getIntent().getStringExtra("NAME"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width * 8, height * 6);
    }
}
