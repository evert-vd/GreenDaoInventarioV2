package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evertvd on 13/03/2017.
 */

public class DialogValidarConteo extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogValidarConteo.class.getSimpleName();

    private Button btnAceptar;
    private TextView tvCodigos;


    public DialogValidarConteo() {
        //this.idProducto=idProducto;
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

        Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();
        //List<BeanConteo> listaPorValidar = iConteo.listarEstadoValidacion(getActivity(),0);
        //List<BeanProducto> listProd=new ArrayList<BeanProducto>();

       /* BeanProducto producto;
        //lista de objetos
        for (int i = 0; i < listaPorValidar.size(); i++) {
            Log.e("Lista por validar", String.valueOf(listaPorValidar.get(i).getConteo())+" id:"+String.valueOf(listaPorValidar.get(i).getIdProducto()));
            producto=iProducto.obtenerProductoId(getActivity(), listaPorValidar.get(i).getIdProducto());
            listProd.add(producto);
        }
        // A. Creamos el arreglo de Strings para llenar la lista
        final String[] codigosPorValidar = new String[listProd.size()];

        //Parseo de objeto a string
        //for(int i=0; i<listProd.size(); i++){
            //Obtiene el campo Descripción y lo agrega al array de strings.
          //  codigosPorValidar[i]=String.valueOf(listProd.get(i).getCodigo())+"\t-\t"+listProd.get(i).getZona();

            //Log.e("Lista por validar2", String.valueOf(listProd.get(i).getCodigo())+" id:"+String.valueOf(listProd.get(i).getStock()));
        //}
        // B. Creamos un nuevo ArrayAdapter con nuestra lista
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, codigosPorValidar);

        // C. Seleccionamos la lista de nuestro layout
        ListView miLista = (ListView)v.findViewById(R.id.miLista);

        // D. Asignamos el adaptador a nuestra lista
        miLista.setAdapter(arrayAdapter);
        builder.setView(v);

       btnAceptar = (Button) v.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);
 */
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            this.dismiss();
        }

    }

    private void restaurarEstadoZona(){
       // IZona iZona=new Sqlite_Zona();
        //iZona.asignarEstadoCero(getActivity());
    }

}