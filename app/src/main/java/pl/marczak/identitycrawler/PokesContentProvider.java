package pl.marczak.identitycrawler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * @author Lukasz Marczak
 * @since 03.06.16.
 */
public class PokesContentProvider extends ContentProvider {

    private static final String AUTH = "pl.marczak.identitycrawler.PokesContentProvider";
    public static final Uri POKES_URI = Uri.parse("content://" + AUTH + "/" + DbHelper.TABLE_NAME);


    final static int POKE = 1;
    SQLiteDatabase db;
    DbHelper dbHelper;

    private final
    static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, DbHelper.TABLE_NAME, POKE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(@Nullable Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@Nullable Uri uri, ContentValues values) {
        dbHelper.getWritableDatabase();
        if (uriMatcher.match(uri) == POKE) {
            db.insert(DbHelper.TABLE_NAME, null, values);
        }
        db.close();
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Nullable
    @Override
    public Cursor query(    @Nullable Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        db = dbHelper.getReadableDatabase();

        cursor = db.query(DbHelper.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(),
                    uri);
        return cursor;
    }

    @Override
    public int delete(@Nullable Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@Nullable Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
