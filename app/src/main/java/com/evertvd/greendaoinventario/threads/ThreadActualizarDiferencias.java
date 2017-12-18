package com.evertvd.greendaoinventario.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.vista.fragments.FragmentZonas;


/**
 * Created by evertvd on 18/09/2017.
 */

public class ThreadActualizarDiferencias extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    private Context context;
    //private IInventario iInventario;
    //private Inventario inventario;
    private FragmentManager fragmentManager;

    //para progresDialog
    public ThreadActualizarDiferencias(ProgressDialog dialog, Context context, FragmentManager fragmentManager) {
        //this.activity=activity;
        this.dialog=dialog;
        this.context=context;
        this.fragmentManager=fragmentManager;
        //this.contexto=contexto;
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
        iProducto.calcularPoductoDiferencia();
        IZona iZona=new SqliteZona();
        iZona.calcularDiferenciaZona();
    }

    private void abrirFragment(){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentZonas fragment = new FragmentZonas();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            //setTitle("Inventario "+inventario.getNuminventario());
            fragmentTransaction.commit();

    }

}