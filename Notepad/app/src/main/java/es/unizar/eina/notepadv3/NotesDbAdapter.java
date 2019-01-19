package es.unizar.eina.notepadv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class NotesDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CAT = "category";



    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null, category text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Crea una nueva nota a partir del título y texto proporcionados. Si la
     * nota se crea correctamente, devuelve el nuevo rowId de la nota; en otro
     * caso, devuelve -1 para indicar el fallo.
     *
     * @param title
     *            el título de la nota;
     *            title != null y title.length() > 0
     * @param body
     *            el texto de la nota;
     *            body != null
     * @return rowId de la nueva nota o -1 si no se ha podido crear
     */

    public long createNote(String title, String body, String category) {
        if (title == null) {
            return -1;
        } else if (title.length() == 0) {
            return -1;
        } else {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_TITLE, title);
            initialValues.put(KEY_BODY, body);
            initialValues.put(KEY_CAT, category);

            return mDb.insert(DATABASE_TABLE, null, initialValues);
        }
    }

    /**
     * Borra la nota cuyo rowId se ha pasado como parámetro
     *
     * @param rowId
     *            el identificador de la nota que se desea borrar;
     *            rowId > 0
     * @return true si y solo si la nota se ha borrado
     */

    public boolean deleteNote(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CAT}, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of a group of notes by category
     *
     * @return Cursor over group of notes
     */
    public Cursor fetchNotesbyCat(String category) {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_CAT}, KEY_CAT + "==" + category, null, null, null, KEY_TITLE);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_CAT}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Actualiza una nota a partir de los valores de los parámetros. La nota que
     * se actualizará es aquella cuyo rowId coincida con el valor del parámetro.
     * Su título y texto se modificarán con los valores de title y body,
     * respectivamente.
     *
     * @param rowId
     *            el identificador de la nota que se desea borrar;
     *            rowId > 0
     * @param title
     *            el título de la nota;
     *            title != null y title.length() > 0
     * @param body
     *            el texto de la nota;
     *            body != null
     * @return true si y solo si la nota se actualizó correctamente
     */

    public boolean updateNote(long rowId, String title, String body, String category) {
        try {
            if (title.length() == 0) {
                return false;
            } else {
                ContentValues args = new ContentValues();
                args.put(KEY_TITLE, title);
                args.put(KEY_BODY, body);
                args.put(KEY_CAT, category);
                return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
            }
        } catch (Throwable error) {
            Log.e(TAG, "Exception catched", error);
            return false;
        }
    }
}