package pl.marczak.identitycrawler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;

public class HttpExecutorService extends Service {
    public static final String TAG = HttpExecutorService.class.getSimpleName();

    public HttpExecutorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String query = intent.getStringExtra("QUERY");
        Log.d(TAG, "onStartCommand: " + query);
        Pokemon.getPoke(Integer.valueOf(query))
                .delaySubscription(5, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Pokemon.Poke>() {
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
                    public void onNext(Pokemon.Poke poke) {
                        Log.d(TAG, "onNext: inside thread: " + Thread.currentThread().toString());
                        Log.d(TAG, "onNext: " + poke.forms.get(0).name);
                        Intent i = new Intent("RESULT");
                        i.putExtra("POKE", poke.forms.get(0).name);
                        sendBroadcast(i);
                        Toast.makeText(HttpExecutorService.this, "RECEIVED", Toast.LENGTH_SHORT).show();
                    }
                });
        return Service.START_FLAG_REDELIVERY;
    }
}
