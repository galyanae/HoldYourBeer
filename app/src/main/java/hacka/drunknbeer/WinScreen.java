//
//        ~ Copyright (c) Anna Galian and Leonid Diner

package hacka.drunknbeer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class WinScreen extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);
        mediaPlayer=MediaPlayer.create(this,R.raw.win_sound);
        mediaPlayer.start();
    }

    public void cancel(View view) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }}
        Intent toMain = new Intent(WinScreen.this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent toLanding = new Intent(WinScreen.this, LandingScreen.class);
        startActivity(toLanding);
        finish();
    }
}
