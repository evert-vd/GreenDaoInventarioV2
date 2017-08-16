package com.evertvd.greendaoinventario.utils;

import android.os.Environment;

/**
 * Created by evertvd on 10/02/2017.
 */

public class ComprobarSD {


    public static boolean comprobarMemoriaSD() {

        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;
        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();
        boolean memoria = false;

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
            memoria = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
            memoria = true;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
            memoria = false;
        }

        return memoria;
    }


}
