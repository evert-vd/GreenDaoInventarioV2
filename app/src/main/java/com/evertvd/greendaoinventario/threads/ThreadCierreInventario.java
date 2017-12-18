package com.evertvd.greendaoinventario.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.fragments.FragmentZonas;
import com.evertvd.greendaoinventario.vista.fragments.FrmContainerResumen;


/**
 * Created by evertvd on 18/09/2017.
 */

public class ThreadCierreInventario extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    private Context context;
    private int contexto;
    //private IInventario iInventario;
    //private Inventario inventario;
        private FragmentManager fragmentManager;

    //para progresDialog
    public ThreadCierreInventario(ProgressDialog dialog, Context context, FragmentManager fragmentManager, int contexto) {
        //this.activity=activity;
        this.dialog=dialog;
        this.context=context;
        this.fragmentManager=fragmentManager;
        this.contexto=contexto;
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
        //iInventario=new SqliteInventario();
        //inventario=iInventario.obtenerInventario();
        dialog.show();
    }

    public void onPostExecute(Void unused) {
        //aquí se puede colocar código que
        //se ejecutará tras finalizar
        dialog.dismiss();
        abrirFragment();

            //guarda en cache y abre el dialog fragment
            //Toast.makeText(context,"Foto Guardada correctamento",Toast.LENGTH_SHORT).show();
                //DialogFragment dialogFragment = new DialogViewCollage();
                //Bundle bundle=new Bundle();
                //bundle.putString("activo",activo);
                //dialogFragment.setArguments(bundle);
                //dialogFragment.setCancelable(false);
                //dialogFragment.show(fragmentManager, "dialogoFotoview");

             //((Activity)context).finish();//finaliza la actividad
        }

    protected Void doInBackground(Void... params) {
        //aquí se puede colocar código que
        //se ejecutará en background
        try{
        calcularDiferencias();
        }catch (Exception e){
        }
        return null;
    }

    private void calcularDiferencias(){
        IProducto iProducto=new SqliteProducto();
        iProducto.calcularPoductoDiferencia();//calcula las diferencias en el producto
        IZona iZona=new SqliteZona();
        iZona.calcularDiferenciaZona();//calcula las diferencias en la zona
        IInventario iInventario=new SqliteInventario();
        Inventario inventario=iInventario.obtenerInventario();
        if(contexto==0){
            inventario.setContexto(1);
        }else if(contexto==1){
            inventario.setContexto(2);
            inventario.setFechaCierre(Utils.fechaActual());
            //inventario.setEstado(1);//cerrado
        }
        iInventario.actualizarInventario(inventario);
    }

    private void abrirFragment(){
        if (contexto==0){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentZonas fragment = new FragmentZonas();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            //setTitle("Inventario "+inventario.getNuminventario());
            fragmentTransaction.commit();
        }else{
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FrmContainerResumen fragment = new FrmContainerResumen();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            //setTitle("Inventario "+inventario.getNuminventario());
            fragmentTransaction.commit();
        }
    }

}