package com.unaj.gabbo.picmoments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.unaj.gabbo.picmoments.moment.*;

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
        View rootView = inflater.inflate(R.layout.moment_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            byte[] imageAsByteArray = mItem.getImageAsBytes();
            Bitmap imageAsBitmap = BitmapFactory.decodeByteArray(imageAsByteArray,0, imageAsByteArray.length);
            ((TextView) rootView.findViewById(R.id.moment_detail)).setText(mItem.getDescription());
            ((TextView) rootView.findViewById(R.id.moment_date)).setText(mItem.getFormatDate());
            ((ImageView) rootView.findViewById(R.id.photoDetail)).setImageBitmap(imageAsBitmap);
        }

        FloatingActionButton seeMap = (FloatingActionButton) rootView.findViewById(R.id.seeMap);
        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coordenadas = mItem.getLocation();
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("location", mItem.getLocation());
                startActivity(intent);
            }
        });


        return rootView;
    }

}
