package com.example.fernando.entregable4;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {

    protected final static String RESPONSE="";
    protected final static String RESPONSE_ACTION = "worker thread id creado en iteracion ";
    private final static String TAG = MyIntentService.class.getCanonicalName();
    private String msg;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //get iteration number of extra
        Integer iteration = intent.getIntExtra(MainActivity.ITERATION,-1);
        //create and fill the response to broadcast delivery
        Intent response = new Intent();
        msg = (RESPONSE_ACTION+iteration);
        response.setAction(RESPONSE_ACTION); //important step
        response.putExtra(RESPONSE,msg);
        //show data in the Logcat
        Log.d(TAG,msg);
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //notify to the interest that i finish
        sendBroadcast(response);
    }
}