package com.createful.escaperoom.escaperoom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;


public class RoomActivity extends AppCompatActivity implements View.OnClickListener {
    //if pass is true the user has completed all tasks
    static boolean pass = false;
    //sets a standard placeholder room for roomIn
    static Room roomIn = new Room("null", "00:00", new String[]{"bbbb", "aaaa", "aaaa", "aaaa"});
    //button texts for answering correctly or incorrectly
    String btnCorrect = "CORRECT";
    String btnWrong = "WRONG";
    //list of every task
    protected static ArrayList<LinearLayout> codeViews = new ArrayList<>();
    LinearLayout linearLayout;
    //number of tasks on the last row
    int lastRowNum;
    //tells us if the editTexts are focusable or not
    boolean focusableEdits = false;
    static Intent intent;

    EditText timerEditText;


    static RoomActivity roomActivityInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_room);

        //passing Room data into this activity
        Bundle data = getIntent().getExtras();
        roomIn = data.getParcelable("room");

        roomActivityInstance = this;

        //setting timer
        timerEditText = (EditText) findViewById(R.id.CountDown);
        timerEditText.setText(roomIn.getGivenTime());

        linearLayout = (LinearLayout) findViewById(R.id.scroll_container);



        //declaring what actions the start timer button should perform
        final Button startTime = (Button) findViewById(R.id.TimerStart);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startTimer(roomIn.getGivenTime());
                startTime.setVisibility(INVISIBLE);
                setEditFocus();

                intent = new Intent(getApplicationContext(), MyService.class);
                Log.i("INTENT: ",intent.toString());
                startService(intent);
                //passing Room data into this activity
                //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

        //creating code views
        populateCodes();

    }

    @Override
    public void onClick(View v) {
    }




    /**
     * called if all checkboxes are ticked
     * removes all code views and replaces it with success text,
     * as well as stopping the timer
     */
    public void isComplete() {
        for(int i = 0;i<codeViews.size();i++){
            codeViews.get(i).setVisibility(GONE);
        }
        findViewById(R.id.codes_scroll_view).setVisibility(GONE);

        TextView tV = (TextView) findViewById(R.id.success_msg);
        tV.setVisibility(VISIBLE);

        //hides the keyboard after successful entry
        try{
            //hides the keyboard after time up
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.success_msg).getWindowToken(), 0);
        } catch (Exception e) {
            Log.i("ERR","KEYBOARD NOT UP");
        }
        pass = true;

        stopService(intent);
    }

    /**
     * creates all of the code views
     */
    public void populateCodes() {
        int toPop = layoutsNeeded(); //number of layouts to be made

        if(toPop > 0) {
            for (int i = 0; i < toPop; i++) {
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                layout.setGravity(Gravity.CENTER);
                for (int j = 0; j<4;j++){
                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.code_view, null);
                    layout.addView(view);
                    //adds to list of layouts
                    codeViews.add(layout);
                }
                linearLayout.addView(layout);
            }
        }
        if(lastRowNum != 0){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            layout.setGravity(Gravity.CENTER);
            for (int i = 0; i<lastRowNum;i++){
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.code_view, null);
                layout.addView(view);
                //adds to list of layouts
                codeViews.add(layout);
            }
            linearLayout.addView(layout);
        }


        for(int i = 0; i<codeViews.size();i++){
            //BUTTON IDS START AT 0
            Button btn = (Button) codeViews.get(i).findViewById(R.id.code_submit);
            btn.setId(i);
            //EDIT TEXT IDS START AT 100
            EditText edit = (EditText) codeViews.get(i).findViewById(R.id.code_entry);
            edit.setId(100 + i);
            //CHECKBOX IDS START AT 200
            CheckBox check = (CheckBox) codeViews.get(i).findViewById(R.id.code_check);
            check.setId(200 + i);
            check.setClickable(false);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnPress(v);
                }
            });
        }
    }

    /**
     * figures out how many rows are needed
     * @return the number of rows
     */
    public int layoutsNeeded() {
        int noOfLayouts = roomIn.getRoomCode().length / 4;
        if (roomIn.getRoomCode().length % 4 != 0) {
            lastRowNum = roomIn.getRoomCode().length % 4;
        }
        return noOfLayouts;
    }

    /**
     * changes the state of every single edit text to either focusable or non focusable
     */
    public void setEditFocus(){
        if(focusableEdits){
            for(int i = 0;i<codeViews.size();i++){
                codeViews.get(i).findViewById(100 + i).setFocusable(false);
                codeViews.get(i).findViewById(i).setEnabled(false);
                Log.i("FOCUS_SET_FALSE",codeViews.get(i).findViewById(100 + i).toString());
            }
            focusableEdits = false;
        } else {
            for(int i = 0;i<codeViews.size();i++){
                codeViews.get(i).findViewById(100 + i).setFocusable(true);
                codeViews.get(i).findViewById(100 + i).setFocusableInTouchMode(true);
                codeViews.get(i).findViewById(i).setEnabled(true);
                Log.i("FOCUS_SET_TRUE",""+codeViews.get(i).findViewById(100 + i).isFocusable());
            }
            focusableEdits = true;
        }
    }

    /**
     * called every single time onClick is used dynamically
     * @param v the view that has been pressed
     */
    public void btnPress(View v){
        Log.i("TAG", "The index is " + v.getId());
        Button b = (Button) v;
        CheckBox c = (CheckBox) findViewById(v.getId() + 200);
        EditText e = (EditText) findViewById(v.getId() + 100);
        Log.i("CODE_IS: ",roomIn.getRoomCode()[v.getId()]);
        Log.i("CODE_ENTERED: ",e.getText().toString());
        if(roomIn.getRoomCode()[v.getId()].equals(e.getText().toString())){
            c.setChecked(true);
            e.setFocusable(false);
            b.setClickable(false);
            b.setText(btnCorrect);
        } else {
            b.setText(btnWrong);
        }
        //checking if all complete
        boolean allTicked = true;
        for(int i = 0;i<codeViews.size();i++){
            CheckBox checkText = (CheckBox) findViewById(i + 200);
            if (!checkText.isChecked()){
                allTicked = false;
            }
        }
        if(allTicked){
            isComplete();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    public static boolean getPass(){
        return pass;
    }

    public static ArrayList<LinearLayout> getCodeViews(){
        return codeViews;
    }

    public static Room getRoomIn(){
        return roomIn;
    }

    public void updateTimeDisplay(final String timeLeft) {
        Log.i("RUN","UPDATER RUNNING");
        timerEditText.post(new Runnable() {
            @Override
            public void run() {

                timerEditText.setText(timeLeft);
            }
        });
    }

    public static Intent getTimeIntent(){
        return intent;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(!pass){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("are you sure you want to exit?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RoomActivity.super.onBackPressed();
                            dialog.cancel();
                            codeViews.clear();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    null);

            AlertDialog leaveAlert = builder1.create();
            leaveAlert.show();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("test","value");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i("BUNDLE",savedInstanceState.getString("test"));
    }
}