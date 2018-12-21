package com.example.lauti.entregable5;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    /** Messenger for communicating with the service. */
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean isBound;

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This isBound called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This isBound called when the connection with the service has been
            // unexpectedly disconnected -- that isBound, its process crashed.
            mService = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBound) return;
				// Create and send a message to the service, using a supported 'what' value
				Message msg = Message.obtain(null, 1);
				try {
					mService.send(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
       // bindService(new Intent(this, MessengerService.class), mConnection,  Context.BIND_AUTO_CREATE);
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.example.lauti.entregable4", "com.example.lauti.entregable4.MyMessengerService"));
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
}
