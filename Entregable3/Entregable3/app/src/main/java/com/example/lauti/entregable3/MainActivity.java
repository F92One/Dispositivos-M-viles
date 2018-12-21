package com.example.lauti.entregable3;

/**
 * @author: Oneto, Fernando
 * @author: Diez, Lautaro
 */

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView myTextView;
    private Button startButton;
    private Button stopButton;
    private Button restartButton;
    private SharedPreferences preferences;

    private boolean started = false;
    private boolean restarted = false;

    private static final String COUNTER = "counter";
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // Restore shared preferences
        preferences = getSharedPreferences(COUNTER, MODE_PRIVATE);
        //counter = preferences.getInt(COUNTER, 0);
        //myTextView.setText("Contador = " + counter);
        new MyAsyncTask().execute();
    }

    // save counter in SharedPreferences
    private void saveCounter() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); this is incorrect, do not create another, use the same!!
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COUNTER, myTextView.getText().toString());
        //editor.commit();
        editor.apply(); // guarda asincrónicamente y seguro
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCounter(); // comment this line and when you finish the activity counter will be loss
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCounter();

    }

    private void init() {
        myTextView = (TextView) findViewById(R.id.myTextView);
        startButton = (Button) findViewById(R.id.startCount);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                started = true;
                Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();
            }
        });
        stopButton = (Button) findViewById(R.id.stopCount);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                started = false;
                Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_LONG).show();
            }
        });
        restartButton = (Button) findViewById(R.id.restartCount);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                started = false;
                restarted = true;
                Toast.makeText(getApplicationContext(), "Restarted", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class RefreshSeconds implements Runnable {
        @SuppressWarnings("unused")
        public void run() {
            int hh = 0;
            int mm = 0;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (started) {
                        counter++;
                        if (counter < 60) {
                            myTextView.setText( String.format("%02d", hh) + ":" +  String.format("%02d", mm) + ":" +  String.format("%02d", counter));
                        } else {    // 1 minute
                            mm++;
                            counter = 0;
                            if (mm < 60) {
                                myTextView.setText( String.format("%02d", hh) + ":" +  String.format("%02d", mm) + ":" +  String.format("%02d", counter));
                            } else {    // 1 hour
                                hh++;
                                mm = 0;
                                myTextView.setText( String.format("%02d", hh) + ":" +  String.format("%02d", mm) + ":" +  String.format("%02d", counter));
                            }
                        }
                        Thread.sleep(1000); // sleep 1 second
                    } else {
                        if (restarted) {    // restarted the seconds
                            hh = 0;
                            mm = 0;
                            counter = 0;
                            myTextView.setText(String.format("%02d", hh) + ":" +  String.format("%02d", mm) + ":" +  String.format("%02d", counter));
                            restarted = false;
                        }
                    }
                } catch (Exception e) {
                    Log.e("Exception ", e.getMessage());
                }
        }
    }


}

private class MyAsyncTask extends AsyncTask<URL, Integer, Long> {

    @Override
    protected Long doInBackground(URL... urls) {
        // do something
        new Thread(new RefreshSeconds()).start();
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        // save counter when we pause the activity
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COUNTER, myTextView.getText().toString());
        //editor.commit();
        editor.apply();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //get sharedPreferences here
        SharedPreferences sharedPreferences = getSharedPreferences(COUNTER, getApplicationContext().MODE_PRIVATE);
        String saved = sharedPreferences.getString(COUNTER, "00:00:00");
        myTextView.setText(saved);
    }

}
}
