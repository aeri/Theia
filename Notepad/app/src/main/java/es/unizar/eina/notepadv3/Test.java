package es.unizar.eina.notepadv3;


public class Test {
    private static int MAX_NOTAS = 1000;
    private static int TEST_CHAR = 1100000;

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
        android.util.Log.d("##Categorías## ", Integer.toString(i));

        result = mDbHelperC.createCategory(null);
        android.util.Log.d("Expected " + i, "-1");
        android.util.Log.d("Returned " + i, Long.toString(result));
        i++;

        result = mDbHelperC.createCategory("");
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

        for (int j = 0; j < MAX_NOTAS; ++j) {
            mDbHelper.createNote("Nota: " + j, "Cosas", "NO CATEGORY");

        }
        android.util.Log.d("Insertadas:", Integer.toString(MAX_NOTAS));
        String body = "AAA";
        for (int j = 3; j <= TEST_CHAR; j *= 2) {
            body += body;
            android.util.Log.d("Se procede a insertar: ", Integer.toString(j));

            mDbHelper.createNote("CharTest", body, "NO CATEGORY");

            android.util.Log.d("¡INSERTADA! ", Integer.toString(j));
        }
    }

}
