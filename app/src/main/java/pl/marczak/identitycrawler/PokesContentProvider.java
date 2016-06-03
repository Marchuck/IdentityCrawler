package pl.marczak.identitycrawler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
