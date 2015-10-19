package net.sanclemente.a14felipecm.u4_coches;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.DialogInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class U4_Coches extends Activity {
    boolean sdDisponhible = false;
    boolean sdAccesoEscritura = false;
    File dirFicheiroSD;
    File rutaCompleta;
    public static String nomeFicheiro = "ficheiro_coches.txt";
    AlertDialog.Builder dialogo;

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
        //Cojo la fecha y la hora con el formato elegido
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());


        if (!editMarca.getText().toString().equals("")){
            if(sdAccesoEscritura) {
                if (rbAdd.isChecked()) {
                    try {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(rutaCompleta, true));
                        osw.write(editMarca.getText() + " - " + currentDateandTime + "\n");
                        osw.close();
                        Log.i("ESCRITURA", rutaCompleta.toString());
                        Log.i("ESCRITURA", editMarca.getText().toString()+ " - " + currentDateandTime);
                        editMarca.setText("");
                    } catch (Exception ex) {
                        Log.e("E/S", "Error al sobreescribir el fichero");
                        ex.printStackTrace();
                    }
                }
                if (rbOve.isChecked()) {
                    try {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(rutaCompleta, false));
                        osw.write(editMarca.getText() + " - " + currentDateandTime + "\n");
                        osw.close();
                        Log.i("ESCRITURA", rutaCompleta.toString());
                        Log.i("ESCRITURA", editMarca.getText().toString()+ " - " + currentDateandTime);
                        editMarca.setText("");
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
    }//Fin add/over



    public void lanzarList (View v){
        Intent inten = new Intent(this,List.class);
        inten.putExtra("RUTA",rutaCompleta.toString());
        startActivity(inten);
    }

    public void lanzarDialog(View v){
        showDialog(1);
    }

    /*
    *Metodo que crea y muestra los dialogs segun el valor que recibe como parametro
    * en este caso solo recibimos una unica llamada, asi que no comprobamos con un case el valor
    * y solo crearemos un dialog
     */
    @Override
    protected Dialog onCreateDialog(int id){
        dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Mostrar Datos");
        dialogo.setMessage("Elige como quieres visualizar los datos");
        dialogo.setIcon(android.R.mipmap.sym_def_app_icon);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("ListView", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(U4_Coches.this , List.class);
                intent.putExtra("RUTA",rutaCompleta.toString());
                startActivity(intent);
            }
        });
        dialogo.setNegativeButton("Spinner", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(U4_Coches.this , Spin.class);
                intent.putExtra("RUTA",rutaCompleta.toString());
                startActivity(intent);
            }
        });
        return dialogo.create();
    }//FIN onCreateDialog

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
