package net.sanclemente.a14felipecm.u4_coches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class U4_Coches extends AppCompatActivity {
    boolean sdDisponhible = false;
    boolean sdAccesoEscritura = false;
    File dirFicheiroSD;
    File rutaCompleta;
    public static String nomeFicheiro = "ficheiro_coches.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u4__coches);
        comprobarEstadoSD();
        establecerDirectorioFicheiro();


    }//Fin Oncreate

    //Metodo para comprobar estado
    public void comprobarEstadoSD() {
        String estado = Environment.getExternalStorageState();
        Log.e("STATUS", estado);
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponhible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            sdDisponhible = true;
    }//Fin comprobar estado

    //Metodo para establecer donde se guardara el archivo
    public void establecerDirectorioFicheiro() {
        if (sdDisponhible) {
            dirFicheiroSD = getExternalFilesDir(null);
            rutaCompleta = new File(dirFicheiroSD.getAbsolutePath(), nomeFicheiro);
        }
    }//Fin establecer directorio

    //Metodo que al pulsar el boton, efectua la accion de sobreescribir o anadir
    public void addOverAction(View v){
        RadioButton rbAdd = (RadioButton) findViewById(R.id.radiob_add);
        RadioButton rbOve = (RadioButton) findViewById(R.id.radoib_over);
        EditText editMarca = (EditText) findViewById(R.id.edit_marca);
        Calendar cal = Calendar.getInstance();

        if (!editMarca.getText().toString().equals("")){
            if(sdAccesoEscritura) {
                if (rbAdd.isChecked()) {
                    try {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(rutaCompleta, true));
                        osw.write(editMarca.getText() + "," + cal.get(Calendar.DATE) + "\n");
                        osw.close();
                        editMarca.setText("");
                        Log.i("ESCRITURA", rutaCompleta.toString());
                        Log.i("ESCRITURA", editMarca.getText().toString());
                    } catch (Exception ex) {
                        Log.e("E/S", "Error al sobreescribir el fichero");
                        ex.printStackTrace();
                    }
                }
                if (rbOve.isChecked()) {
                    try {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(rutaCompleta, false));
                        osw.write(editMarca.getText() + "," + cal.get(Calendar.DATE) + "\n");
                        osw.close();
                        editMarca.setText("");
                        Log.i("ESCRITURA", rutaCompleta.toString());
                        Log.i("ESCRITURA", editMarca.getText().toString());
                    } catch (Exception ex) {
                        Log.e("E/S", "Error al escribir el fichero");
                        ex.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(U4_Coches.this, "No hay acceso de escritura en la SD", Toast.LENGTH_SHORT).show();
                Log.e("E/S","No hay acceso a la SD en modo escritura");
            }
        }else{
            Toast.makeText(U4_Coches.this, "No puedes añadir una marca vacia", Toast.LENGTH_SHORT).show();
            Log.e("E/S", "Se introdujo String vacio");
        }
    }

    public void mostrarFichero(View v) {
        String documento ="";
        String linha = "";
        //TextView tv = (TextView) findViewById(R.id.tvAmosar);
        //tv.setText(linha);
        if (sdDisponhible) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaCompleta)));
                while ((linha = br.readLine()) != null)
                    documento+=(linha + "\n");
                br.close();
                Toast.makeText(U4_Coches.this, documento, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(this, "Problemas lendo o ficheiro", Toast.LENGTH_SHORT).show();
                Log.e("SD", "Erro lendo o ficheiro. ");
            }
        } else
            Toast.makeText(this, "A tarxeta SD non está dispoñible", Toast.LENGTH_SHORT).show();
    }

    public void lanzarList (View v){
        Intent inten = new Intent(this,List.class);
        inten.putExtra("RUTA",rutaCompleta.toString());
        startActivity(inten);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_u4__coches, menu);
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
}
