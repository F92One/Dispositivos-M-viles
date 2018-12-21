package com.example.lauti.entregable2;
/**
 * @author: Diez, Lautaro
 */

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView contactName;
    private Button selectContact;
    private Button multiplyButton;
    private static final int SELECT_CONTACT = 127;    // the code we use to identify the returned Result. It can be any number (> 0, I think)
    private static final int MULTIPLY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectContact = (Button) findViewById(R.id.selectContact);
        contactName = (TextView) findViewById(R.id.contactTV);

        // It's give the permissions needed to read phone numbers
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, SELECT_CONTACT);

        selectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); // Create an Intent to select a contact from the device.
                                                                                                        // ACTION_PICK allows to start the conctact selection activity
                // Other way to create the Intent
               // Intent intent = new Intent(Intent.ACTION_PICK);// ACTION_PICK allows to start the conctact selection activity
                //intent.setType(ContactsContract.Contacts.CONTENT_TYPE); // Create an Intent to select a contact from the device.

                startActivityForResult(intent, SELECT_CONTACT);
            }
        });

        multiplyButton = (Button) findViewById(R.id.mulButton);
        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MultiplyActivity.class);
                startActivityForResult(intent, MULTIPLY);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Contacto", (String) contactName.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contactName.setText(savedInstanceState.getString("Contacto"));
    }

    // Display contacts info
    private void showContact(Uri contactUri) {
        try {
            String name = "Contacto: " + getName(contactUri);
            String phone = "Teléfono: " + getPhone(contactUri);
            contactName.setText(name + "\n" + phone);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    // Get the selected contact name
    private String getName(Uri uri) {
        String name = null;
        ContentResolver contentResolver = getContentResolver(); // get ContentResolver instance
        Cursor c = contentResolver.query(uri, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);

        // consult the contact we got
        if (c.moveToFirst()) {
            name = c.getString(0);
        }
        c.close();

        return name;
    }

    // Get the selected contact phone
    private String getPhone(Uri uri) {
        String id = uri.getLastPathSegment();
        Cursor phoneCsr = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + " = ?", new String[] {id},
                null);
        final ArrayList<String> phonesList = new ArrayList<String>();
        while (phoneCsr.moveToNext()) {
            // This allows to get several phone addresses
            // if the phone addresses were store in an array
            String phone = phoneCsr.getString(phoneCsr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
            if (! phonesList.contains(phone)) {
                phonesList.add(phone);
            }
        }
        phoneCsr.close();
        String phoneNumber="";
        for (String number : phonesList) {
            phoneNumber += number + ", ";
        }
        return phoneNumber;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case SELECT_CONTACT:
                if (resultCode == RESULT_OK) {  // if the result code is ok
                    Uri contactUri = intent.getData();  // we get the Uri retrieving data on the operating intent. This URI specifies the name of the data; often it uses the content: scheme, specifying data in a content provider.
                    showContact(contactUri);
                } else {
                    Toast.makeText(this, "No se ha obtenido respuesta.", Toast.LENGTH_LONG).show();
                }
                break;
            case MULTIPLY:
                if (resultCode == RESULT_OK) {  // if the result code is ok
                    // get the multiply result
                    int mresult = intent.getIntExtra("mresult", 0);
                    // set the multiply result into the text view
                    setMultiplyResult(mresult);
                } else {
                    Toast.makeText(this, "No se ha obtenido respuesta.", Toast.LENGTH_LONG).show();
                }
                break;
        }
/*
        if (requestCode == SELECT_CONTACT) {    // if the result is the one we are looking for
            if (resultCode == RESULT_OK) {  // if the result code is ok
                Uri contactUri = intent.getData();  // we get the Uri retrieving data on the operating intent. This URI specifies the name of the data; often it uses the content: scheme, specifying data in a content provider.
                showContact(contactUri);
            } else {
                Toast.makeText(this, "No se ha obtenido respuesta.", Toast.LENGTH_LONG).show();
            }
        }
*/
    }

    private void setMultiplyResult(int mresult) {
        contactName.setText("Resultado de la multiplicación: " + mresult);
    }
}
