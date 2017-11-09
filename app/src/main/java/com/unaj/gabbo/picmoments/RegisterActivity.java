package com.unaj.gabbo.picmoments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;

/**
 * Created by Gabbo on 28/10/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    SQLiteDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //To hide AppBar for fullscreen.
//        ActionBar ab = getSupportActionBar();
//        ab.hide();

        dbHelper = new SQLiteDBHelper(this);

        //Referencias a los inputs
        final EditText fullName_ = (EditText) findViewById(R.id.txtname_reg);
        final EditText user_ = (EditText) findViewById(R.id.txteUser_reg);
        final EditText password = (EditText) findViewById(R.id.txtpass_reg);
        final EditText mobileNumber = (EditText) findViewById(R.id.txtmobile_reg);
        Button regButton = (Button) findViewById(R.id.btn_reg);

        //Al clickear el boton de registro
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = dbHelper.getWritableDatabase();

                String fullname = fullName_.getText().toString();
                String user = user_.getText().toString();
                String pass = password.getText().toString();
                String mobile = mobileNumber.getText().toString();

                //Metodo declarado debajo
                InsertData(fullname, user, pass, mobile);
                Toast.makeText(RegisterActivity.this, "Success :)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    //Insrcion en la base de datos
    public void InsertData(String fullName, String user, String password, String mobile ) {

        ContentValues values = new ContentValues();
        values.put("fullname",fullName);
        values.put("user",user);
        values.put("password",password);
        values.put("mobile",mobile);

        long id = db.insert("usuarios",null,values);

    }
}
