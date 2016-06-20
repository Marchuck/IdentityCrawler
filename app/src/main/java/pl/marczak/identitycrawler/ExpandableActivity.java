package pl.marczak.identitycrawler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import pl.lukaszmarczak.expandabledelegates.utils.ExpandableBuilder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExpandableActivity extends AppCompatActivity {
    public static final String TAG = ExpandableActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);

        Log.d(TAG, "onCreate: ");
        ExpandableWrapper.getAdapter().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecyclerView.Adapter>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RecyclerView.Adapter adapter) {
                        ExpandableBuilder builder = new ExpandableBuilder(getBaseContext())
                                .withAdapter(adapter)
                                .withRecyclerView((RecyclerView) findViewById(R.id.recycler_view))
                                .withGroupClickListener(null);
                        builder.build();
                    }
                });
    }
}
