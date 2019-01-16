package es.unizar.eina.notepadv3;


public class Test {

    public void iniciarPruebas(NotesDbAdapter mDbHelper, CatDbAdapter mDbHelperC) {
        long result;
        boolean bresult;
        // Pruebas al crear
        int i = 0;

        result = mDbHelper.createNote("Frutas", null, "NO CATEGORY");
        android.util.Log.d("Expected " + i, "-1");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        result = mDbHelper.createNote(null, "Manzanas", "NO CATEGORY");
        android.util.Log.d("Expected " + i, "-1");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        result = mDbHelper.createNote("", "Fresas", "NO CATEGORY");
        android.util.Log.d("Expected " + i, "-1");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        result = mDbHelper.createNote("Frutas", "Melocotón", null);
        android.util.Log.d("Expected " + i, "-1");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        result = mDbHelper.createNote("Frutas", "Naranjas", "NO CATEGORY");
        android.util.Log.d("Expected " + i, ">=0");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;



        // Pruebas al actualizar

        bresult = mDbHelper.updateNote(0, "Frutas", "Naranjas", "NO CATEGORY");
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelper.updateNote(1, null, "Fresas", "NO CATEGORY");
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelper.updateNote(result, "", "Plátanos", "NO CATEGORY");
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelper.updateNote(result, "Frutas", null, "NO CATEGORY");
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelper.updateNote(result, "Herramientas", "Destornillador", null);
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelper.updateNote(result, "Herramientas", "Destornillador", "NO CATEGORY");
        android.util.Log.d("Expected " + i, "true");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;


        // Prueba al eliminar

        bresult = mDbHelper.deleteNote(0);
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelper.deleteNote(result);
        android.util.Log.d("Expected " + i, "true");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        // Pruebas de categorías

        result = mDbHelperC.createCategory(null);
        android.util.Log.d("Expected " + i, "-1");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        result = mDbHelperC.createCategory("Frutas");
        android.util.Log.d("Expected " + i, ">=0");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        bresult = mDbHelperC.updateCategory(0, "Frutas");
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelperC.updateCategory(result, "");
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelperC.updateCategory(result, null);
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelperC.updateCategory(result, "Herramientas");
        android.util.Log.d("Expected " + i, "true");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelperC.deleteCategory(0);
        android.util.Log.d("Expected " + i, "false");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;

        bresult = mDbHelperC.deleteCategory(result);
        android.util.Log.d("Expected " + i, "true");
        android.util.Log.d("Returned " + i, Boolean.toString(bresult));
        i++;
    }

}
