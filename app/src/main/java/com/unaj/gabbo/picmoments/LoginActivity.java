package com.unaj.gabbo.picmoments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;

/**
 * Created by Gabbo on 28/10/2017.
 */

public class LoginActivity extends AppCompatActivity {

    SQLiteDBHelper dbhelper;
    SQLiteDatabase db;
    Cursor cursor;
    final static String LOGUEADO = "logueado";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To hide AppBar for fullscreen.
       /* ActionBar ab = getSupportActionBar();
        ab.hide();*/
       sharedPreferences = getSharedPreferences(LOGUEADO, Context.MODE_PRIVATE);
        String logueado = sharedPreferences.getString(LOGUEADO, "");
       if (logueado.length()>0 || logueado!=""){
           Intent intent = new Intent(LoginActivity.this,MomentListActivity.class);
           intent.putExtra("user",logueado);
           startActivity(intent);
       }

        //Referencing UserEmail, Password EditText and TextView for SignUp Now
        final EditText _textUser = (EditText) findViewById(R.id.textUser);
        final EditText _textPass = (EditText) findViewById(R.id.textPass);
        Button _btnlogin = (Button) findViewById(R.id.btnsignin);
        TextView _btnreg = (TextView) findViewById(R.id.btnreg);

        //Opening SQLite Pipeline
        dbhelper = new SQLiteDBHelper(this);
        db = dbhelper.getReadableDatabase();

        _btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String user = _textUser.getText().toString();
                String pass = _textPass.getText().toString();

                cursor = db.rawQuery("SELECT * FROM usuarios WHERE user=? AND password=?",new String[] {user,pass});
                if (cursor != null) {
                    if(cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        //Retrieving User FullName and Email after successfull login and passing to LoginSucessActivity
                        String _fname = cursor.getString(cursor.getColumnIndex("fullName"));
                        String _user= cursor.getString(cursor.getColumnIndex("user"));

                        //Agrego nueva preference
                        editor = sharedPreferences.edit();
                        editor.putString(LOGUEADO, _user);
                        editor.commit();

                        Toast.makeText(LoginActivity.this, ":)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MomentListActivity.class);
                        intent.putExtra("fullname",_fname);
                        intent.putExtra("user",_user);
                        startActivity(intent);

                        //Se elimina la actividad.
                        finish();
                    }
                    else {

                        View errorLogin = (View) findViewById(R.id.errorLogin);
                        errorLogin.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        // Intent For Opening RegisterAccountActivity
        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
