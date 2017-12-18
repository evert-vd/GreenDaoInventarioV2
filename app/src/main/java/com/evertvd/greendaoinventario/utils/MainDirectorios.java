package com.evertvd.greendaoinventario.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.evertvd.greendaoinventario.R;

import java.io.File;

/**
 * Created by evertvd on 11/12/2017.
 */

public class MainDirectorios {

    public static void crearDirectorioApp(Context context) {
        File f = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.directorio_app));//inventario
        // Comprobamos si la carpeta estÃ¡ ya creada
        // Si la carpeta no estÃ¡ creada, la creamos.
        if (!f.isDirectory()) {
            String newFolder = "/" + context.getString(R.string.directorio_app); //nombre de la Carpeta que vamos a crear
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folderFile = new File(extStorageDirectory + newFolder);
            folderFile.mkdir(); //creamos la carpeta
            //return folderFile;
        } else {
            Log.d("TAG", "La carpeta ya estaba creada");
            //return f;
        }
    }

    public static File obtenerDirectorioApp(Context context) {
        return new File(Environment.getExternalStorageDirectory(), context.getString(R.string.directorio_app));
        // Comprobamos si la carpeta estÃ¡ ya creada
    }

    public static boolean validarArchivo(Context context) {
        //File file=obtenerDirectorioApp(context);
        boolean alreadyExists = new File(obtenerArchivo(context)).exists();
        return alreadyExists;
    }

    public static String obtenerArchivo(Context context) {
        String nameFile = "/" + context.getString(R.string.directorio_app) + "/" + context.getString(R.string.file_resumido) + context.getString(R.string.extension_file);
        return Environment.getExternalStorageDirectory() + nameFile;
    }

    public static void eliminarFichero(Context context,String nameFile) {
        File file = new File(MainDirectorios.obtenerDirectorioApp(context),nameFile);
        Log.e("file2", file.toString());
        if (file.delete()) {
            Log.e("TAG", "archivo eliminado");
        }
    }
}

