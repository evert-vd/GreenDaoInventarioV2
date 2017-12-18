package com.evertvd.greendaoinventario.utils;

import android.content.Context;
import android.util.Log;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class Utils {

    public Utils(){
    }
       public static String fechaActual(){
           SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
           Date hoy = new Date();
           return formato.format(hoy);
       }

       public static int aleatorio(){
           return (int)(Math.random()*(999-100+1)+100);
       }

       public static String formatearNumero(int numero){
           DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
           simbolo.setDecimalSeparator('.');
           simbolo.setGroupingSeparator(',');
           DecimalFormat formateador = new DecimalFormat("###,###",simbolo);
           return String.valueOf(formateador.format(numero));
       }

    public static void copyRawToSDCard(int id, String path, Context context) {
        InputStream in = context.getResources().openRawResource(id);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            in.close();
            out.close();
            Log.i("TAG", "copyFile, success!");
        } catch (FileNotFoundException e) {
            Log.e("TAG", "copyFile FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", "copyFile IOException " + e.getMessage());
        }
    }

}
