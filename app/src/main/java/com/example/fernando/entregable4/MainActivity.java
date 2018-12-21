package com.example.fernando.entregable4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String ITERATION = "iteration";
    private TextView isText;
    private LocalReceiver receiver = new LocalReceiver();

    //create the listener class to receive the notifications to the workers that they finish
    private class LocalReceiver extends BroadcastReceiver{

        @Override
        //must override listener
        public void onReceive(Context context, Intent intent){
            //get from the extra the message
            String msg = intent.getStringExtra(MyIntentService.RESPONSE);//RESPONSE
            //show in the text view the final message
            isText.setText(msg);
            //also, a Toast will show in companion to the text view to reflect the end of the worker thread
            Toast.makeText(MainActivity.this, "Intent response ok", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get control to the textView of layout
        isText = findViewById(R.id.isText);
        //get control to the button of layout
        Button button = findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create the iterations and intents and start background work
                for (int i=0; i<4; i++){
                    //create new intenr with context and (target?)
                    Intent intent = new Intent(MainActivity.this, MyIntentService.class);
                    //set the iteration number to the extra, will be recovered in the intentService
                    intent.putExtra(ITERATION,i);
                    //run
                    startService(intent);
                }
                //explicity call to destroy, is necesary?
                onStop();
            }
        });
        //set up the args to create register the receiver. He will manage the response to the backgroudn job
        IntentFilter myintentfilter = new IntentFilter();
        myintentfilter.addAction(MyIntentService.RESPONSE_ACTION); //important step
        //register receiver "link" the activity main with the background jobs
        registerReceiver( receiver, myintentfilter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        // maybe only call in this way --> unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
