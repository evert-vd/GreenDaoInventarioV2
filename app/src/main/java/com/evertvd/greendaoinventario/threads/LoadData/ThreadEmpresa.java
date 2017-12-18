package com.evertvd.greendaoinventario.threads.LoadData;

import android.content.Context;
import android.util.Log;

import com.evertvd.greendaoinventario.sqlitedao.SqliteEmpresa;
import com.evertvd.greendaoinventario.interfaces.IEmpresa;
import com.evertvd.greendaoinventario.modelo.Empresa;

import java.util.concurrent.TimeUnit;

/**
 * Created by evertvd on 18/09/2017.
 */

    public class ThreadEmpresa extends Thread {
    private String path;
    Context context;

    public ThreadEmpresa(ThreadGroup nombreGrupo, String nombreHilo, String path) {
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
        IEmpresa iEmpresa=new SqliteEmpresa();
        Empresa empresa=new Empresa();
        empresa.setEmpresa("Comercio");
        empresa.setCodempresa(11);
        empresa.setEstado(0);
        iEmpresa.agregarEmpresa(empresa);

    }

}
