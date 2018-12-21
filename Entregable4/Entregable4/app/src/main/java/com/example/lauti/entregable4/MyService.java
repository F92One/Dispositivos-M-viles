package com.example.lauti.entregable4;

/**
 *
 * @Author: Oneto, Fernando
 * @Author: Diez, Lautaro
 *
 */

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class MyService extends Service {

    private String msg1;
    protected final static String RESPONSE_MESSAGE = "worker thread id creado en iteracion ";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                Intent intent = (Intent) msg.obj;
                // Get iteration number from extra
                int iteration = intent.getIntExtra(MainActivity.ITERATION, -1);

                //Create the response
                Intent response = new Intent();
                msg1 = RESPONSE_MESSAGE + iteration;

                response.putExtra(MainActivity.RESPONSE, msg1);
                response.putExtra(MainActivity.RESPONSE_ID, MainActivity.SERVICE_ID);
                //notify to the interest that i finished
                response.setAction(RESPONSE_MESSAGE); //important step

                sendBroadcast(response);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-internsive work will not disrupt our UI.

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        // Log.e("Service", "created");
    }

    /**
     *
     * onStartCommand: If Server is started, the next intents aren't going to generate an "onCreate" method. They just pass through this method. Se ejecuta cuando el servicio recibe un intent lanzado mediante el comando startService. Si el servicio ya está iniciado,
     * los intents sucesivos no volverán a generar un “onCreate” si no que pasaran directamente aquí. Es importante tener en cuenta que
     * si se crean hilos en este método, debe comprobarse que el hilo no esté ya funcionando o podrían accidentalmente crearse hilos nuevos
     * cada vez que se pasa por aquí.
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we are stopping when we finish the job
        final Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;   // set the intent to the message

        new Thread(new Runnable() {
            @Override
            public void run() {
                mServiceHandler.sendMessage(msg);
            }
        }).start();

        // If we get killed, after returning from here, restart
        return START_STICKY; // service will be explicity started and stopped

    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "service done", Toast.LENGTH_SHORT).show();
        //super.onDestroy();
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binder, so return null
        return null;
    }

}
