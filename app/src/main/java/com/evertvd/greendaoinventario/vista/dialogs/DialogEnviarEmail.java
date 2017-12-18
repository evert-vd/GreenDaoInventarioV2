package com.evertvd.greendaoinventario.vista.dialogs;





import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.utils.MainDirectorios;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by evertvd on 13/03/2017.
 */

public class DialogEnviarEmail extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar Reportes");
        int totalArchivos = obtenerDirectorioApp().length;
        boolean[] cantSeleccionado = new boolean[totalArchivos];

        builder.setMultiChoiceItems(obtenerDirectorioApp(), cantSeleccionado, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView list = ((AlertDialog) dialog).getListView();
                        // make selected item in the comma seprated string
                        //StringBuilder stringBuilder = new StringBuilder();
                        List<String> archivosAdjuntos = new ArrayList<String>();
                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);
                            if (checked) {
                                //File carpeta = getActivity().getExternalFilesDir(getResources().getString(R.string.carpetaApp));//Ruta relativa a la app
                                File carpeta=MainDirectorios.obtenerDirectorioApp(getActivity());
                                //File ubicacionReporte = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                //File ruta = Environment.getExternalStorageDirectory();
                                //String archivo = "Download/"+list.getItemAtPosition(i);
                                String archivo = list.getItemAtPosition(i).toString();
                                //File reporte = new File(ruta, archivo);
                                File reporte = new File(carpeta, archivo);
                                archivosAdjuntos.add(reporte.getAbsolutePath());
                            }
                        }
                        //Envio del List con las rutas
                        compartirPorEmail(archivosAdjuntos);
                    }
                });

        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //alert.show();

        return builder.create();
    }


    private CharSequence[] obtenerDirectorioApp(){
        List<String> list = new ArrayList<String>();
        //File ubicacionReporte = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File carpeta = getActivity().getExternalFilesDir(getResources().getString(R.string.directorio_app));//Ruta relativa a la app

        //File sdCardRoot = Environment.getExternalStorageDirectory();
        //File yourDir = new File(sdCardRoot, "Download");
        for (File f : MainDirectorios.obtenerDirectorioApp(getActivity()).listFiles()) {
            if (f.isFile()) {
                String name = f.getName();
                //Log.i("file names", name);
                list.add(name);
            }
        }
        // Intialize  readable sequence of char values
        CharSequence[] archivosDownload = list.toArray(new CharSequence[list.size()]);

        return archivosDownload;
    }


    private void compartirPorEmail(List<String> archivosAdjuntos){
       IInventario iInventario=new SqliteInventario();
       Inventario inventario=iInventario.obtenerInventario();

        String numeroEquipo="";
        if(inventario.getNumequipo()<10){
            numeroEquipo="0"+String.valueOf(inventario.getNumequipo());
        }else{
            numeroEquipo=String.valueOf(inventario.getNumequipo());
        }
        String cuerpoMje="";
        String titulo=getResources().getString(R.string.mensaje);
        String archivoOrigen="INV-"+inventario.getEmpresa().getCodempresa()+"-"+inventario.getNuminventario()+"-"+numeroEquipo;
        String fechaInicio=inventario.getFechaCreacion();
        String fechaCierre=inventario.getFechaCierre();
        cuerpoMje=titulo+"\n"+"Archivo Origen: "+archivoOrigen+"\n"+"Fecha de inicio: "+fechaInicio+"\n"+"Fecha de cierre: "+fechaCierre;

        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        String[] para= getResources().getStringArray(R.array.para);
        String[] cc= getResources().getStringArray(R.array.cc);
        emailIntent.setType("text/plain");
        //emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,para);
        //emailIntent.putExtra(Intent.EXTRA_EMAIL,posiciones,new String[]{"correo"}));
        emailIntent.putExtra(Intent.EXTRA_CC,cc);
        //emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"dsada@correo"});
        //emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.asunto)+" Equipo "+numeroEquipo);
        emailIntent.putExtra(Intent.EXTRA_TEXT,cuerpoMje);
        //emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.mensaje));
        //has to be an ArrayList
        ArrayList<Uri> uris = new ArrayList<Uri>();
        //convert from paths to Android friendly Parcelable Uri's
        for (String file : archivosAdjuntos) {
            File fileIn = new File(file);
            Uri uri = Uri.fromFile(fileIn);
            uris.add(uri);
        }
        if(!uris.isEmpty()){
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(emailIntent,  getResources().getString(R.string.compartir)));
        }else{
            Toast.makeText(getActivity(), "Error. Debes seleccionar almenos 1 reporte para enviar.", Toast.LENGTH_SHORT).show();
        }
    }
}
