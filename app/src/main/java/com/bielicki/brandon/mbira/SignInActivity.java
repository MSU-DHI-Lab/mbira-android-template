package com.bielicki.brandon.mbira;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignInActivity extends ActionBarActivity implements SetSignIn.PostSignIn {
    private Toolbar toolbar;
    private EditText user;
    private EditText password;
    private ProgressDialog dialog;
    SetSignIn signIn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.expert_sign_in);
        user = (EditText) findViewById(R.id.userText);
        password = (EditText) findViewById(R.id.userPassword);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signIn(View view) {
        String passwordValue = password.getText().toString();
        String userValue = user.getText().toString();

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Signing In");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


        signIn = new SetSignIn(this);
        signIn.execute(userValue, passwordValue);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                signIn.cancel(true);
            }
        });


    }

    public void postSignIn(Object text) {
        dialog.dismiss();
        String result = text.toString();
        if(result.equals("true")) {
            //store into shared preference
            Log.d("USERNAME", user.getText().toString());
            Log.d("PASSWORD", sha256(password.getText().toString()));


            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), text.toString(), Toast.LENGTH_LONG).show();

        }

    }

    public void newUser(View view) {
        Intent intent = new Intent(this, NewUserForm.class);
        startActivity(intent);
    }

    public String sha256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA256");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
