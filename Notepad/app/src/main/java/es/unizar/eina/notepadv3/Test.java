package es.unizar.eina.notepadv3;


public class Test {

    public void iniciarPruebas(NotesDbAdapter mDbHelper) {
        long result;
        // Pruebas al crear

        result = mDbHelper.createNote("Frutas", null, "NO CATEGORY");
        android.util.Log.d("Prueba 1", Long.toString(result));
        result = mDbHelper.createNote(null, "Manzanas", "NO CATEGORY");
        android.util.Log.d("Prueba 2", Long.toString(result));

        result = mDbHelper.createNote("", "Fresas", "NO CATEGORY");
        android.util.Log.d("Prueba 3", Long.toString(result));

        // Prueba al eliminar
        /*
        mDbHelper.deleteNote(0);

        // Pruebas al actualizar

        mDbHelper.updateNote(0,"Frutas" ,"Naranjas","NO CATEGORY");

        mDbHelper.updateNote(1,null ,"Naranjas","NO CATEGORY");

        mDbHelper.updateNote(1,"" ,"Plátanos","NO CATEGORY");

        mDbHelper.updateNote(1,"Frutas" ,null,"NO CATEGORY");
        */


    }

}
