package com.evertvd.greendaoinventario.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;
import android.widget.Toast;

import com.evertvd.greendaoinventario.utils.ReportePDF;
import com.evertvd.greendaoinventario.utils.ReporteCSV;


/**
 * Created by evertvd on 18/09/2017.
 */

public class ThreadReporte extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;
    private Context context;
    private int numReporte;
    private Menu menu;

    //para progresDialog
    public ThreadReporte(ProgressDialog dialog, Context context, Menu menu, int numReporte) {
        //this.activity=activity;
        this.dialog=dialog;
        this.context=context;
        this.menu=menu;
        this.numReporte=numReporte;
    }


    public void onPreExecute() {
        //aquí se puede colocar código a ejecutarse previo
        //a la operación
        dialog.show();
    }

    public void onPostExecute(Void unused) {
        //aquí se puede colocar código que
        //se ejecutará tras finalizar
        dialog.dismiss();
        menu.getItem(0).getSubMenu().getItem(1).setEnabled(true);
        Toast.makeText(context, "Reportes generados con éxito", Toast.LENGTH_SHORT).show();
        //dialgoEmail();
        }

    protected Void doInBackground(Void... params) {
        //aquí se puede colocar código que
        //se ejecutará en background
        try{
            generarReportes();
        }catch (Exception e){
        }
        return null;
    }

    private void generarReportes(){
        ReporteCSV repote=new ReporteCSV();
        if(numReporte==0){
            repote.Resumido(context, 0);
            repote.Detallado(context,0);
        }else{
            repote.Resumido(context, 1);
            repote.Detallado(context,1);
        }

        //ReportePDF reportePDF=new ReportePDF();
        //reportePDF.crearDocumentoPdf(context);

        ReportePDF createPdfV2=new ReportePDF();
        createPdfV2.crearPDF(context);

    }


}