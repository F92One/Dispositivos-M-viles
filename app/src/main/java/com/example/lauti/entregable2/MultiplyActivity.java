package com.example.lauti.entregable2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MultiplyActivity extends AppCompatActivity {

    private Button multiplyButton;
    private EditText multiplicando;
    private EditText multiplicador;
    private static final String MULTIPLICANDO = "multiplicando";
    private static final String MULTIPLICADOR = "multiplicador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiply);

        multiplyButton = (Button) findViewById(R.id.multiplyButton);
        multiplicando = (EditText)findViewById(R.id.multiplicando);
        multiplicador = (EditText) findViewById(R.id.multiplicador);


        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Integer.parseInt(multiplicando.getText().toString()) * Integer.parseInt(multiplicador.getText().toString());

                Intent returnIntent = new Intent();
                returnIntent.putExtra("mresult", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MULTIPLICANDO, multiplicando.getText().toString());
        outState.putString(MULTIPLICADOR, multiplicador.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        multiplicando.setText(savedInstanceState.getString(MULTIPLICANDO));
        multiplicador.setText(savedInstanceState.getString(MULTIPLICADOR));
    }
}
