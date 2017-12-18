package com.evertvd.greendaoinventario.threads;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.evertvd.greendaoinventario.threads.LoadData.ThreadInventario;
import com.evertvd.greendaoinventario.threads.LoadData.ThreadProducto;
import com.evertvd.greendaoinventario.threads.LoadData.ThreadZona;
import com.evertvd.greendaoinventario.vista.activitys.Login;
import com.evertvd.greendaoinventario.vista.activitys.MainActivity;


/**
 * Created by evertvd on 18/09/2017.
 */

public class MainThreadsReadData extends AsyncTask<Void, Void, Void> {
    private Context context;
    //private ProgressBar progressBar;
    private String path;
    private Button  btnLeerArchivo;
    private EditText ruta;

    //para progresDialog
    public MainThreadsReadData(Context context, Button btnLeerArchivo, EditText ruta, String path) {
        this.context=context;
        //this.progressBar=progressBar;
        this.path=path;
        this.btnLeerArchivo=btnLeerArchivo;
        this.ruta=ruta;
    }

    /*
    //para dialog fragment
    public TareaCarga(DialogFragment progress, Context context, String path) {
        this.dialogTask = progress;
        this.context = context;
        this.path=path;
    }
    */

    public void onPreExecute() {

        //aquí se puede colocar código a ejecutarse previo
        //a la operación

        //hiloDepartamento=new HiloDepartamento(path);
        //hiloCentroCosto=new HiloCentroCosto(path);
        //hiloResponsable=new HiloResponsable(path);
        //hiloCuentaContable=new HiloCuentaContable(path);
        //progressBar.setVisibility(View.VISIBLE);
        btnLeerArchivo.setEnabled(false);
        ruta.setEnabled(false);

    }


    public void onPostExecute(Void unused) {
        //aquí se puede colocar código que
        //se ejecutará tras finalizar
        try {
            Intent intent=new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        //context.startActivity(new Intent(context,MainActivity.class));
        //context.getApplicationContext().startActivity(getApplicationContext(),MainActivity.class);
        //Toast.makeText(context,total.getTotal()+"registros Cargados",Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        return;
    }

       }


    protected Void doInBackground(Void... params) {
        try{
            //realizar la operación aquí
            ThreadGroup grupo1 = new ThreadGroup("TIndependientes");
            ThreadZona threadZona=new ThreadZona(grupo1, "Zona", path);
            ThreadInventario threadInventario=new ThreadInventario(grupo1,"Inventario",path);
            ThreadProducto threadProducto=new ThreadProducto(grupo1,"Producto",path);
            //ThreadEmpresa threadEmpresa=new ThreadEmpresa(grupo1,"Empresa", path);
            //HiloCuentaContable hiloCuentaContable=new HiloCuentaContable(grupo1, "Cuenta", path);
            //HiloResponsable hiloResponsable=new HiloResponsable(grupo1, "Responsable", path);

            //try {
                //hilos Grupo1
                threadInventario.start();
                threadInventario.join();
                threadZona.start();
                threadZona.join();
                threadProducto.start();
                //threadEmpresa.start();
                //hiloResponsable.start();
                /*
                //hilos Grupo2
                hiloDepartamento.start();
                hiloDepartamento.join();
                hiloSede.start();
                hiloSede.join();
                hiloUbicacion.start();

                //hilos Grupo3
                hiloEmpresa.start();
                hiloEmpresa.join();
                hiloCatalogo.start();

                //hilo Grupo4
                hiloUbicacion.join();
                hiloCatalogo.join();
                hiloCentroCosto.join();
                hiloCuentaContable.join();
                hiloResponsable.join();
                hiloActivo.start();*/
            /*} catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }catch (Exception e){

        }
        return null;
    }

}