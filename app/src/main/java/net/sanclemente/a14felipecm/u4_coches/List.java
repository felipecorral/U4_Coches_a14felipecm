package net.sanclemente.a14felipecm.u4_coches;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class List extends Activity {
    boolean sdDisponhible = false;
    boolean sdAccesoEscritura = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ListView lvMarcas = (ListView) findViewById(R.id.list_marcas);
        ArrayList<String> arrayMarcas = new ArrayList<String>();
        comprobarEstadoSD();

        Intent intent = getIntent();
        Toast.makeText(List.this, intent.getExtras().getString("RUTA"), Toast.LENGTH_SHORT).show();

        //Fonte de datos
        arrayMarcas = leerFichero(intent.getExtras().getString("RUTA"));

        //Enlace do adaptador coa fonte de datos
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayMarcas);

        //Enlace do adaptador co List
        lvMarcas.setAdapter(adaptador);

        //Escoitador
        lvMarcas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getBaseContext(), "Seleccionaches: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Seleccionaches: " + ((TextView) view).getText()+ parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

            }
        });
    }//Fin oncreate

    //Metodo para leer
    public ArrayList<String> leerFichero(String ruta) {
        ArrayList<String> arrayListMarcas = new ArrayList<String>();
        String linha = "";
        //TextView tv = (TextView) findViewById(R.id.tvAmosar);
        //tv.setText(linha);
        if (sdDisponhible) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta)));
                while ((linha = br.readLine()) != null)
                    arrayListMarcas.add(linha + "\n");
                br.close();
            } catch (Exception ex) {
                Toast.makeText(List.this, "Problemas lendo o ficheiro", Toast.LENGTH_SHORT).show();
                Log.e("SD", "Erro lendo o ficheiro. ");
            }
        } else
            Toast.makeText(this, "A tarxeta SD non está dispoñible", Toast.LENGTH_SHORT).show();
        return arrayListMarcas;
    }//Fin Leer

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


}
