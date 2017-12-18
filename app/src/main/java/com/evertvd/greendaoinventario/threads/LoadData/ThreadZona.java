package com.evertvd.greendaoinventario.threads.LoadData;

import android.content.Context;
import android.util.Log;

import com.csvreader.CsvReader;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Zona;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by evertvd on 18/09/2017.
 */

    public class ThreadZona extends Thread {
    private String path;
    Context context;

    public ThreadZona(ThreadGroup nombreGrupo,String nombreHilo, String path) {
        this.path=path;
        this.context=context;
    }

        @Override
        public void run() {

            Long startTime = System.nanoTime();

            leerArchivo();


            long endTime = System.nanoTime();
            int time2 = (int) TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
            Log.e("hiloCC", String.valueOf(time2));

                try {
                    // Dejar libre la CPU durante
                    // unos milisegundos
                    //Thread.sleep(100);
                    //context.getApplicationContext().startActivity();
                    //Toast.makeText(context,total.getTotal()+"registros Cargados",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    return;
                }
            }


    private void leerArchivo() {

        try {

            CsvReader activos = new CsvReader(path);
            //activos.readHeaders();
            int i=0;
            while (activos.readRecord()) {
                IZona iZona=new SqliteZona();
                if(iZona.buscarZonaNombre(activos.get(0))==null){
                    Zona zona=new Zona();
                    zona.setNombre(activos.get(0));
                    //IInventario iInventario=new SqliteInventario();
                    //Inventario inventario=iInventario.obtenerInventario();
                    //Log.e("HILO INV", String.valueOf(inventario.getId()));
                    //zona.setInventario_id(inventario.getId());
                    zona.setEstado(1);//con direncia
                    iZona.agregarZona(zona);
                }
            }
            activos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
