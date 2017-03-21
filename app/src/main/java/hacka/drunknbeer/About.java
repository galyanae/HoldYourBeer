//
//        ~ Copyright (c) Anna Galian and Leonid Diner

package hacka.drunknbeer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class About extends AppCompatActivity implements View.OnClickListener{

    private Button playNowAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        playNowAbout = (Button) findViewById(R.id.btn_play_from_about);
        playNowAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent toMain = new Intent(About.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent toLanding = new Intent(About.this, LandingScreen.class);
        startActivity(toLanding);
        finish();
    }
}
