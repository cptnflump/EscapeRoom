package com.createful.escaperoom.escaperoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import java.util.concurrent.TimeUnit;

import static com.createful.escaperoom.escaperoom.RoomActivity.getRoomIn;
import static com.createful.escaperoom.escaperoom.RoomActivity.pass;

public class Timer extends AppCompatActivity {
    RoomActivity activity = RoomActivity.roomActivityInstance;
    Intent intent = RoomActivity.getTimeIntent();
    long timeToTick;

    public Timer(){
        startTimer(getRoomIn().getGivenTime());
    }

    /**
     * starts the timer given the rooms <code>getGivenTimer() value</code>
     *
     * @param time time value given in format MM:SS
     */
    public void startTimer(String time) {


        final String finishText = "Time up";
        long minutes = Integer.parseInt(time.substring(0, 2));
        long seconds = Integer.parseInt(time.substring(3));
        long toSeconds = (minutes * 60L) + seconds;
        long result = TimeUnit.SECONDS.toMillis(toSeconds);

        //create countdown timer for the room
        new CountDownTimer(result, 100) {
            private static final String FORMAT = "%02d:%02d";

            public void onTick(long millis) {
                timeToTick = millis;
                String timeLeft = String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                //stop the timer if the user is successful
                if (RoomActivity.getPass()) {
                    cancel();
                } else {
                    updateTimerOnActivity(timeLeft);
                }
            }

            public void onFinish() {
                updateTimerOnActivity(finishText);
                for(int i = 0;i<RoomActivity.getCodeViews().size();i++){
                    RoomActivity.getCodeViews().get(i).findViewById(i).setClickable(false);
                    RoomActivity.getCodeViews().get(i).findViewById(i + 100).setFocusable(false);
                    RoomActivity.getCodeViews().get(i).findViewById(i + 200).setClickable(false);
                }

                try{
                    //hides the keyboard after time up
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(R.id.success_msg).getWindowToken(), 0);
                } catch (Exception e) {
                    Log.i("ERR","KEYBOARD NOT UP");
                }
                activity.stopService(intent);
            }
        }.start();
    }

    void updateTimerOnActivity(String timeLeft){
        if (activity!=null){
            activity.updateTimeDisplay(timeLeft);
        }
    }
}
