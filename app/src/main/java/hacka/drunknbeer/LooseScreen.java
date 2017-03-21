//
//        ~ Copyright (c) Anna Galian and Leonid Diner

package hacka.drunknbeer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LooseScreen extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loose_screen);
        mediaPlayer= MediaPlayer.create(this,R.raw.loose_sound);
        mediaPlayer.start();
    }

    public void cancel(View view) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }}
        Intent toMain = new Intent(LooseScreen.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent toMain = new Intent(LooseScreen.this, LandingScreen.class);
        startActivity(toMain);
        finish();
    }
}
