package com.unaj.gabbo.picmoments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;
import com.unaj.gabbo.picmoments.moment.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * A fragment representing a single Moment detail screen.
 * This fragment is either contained in a {@link MomentListActivity}
 * in two-pane mode (on tablets) or a {@link MomentDetailActivity}
 * on handsets.
 */
public class MomentDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Moment mItem;

    private int userid;
    private Bitmap bitmapGlobal;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MomentDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = MomentContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            int userid = getArguments().getInt("userid");
            setUserId (userid);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Moment");
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.moment_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {

            EditText moment_detailET = (EditText) rootView.findViewById(R.id.moment_detail);
            moment_detailET.setFocusable(false);
            moment_detailET.setFocusableInTouchMode(false);
            moment_detailET.setClickable(false);

            byte[] imageAsByteArray = mItem.getImageAsBytes();
            Bitmap imageAsBitmap = BitmapFactory.decodeByteArray(imageAsByteArray,0, imageAsByteArray.length);
            setBitmapGlobal (imageAsBitmap);
            moment_detailET.setText(mItem.getDescription());
            ((TextView) rootView.findViewById(R.id.moment_date)).setText(mItem.getFormatDate());
            ((ImageView) rootView.findViewById(R.id.photoDetail)).setImageBitmap(imageAsBitmap);
        }

        FloatingActionButton seeMap = (FloatingActionButton) rootView.findViewById(R.id.seeMap);
        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coordenadas = mItem.getLocation();
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("location", coordenadas);
                startActivity(intent);
            }
        });

        final FloatingActionButton editDesc = (FloatingActionButton) rootView.findViewById(R.id.editMoment);
        editDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText moment_detailET = (EditText) rootView.findViewById(R.id.moment_detail);
                moment_detailET.setFocusable(true);
                moment_detailET.setEnabled(true);
                moment_detailET.setFocusableInTouchMode(true);
                moment_detailET.setClickable(true);
                rootView.findViewById(R.id.saveMoment).setVisibility(View.VISIBLE);
                editDesc.setVisibility(View.INVISIBLE);
            }
        });

        final FloatingActionButton saveEditMoment = (FloatingActionButton) rootView.findViewById(R.id.saveMoment);
        saveEditMoment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText newDescription = (EditText) rootView.findViewById(R.id.moment_detail);
                String description = newDescription.getText().toString();
                ContentValues values = new ContentValues();
                values.put("description", description);
                int id =Integer.parseInt( mItem.getId());
                final SQLiteDBHelper dbHelper = new SQLiteDBHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int result = db.update("moment", values, "_ID="+""+id, null );

                if  (result >0){
                    Intent intent = new Intent(getActivity(), MomentListActivity.class);
                    Toast.makeText(getActivity(), ":)", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                }

            }
        });

        FloatingActionButton deleteMoment = (FloatingActionButton) rootView.findViewById(R.id.deleteMoment);
        deleteMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id =Integer.parseInt( mItem.getId());
                final SQLiteDBHelper dbHelper = new SQLiteDBHelper(getActivity());
                SQLiteDatabase db       =   dbHelper.getWritableDatabase();
                int result = db.delete("moment", "_ID="+""+id, null);

                if  (result >0){
                    Intent intent = new Intent(getActivity(), MomentListActivity.class);
                    Toast.makeText(getActivity(), ":)", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                }

            }
        });

        int itemId = mItem.getUserId();
        if (itemId != userid){
            deleteMoment.setVisibility(View.INVISIBLE);
            editDesc.setVisibility(View.INVISIBLE);
        }

        ImageView photoDetail = (ImageView) rootView.findViewById(R.id.photoDetail);
        photoDetail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bitmap bitmapToSave = getBitmapGlobal ();
                return imageToExternalSD(bitmapToSave);
            }
        });

        return rootView;
    }

    public void setUserId(int userId) {
        this.userid = userId;
    }

    private boolean imageToExternalSD (Bitmap imageAsBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageAsBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getActivity(), ":)", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), ":(", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    public void setBitmapGlobal(Bitmap bitmapGlobal) {
        this.bitmapGlobal = bitmapGlobal;
    }

    public Bitmap getBitmapGlobal() {
        return bitmapGlobal;
    }
}
