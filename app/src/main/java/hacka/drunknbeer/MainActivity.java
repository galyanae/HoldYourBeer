//
//        ~ Copyright (c) Anna Galian and Leonid Diner


package hacka.drunknbeer;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.flurgle.camerakit.CameraView;

public class MainActivity extends Activity {

    private android.support.v7.app.AlertDialog show;
    private boolean isZAngleStable;
    private boolean isYAngleStable;
    private boolean itWasNiceToMeetYouFinishHim;
    private boolean isDialogOpen;
    private ProgressBar progressBar;
    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private Sensor stepCounter;
    private CameraView cameraView;
    private Button button;
    private ImageView beer;
    private GameTimer gameTimer;
    private TextView timeTXT;
    public View v;
    private int steps = -1;
    private GlideDrawableImageViewTarget imageViewTarget;
    private float xAngleOfDevice, yAngleOfDevice, zAngleOfDevice;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(20);
        isDialogOpen = false;
        beer = (ImageView) findViewById(R.id.Button01);
        cameraView = (CameraView) findViewById(R.id.camera);
        timeTXT = (TextView) findViewById(R.id.timeTXT);
        gameTimer = new GameTimer(20000, 1000, timeTXT);
        steps = 0;
        openDialog();

    }
    //CountDown Game Timer
    //----------------------------------------------------------------------------------------------
    public class GameTimer extends CountDownTimer {
        public void onTick(long millisUntilFinished) {
            timeTXT.setText("00:" + millisUntilFinished / 1000);
        }
        @Override
        public void onFinish() {
            System.out.println("FINISH");
            cameraView.stop();
            if (steps >= 21) {
                killSensors();
                finish();
            } else {
                onIMageStationChange(ImageStation.DEAD);
            }
        }

        public GameTimer(long millisInFuture, long countDownInterval, TextView timeLeft) {
            super(millisInFuture, countDownInterval);
        }
    }
    //Instructions Dialog
    //----------------------------------------------------------------------------------------------
    public void openDialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.activity_instructions, null, false);
        isDialogOpen = true;
        button = (Button) view.findViewById(R.id.btn_play_instructions);
        builder.setView(view);
        show = builder.show();
        show.setView(v);
        show.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //finish();
                    //onBackPressed();
                    //finish();
                    show.dismiss();
                    onBackPressed();
                }
                return true;
            }
        });
    }

    public void activatePlayButton() {
        button.setBackgroundResource(R.drawable.play);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isDialogOpen = false;
                gameTimer.start();
                cameraView.start();
                cancel(arg0);
            }
        });
    }
    //Cancel, Resume, Stop
    //----------------------------------------------------------------------------------------------
    public void cancel(View v) {
        isDialogOpen = false;
        show.dismiss();
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(orientationListener, orientationSensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(stepCounterListener, stepCounter,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void onStop() {
        super.onStop();
        killSensors();
        gameTimer.cancel();
        cameraView.stop();
        finish();
    }
    //Getting devise rotation angle
    //----------------------------------------------------------------------------------------------
    public SensorEventListener orientationListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor orientationSensor, int acc) {
        }
        public void onSensorChanged(SensorEvent event) {
            xAngleOfDevice = Math.round(event.values[0]);
            yAngleOfDevice = Math.round(event.values[1]);
            zAngleOfDevice = Math.round(event.values[2]);
            getRotationDirections(yAngleOfDevice, xAngleOfDevice, zAngleOfDevice);
        }
    };
    //Game logic depends on device rotation angle
    //----------------------------------------------------------------------------------------------
    private enum ImageStation {
        STABLE, RIGHT, LEFT, FRONT, BACK, DEAD
    }

    public void onIMageStationChange(ImageStation imageStation) {
        switch (imageStation) {
            case STABLE:
                beer.setBackgroundResource(R.drawable.stable);
                beer.setScaleX(1f);
                break;
            case RIGHT:
                beer.setBackgroundResource(R.drawable.right);
                beer.setScaleX(1f);
                break;
            case LEFT:
                beer.setBackgroundResource(R.drawable.right);
                beer.setScaleX(-1f);
                break;
            case FRONT:
                beer.setBackgroundResource(R.drawable.front);
                beer.setScaleX(1f);
                break;
            case BACK:
                beer.setBackgroundResource(R.drawable.back);
                beer.setScaleX(1f);
                break;
            case DEAD:
                if (isDialogOpen) {
                    System.out.println("Dialog is open");
                } else {
                    killSensors();
                    steps = 0;
                    Intent toLoose = new Intent(MainActivity.this, LooseScreen.class);
                    startActivity(toLoose);
                    finish();
                }
                break;
        }
    }

    private void getRotationDirections(float y, float x, float z) {
        isZAngleStable = z <= 5 && z >= -5;
        isYAngleStable = y >= -5 && y <= 5;
        boolean isAllStable = isYAngleStable && isZAngleStable;
        boolean isYAngleUnStableToTheLeft = isYAngleStable && z < 15 && z > 5;
        boolean isYAngleUnStableToTheRight = isYAngleStable && z > -15 && z < -5;
        itWasNiceToMeetYouFinishHim = y > 10 || y < -10 || z > 15 || z < -15;
        boolean isZAngleUnStableToTheFront = isZAngleStable && y <= 10 && y > 5;
        boolean isZAngleUnStableToTheBack = isZAngleStable && y >= -10 && y < -5;
        if (isAllStable) {
            if (isDialogOpen) {
                activatePlayButton();
            } else {
                onIMageStationChange(ImageStation.STABLE);
            }
        } else if (isDialogOpen) {
            button.setBackgroundResource(R.drawable.play_disable);
            button.setClickable(false);
        } else if (!isDialogOpen) {
            if (isYAngleUnStableToTheRight) {
                onIMageStationChange(ImageStation.RIGHT);
            }
            if (isZAngleUnStableToTheBack) {
                onIMageStationChange(ImageStation.BACK);
            }
            if (isZAngleUnStableToTheFront) {
                onIMageStationChange(ImageStation.FRONT);
            }
            if (itWasNiceToMeetYouFinishHim) {
                onIMageStationChange(ImageStation.DEAD);
            }
            if (isYAngleUnStableToTheLeft) {
                onIMageStationChange(ImageStation.LEFT);
            }
        }
    }

    //Pedometer game logic
    //----------------------------------------------------------------------------------------------
    public SensorEventListener stepCounterListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor stepCounter, int acc) {
        }
        public void onSensorChanged(SensorEvent event) {
            steps++;
            progressBar.setProgress(steps);
            if (steps >= 21) {
                killSensors();
                Intent toWin = new Intent(MainActivity.this, WinScreen.class);
                startActivity(toWin);
                finish();
            }
        }

    };
    @Override
    public void onBackPressed(){
        Intent toLanding = new Intent(MainActivity.this, LandingScreen.class);
        startActivity(toLanding);
        finish();
    }

    public void killSensors(){
        sensorManager.unregisterListener(orientationListener);
        sensorManager.unregisterListener(stepCounterListener);
    }
}