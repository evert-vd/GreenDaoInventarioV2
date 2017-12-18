package com.evertvd.greendaoinventario.threads.LoadData;

import android.content.Context;
import android.util.Log;

import com.csvreader.CsvReader;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Producto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by evertvd on 18/09/2017.
 */

    public class ThreadProducto extends Thread {
    private String path;
    Context context;

    public ThreadProducto(ThreadGroup nombreGrupo, String nombreHilo, String path) {
        this.path=path;
        this.context=context;
    }

        @Override
        public void run() {

            Long startTime = System.nanoTime();

            leerArchivo();


            long endTime = System.nanoTime();
            int time2 = (int) TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
            Log.e("hiloProducto", String.valueOf(time2));

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
            IProducto iProducto=new SqliteProducto();
            IZona iZona=new SqliteZona();
            //activos.readHeaders();
            int i=0;
            while (activos.readRecord()) {
                Producto producto=new Producto();
                producto.setCodigo(Integer.parseInt(activos.get(1)));
                producto.setDescripcion(activos.get(2));
                producto.setZona_id(iZona.buscarZonaNombre(activos.get(0)).getId());
                producto.setStock(stockDesencriptado(activos.get(3),activos.get(1)));
                producto.setEstado(1);//estado inicial: con diferencia
                producto.setTipo("Sistema");//origen del dato (sistema/app)
                iProducto.agregarProducto(producto);
            }
            activos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double stockDesencriptado(String stockCifrado, String codProducto) {
        //Log.e("codProd", String.valueOf(codProducto));
        //Log.e("stocCifrado", String.valueOf(stockCifrado));
        //Log.e("Auxiliar", String.valueOf(numeroAuxiliar()));
        Double stockCif=Double.parseDouble(stockCifrado);
        int codProd=Integer.parseInt(codProducto);
        return (stockCif - codProd) / numeroAuxiliar();
    }

    private int numeroAuxiliar() {
        String nombreArchivo= path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
        String[] valores = nombreArchivo.split("-");
        int codigoEmpresa = Integer.parseInt(valores[1]);
        int nroInventario = Integer.parseInt(valores[2]);
        //String equipo=valores[3];
        //4:longitud de .csv
        String equipo = valores[3].substring(0, valores[3].length());
        int nroEquipo = Integer.parseInt(equipo);
        return (codigoEmpresa * nroEquipo) + nroInventario;
    }

}
