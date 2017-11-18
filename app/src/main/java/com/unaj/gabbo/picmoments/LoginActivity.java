package com.unaj.gabbo.picmoments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;

import java.util.Locale;

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
    android.support.v4.app.NotificationCompat.Builder mBuilder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To hide AppBar for fullscreen.
       /* ActionBar ab = getSupportActionBar();
        ab.hide();*/
        sharedPreferences = getSharedPreferences(LOGUEADO, Context.MODE_PRIVATE);
        int logueado = sharedPreferences.getInt(LOGUEADO, -1);
        if (logueado > 0) {
            Intent intent = new Intent(LoginActivity.this, MomentListActivity.class);
            intent.putExtra("userid", logueado);
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

                cursor = db.rawQuery("SELECT * FROM usuarios WHERE user=? AND password=?", new String[]{user, pass});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        int id = cursor.getInt(0);
                        String _fname = cursor.getString(cursor.getColumnIndex("fullName"));
                        String _user = cursor.getString(cursor.getColumnIndex("user"));

                        //Agrego nueva preference
                        editor = sharedPreferences.edit();
                        editor.putInt(LOGUEADO, id);
                        editor.commit();

                        Toast.makeText(LoginActivity.this, ":)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MomentListActivity.class);
                        intent.putExtra("fullname", _fname);
                        intent.putExtra("userid", id);
                        startActivity(intent);

                        //Se elimina la actividad.
                        finish();
                    } else {

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

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        View lenguaje = findViewById(R.id.selectLanguage);
        registerForContextMenu(lenguaje);
        lenguaje.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        createNotification();
        startActivity(refresh);
        finish();
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.language_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.spanish:
                setLocale("ES");
                break;
            case R.id.italian:
                setLocale("IT");
                break;
            case R.id.english:
                setLocale("EN");
                break;
        }
        return true;
    }

    private void createNotification() {

        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        int icono = R.drawable.language;
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(LoginActivity.this, 0,intent, 0);
        mBuilder = new NotificationCompat.Builder(getApplicationContext()).
                setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle(getResources().getString(R.string.languageChange))
                .setContentText(getResources().getString(R.string.languageChange))
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);

        mNotifyMgr.notify(1, mBuilder.build());

    }

}