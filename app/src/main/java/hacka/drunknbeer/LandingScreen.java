//
//        ~ Copyright (c) Anna Galian and Leonid Diner

package hacka.drunknbeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LandingScreen extends AppCompatActivity implements View.OnClickListener {
    private boolean clicked = false;
    private boolean hasperm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        Button play = (Button) findViewById(R.id.btn_play_now);
        play.setOnClickListener(this);
        Button instructions = (Button) findViewById(R.id.btn_instructions);
        instructions.setOnClickListener(this);
        Button about = (Button) findViewById(R.id.btn_about);
        about.setOnClickListener(this);

        // = PermissionsHandler.requestPermissions(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btn_play_now):
                hasperm = PermissionsHandler.requestPermissions(this);
                if (hasperm ){
                    Intent playNow = new Intent(LandingScreen.this, MainActivity.class);
                    startActivity(playNow);
                    finish();
                }else{
                    notifyUser();
                }
                break;
            case (R.id.btn_instructions):
                hasperm = PermissionsHandler.requestPermissions(this);
                if (hasperm ) {
                    Intent instructions = new Intent(LandingScreen.this, MainActivity.class);
                    startActivity(instructions);
                    finish();
                }else{
                    notifyUser();
                }
                break;
            case (R.id.btn_about):
                hasperm = PermissionsHandler.requestPermissions(this);
                if (hasperm ) {
                    Intent about = new Intent(LandingScreen.this, About.class);
                    startActivity(about);
                    finish();
                }else{
                    notifyUser();
                }
                break;
        }
    }

    private void notifyUser(){
        Toast.makeText(this, "Please grant all requested permissions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        if (!clicked) {
            Toast.makeText(this, "Click 'Back' button again to exit", Toast.LENGTH_SHORT).show();
            clicked = true;
        }else {
            finish();
        }

    }
}
