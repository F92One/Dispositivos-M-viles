package com.example.lauti.entregable4;

/**
 *
 * @Author: Oneto, Fernando
 * @Author: Diez, Lautaro
 *
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    public static final String ITERATION = "iteration";
    private static final int ITERATIONS = 4;    // number of iterations
    private TextView intentServiceText;
    private TextView serviceText;
    private Button buttonIS;
    private Button buttonS;
    private LocalReceiver receiver = new LocalReceiver();

    // variables to control the broadcast receiver
    public static final int INTENT_SERVICE_ID = 2;
    public static final int SERVICE_ID = 1;
    public static final int BOUND_SERVICE_ID = 3;
    public static final String RESPONSE = "response";
    public static final String RESPONSE_ID = "id";


    ////////////////////////////////////////////////////////////////////////////////////
    /////////////////           Exercise 5          ///////////////////////////////////
    private TextView boundServiceText;

    //create the listener class to receive the notifications to the workers that they finish
    private class LocalReceiver extends BroadcastReceiver {

        @Override
        //must override listener
        public void onReceive(Context context, Intent intent){
            //get from the extra the message
            int response = intent.getIntExtra(RESPONSE_ID, -1);
            String msg = intent.getStringExtra(RESPONSE); // get the message
            switch (response) {
                case INTENT_SERVICE_ID:
                    //show in the text view the final message
                    intentServiceText.setText(msg);
                    //also, a Toast will show in companion to the text view to reflect the end of the worker thread
                    // Toast.makeText(MainActivity.this, "Intent response ok", Toast.LENGTH_SHORT).show();
                    break;
                case SERVICE_ID:
                    //show in the text view the final message
                    serviceText.setText(msg);
                    //also, a Toast will show in companion to the text view to reflect the end of the worker thread
                    // Toast.makeText(MainActivity.this, "Intent response ok", Toast.LENGTH_SHORT).show();
                    break;


                // Exercise 5
                case BOUND_SERVICE_ID:
                    boundServiceText.setText(msg);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        //set up the args to create register the receiver. He will manage the response to the backgroudn job
        IntentFilter myintentfilter = new IntentFilter();
        myintentfilter.addAction(MyIntentService.RESPONSE_ACTION); //important step
        //register receiver "link" the activity main with the background jobs
        registerReceiver(receiver, myintentfilter);
    }

    // Get the layout compenents control
    private void initComponents() {
        intentServiceText = (TextView) findViewById(R.id.isText);
        serviceText = (TextView) findViewById(R.id.sText);
        buttonIS = (Button) findViewById(R.id.buttonIS);
        buttonIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start IntentService", Toast.LENGTH_SHORT).show();
                //create the iterations and intents and start background work
                for (int i=1; i <= ITERATIONS; i++){
                    Intent intent = new Intent(MainActivity.this, MyIntentService.class);
                    //set the iteration number to the extra, will be recovered in the intentService
                    intent.putExtra(ITERATION, i);
                    startService(intent);
                }
            }
        });
        buttonS = (Button) findViewById(R.id.buttonS);
        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start Service", Toast.LENGTH_SHORT).show();
                //create the iterations and intents and start background work
                for (int i=1; i <= ITERATIONS; i++){
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    //set the iteration number to the extra, will be recovered in the intentService
                    intent.putExtra(ITERATION, i);
                    startService(intent);
                }

            }
        });
        //set up the args to create register the receiver. He will manage the response to the backgroudn job
        IntentFilter myintentfilter = new IntentFilter();
        myintentfilter.addAction(MyIntentService.RESPONSE_ACTION); //important step
        //register receiver "link" the activity main with the background jobs
        registerReceiver( receiver, myintentfilter);
        IntentFilter sintentfilter = new IntentFilter();
        sintentfilter.addAction(MyService.RESPONSE_MESSAGE); //important step
        //register receiver "link" the activity main with the background jobs
        registerReceiver( receiver, sintentfilter);

        // Exercise 5
        boundServiceText = (TextView) findViewById(R.id.bsText);

        IntentFilter msintentfilter = new IntentFilter();
        msintentfilter.addAction(MyMessengerService.MESSENGER_MSG); //important step
        //register receiver "link" the activity main with the background jobs
        registerReceiver( receiver, msintentfilter);
    }

}
