package com.example.lauti.entregable4;
/**
 *
 * @author: Oneto, Fernando
 * @author: Diez, Lautaro
 *
 */

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.util.Random;

public class MyMessengerService extends Service {

    private Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler
    private final Random mGenerator = new Random();

    private static final int MAX = 100;
    private static final int MIN = 0;
    static final String MESSENGER_MSG = "Numero random  ";
    static final int NEXT_RANDOM = 1; // command to the service to display a message

    public MyMessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Toast.makeText(getApplicationContext(), "Binding Service", Toast.LENGTH_SHORT).show();

        return mMessenger.getBinder();
    }

    // Handler of incoming messages from clients
    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT_RANDOM:
                    String msg1 = MESSENGER_MSG + (mGenerator.nextInt(MAX) + MIN);
                    Intent response = new Intent();
                    response.putExtra(MainActivity.RESPONSE_ID, MainActivity.BOUND_SERVICE_ID);
                    response.putExtra(MainActivity.RESPONSE, msg1);
                    response.setAction(MESSENGER_MSG);
                    Toast.makeText(getApplicationContext(), "Binded", Toast.LENGTH_SHORT).show();
                    sendBroadcast(response);
                    break;
                    default:
                        super.handleMessage(msg);
            }
        }
    }
}
