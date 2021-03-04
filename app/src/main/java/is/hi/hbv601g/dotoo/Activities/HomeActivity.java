package is.hi.hbv601g.dotoo.Activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.dotoo.R;

public class HomeActivity extends AppCompatActivity {

    private TextView mWelcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mWelcomeText = (TextView) findViewById(R.id.welcomeUser);
        String user = getIntent().getStringExtra("is.hi.hbv601g.dotoo.user_result");
        mWelcomeText.setText(user);
    }
}
