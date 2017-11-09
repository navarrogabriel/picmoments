package com.unaj.gabbo.picmoments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;
import com.unaj.gabbo.picmoments.moment.Moment;
import com.unaj.gabbo.picmoments.moment.MomentContent;

import java.util.List;

/**
 * An activity representing a list of Moments. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MomentDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MomentListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    final static String LOGUEADO ="logueado";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private LocationManager locationManager = null;
    SQLiteDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_list);

        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final FloatingActionButton logout = (FloatingActionButton) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(LOGUEADO, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(LOGUEADO, "");
                editor.commit();

                Intent intent = new Intent(MomentListActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton upload = (FloatingActionButton) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        View recyclerView = findViewById(R.id.moment_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, dbHelper);

        if (findViewById(R.id.moment_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, SQLiteDBHelper dbHelper) {
        MomentContent momentContent = new MomentContent();
        List <Moment> momentsFromDB = momentContent.getMomentsToList(dbHelper);
        if (momentsFromDB!= null ) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(momentsFromDB));
        }
        else{
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(MomentContent.ITEMS));
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String currentLocation = getLocation();

            Intent intent = new Intent(MomentListActivity.this, SaveMomentsActivity.class);
            intent.putExtra("imageBitMap", imageBitmap);
            intent.putExtra ("location", currentLocation);
            startActivity(intent);
        }
    }

    private String getLocation (){

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        String currentLocation = locationListener.getLocation();
        return  currentLocation;
}

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Moment> mValues;

        public SimpleItemRecyclerViewAdapter(List<Moment> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.moment_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MomentDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        MomentDetailFragment fragment = new MomentDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.moment_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MomentDetailActivity.class);
//                        intent.putExtra("coordenadas")
                        intent.putExtra(MomentDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Moment mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
