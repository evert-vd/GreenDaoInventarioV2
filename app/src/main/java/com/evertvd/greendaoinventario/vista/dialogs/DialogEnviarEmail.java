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


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;

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
        int totalArchivos = obtenerDirectorioDownload().length;
        boolean[] cantSeleccionado = new boolean[totalArchivos];

        builder.setMultiChoiceItems(obtenerDirectorioDownload(), cantSeleccionado, new DialogInterface.OnMultiChoiceClickListener() {
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
                                File carpeta = getActivity().getExternalFilesDir(getResources().getString(R.string.carpetaReporte));//Ruta relativa a la app
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


    private CharSequence[] obtenerDirectorioDownload(){
        List<String> list = new ArrayList<String>();
        //File ubicacionReporte = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File carpeta = getActivity().getExternalFilesDir(getResources().getString(R.string.carpetaReporte));//Ruta relativa a la app
        //File sdCardRoot = Environment.getExternalStorageDirectory();
        //File yourDir = new File(sdCardRoot, "Download");
        for (File f : carpeta.listFiles()) {
            if (f.isFile()) {
                String name = f.getName();
                Log.i("file names", name);
                list.add(name);
            }
        }
        // Intialize  readable sequence of char values
        CharSequence[] archivosDownload = list.toArray(new CharSequence[list.size()]);

        return archivosDownload;
    }


    private void compartirPorEmail(List<String> archivosAdjuntos){
        Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();

        String numeroEquipo="";
        if(inventario.getNumequipo()<10){
            numeroEquipo="0"+String.valueOf(inventario.getNumequipo());
        }else{
            numeroEquipo=String.valueOf(inventario.getNumequipo());
        }
        String cuerpoMje="";
        String titulo=getResources().getString(R.string.mensaje);
        String archivoOrigen="INV-"+inventario.getEmpresa().getCodempresa()+"-"+inventario.getNuminventario()+"-"+inventario.getNumequipo();
        String fechaInicio=inventario.getFechaCreacion();
        String fechaCierre=inventario.getFechaCierre();
        cuerpoMje=titulo+"\n"+"Archivo Origen: "+archivoOrigen+"\n"+"Fecha Inicio: "+fechaInicio+"\n"+"Fecha Cierre: "+fechaCierre;


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


        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(emailIntent,  getResources().getString(R.string.tituloApss)));

    }

}
