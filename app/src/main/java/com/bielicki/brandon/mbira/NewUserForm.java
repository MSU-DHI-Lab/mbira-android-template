package com.bielicki.brandon.mbira;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NewUserForm extends ActionBarActivity implements SetNewUser.PostNewUser {
    private EditText username;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText passwordOne;
    private EditText passwordTwo;
    SetNewUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_form);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user_form, menu);
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

    public void sendNewUser(View view) {
        username = (EditText) findViewById(R.id.userNameText);
        firstName = (EditText) findViewById(R.id.firstNameText);
        lastName = (EditText) findViewById(R.id.lastNameText);
        email = (EditText) findViewById(R.id.emailText);
        passwordOne = (EditText) findViewById(R.id.passwordOneText);
        passwordTwo = (EditText) findViewById(R.id.passwordTwoText);

        newUser = new SetNewUser(this);
        newUser.execute(username.getText().toString(), firstName.getText().toString(),lastName.getText().toString(), email.getText().toString(), passwordOne.getText().toString(), passwordTwo.getText().toString());

    }

    public void postNewUser(Object text) {
        Log.d("RETURN-TEXT", text.toString());
        String result = text.toString();
        if(result.equals("true")) {

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), text.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
