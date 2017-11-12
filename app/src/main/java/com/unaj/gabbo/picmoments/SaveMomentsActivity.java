package com.unaj.gabbo.picmoments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gabbo on 1/11/2017.
 */

public class SaveMomentsActivity extends AppCompatActivity {

    SQLiteDBHelper dbHelper;
    SQLiteDatabase db;

    private ImageView imageView;
    private EditText descriptionEdit;
    private Button uploadBtn;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_moment);
        Intent intent = getIntent();

        imageView = (ImageView) findViewById(R.id.renderPhoto);
        descriptionEdit = (EditText) findViewById(R.id.txtDescription);
        uploadBtn = (Button) findViewById(R.id.btnupload);

        final String location = intent.getStringExtra("location");
        final Bitmap imageAsBitmap =(Bitmap) intent.getParcelableExtra("imageBitMap");
        final int id = intent.getIntExtra("userid", -1);
//        Bitmap reSizeImage = BitmapFactory.decodeByteArray(imageAsBitmap, 0, imageAsBitmap)
        if (location != null || location.length()>0){
            imageView.setImageBitmap(imageAsBitmap);

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String description = descriptionEdit.getText().toString();
                    insertMoment(imageAsBitmap, location, description, id);

                }
            });
        }
        else {
            Toast.makeText(SaveMomentsActivity.this, "No hay datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertMoment (Bitmap imageAsBitmap, String location, String description, int id){
        dbHelper = new SQLiteDBHelper(this);
        db = dbHelper.getWritableDatabase();
        Date now = new Date();
        String formatDate = dateFormat.format(now);

        byte [] imageAsBytes = getBitmapAsByteArray(imageAsBitmap);

        ContentValues values = new ContentValues();
        values.put("image", imageAsBytes);
        values.put("location", location);
        values.put ("description", description);
        values.put ("date", formatDate);
        values.put ("userid", id);

        long result = db.insert("moment", null, values);
        Toast.makeText(SaveMomentsActivity.this, ":D", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MomentListActivity.class);
        startActivity(intent);
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}

