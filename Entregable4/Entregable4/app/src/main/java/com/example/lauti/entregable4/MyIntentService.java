package com.example.lauti.entregable4;
/**
 *
 * @Author: Oneto, Fernando
 * @Author: Diez, Lautaro
 *
 */

import android.app.IntentService;
import android.content.Intent;


public class MyIntentService extends IntentService {

    protected final static String RESPONSE_ACTION = "worker thread id creado en iteracion ";
    private String msg;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //get iteration number of extra
        int iteration = intent.getIntExtra(MainActivity.ITERATION,-1);
        //create and fill the response to broadcast delivery
        Intent response = new Intent();
        msg = (RESPONSE_ACTION + iteration);
        response.setAction(RESPONSE_ACTION); //important step
        response.putExtra(MainActivity.RESPONSE_ID, MainActivity.INTENT_SERVICE_ID);
        response.putExtra(MainActivity.RESPONSE, msg);

        //show data in the Logcat
        //Log.d(TAG, msg);

        //notify to the interest that i finished
        sendBroadcast(response);
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}