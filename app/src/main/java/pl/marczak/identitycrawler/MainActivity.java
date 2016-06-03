package pl.marczak.identitycrawler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import java.util.List;

import pl.marczak.identitycrawler.hacked.Instagram;
import pl.marczak.identitycrawler.hacked.InstagramUser;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private AppCompatButton btn;
    private SearchView searchView;
    BroadcastReceiver
            receiver;
    long t0, t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView) findViewById(R.id.search_view);
        btn = (AppCompatButton) findViewById(R.id.btn);
        if (btn == null) throw new NullPointerException("Nullable button!");
        btn.setOnClickListener(mListener);
        IntentFilter filter = new IntentFilter();
        filter.addAction("RESULT");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //do something based on the intent's action
                if (intent.getAction().equals("RESULT")) {
                    Log.d(TAG, "onReceive: from" + Thread.currentThread().toString());
                    t1 = System.nanoTime();
                    String poke = intent.getStringExtra("POKE");
                    Log.i(TAG, "onReceive: " + poke + " after " + (t1 - t0) + " ms");
                }
            }
        };
        registerReceiver(receiver, filter);
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            t0 = System.nanoTime();
            String query = searchView.getQuery().toString();
            Pokemon.getPoke(Integer.valueOf(query)).subscribe(new Subscriber<Pokemon.Poke>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Pokemon.Poke poke) {
                    t1 = System.nanoTime();
                    Log.d(TAG, "onNext: inside thread: " + Thread.currentThread().toString());
                    Log.d(TAG, "onNext: " + poke.forms.get(0).name + " after " + (t1 - t0) + " ms");

                }
            });
//            Intent intent = new Intent(MainActivity.this, HttpExecutorService.class);
//            intent.putExtra("QUERY", query);
//            startService(intent);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        btn.setOnClickListener(null);
        mListener = null;
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}
