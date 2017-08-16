package com.evertvd.greendaoinventario.vista.activitys;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.modelo.BeanFile;
import com.evertvd.greendaoinventario.utils.ComprobarSD;
import com.evertvd.greendaoinventario.vista.adapters.FileAdapter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FileInterno extends ListActivity {
    private static final int OK_RESULT_CODE = 1;
    private static final int RESULT_CANCELLED = 2;
    private File direccion;
    private FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        direccion = new File("/sdcard/");
        fill(direccion);
    }

    public String leer(String nombre) {
        try {
            File file;
            FileReader lectorArchivo;
            file = new File(nombre);
            lectorArchivo = new FileReader(file);
            BufferedReader br = new BufferedReader(lectorArchivo);
            String l = "";
            String aux = "";

            while (true) {
                aux = br.readLine();
                if (aux != null)
                    l = l + aux + " ";
                else
                    break;
            }

            br.close();
            lectorArchivo.close();
            return l;

        } catch (IOException e) {
            // System.out.println("Error:"+e.getMessage());
        }
        return null;
    }


    private String[] leerArchivoSD(String nombre) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (ComprobarSD.comprobarMemoriaSD()) {
            try {
                File file;
                FileReader lectorArchivo;

                file = new File(nombre);
                lectorArchivo = new FileReader(file);
                //BufferedReader br=new BufferedReader(lectorArchivo);

                byteArrayOutputStream = new ByteArrayOutputStream();
                //File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                //File fichero = new File(ruta.getAbsolutePath(), lblNombreArchivo.getText().toString());
                //Log.e("ruta y nombre: ", ruta.getAbsolutePath() + " .." + lblNombreArchivo.getText().toString());
                // File file=new File(lblRuta.getText().toString(),"inventario2.txt");
                //FileReader fr=new FileReader(fichero);//importante
                //InputStreamReader is = new InputStreamReader(new FileInputStream(lectorArchivo));
                BufferedReader bufferedReader = new BufferedReader(lectorArchivo);
                int i = bufferedReader.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = bufferedReader.read();
                }

                bufferedReader.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error al leer el archivo .txt", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error al leer el archivo .txt", Toast.LENGTH_LONG).show();
                //btnCargarDatos.setVisibility(View.VISIBLE);
            }


        } else {
            Toast.makeText(this, "La memoria no está disponible", Toast.LENGTH_LONG).show();
        }

        return byteArrayOutputStream.toString().split("\n");
    }


    private void fill(File f) {
        File[] dirs = f.listFiles();
        this.setTitle("Directorio: " + f.getName());
        List<BeanFile> dir = new ArrayList<BeanFile>();
        List<BeanFile> fls = new ArrayList<BeanFile>();


        try {
            for (File ff : dirs) {
                if (ff.isDirectory())
                    dir.add(new BeanFile(ff.getName(), "Folder", ff.getAbsolutePath()));
                else {
                    if (ff.length() < 1024) {
                        fls.add(new BeanFile(ff.getName(), "Archivo tamaño: " + ff.length() + " Bytes", ff.getAbsolutePath()));

                    } else if (ff.length() / 1024 < 1024) {
                        fls.add(new BeanFile(ff.getName(), "Archivo tamaño: " + ff.length() / 1024 + " Kb", ff.getAbsolutePath()));
                    } else {
                        fls.add(new BeanFile(ff.getName(), "Archivo tamaño: " + ff.length() / (1024 * 1024) + " Mb", ff.getAbsolutePath()));
                    }

                    //Log.e("tamaño", String.valueOf(ff.length()/1024)+" Mb");
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0, new BeanFile("<---", "Regresar al Directorio", f.getParent()));

        adapter = new FileAdapter(FileInterno.this, R.layout.activity_file_interno, dir);
        this.setListAdapter(adapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        final BeanFile opcion = adapter.getItem(position);
        if (opcion.getData().equalsIgnoreCase("folder") || opcion.getData().equalsIgnoreCase("Regresar al Directorio")) {
            direccion = new File(opcion.getPath());
            fill(direccion);

        }
        //  else if(){        }
        else {
            //System.out.println("leer: "+leer(o.getPath()));
            System.out.println("leerSD" + leerArchivoSD(opcion.getPath()));

            AlertDialog.Builder cargarArchivo = new AlertDialog.Builder(this);
            cargarArchivo.setMessage("¿Cargar el archivo " + opcion.getName() + "?")
                    .setTitle("Advertencia")
                    .setCancelable(false)
                    .setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //dialog.cancel();
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent();
                                    intent.putExtra("path", opcion.getPath());
                                    intent.putExtra("nombreArchivo", opcion.getName());
                                    setResult(OK_RESULT_CODE, intent);
                                    finish();

                                    //inicializar(opcion.getPath(), opcion.getName());
                                    //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    //finish();//finalizamos la actividad FileInterno

                                }
                            });
            AlertDialog alert = cargarArchivo.create();
            alert.show();
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        //Intent intent = new Intent("xper.activity.ACTIVITY_BAR_RESULT_INTENT");
        //intent.putExtra("codBar", "bar");
        //setResult(Activity.RESULT_CANCELED, intent);

        /**/
        //setResult(LoginActivity.RESULT_CANCELED);
        //finish();
    }
}
