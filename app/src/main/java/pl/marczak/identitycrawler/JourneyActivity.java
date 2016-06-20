package pl.marczak.identitycrawler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import pl.lukaszmarczak.expandabledelegates.utils.ExpandableBuilder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class JourneyActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = JourneyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        RecyclerView r = (RecyclerView) findViewById(R.id.recycler_view);
        RelativeLayout behindView = (RelativeLayout) findViewById(R.id.google_map_placeholder);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        mapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.google_map_placeholder, mapFragment)
                .commitAllowingStateLoss();

        ExpandableWrapper.getGoogleMapAdapter(behindView).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecyclerView.Adapter>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RecyclerView.Adapter adapter) {
                        ExpandableBuilder builder = new ExpandableBuilder(getBaseContext())
                                .withAdapter(adapter)
                                .withRecyclerView(r)
                                .withGroupClickListener(null);
                        builder.build();
                    }
                });
        setTitle("ExpandableDelegates Demo");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap. animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition(new LatLng(51,18), 4, 0, 0)));
        googleMap.setOnMapLongClickListener(latLng -> Log.d(TAG, "onMapLongClick: "+latLng));
    }
}
