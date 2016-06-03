package pl.marczak.identitycrawler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * @author Lukasz Marczak
 * @since 03.06.16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private Context ctx;
    //version of database
    private static final int version = 1;
    //database name
    public static final String DB_NAME = "pokesDB";
    //name of table
    public static final String TABLE_NAME = "pokes";
    //column names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CONTENT = "noteContent";
    public static final String KEY_DATE = "date";
    //sql query to creating table in database
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAME
                    + " (" + KEY_ID + " TEXT, "
                    + KEY_NAME + " TEXT, "
                    + KEY_CONTENT + " TEXT, "
                    + KEY_DATE + " TEXT);";

    //contructor of DBHelper
    public DbHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.ctx = context;
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Context ctx) {
        super(context, DB_NAME, factory, 1);
        this.ctx = ctx;
    }

    //creating the table in database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }


    //in case of upgrade we're dropping the old table, and create the new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);

        onCreate(db);

    }

    //function for adding the note to database
    public void addNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        //creating the contentValues object
        //read more here -> http://developer.android.com/reference/android/content/ContentValues.html
        ContentValues cv = new ContentValues();
        cv.put("noteTitle", title);
        cv.put("noteContent", content);
        cv.put("date", new Date().toString());

        //inserting the note to database
        db.insert(TABLE_NAME, null, cv);

        //closing the database connection
        db.close();

        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class

    }


    //getting all notes
    public Cursor getNotes(SQLiteDatabase db) {
        //db.query is like normal sql query
        //cursor contains all notes
        Cursor c = db.query(TABLE_NAME, new String[]{KEY_NAME, KEY_CONTENT}, null, null, null, null, "id DESC");
        //moving to the first note
        c.moveToFirst();
        //and returning Cursor object
        return c;
    }

    public Cursor getNotes2(SQLiteDatabase db) {
        //db.query is like normal sql query
        //cursor contains all notes
        Cursor c = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME}, null, null, null, null, "id DESC");
        //moving to the first note
        c.moveToFirst();
        //and returning Cursor object
        return c;
    }

    public Cursor getNote(SQLiteDatabase db, int id) {
        Cursor c = db.query(TABLE_NAME, new String[]{KEY_NAME, KEY_CONTENT, KEY_DATE}, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        c.moveToFirst();
        return c;
    }

    public void removeNote(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateNote(String title, String content, String editTitle) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("noteTitle", title);
        cv.put("noteContent", content);
        cv.put("date", new Date().toString());

        db.update(TABLE_NAME, cv, KEY_NAME + " LIKE '" + editTitle + "'", null);

        db.close();


    }

}