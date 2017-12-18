package com.evertvd.greendaoinventario.threads.LoadData;

import android.content.Context;
import android.util.Log;

import com.evertvd.greendaoinventario.interfaces.IEmpresa;
import com.evertvd.greendaoinventario.sqlitedao.SqliteEmpresa;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.utils.Utils;


import java.util.concurrent.TimeUnit;

/**
 * Created by evertvd on 18/09/2017.
 */

    public class ThreadInventario extends Thread {
    private String path;
    Context context;

    public ThreadInventario(ThreadGroup nombreGrupo, String nombreHilo, String path) {
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
            String file = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
            Log.e("NAMEFILE", file);
            String[] valores = file.split("-");
            int codEmpresa = Integer.parseInt(valores[1]);
            int numInventario = Integer.parseInt(valores[2]);
            String equipo = valores[3].substring(0, valores[3].length());
            //String equipo = valores[3].substring(0, valores[3].length() - 4);
            //4:longitud de ".csv"
            int numEquipo = Integer.parseInt(equipo);

            Inventario inventario=new Inventario();
            inventario.setNumequipo(numEquipo);
            inventario.setNuminventario(numInventario);
            inventario.setFechaCreacion(Utils.fechaActual());
            IEmpresa iEmpresa=new SqliteEmpresa();
            inventario.setEmpresa_id(iEmpresa.obtenerEmpresa(codEmpresa).getId());
            inventario.setContexto(0);//inventario
            //inventario.setEstado(0);//abierto

            IInventario iInventario=new SqliteInventario();
            iInventario.agregarInventario(inventario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
