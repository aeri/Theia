package es.unizar.eina.notepadv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple categories database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class CatDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "CatDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table categories (_id integer primary key autoincrement, "
                    + "title text not null);";

    private static final String NULL_CAT =
            "insert into categories (_id,title) values (0,\"NO CATEGORY\")";
    private static final String DATABASE_NAME = "dato";
    private static final String DATABASE_TABLE = "categories";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    /**
     * Return a Cursor over the list of all categories in the database
     * @param all Include category "NO CATEGORY" in the query
     * @return Cursor over all notes
     */
    public Cursor fetchAllCategories(boolean all) {

        if (all) {
            return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE},
                    null, null, null, null, KEY_ROWID);
        } else {
            return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE},
                    KEY_ROWID + ">0", null, null, null, KEY_ROWID);
        }


    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public CatDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the categories database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public CatDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new category using the title provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the category
     * @return rowId or -1 if failed
     */
    public long createCategory(String title) {
        if (title != null) {
            if (title.length() == 0) {
                return -1;
            } else {
                ContentValues initialValues = new ContentValues();
                initialValues.put(KEY_TITLE, title);

                return mDb.insert(DATABASE_TABLE, null, initialValues);
            }
        } else return -1;
    }

    /**
     * Delete the category with the given rowId
     *
     * @param rowId id of category to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteCategory(long rowId) {
        if (rowId <= 0) {
            return false;
        } else {
            return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
            db.execSQL(NULL_CAT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS categories");
            onCreate(db);
        }
    }

    /**
     * Return a Cursor positioned at the category that matches the given rowId
     *
     * @param rowId id of category to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchCategory(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the category using the details provided. The category to be updated is
     * specified using the rowId, and it is altered to use the title
     * value passed in
     *
     * @param rowId id of note to update
     * @param title value to set note title to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateCategory(long rowId, String title) {
        try {
            if (rowId <= 0 || title.length() == 0) {
                return false;
            } else {
                ContentValues args = new ContentValues();
                args.put(KEY_TITLE, title);
                return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
            }
        } catch (Throwable error) {
            Log.e(TAG, "Exception catched", error);
            return false;
        }
    }
}