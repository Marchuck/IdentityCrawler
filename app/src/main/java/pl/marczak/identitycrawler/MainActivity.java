package pl.marczak.identitycrawler;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private AppCompatButton btn;
    private SearchView searchView;
    BroadcastReceiver
            receiver;
    long t0, t1;
    SQLiteDatabase db;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView) findViewById(R.id.search_view);
        btn = (AppCompatButton) findViewById(R.id.btn);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

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
        db = dbHelper.getReadableDatabase();
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            t0 = System.nanoTime();
            String[] columns = new String[]{
                    DbHelper.KEY_NAME, DbHelper.KEY_CONTENT,
                    DbHelper.KEY_DATE
            };

            Cursor cursor = db.query(DbHelper.TABLE_NAME,
                    columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String name = cursor
                        .getString(cursor
                                .getColumnIndex(DbHelper.KEY_NAME));
                String comment = cursor
                        .getString(cursor.getColumnIndex(DbHelper.KEY_CONTENT));
                String date = cursor
                        .getString(cursor.getColumnIndex(DbHelper.KEY_DATE));

                Log.d(TAG, "onClick: " + name + ", " + comment + "," + date);


            }
            cursor.close();

            String query = searchView.getQuery().toString();

            ContentValues cv = new ContentValues();
            cv.put(DbHelper.KEY_NAME, query);
            cv.put(DbHelper.KEY_CONTENT, query);
            cv.put(DbHelper.KEY_ID, String.valueOf(t0));
            cv.put(DbHelper.KEY_DATE, String.valueOf(t0));

            db.insert(DbHelper.TABLE_NAME, null, cv);

            Intent intent = new Intent(MainActivity.this, HttpExecutorService.class);
            intent.putExtra("QUERY", query);
            startService(intent);
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
