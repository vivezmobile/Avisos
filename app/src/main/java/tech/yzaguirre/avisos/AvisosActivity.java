package tech.yzaguirre.avisos;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

public class AvisosActivity extends AppCompatActivity {
    private ListView listView;
    private AvisosDBAdapter avisosDBAdapter;
    private AvisosSimpleCursorAdapter avisosSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);
        listView = (ListView) findViewById(R.id.listView_avisos);
        listView.setDivider(null);
        avisosDBAdapter = new AvisosDBAdapter(this);
        avisosDBAdapter.open();

        if (savedInstanceState == null){
            // limpiar todos los datos
            avisosDBAdapter.deleteAllReminders();
            // add algunos datos
            avisosDBAdapter.createReminder("visital el centro", true);
            avisosDBAdapter.createReminder("Enviar regalos", false);
            avisosDBAdapter.createReminder("Comprobar correo", true);
        }

        Cursor cursor = avisosDBAdapter.fetchAllReminders();

        // desde las columnas definidas en la base de datos
        String[] from = new String[]{
                AvisosDBAdapter.COL_CONTENT
        };

        // a la id de views en el layout
        int[] to = new int[]{
                R.id.textView_rowText
        };

        avisosSimpleCursorAdapter = new AvisosSimpleCursorAdapter(
                // context
                AvisosActivity.this,
                // el layout de la fila
                R.layout.avisos_row,
                // cursor
                cursor,
                // desde las columnas definidas en la base de datos
                from,
                // a las ids de views en el layout
                to,
                // flag - no usado
                0
        );
        listView.setAdapter(avisosSimpleCursorAdapter);
        // cuando pulsamos en un item individual en la listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AvisosActivity.this);
                ListView modeListView = new ListView(AvisosActivity.this);
                String[] modes = new String[]{"Editar Aviso", "Borrar Aviso"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(AvisosActivity.this, android.R.layout.simple_list_item_1, android.R. id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // editar aviso
                        if (position == 0) {
                            Toast.makeText(AvisosActivity.this, "editar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                            // borrar aviso
                        } else {
                            Toast.makeText(AvisosActivity.this, "borrar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avisos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_nuevo :
                Log.d(getLocalClassName(), "Crear nuevo Aviso");
                return true;
            case R.id.action_salir:
                finish();
                return true;
            default:
                return false;
        }
    }
}
