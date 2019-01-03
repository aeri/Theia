package es.unizar.eina.notepadv3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class NoteEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private NotesDbAdapter mDbHelper;
    private CatDbAdapter mDbHelperC;
    private EditText mTitleText;
    private EditText mBodyText;
    private EditText mIdText;
    private Long mRowId;
    private Spinner spinner;
    private String category;

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        String cosa = category;
        //String category = spinner.getSelectedItem().toString();
        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body, category);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body, category);
        }
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
            mIdText.setText(mRowId.toString());

            String catt = note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_CAT));

            if (catt != null) {
                spinner.setSelection(getIndex(spinner, catt));
            }


        }
        else{
            mIdText.setText("***");
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        setTitle(R.string.edit_note);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelperC = new CatDbAdapter(this);
        mDbHelper.open();
        mDbHelperC.open();

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mIdText = (EditText) findViewById(R.id.id);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    : null;
        }
        fillData();

        confirmButton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }
    private void fillData() {
        Cursor notesCursor = mDbHelperC.fetchAllCategories(true);
        // Get all of the notes from the database and create the item list
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { CatDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        spinner.setAdapter(notes);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = ((Cursor)parent.getItemAtPosition(position)).getString(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = null;
    }

    private int getIndex(Spinner spinner, String myString) {
        int rt = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((Cursor) spinner.getItemAtPosition(i))
                    .getString(0).equalsIgnoreCase(myString)) {
                rt = i;
            }
        }

        return rt;
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
