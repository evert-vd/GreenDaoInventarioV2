package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.HistorialDao;
import com.evertvd.greendaoinventario.vista.adapters.HistorialConteoAdapter;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class DialogHistorialConteo extends DialogFragment implements View.OnClickListener {


        //private Button btnAceptar;
        private TextView txtSinHistorial, txtConteoActual;
        private long idconteo;


        public DialogHistorialConteo() {
            //this.idProducto=idProducto;
        }

    public static DialogHistorialConteo newInstance(int num) {

        DialogHistorialConteo dialogFragment = new DialogHistorialConteo();
        Bundle bundle = new Bundle();
        bundle.putInt("num", num);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // this.idProducto=idProducto;
        //recuperacion del parametro que viene del punto de invocacion del dialog--viene como string
        //idDetalleProducto = getArguments().getInt("idDetalleProducto");
        //Log.e("idDetalleProducDialog",String.valueOf(idDetalleProducto));
        idconteo=getArguments().getLong("idconteo");

        return crearModificarCantidad();
    }

    /**
     * Crea un diÃƒÂ¡logo con personalizado para comportarse
     * como formulario de entrada de cantidad
     *
     * @return DiÃƒÂ¡logo
     */
    public AlertDialog crearModificarCantidad() {
        /*
        IHistorial iHistorial=new Sqlite_Historial();
                List<BeanHistorial> historialList=iHistorial.obtenerHistorial(mContext, listaDetalle.get(position).getIdconteo());
         */
        //final String[] items={"A","B"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogo_historial, null);
        txtSinHistorial=(TextView)v.findViewById(R.id.txtSinHistorial);
        txtConteoActual=(TextView)v.findViewById(R.id.txtConteoActual);

        Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(idconteo)).unique();
        txtConteoActual.setText("Conteo Actual:"+String.valueOf(conteo.getCantidad()));
        //lista de objetos

        List<Historial> historialList= Controller.getDaoSession().getHistorialDao().queryBuilder().where(HistorialDao.Properties.Conteo_id.eq(idconteo)).list();


        for(int i=0; i<historialList.size(); i++){
            Log.e("hist", String.valueOf(historialList.get(i).getCantidad()));
        }

        // A. Creamos el arreglo de Strings para llenar la lista
        final String[] codigosPorValidar = new String[historialList.size()];

        //Parseo de objeto a string
        for(int i=0; i<historialList.size(); i++){
            //Obtiene el campo DescripciÃ³n y lo agrega al array de strings.
            codigosPorValidar[i]=String.valueOf(historialList.get(i).getFecharegistro());
        }
        // B. Creamos un nuevo ArrayAdapter con nuestra lista

        HistorialConteoAdapter adapter = new HistorialConteoAdapter(getActivity(), historialList);


        // C. Seleccionamos la lista de nuestro layout

        ListView miLista = (ListView)v.findViewById(R.id.miLista);

        // D. Asignamos el adaptador a nuestra lista

        if (historialList.isEmpty()){
            miLista.setVisibility(View.GONE);
            txtSinHistorial.setVisibility(View.VISIBLE);
        }else{
            miLista.setVisibility(View.VISIBLE);
            txtSinHistorial.setVisibility(View.GONE);
        }

        miLista.setAdapter(adapter);
        builder.setView(v);

        //btnAceptar = (Button) v.findViewById(R.id.btnAceptar);
        //btnAceptar.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            this.dismiss();
        }

    }



}
