package com.evertvd.greendaoinventario.vista.dialogs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.vista.adapters.HistorialConteoAdapter;
import com.evertvd.greendaoinventario.vista.adapters.ValidarConteoAdapter;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evertvd on 13/03/2017.
 */

public class DialogValidarConteo extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogValidarConteo.class.getSimpleName();

    private Button btnAceptar;

    public DialogValidarConteo( ) {
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // this.idProducto=idProducto;
        //recuperacion del parametro que viene del punto de invocacion del dialog--viene como string
        //idDetalleProducto = getArguments().getInt("idDetalleProducto");
        //Log.e("idDetalleProducDialog",String.valueOf(idDetalleProducto));

        return crearModificarCantidad();
    }

    /**
     * Crea un diÃ¡logo con personalizado para comportarse
     * como formulario de entrada de cantidad
     *
     * @return DiÃ¡logo
     */
    public AlertDialog crearModificarCantidad() {

        //final String[] items={"A","B"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_validar_conteo, null);

        QueryBuilder<Conteo> conteoQueryBuilder=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Validado.eq(0)).where(ConteoDao.Properties.Estado.notEq(-1));//por validar, no eliminado
        Join producto=conteoQueryBuilder.join(ConteoDao.Properties.Producto_id, Producto.class).where(ProductoDao.Properties.Estado.eq(-1));//con diferencia;
        Join inventario=conteoQueryBuilder.join(producto, ProductoDao.Properties.Inventario_id, Inventario.class,InventarioDao.Properties.Id);//inventario activado
        inventario.where(InventarioDao.Properties.Estado.eq(0));
        List<Conteo> conteoList=conteoQueryBuilder.list();


        ValidarConteoAdapter adapter = new ValidarConteoAdapter(getActivity(), conteoList);

        ListView miLista = (ListView)v.findViewById(R.id.miLista);

        miLista.setAdapter(adapter);
        builder.setView(v);

       btnAceptar = (Button) v.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            this.dismiss();
        }

    }


}